package cn.zjw.mrs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.MovieFeature;
import cn.zjw.mrs.service.MovieFeatureService;
import cn.zjw.mrs.mapper.MovieFeatureMapper;
import org.springframework.stereotype.Service;

/**
* @author zjw
* @description 针对表【movie_feature】的数据库操作Service实现
* @createDate 2022-04-22 10:29:18
*/
@Service
public class MovieFeatureServiceImpl extends ServiceImpl<MovieFeatureMapper, MovieFeature>
    implements MovieFeatureService{

}




