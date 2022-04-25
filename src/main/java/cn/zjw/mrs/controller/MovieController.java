package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.SameLikes;
import cn.zjw.mrs.mapper.MovieMapper;
import cn.zjw.mrs.mapper.SameLikesMapper;
import cn.zjw.mrs.service.MovieService;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import cn.zjw.mrs.vo.movie.MovieStripVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import cn.zjw.mrs.vo.movie.relation.CategoryVo;
import cn.zjw.mrs.vo.movie.relation.LinkVo;
import cn.zjw.mrs.vo.movie.relation.NodeVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zjw
 * @Classname MovieController
 * @Date 2022/4/7 12:58
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
        Map<String, List<MovieCardVo>> res = new HashMap<>(1);
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

    /**
     * 获取搜索关键字模糊匹配的电影名称列表
     * @param keywords 搜索关键字
     * @return 模糊匹配查询到的电影名称列表
     */
    @GetMapping("/match/name")
    public Result<?> getMatchMovieName(@RequestParam(defaultValue = "我打赌你什么都查不到") String keywords) {
        List<String> names = movieService.getMatchMovieName(keywords);
        return Result.success(names);
    }

    /**
     * 获取最多人看过的（评论过的）电影列表
     * @return 最多人看过的（评论过的）电影列表
     */
    @GetMapping("/most/watched")
    public Result<?> getMostWatchedMovies() {
        List<MovieCardVo> movies = movieService.getMostWatchedMovies();
        return Result.success(movies);
    }

    /**
     * 获得评分最高的前n部电影列表
     * @return 评分最高的前n部电影列表
     */
    @GetMapping("/highest/score")
    public Result<?> getHighestRatedMovies() {
        List<MovieCardVo> movies = movieService.getHighestRatedMovies();
        return Result.success(movies);
    }

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private SameLikesMapper sameLikesMapper;
    @GetMapping("/relations")
    public Result<?> getMovieRelations() {
        // TODO 测试使用
        Map<String, List<?>> res = new HashMap<>(2);

        List<CategoryVo> categories = new ArrayList<>();
        categories.add(new CategoryVo("test"));
        categories.add(new CategoryVo("test2"));

        List<Movie> movies = movieMapper.selectList(null);
        List<NodeVo> nodes = new ArrayList<>();
        for (Movie movie: movies) {
//            nodes.add(new NodeVo(movie.getDid(), movie.getId(), movie.getName(), "1", (int) (movie.getId() % 2)), "", "");
        }

        List<SameLikes> sameLikes = sameLikesMapper.selectList(null);
        List<LinkVo> links = new ArrayList<>();
        for (SameLikes s: sameLikes) {
            LinkVo link = new LinkVo();
            link.setSource(String.valueOf(s.getDid()));
            link.setTarget(String.valueOf(s.getSid()));
            links.add(link);
        }
        res.put("categories", categories);
        res.put("nodes", nodes);
        res.put("links", links);
        return Result.success(res);
    }
}
