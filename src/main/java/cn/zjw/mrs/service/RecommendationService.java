package cn.zjw.mrs.service;

import cn.hutool.core.lang.Pair;
import cn.zjw.mrs.entity.Recommendation;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;
import java.util.Map;

/**
* @author 95758
* @description 针对表【recommendation】的数据库操作Service
* @createDate 2022-04-24 00:04:20
*/
public interface RecommendationService extends IService<Recommendation> {
    /**
     * 解决用户冷启动问题，用户注册是调用
     * @param uid 用户id
     */
    void solveColdStart(long uid);

    /**
     * 随机电影推荐，从100部电影中随机获取num部电影推荐给用户
     * @param uid 用户id
     * @param num 获取随机电影数量
     * @return 推荐结果
     */
    List<Recommendation> randomRecommended(Long uid, Integer num);

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
     * @param uid 用户id
     */
    void updateRecommendation(Long uid);
}
