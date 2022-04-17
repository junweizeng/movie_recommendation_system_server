package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.service.OssService;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.BASE64DecodedMultipartFile;
import cn.zjw.mrs.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Resource
    OssService ossService;

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
    public Result<?> getUserInfo(Principal principal) {
        return userService.getUserInfo(principal.getName());
    }

    @GetMapping("/types/and/regions")
    public Result<?> getTypesAndRegions(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return userService.getTypesAndRegions(loginUser.getUser().getId());
    }

    @PutMapping("/update/nickname")
    public Result<?> updateUserNickname(@RequestBody String nickname) {
        return userService.updateNickname(nickname);
    }

    @PutMapping("/update/sex")
    public Result<?> updateUserSex(@RequestBody String sex, Principal principal) {
        return userService.updateSex(sex, principal.getName());
    }

    @PostMapping("/update/avatar")
    public Result<?> updateUserAvatar(@RequestBody String avatar, Principal principal) {
        if (Strings.isBlank(avatar)) {
            return Result.error(404, "头像更新失败(┬┬﹏┬┬)");
        }
        MultipartFile avatarFile = BASE64DecodedMultipartFile.base64ToMultipart(avatar);

        return ossService.updateAvatar(principal.getName(), avatarFile);
    }
}
