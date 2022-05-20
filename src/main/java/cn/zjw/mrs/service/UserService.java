package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author zjw
* @description 针对表【user】的数据库操作Service
* @createDate 2022-04-10 22:46:38
*/
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param user 用户登录表单
     * @return 登录结果
     */
    Result<?> login(User user);

    /**
     * 用户登出
     * @return 登出结果
     */
    Result<?> logout();

    /**
     * 用户注册
     * @param user 注册表单信息
     * @return 注册结果
     */
    Result<?> register(User user);

    /**
     * 获取用户基本信息
     * @param id 用户id
     * @return 用户基本信息
     */
    UserInfoVo getUserInfo(Long id);

    /**
     * 获取用户的类型喜好和地区喜好
     * @param id 用户id
     * @return 用户的类型喜好和地区喜好
     */
    Map<String, List<?>> getTypesAndRegions(Long id);

    /**
     * 更新用户昵称
     * 若用户昵称更新成功，同时更新评论表中该用户的昵称，返回成功信息
     * 否则，返回失败信息
     * @param nickname 新用户昵称
     * @return 更新成功或失败信息
     */
    int updateNickname(String nickname);

    /**
     * 更新用户性别
     * @param sexName 修改后的性别
     * @param username 用户账号，唯一标识
     * @return 更新成功或失败信息
     */
    int updateSex(String sexName, String username);

    /**
     * 更新密码
     * @param username 用户名
     * @param prePassword 原密码
     * @param newPassword 新密码
     * @param isFindPassword 是否为找回密码，如果是找回密码则跳过原密码检测
     * @return 更新结果（-1表示用户输入的原密码不正确，0表示修改失败，1表示修改成功）
     */
    int updatePassword(String username, String prePassword, String newPassword, boolean isFindPassword);
}
