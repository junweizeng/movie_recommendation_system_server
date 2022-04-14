package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 95758
* @description 针对表【movie】的数据库操作Service
* @createDate 2022-04-07 19:01:20
*/
public interface MovieService extends IService<Movie> {

    /**
     * 返回电影分页结果
     * @param currentPage 当前页码
     * @param pageSize 每页电影数量
     * @param type 电影类型
     * @param region 电影地区
     * @param search 搜索关键字
     * @return 分页结果-电影列表
     */
    Page<Movie> getPageMovies(Integer currentPage, Integer pageSize, String type, String region, String search);

    List<MovieCardVo> getRecommendedMoviesByMovieId(Integer id);
}
