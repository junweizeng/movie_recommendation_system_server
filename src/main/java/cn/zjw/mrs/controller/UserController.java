package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.service.OssService;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.Base64DecodedMultipartFile;
import cn.zjw.mrs.vo.user.UserInfoVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author zjw
 * @Classname UserController
 * @Date 2022/4/10 22:47
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
    public Result<?> getUserInfo(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        UserInfoVo userInfo = userService.getUserInfo(loginUser.getUser().getId());
        return Result.success(userInfo);
    }

    @GetMapping("/types/and/regions")
    public Result<?> getTypesAndRegions(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Map<String, List<?>> typesAndRegions = userService.getTypesAndRegions(loginUser.getUser().getId());
        return Result.success(typesAndRegions);
    }

    @PutMapping("/update/nickname")
    public Result<?> updateUserNickname(@RequestBody String nickname) {
        if (userService.updateNickname(nickname) == 0) {
            return Result.error("昵称更新失败(┬┬﹏┬┬)");
        }
        return Result.success("昵称更新成功(‾◡◝)");
    }

    @PutMapping("/update/sex")
    public Result<?> updateUserSex(@RequestBody String sex, Principal principal) {
        if (userService.updateSex(sex, principal.getName()) == 0) {
            return Result.error("性别更新失败(┬┬﹏┬┬)");
        }
            return Result.success("性别更新成功(‾◡◝)", null);
    }

    @PostMapping("/update/avatar")
    public Result<?> updateUserAvatar(@RequestBody String avatar, Principal principal) {
        if (Strings.isBlank(avatar)) {
            return Result.error("头像更新失败(┬┬﹏┬┬)");
        }
        // base64转MultipartFile文件
        MultipartFile avatarFile = Base64DecodedMultipartFile.base64ToMultipart(avatar);
        boolean isSuccess = ossService.updateAvatar(principal.getName(), avatarFile);
        if (!isSuccess) {
            return Result.error("头像上传失败(┬┬﹏┬┬)");
        }
        return Result.success("头像修改成功(‾◡◝)", null);
    }
}
