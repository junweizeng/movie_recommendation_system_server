package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Recommendation;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 95758
* @description 针对表【recommendation】的数据库操作Service
* @createDate 2022-04-24 00:04:20
*/
public interface RecommendationService extends IService<Recommendation> {

    /**
     * 通过用户id获取数据库中的电影推荐列表
     * @param uid 用户id
     * @return 电影推荐列表
     */
    List<RecommendedMovieVo> getRecommendedMoviesByUserId(Long uid);

    /**
     * 获取用户id为uid的用户，看过的电影和推荐电影之间的联系，用于绘制Echarts关系图
     * @param uid 用户id
     * @return 看过的电影和推荐电影之间的联系
     */
    Map<String, List<?>> getLinksBetweenWatchedMoviesAndRecommendedMovies(Long uid);

    /**
     * 更新指定用户推荐结果
     * 这里用到基于内容的电影推荐算法
     * @param uid 用户id
     */
    void updateRecommendation(Long uid);
}
