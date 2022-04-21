package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.TypeLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 95758
* @description 针对表【type_like】的数据库操作Service
* @createDate 2022-04-20 16:13:21
*/
public interface TypeLikeService extends IService<TypeLike> {
    /**
     * 更新用户电影类型喜好
     * @param id 用户id
     * @param types 用户的电影类型喜好
     * @return 更新结果
     */
    int updateUserTypeLike(Long id, int[] types);

    /**
     * 更新用户电影地区喜好
     * @param id 用户id
     * @param regions 用户的电影地区喜好
     * @return 更新结果
     */
    int updateUserRegionLike(Long id, int[] regions);
}
