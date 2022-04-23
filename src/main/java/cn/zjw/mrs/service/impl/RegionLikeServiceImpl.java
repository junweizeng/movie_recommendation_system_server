package cn.zjw.mrs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.RegionLike;
import cn.zjw.mrs.service.RegionLikeService;
import cn.zjw.mrs.mapper.RegionLikeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author zjw
* @description 针对表【region_like】的数据库操作Service实现
* @createDate 2022-04-20 16:13:15
*/
@Service
public class RegionLikeServiceImpl extends ServiceImpl<RegionLikeMapper, RegionLike>
    implements RegionLikeService{

    @Resource
    private RegionLikeMapper regionLikeMapper;

    @Override
    public int updateUserRegionLike(Long id, int[] regions) {
        int len = Math.min(regions.length, 5);
        regionLikeMapper.delete(new LambdaQueryWrapper<RegionLike>().eq(RegionLike::getUid, id));
        int cnt = 0;
        for (int i = 0; i < len; ++ i) {
            cnt += regionLikeMapper.insert(new RegionLike(id, regions[i], i));
        }
        return cnt;
    }
}




