package cn.zjw.mrs;

import cn.zjw.mrs.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zjw
 * @Classname RecommendTest
 * @Date 2022/4/21 22:19
 * @Description
 */
@SpringBootTest
public class RecommendTest {
    @Resource
    private RecommendationService service;

    @Test
    public void testUpdateRecommendation() {
        service.updateRecommendation(19L);
    }
}
