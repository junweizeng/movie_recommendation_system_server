package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.Recommendation;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 95758
* @description 针对表【recommendation】的数据库操作Mapper
* @createDate 2022-04-24 00:04:20
* @Entity cn.zjw.mrs.entity.Recommendation
*/
public interface RecommendationMapper extends BaseMapper<Recommendation> {

    /**
     * 获取用户id为uid的电影推荐列表
     * @param num 推荐指数排名前n部电影
     * @param uid 用户id
     * @return 电影推荐列表
     */
    List<RecommendedMovieVo> selectRecommendedMoviesByUserId(Long uid, Integer num);
}




