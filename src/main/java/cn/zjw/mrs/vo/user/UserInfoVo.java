package cn.zjw.mrs.vo.user;

import cn.zjw.mrs.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjw
 * @Classname UserInfoVo
 * @Date 2022/4/12 13:53
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String sex;
}
