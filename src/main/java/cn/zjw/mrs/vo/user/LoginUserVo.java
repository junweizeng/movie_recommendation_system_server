package cn.zjw.mrs.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjw
 * @Classname LoginUserVo
 * @Date 2022/4/12 13:49
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserVo {

    private String token;
    private UserInfoVo userInfo;
}
