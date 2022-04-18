package cn.zjw.mrs;

import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.enums.SexEnum;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.mapper.MovieMapper;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * @Classname UserMapperTest
 * @Date 2022/4/11 16:12
 * @Created by zjw
 * @Description
 */

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testUserMapper() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }


    @Test
    public void TestBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("root");
        String encode2 = passwordEncoder.encode("12345");
        System.out.println(encode);
        System.out.println(encode2);

        boolean matches = passwordEncoder.matches("12345", "$2a$10$W041517SRn1RXfCBQqra4.HxkvP99CBCYs2OdPfZc6j2X3/KyxjCW");
        System.out.println(matches);
    }

    @Test
    public void TestEnum() {
        User user = new User();
        user.setUsername("Enum");
        user.setSex(SexEnum.FEMALE);
        userMapper.insert(user);
    }

    @Test
    public void TestGetUserTypes() {
        List<String> types = userMapper.selectUserTypes((long) 10);
        System.out.println(types);

        List<String> regions = userMapper.selectUserRegions((long) 10);
        System.out.println(regions);
    }

    @Test
    public void TestGetRecommendedMovies() {
        List<MovieCardVo> movies = movieMapper.selectRecommendedMoviesByMovieId(1295644L);
        for (MovieCardVo movie : movies) {
            System.out.println(movie);
        }
    }
}
