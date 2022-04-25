package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.RecommendationService;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zjw
 * @Classname RecommendationController
 * @Date 2022/4/24 0:18
 * @Description
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Resource
    private RecommendationService recommendationService;

    @GetMapping
    public Result<?> getRecommendedMovies(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        List<RecommendedMovieVo> recommendedMovies = recommendationService.getRecommendedMoviesByUserId(uid);

        return Result.success(recommendedMovies);
    }

    /**
     * 获取看过的电影和推荐电影之间的联系，用于绘制Echarts关系图
     * @return 看过的电影和推荐电影之间的联系
     */
    @GetMapping("/relations")
    public Result<?> getLinksBetweenWatchedMoviesAndRecommendedMovies(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        Map<String, List<?>> res = recommendationService.getLinksBetweenWatchedMoviesAndRecommendedMovies(uid);
        return Result.success(res);
    }
}
