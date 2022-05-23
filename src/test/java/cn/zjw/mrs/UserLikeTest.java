package cn.zjw.mrs;

import cn.zjw.mrs.service.UserLikeRedisService;
import cn.zjw.mrs.service.UserLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author zjw
 * @Classname UserLikeTest
 * @Date 2022/5/22 19:39
 * @Description
 */
@SpringBootTest
public class UserLikeTest {

    @Resource
    private UserLikeRedisService userLikeRedisService;

    @Resource
    private UserLikeService userLikeService;

    @Test
    public void test() {
        userLikeRedisService.likeComment(3367, 21);
        userLikeRedisService.increaseLikedCount(10);

        userLikeRedisService.unlikeComment(20, 10);
        userLikeRedisService.decreaseLikedCount(20);

        int status = userLikeRedisService.getUserLikeStatusFromRedis(30, 40);
        System.out.println("点赞状态:" + status);

        status = userLikeRedisService.getUserLikeStatusFromRedis(3367, 21);
        System.out.println("点赞状态:" + status);

        status = userLikeRedisService.getUserLikeStatusFromRedis(20, 10);
        System.out.println("点赞状态:" + status);

//        List<UserLike> userLikes = redisService.getLikedDataFromRedis();
//        userLikes.forEach(System.out::println);
//
//        List<Comment> comments = redisService.getLikedCountFromRedis();
//        comments.forEach(System.out::println);

//        userLikeService.transLikedFromRedis2Database();
    }
}
