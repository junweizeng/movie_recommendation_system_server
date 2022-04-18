package cn.zjw.mrs;

import cn.zjw.mrs.mapper.MovieMapper;
import cn.zjw.mrs.vo.movie.MovieStripVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Classname MovieTest
 * @Date 2022/4/18 21:29
 * @Created by zjw
 * @Description
 */
@SpringBootTest
public class MovieTest {

    @Resource
    private MovieMapper movieMapper;

    @Test
    public void selectAllReviewedMoviesByUserId() {
        List<ReviewedMovieStripVo> movies = movieMapper.selectAllReviewedMoviesByUserId(10L);
        for (ReviewedMovieStripVo movie : movies) {
            System.out.println(movie.toString());
        }
    }
}
