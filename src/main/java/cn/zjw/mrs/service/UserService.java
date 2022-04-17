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

    Result<?> getUserInfo(String username);

    Result<?> getTypesAndRegions(Long id);

    /**
     * 更新用户昵称
     * 若用户昵称更新成功，同时更新评论表中该用户的昵称，返回成功信息
     * 否则，返回失败信息
     * @param nickname 新用户昵称
     * @return 更新成功或失败信息
     */
    Result<?> updateNickname(String nickname);

    /**
     * 更新用户性别
     * @param sexName 修改后的性别
     * @param username 用户账号，唯一标识
     * @return 更新成功或失败信息
     */
    Result<?> updateSex(String sexName, String username);

}
