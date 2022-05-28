package cn.zjw.mrs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author zjw
 * @Classname RecommendationTypeEnum
 * @Date 2022/5/28 16:34
 * @Description
 */
@Getter
public enum RecommendationTypeEnum {
    /**
     * 1表示基于内容，2表示协同过滤，0表示随机推荐
     */
    RANDOM(0, "随机推荐"),
    CONTENT_BASED(1, "基于内容推荐"),
    USER_BASED_CF(2, "基于用户协同过滤推荐");

    /**
     * 推荐类型代码
     */
    @EnumValue
    private final Integer typeCode;
    /**
     * 推荐类型名称
     */
    private final String typeName;

    RecommendationTypeEnum(Integer typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }
}
