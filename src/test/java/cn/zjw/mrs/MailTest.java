package cn.zjw.mrs;
import cn.zjw.mrs.service.AuthService;
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
    private AuthService authService;

    @Test
    public void sendMailTest() {
        authService.sendMailAuthCode("123", "xxx@qq.com");
    }
}
