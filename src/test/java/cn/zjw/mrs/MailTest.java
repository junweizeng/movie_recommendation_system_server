package cn.zjw.mrs;
import cn.zjw.mrs.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zjw
 * @Classname MailTest
 * @Date 2022/5/10 18:12
 * @Description
 */
@SpringBootTest
public class MailTest {

    @Resource
    private MailService mailService;

    @Test
    public void sendMailTest() {
        mailService.sendSimpleMail("957589005@qq.com",
                "电影推荐系统",
                "【电影推荐系统】验证码：666666，用于验证你的邮箱，如非本人操作，请忽略。");
    }
}
