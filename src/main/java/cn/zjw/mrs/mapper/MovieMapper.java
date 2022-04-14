package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
* @author 95758
* @description 针对表【movie】的数据库操作Mapper
* @createDate 2022-04-07 19:01:20
* @Entity cn.zjw.mrs.entity.Movie
*/
@Repository
public interface MovieMapper extends BaseMapper<Movie> {
    List<MovieCardVo> selectRecommendedMoviesByMovieId(Integer id);
}




