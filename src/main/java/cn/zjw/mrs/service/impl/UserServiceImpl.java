package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.*;
import cn.zjw.mrs.enums.SexEnum;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.service.RecommendationService;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.JwtUtil;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.utils.RedisCache;
import cn.zjw.mrs.vo.user.LoginUserVo;
import cn.zjw.mrs.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
* @author zjw
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-04-10 22:46:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RecommendationService recommendationService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Result<?> login(User user) {
        // AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 如果认证没有通过，给出相应的提示
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登录失败");
        }
        // 如果认证通过了，使用userid生成一个jwt，jwt存入Result返回
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJwt(userId);

        // 把完整的用户信息存入redis，userid作为可以
        redisCache.setCacheObject("login:" + userId, loginUser);

        // 把User转化成UserInfoVo
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setId(loginUser.getUser().getId());
        userInfoVo.setNickname(loginUser.getUser().getNickname());
        userInfoVo.setSex(loginUser.getUser().getSex().getSexName());
        userInfoVo.setAvatar(loginUser.getUser().getAvatar());
        // 把token和userInfo封装 返回给前端
        LoginUserVo vo = new LoginUserVo(jwt, userInfoVo);

        return Result.success("登录成功", vo);
    }

    @Override
    public Result<?> logout() {
        // 获取SecurityHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        // 删除redis中的userid值
        redisCache.deleteObject("login:" + userId);
        return Result.success("注销成功", "注销成功");
    }

    @Override
    public Result<?> register(User user) {
        // 先判断用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User res = userMapper.selectOne(queryWrapper);
        System.out.println("测试:" + res);
        if (!Objects.isNull(res)) {
            return Result.error("用户名已存在");
        }
        // 对密码进行BCrypt加密后存入数据库中
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickname(user.getUsername());
        user.setSex(SexEnum.SECRET);
        userMapper.insert(user);

        // 解决用户冷启动问题
        User newUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        Long id = newUser.getId();
        recommendationService.solveColdStart(id);

        return Result.success("注册成功");
    }

    @Override
    public UserInfoVo getUserInfo(Long id) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
        return new UserInfoVo(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                PicUrlUtil.getFullAvatarUrl(user.getAvatar()),
                user.getSex().getSexName());
    }

    @Override
    public Map<String, List<?>> getTypesAndRegions(Long id) {
        List<String> types = userMapper.selectUserTypes(id);
        List<String> regions = userMapper.selectUserRegions(id);

        Map<String, List<?>> res = new HashMap<>(2);
        res.put("types", types);
        res.put("regions", regions);
        return res;
    }

    @Override
    public int updateNickname(String nickname) {
        // 获取SecurityHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();

        int update = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getNickname, nickname).eq(User::getId, uid));
        if (update == 0) {
            return 0;
        }

        // 将评论中所有id为uid的记录的nickname改为更新后的nickname
        commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                .set(Comment::getNickname, nickname).eq(Comment::getUid, uid));
        return 1;
    }

    @Override
    public int updateSex(String sexName, String username) {
        int update = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getSex, SexEnum.findSexBySexName(sexName)).eq(User::getUsername, username));
        if (update == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int updatePassword(String username, String prePassword, String newPassword, boolean isFindPassword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        String userPassword = user.getPassword();
        // 将数据库中的密码 与 用户输入的密码 进行匹配
        boolean isMatches = passwordEncoder.matches(prePassword, userPassword);
        // 匹配失败返回-1
        if (!isMatches && !isFindPassword) {
            return -1;
        }

        // 对新密码进行BCrypt加密后再存入数据库
        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        return userMapper.update(null,
                new LambdaUpdateWrapper<User>().set(User::getPassword, newPassword).eq(User::getUsername, username));
    }
}




