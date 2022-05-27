package cn.zjw.mrs;

import cn.zjw.mrs.entity.MovieRegion;
import cn.zjw.mrs.entity.MovieType;
import cn.zjw.mrs.mapper.MovieMapper;
import cn.zjw.mrs.mapper.MovieRegionMapper;
import cn.zjw.mrs.mapper.MovieTypeMapper;
import cn.zjw.mrs.vo.movie.MovieStripVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import com.jayway.jsonpath.internal.function.text.Length;
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

    @Resource
    private MovieTypeMapper movieTypeMapper;

    @Resource
    private MovieRegionMapper movieRegionMapper;

    @Test
    public void selectAllReviewedMoviesByUserId() {
        List<ReviewedMovieStripVo> movies = movieMapper.selectMoreReviewedMoviesByUserId(10L, 1, 10);
        for (ReviewedMovieStripVo movie : movies) {
            System.out.println(movie.toString());
        }
    }

    @Test
    public void testMovieTypeAndRegionMapper() {
        List<MovieType> movieTypes = movieTypeMapper.selectRecommendedMoviesTypesByUserId(10L);
        List<MovieType> movieTypes1 = movieTypeMapper.selectWatchedMoviesTypesByUserId(10L, 30);
        System.out.println(movieTypes);
        System.out.println(movieTypes.size());
        System.out.println();
        System.out.println(movieTypes1);
        System.out.println(movieTypes1.size());

        List<MovieRegion> movieRegions = movieRegionMapper.selectRecommendedMoviesRegionsByUserId(10L);
        List<MovieRegion> movieRegions1 = movieRegionMapper.selectWatchedMoviesRegionsByUserId(10L, 30);
        System.out.println(movieRegions);
        System.out.println(movieRegions.size());
        System.out.println();
        System.out.println(movieRegions1);
        System.out.println(movieRegions1.size());
    }
}
