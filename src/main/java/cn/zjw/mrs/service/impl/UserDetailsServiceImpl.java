package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author zjw
 * @Classname UserDetailsServiceImpl
 * @Date 2022/4/11 16:15
 * @Description
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        // 如果查询不到用户，则抛出异常提示
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 查询对应的权限信息
        List<String> list = new ArrayList<>(Arrays.asList("admin", "tourist"));

        // 把数据封装成UserDetails返回
        return new LoginUser(user, list);
    }
}
