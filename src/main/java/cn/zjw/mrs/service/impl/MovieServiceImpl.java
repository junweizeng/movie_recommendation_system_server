package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.Movie;
import cn.zjw.mrs.service.MovieService;
import cn.zjw.mrs.mapper.MovieMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author zjw
* @description 针对表【movie】的数据库操作Service实现
* @createDate 2022-04-07 19:01:20
*/
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie>
    implements MovieService{
    private static final String TOTAL = "全部";

    @Resource
    MovieMapper movieMapper;

    @Override
    public Page<Movie> getPageMovies(Integer currentPage, Integer pageSize, String type, String region, String search) {
        Page<Movie> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Movie> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // select用于选取电影中的某几个字段
        lambdaQueryWrapper.select(Movie::getId, Movie::getDid, Movie::getName, Movie::getDirectors, Movie::getActors,
                        Movie::getTypes, Movie::getRegions, Movie::getScore, Movie::getPic)
                // 模糊匹配电影类型
                .like(!type.equals(TOTAL), Movie::getTypes, type)
                // 模糊匹配电影地区
                .like(!region.equals(TOTAL), Movie::getRegions, region)
                // 模糊匹配电影名
                .like(StringUtils.isNotEmpty(search), Movie::getName, search);
        movieMapper.selectPage(page, lambdaQueryWrapper);

        List<Movie> movies = page.getRecords();
        for (Movie movie: movies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return page;
    }

    @Override
    public List<MovieCardVo> getRecommendedMoviesByMovieId(Long did) {
        List<MovieCardVo> movies = movieMapper.selectRecommendedMoviesByMovieId(did);
        for (MovieCardVo movie: movies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return movies;
    }

    @Override
    public Map<String, Object> getMoreReviewedMoviesByUserId(Long uid, Integer currentPage, Integer pageSize) {
        Integer currentIndex = (currentPage - 1) * pageSize;
        List<ReviewedMovieStripVo> reviewedMovies = movieMapper.selectMoreReviewedMoviesByUserId(uid, currentIndex, pageSize);
        for (ReviewedMovieStripVo movie: reviewedMovies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }

        // 获取用户评价过的电影总条目数
        List<ReviewedMovieStripVo> totalMovies = movieMapper.selectMoreReviewedMoviesByUserId(uid, 0, 10000);
        Integer total = totalMovies.size();

        Map<String, Object> page = new HashMap<>(2);
        page.put("records", reviewedMovies);
        page.put("total", total);
        return page;
    }

    @Override
    public List<String> getMatchMovieName(String keywords) {
        List<Movie> movies = movieMapper.selectList(new LambdaQueryWrapper<Movie>().like(Movie::getName, keywords));
        int len = Math.min(movies.size(), 10);
        List<String> res = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            res.add(movies.get(i).getName());
        }
        return res;
    }

    @Override
    public List<MovieCardVo> getMostWatchedMovies() {
        List<MovieCardVo> movies = movieMapper.selectMostWatchedMovies(10);
        for (MovieCardVo movie: movies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return movies;
    }

    @Override
    public List<MovieCardVo> getHighestRatedMovies() {
        List<MovieCardVo> movies = movieMapper.selectHighestRatedMovies(10);
        for (MovieCardVo movie: movies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return movies;
    }
}




