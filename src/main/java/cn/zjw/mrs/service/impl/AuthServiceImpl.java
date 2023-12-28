package cn.zjw.mrs.service.impl;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.service.AuthService;
import cn.zjw.mrs.service.UserService;
import cn.zjw.mrs.utils.MailUtil;
import cn.zjw.mrs.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zjw
 * @Classname MailServiceImpl
 * @Date 2022/5/10 18:09
 * @Description
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisCache redisCache;

    @Resource
    private MailUtil mailUtil;

    @Resource
    private UserMapper userMapper;

    @Override
    public void sendMailAuthCode(String username, String mail) {
        // 1.自定义纯数字的验证码（随机6位数字，可重复）
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String authCode = randomGenerator.generate();

        // 2.将验证码暂存入redis中，并且设置5分钟内有效
        String redisKey = "authCode:" + username;
        redisCache.setCacheObject(redisKey, authCode);
        redisCache.expire(redisKey, 5, TimeUnit.MINUTES);

        // 3.发送邮件给相应用户
        String subject = "【电影推荐系统】验证码" + authCode;
        String content = "尊敬的用户，您好:<br/>"
                + "&emsp;&emsp;本次请求的邮件验证码为: <span style=\"color: red; font-size: 25px;\">"
                + authCode + "</span>,本验证码 5 分钟内有效，请及时输入。（请勿泄露此验证码）<br/>"
                + "&emsp;&emsp;如非本人操作，请忽略该邮件。<br/>&emsp;&emsp;(这是一封通过自动发送的邮件，请不要直接回复）";
        mailUtil.sendHtmlMail(mail, subject, content);
    }

    @Override
    public int judgeAuthCode(String username, String authCode) {
        String redisKey = "authCode:" + username;
        String realAuthCode = redisCache.getCacheObject(redisKey);
        if (realAuthCode == null) {
            return -1;
        } else if (authCode.equals(realAuthCode)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean judgeMailBelongToUser(String username, String mail) {
        User user = userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getUsername, username));
        // 如果user不为空，user中的mail不为空，且user中的mail和传入mail相等，返回true
        return !Objects.isNull(user) && user.getMail() != null && user.getMail().equals(mail);
    }

    @Override
    public boolean judgeUsernameExists(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        return !Objects.isNull(user);
    }
}
