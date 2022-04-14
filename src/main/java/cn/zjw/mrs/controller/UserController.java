package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @RequestMapping("/judge")
    public Result<?> isLogin() {
        return Result.success();
    }

    @RequestMapping("/info")
    public Result<?> getUserInfo(@RequestParam Integer id) {
        return Result.success(userService.getById(id));
    }

    @RequestMapping("/types/and/regions")
    public Result<?> getTypesAndRegions(@RequestParam Integer id) {
        return userService.getTypesAndRegions(id);
    }
}
