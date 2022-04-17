package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 95758
* @description 针对表【user】的数据库操作Service
* @createDate 2022-04-10 22:46:38
*/
public interface UserService extends IService<User> {

    Result<?> login(User user);

    Result<?> logout();

    Result<?> register(User user);

    Result<?> getTypesAndRegions(Long id);

    Result<?> updateNickname(String nickname, String username);

    Result<?> updateSex(Integer sex, String username);
}
