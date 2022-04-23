package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.Recommendation;
import cn.zjw.mrs.service.RecommendationService;
import cn.zjw.mrs.mapper.RecommendationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 95758
* @description 针对表【user_recommendation】的数据库操作Service实现
* @createDate 2022-04-24 00:04:20
*/
@Service
public class RecommendationServiceImpl extends ServiceImpl<RecommendationMapper, Recommendation>
    implements RecommendationService{

    @Resource
    private RecommendationMapper recommendationMapper;

    @Override
    public List<RecommendedMovieVo> getRecommendedMoviesByUserId(Long uid) {
        List<RecommendedMovieVo> recommendedMovies = recommendationMapper.selectRecommendedMoviesByUserId(uid);
        for (RecommendedMovieVo movie: recommendedMovies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return recommendedMovies;
    }
}




