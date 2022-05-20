package cn.zjw.mrs.controller;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.service.AuthService;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author zjw
 * @Classname AuthController
 * @Date 2022/5/14 15:12
 * @Description
 */

@RestController
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserService userService;

    /**
     * 请求发送邮箱验证码
     * @param data 目标邮箱
     * @param authentication 用户身份信息
     * @return 发送成功与否
     */
    @PostMapping("/mail/auth/code")
    public Result<?> sendMailAuthCode(@RequestBody Map<String, String> data, Authentication authentication) {
        String mail = data.get("mail");

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUsername();

        authService.sendMailAuthCode(username, mail);
        return Result.success("验证码已发送");
    }

    /**
     * 在未登录状态下发送邮箱验证码
     * @param data 请求数据，包括用户名和邮箱号码
     * @return 发送成功与否
     */
    @PostMapping("/mail/auth/code/under/logout")
    public Result<?> sendMailAuthCodeUnderLogout(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String mail = data.get("mail");

        boolean isMailBelongToUser = authService.judgeMailBelongToUser(username, mail);
        if (!isMailBelongToUser) {
            return Result.error("输入邮箱有误（输入的账号和邮箱不对应）");
        }

        authService.sendMailAuthCode(username, mail);
        return Result.success("验证码已发送");
    }

    /**
     * 判断验证码是否有误
     * @param data post传递的数据，包括用户名和验证码
     * @return 正误信息
     */
    @PostMapping("/check/auth/code")
    public Result<?> judgeAuthCode(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String authCode = data.get("authCode");

        int judge = authService.judgeAuthCode(username, authCode);
        if (judge == -1) {
            return Result.error("验证码已过期");
        } else if (judge == 1) {
            return Result.success("输入的验证码正确");
        } else {
            return Result.error("输入的验证码有误");
        }
    }

    /**
     * 判断用户是否存在于数据库中
     * @param username 带判断的用户名
     * @return 检查结果
     */
    @GetMapping("/check/username/exists")
    public Result<?> judgeUsernameExists(@RequestParam String username) {
        boolean isUsernameExists = authService.judgeUsernameExists(username);
        if (!isUsernameExists) {
            return Result.error("账号不存在");
        } else {
            return Result.success("账号验证通过");
        }
    }

    /**
     * 再次校验验证码，防止技术人员直接通过请求找回密码
     * 校验成功后，修改原密码，并返回修改后的密码
     * @param data 请求数据，包含用户名和验证码
     * @return 错误信息或正确密码
     */
    @PostMapping("/find/password")
    public Result<?> judgeAndFindPassword(@RequestBody Map<String, String> data) {
        String username = data.get("username");
        String authCode = data.get("authCode");
        // 1. 再次校验验证码，防止技术人员直接通过请求找回密码
        int judge = authService.judgeAuthCode(username, authCode);
        if (judge == -1) {
            return Result.error("验证码已过期");
        } else if (judge == 1) {
            // 2. 校验成功后，生成一个随机新密码，替换原密码
            RandomGenerator randomGenerator = new RandomGenerator(
                    "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 8);
            String newPassword = randomGenerator.generate();
            log.info("新密码" + newPassword);
            userService.updatePassword(username, "", newPassword, true);
            // 3. 将新密码最为结果返回
            Map<String, String> res = new HashMap<>(1);
            res.put("password", newPassword);
            return Result.success(res);
        } else {
            return Result.error("输入的验证码有误");
        }
    }

    /**
     * 判断验证码 并 修改用于邮箱
     * @param data 数据，包括验证码和邮箱号码
     * @param authentication 用户身份信息
     * @return 修改结果
     */
    @PutMapping("/user/update/mail")
    public Result<?> judgeAuthCodeAndUpdateUserMail(@RequestBody Map<String, String> data,
                                                    Authentication authentication) {
        String authCode = data.get("authCode");
        String mail = data.get("mail");

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUser().getUsername();

        int judge = authService.judgeAuthCode(username, authCode);
        // 如果redis中的验证码为空，则说明验证码失效了
        if (judge == -1) {
            return Result.error("验证码已失效");
        }
        // 判断验证码是否与用户输入验证码是否一致
        if (judge == 1) {
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getMail, mail));
            // 判断邮箱是否被其他用户注册过
            if (!Objects.isNull(user)) {
                return Result.error("该邮箱已被注册");
            }

            // 更新完成后，将redis中的验证码删除
            redisCache.deleteObject("authCode:" + username);
            userService.update(new LambdaUpdateWrapper<User>().set(User::getMail, mail).eq(User::getUsername, username));
            return Result.success("邮箱修改成功");
        } else {
            return Result.error("输入的验证码有误");
        }
    }
}
