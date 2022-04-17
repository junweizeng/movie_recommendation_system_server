package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @Classname UserController
 * @Date 2022/4/10 22:47
 * @Created by zjw
 * @Description
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return userService.logout();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/judge")
    public Result<?> isLogin() {
        return Result.success();
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = new UserInfoVo(
                user.getId(),
                user.getNickname(),
                user.getAvatar(),
                user.getSex().getSexName());
        return Result.success(userInfoVo);
    }

    @GetMapping("/types/and/regions")
    public Result<?> getTypesAndRegions(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return userService.getTypesAndRegions(loginUser.getUser().getId());
    }

    @PutMapping("/update/nickname")
    public Result<?> updateUserNickname(@RequestParam String nickname, Principal principal) {
        return userService.updateNickname(nickname, principal.getName());
    }

    @PutMapping("/update/sex")
    public Result<?> updateUserSex(@RequestParam Integer sex, Principal principal) {
        return userService.updateSex(sex, principal.getName());
    }
}
