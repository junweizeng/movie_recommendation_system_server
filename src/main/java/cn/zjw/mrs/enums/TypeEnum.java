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
    // 类型代码 —— 类型名称
    DRAMA(1, "剧情"),
    COMEDY(2, "喜剧"),
    ACTION(3, "动作"),
    LOVE(4, "爱情"),
    SCIENCE_FICTION(5, "科幻"),
    ANIMATION(6, "动画"),
    SUSPENSE(7, "悬疑"),
    THRILLER(8, "惊悚"),
    HORROR(9, "恐怖"),
    CRIME(10, "犯罪"),
    MUSIC(11, "音乐"),
    SONG_AND_DANCE(12, "歌舞"),
    BIOGRAPHY(13, "传记"),
    HISTORY(14, "历史"),
    WAR(15, "战争"),
    WEST(16, "西部"),
    FANTASY(17, "奇幻"),
    ADVENTURE(18, "冒险"),
    DISASTER(19, "灾难"),
    MARTIAL(20, "武侠"),
    OTHER(21, "其他");

    /**
     * 将注解所标识的属性的值存储到数据库中
     */
    @EnumValue
    private final Integer type;
    private final String typeName;

    TypeEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }
}
