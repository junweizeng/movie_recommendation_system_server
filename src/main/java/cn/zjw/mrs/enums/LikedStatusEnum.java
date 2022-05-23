package cn.zjw.mrs.enums;

import lombok.Getter;

/**
 * @author zjw
 * @Classname LikedStatusEnum
 * @Date 2022/5/22 16:51
 * @Description
 */
@Getter
public enum LikedStatusEnum {
    /**
     * 用户点赞的状态
     */
    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞"),;

    private final Integer code;

    private final String msg;

    LikedStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
