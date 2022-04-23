package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.RegionLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zjw
* @description 针对表【region_like】的数据库操作Service
* @createDate 2022-04-20 16:13:15
*/
public interface RegionLikeService extends IService<RegionLike> {
    /**
     * 更新用户电影地区喜好
     * @param id 用户id
     * @param regions 用户的电影地区喜好
     * @return 更新结果
     */
    int updateUserRegionLike(Long id, int[] regions);
}
