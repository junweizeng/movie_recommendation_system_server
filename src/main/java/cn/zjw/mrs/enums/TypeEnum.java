package cn.zjw.mrs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author zjw
 * @Classname TypeEnum
 * @Date 2022/4/12 22:48
 * @Description
 */
@Getter
public enum TypeEnum {
    // 类型标识 —— 类型名称
    DRAMA(0, "剧情"),
    COMEDY(1, "喜剧"),
    ACTION(2, "动作"),
    LOVE(3, "爱情"),
    SCIENCE_FICTION(4, "科幻"),
    ANIMATION(5, "动画"),
    SUSPENSE(6, "悬疑"),
    THRILLER(7, "惊悚"),
    HORROR(8, "恐怖"),
    CRIME(9, "犯罪"),
    MUSIC(10, "音乐"),
    SONG_AND_DANCE(11, "歌舞"),
    BIOGRAPHY(12, "传记"),
    HISTORY(13, "历史"),
    WAR(14, "战争"),
    WEST(15, "西部"),
    FANTASY(16, "奇幻"),
    ADVENTURE(17, "冒险"),
    DISASTER(18, "灾难"),
    MARTIAL(19, "武侠"),
    OTHER(20, "其他");

    /**
     * 类型标识
     * 将注解所标识的属性的值存储到数据库中
     */
    @EnumValue
    private final Integer type;
    /**
     * 类型名称
     */
    private final String typeName;

    TypeEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    /**
     * 通过类型名称查找类型标识
     * @param typeName 类型名称
     * @return 类型标识
     */
    public static Integer findTypeByTypeName (String typeName) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getTypeName().equals(typeName)) {
                return typeEnum.getType();
            }
        }
        return -1;
    }

    /**
     * 通过类型标识查找类型名称
     * @param type 类型标识
     * @return 类型名称
     */
    public static String findRegionNameByRegion (Integer type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum.getTypeName();
            }
        }
        throw new IllegalArgumentException("type is invalid");
    }
}
