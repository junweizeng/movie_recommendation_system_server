package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.MovieService;
import cn.zjw.mrs.vo.MovieCardVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public Result<?> getPagesByTypeAndRegion(@RequestParam(defaultValue = "1") Integer currentPage,
                                             @RequestParam(defaultValue = "12") Integer pageSize,
                                             @RequestParam(defaultValue = "全部") String type,
                                             @RequestParam(defaultValue = "全部") String region,
                                             @RequestParam(defaultValue = "") String search) {
        Page<Movie> page = movieService.getPageMovies(currentPage, pageSize, type, region, search);
        return Result.success(page);
    }

    @RequestMapping("/info")
//    @PreAuthorize("hasAuthority('tourist')")
    public Result<?> getMovie(@RequestParam Integer id) {
        Movie movie = movieService.getById(id);
        return Result.success(movie);
    }

    @RequestMapping("/recommend")
    public Result<?> getRecommendedMoviesByMovieId(@RequestParam Integer id) {
        Integer did = movieService.getById(id).getDid();
        List<MovieCardVo> movies = movieService.getRecommendedMoviesByMovieId(did);
        Map<String, List<MovieCardVo>> res = new HashMap<>();
        res.put("movies", movies);
        return Result.success(res);
    }
}
