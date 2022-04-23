package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.RegionLike;
import cn.zjw.mrs.mapper.RegionLikeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.TypeLike;
import cn.zjw.mrs.service.TypeLikeService;
import cn.zjw.mrs.mapper.TypeLikeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author zjw
* @description 针对表【type_like】的数据库操作Service实现
* @createDate 2022-04-20 16:13:21
*/
@Service
public class TypeLikeServiceImpl extends ServiceImpl<TypeLikeMapper, TypeLike>
    implements TypeLikeService{

    @Resource
    private TypeLikeMapper typeLikeMapper;

    @Override
    public int updateUserTypeLike(Long id, int[] types) {
        int len = Math.min(types.length, 5);
        typeLikeMapper.delete(new LambdaQueryWrapper<TypeLike>().eq(TypeLike::getUid, id));
        int cnt = 0;
        for (int i = 0; i < len; ++ i) {
            cnt += typeLikeMapper.insert(new TypeLike(id, types[i], i));
        }
        return cnt;
    }
}




