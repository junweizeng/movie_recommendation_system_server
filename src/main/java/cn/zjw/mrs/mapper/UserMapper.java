package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 95758
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-04-10 22:46:38
* @Entity cn.zjw.mrs.entity.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户的类型喜好
     * @param id 用户id
     * @return 类型喜好
     */
    List<String> selectUserTypes(Long id);

    /**
     * 获取用户的地区喜好
     * @param id 用户id
     * @return 地区喜好
     */
    List<String> selectUserRegions(Long id);
}




