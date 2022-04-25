package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.MovieRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 95758
* @description 针对表【movie_region】的数据库操作Mapper
* @createDate 2022-04-21 22:58:21
* @Entity cn.zjw.mrs.entity.MovieRegion
*/
public interface MovieRegionMapper extends BaseMapper<MovieRegion> {
    /**
     * 获取用户id为uid的用户，其推荐电影列表中每部电影的地区
     * @param uid 用户id
     * @return 推荐电影列表中每部电影的地区
     */
    List<MovieRegion> selectRecommendedMoviesRegionsByUserId(Long uid);

    /**
     * 获取用户id为uid的用户，其看过的电影列表（最近看过的前num部电影）中每部电影的地区
     * @param uid 用户id
     * @param num 最近评价过的num部电影
     * @return 看过的电影列表中每部电影的地区
     */
    List<MovieRegion> selectWatchedMoviesRegionsByUserId(Long uid, Integer num);
}




