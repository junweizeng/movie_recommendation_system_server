package cn.zjw.mrs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Classname SexEnum
 * @Date 2022/4/11 23:40
 * @Created by zjw
 * @Description
 */
@Getter
public enum SexEnum {
    MALE(1, "男"),
    FEMALE(0, "女"),
    SECRET(2, "保密");

    @EnumValue
    private final Integer sex; // 将注解所标识的属性的值存储到数据库中
    private final String sexName;

    SexEnum(Integer sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }
}
