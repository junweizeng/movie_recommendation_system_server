package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.enums.SexEnum;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.JwtUtil;
import cn.zjw.mrs.utils.RedisCache;
import cn.zjw.mrs.vo.user.LoginUserVo;
import cn.zjw.mrs.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
* @author 95758
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-04-10 22:46:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
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
        String jwt = JwtUtil.createJWT(userId);

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
            return Result.error(-1, "用户名已存在");
        }
        // 对密码进行BCrypt加密后存入数据库中
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickname(user.getUsername());
        user.setSex(SexEnum.SECRET);
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    @Override
    public Result<?> getTypesAndRegions(Long id) {
        List<String> types = userMapper.selectUserTypes(id);
        List<String> regions = userMapper.selectUserRegions(id);

        Map<String, List<?>> res = new HashMap<>();
        res.put("types", types);
        res.put("regions", regions);
        return Result.success(res);
    }

    @Override
    public Result<?> updateNickname(String nickname, String username) {
        int update = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getNickname, nickname).eq(User::getUsername, username));
        if (update == 0) {
            return Result.error(-1, "昵称更新失败(┬┬﹏┬┬)");
        }
        return Result.success("昵称更新成功(‾◡◝)", null);
    }

    @Override
    public Result<?> updateSex(Integer sex, String username) {
        int update = userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getSex, sex).eq(User::getUsername, username));
        if (update == 0) {
            return Result.error(-1, "性别更新失败(┬┬﹏┬┬)");
        }
        return Result.success("性别更新成功(‾◡◝)", null);
    }

}




