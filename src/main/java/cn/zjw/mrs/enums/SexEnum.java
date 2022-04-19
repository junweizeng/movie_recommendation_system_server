package cn.zjw.mrs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author zjw
 * @Classname SexEnum
 * @Date 2022/4/11 23:40
 * @Description
 */
@Getter
public enum SexEnum {
    // 男
    MALE(1, "男"),
    // 女
    FEMALE(0, "女"),
    // 保密
    SECRET(2, "保密");

    /**
     * 将注解所标识的属性的值存储到数据库中
     */
    @EnumValue
    private final Integer sex;
    private final String sexName;

    SexEnum(Integer sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }

    /**
     * 通过性别名称查找性别号码
     * @param sexName 性别名称
     * @return 性别号码
     */
    public static Integer findSexBySexName (String sexName) {
        for (SexEnum sexEnum : SexEnum.values()) {
            if (sexEnum.getSexName().equals(sexName)) {
                return sexEnum.getSex();
            }
        }
        throw new IllegalArgumentException("sexName is invalid");
    }

    /**
     * 通过性别号码查找性别名称
     * @param sex 性别号码
     * @return 性别名称
     */
    public static String findSexNameBySex (Integer sex) {
        for (SexEnum sexEnum : SexEnum.values()) {
            if (sexEnum.getSex().equals(sex)) {
                return sexEnum.getSexName();
            }
        }
        throw new IllegalArgumentException("sex is invalid");
    }
}
