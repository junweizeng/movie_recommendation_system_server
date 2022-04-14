package cn.zjw.mrs.vo;

import cn.zjw.mrs.enums.SexEnum;
import lombok.Data;

/**
 * @Classname UserInfoVo
 * @Date 2022/4/12 13:53
 * @Created by zjw
 * @Description
 */
@Data
public class UserInfoVo {
    private Long id;

    private String nickname;

    String avatar;

    private String sex;
}
