package cn.zjw.mrs;

import cn.zjw.mrs.service.RecommendationService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

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
        service.updateRecommendation(10L);
    }

    @Test
    public void testUserBasedRecommender() {
//        List<RecommendedItem> recommendedItems = service.getUserBasedMovieRecommendationResult(10, 100);
//        recommendedItems.forEach(System.out::println);
        System.out.println("test testUserBasedRecommender------------");
    }
}
