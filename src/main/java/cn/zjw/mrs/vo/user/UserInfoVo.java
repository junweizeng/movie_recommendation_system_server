package cn.zjw.mrs.vo.user;

import cn.zjw.mrs.enums.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname UserInfoVo
 * @Date 2022/4/12 13:53
 * @Created by zjw
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private Long id;

    private String nickname;

    String avatar;

    private String sex;
}
