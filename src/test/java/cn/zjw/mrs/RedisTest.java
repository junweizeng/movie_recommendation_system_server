package cn.zjw.mrs;

import cn.zjw.mrs.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zjw
 * @Classname RedisTest
 * @Date 2022/5/14 14:23
 * @Description
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisCache redisCache;

    @Test
    public void test1() throws InterruptedException {
        redisCache.setCacheObject("test1", "123");
        redisCache.expire("test1", 10);
        Thread.sleep(5000);
        String res = redisCache.getCacheObject("test1");
        System.out.println("5========" + res);

        Thread.sleep(20000);
        res = redisCache.getCacheObject("test1");
        System.out.println("20========" + res);

        System.out.println("结束了");
    }
}
