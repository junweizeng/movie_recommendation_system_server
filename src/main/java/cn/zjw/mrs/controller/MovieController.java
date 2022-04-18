package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.MovieService;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import cn.zjw.mrs.vo.movie.MovieStripVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname MovieController
 * @Date 2022/4/7 12:58
 * @Created by zjw
 * @Description
 */
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Resource
    private MovieService movieService;

    /**
     * 获取一整页电影信息列表
     * @param currentPage 当前页码
     * @param pageSize 每页电影数量
     * @param type 电影类型
     * @param region 电影地区
     * @param search 搜索关键字
     * @return 电影列表
     */
    @GetMapping
    public Result<?> getPagesByTypeAndRegion(@RequestParam(defaultValue = "1") Integer currentPage,
                                             @RequestParam(defaultValue = "12") Integer pageSize,
                                             @RequestParam(defaultValue = "全部") String type,
                                             @RequestParam(defaultValue = "全部") String region,
                                             @RequestParam(defaultValue = "") String search) {
        Page<Movie> page = movieService.getPageMovies(currentPage, pageSize, type, region, search);
        return Result.success(page);
    }

    /**
     * 通过电影id获取这部电影的详细信息
     * @param id 电影id
     * @return 电影详情
     */
    @GetMapping("/info")
    public Result<?> getMovie(@RequestParam Integer id) {
        Movie movie = movieService.getById(id);
        movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        return Result.success(movie);
    }

    /**
     * 通过电影id获取“喜欢这部电影的人也喜欢...”的推荐电影
     * @param id 待查询的电影id
     * @return 推荐电影列表
     */
    @GetMapping( "/recommend")
    public Result<?> getRecommendedMovies(@RequestParam Long id) {
        Long did = movieService.getById(id).getDid();
        List<MovieCardVo> movies = movieService.getRecommendedMoviesByMovieId(did);
        Map<String, List<MovieCardVo>> res = new HashMap<>();
        res.put("movies", movies);
        return Result.success(res);
    }

    /**
     * 获取用户id为uid的用户评价过的所有电影的基本信息
     * @param authentication
     * @return 评价过的电影条目
     */
    @GetMapping("/reviewed")
    public Result<?> getAllReviewedMovies(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        List<ReviewedMovieStripVo> reviewedMovies = movieService.getAllReviewedMoviesByUserId(uid);
        return Result.success(reviewedMovies);
    }
}
