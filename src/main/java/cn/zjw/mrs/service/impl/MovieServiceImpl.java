package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.Result;
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
import java.util.List;

/**
* @author 95758
* @description 针对表【movie】的数据库操作Service实现
* @createDate 2022-04-07 19:01:20
*/
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie>
    implements MovieService{

    @Resource
    MovieMapper movieMapper;

    @Override
    public Page<Movie> getPageMovies(Integer currentPage, Integer pageSize, String type, String region, String search) {
        Page<Movie> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Movie> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Movie::getId, Movie::getDid, Movie::getName, Movie::getDirectors, Movie::getActors,
                        Movie::getTypes, Movie::getRegions, Movie::getScore, Movie::getPic)       // 选取电影中的某几个字段
                .like(!type.equals("全部"), Movie::getTypes, type)                // 模糊匹配电影类型
                .like(!region.equals("全部"), Movie::getRegions, region)          // 模糊匹配电影地区
                .like(StringUtils.isNotEmpty(search), Movie::getName, search);   // 模糊匹配电影名
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
    public List<ReviewedMovieStripVo> getAllReviewedMoviesByUserId(Long uid) {
        List<ReviewedMovieStripVo> reviewedMovies = movieMapper.selectAllReviewedMoviesByUserId(uid);
        for (ReviewedMovieStripVo movie: reviewedMovies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return reviewedMovies;
    }
}




