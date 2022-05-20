package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Result;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zjw
 * @Classname AuthService
 * @Date 2022/5/10 18:08
 * @Description
 */
public interface AuthService {

    /**
     * 发送邮件验证码
     * @param username 用户名
     * @param mail 待接收的邮箱号码
     */
    void sendMailAuthCode(String username, String mail);

    /**
     * 校验验证码
     * @param username 用户名
     * @param authCode 待校验的验证码
     * @return 校验结果。
     *      -1表示验证码已过期；
     *      1表示输入的验证码正确；
     *      0表示输入的验证码有误；
     */
    int judgeAuthCode(String username, String authCode);

    /**
     * 判断邮箱是否属于用户
     * @param username 用户名
     * @param mail 邮箱
     * @return true表示邮箱属于用户；false表示邮箱不属于用户。
     */
    boolean judgeMailBelongToUser(String username, String mail);

    /**
     * 判断用户是否存在于数据库中
     * @param username 待判断的用户名
     * @return true表示存在；false表示不存在。
     */
    boolean judgeUsernameExists(String username);
}
