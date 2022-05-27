package cn.zjw.mrs.vo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author zjw
 * @Classname OwnCommentVo
 * @Date 2022/4/14 22:28
 * @Description
 */
@Data
public class CommentStripVo {
    private long id;

    private int score;

    private String comment;

    /**
     * 注解用于转化时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp time;

    private int agree;

    /**
     * 1表示豆瓣评论，0表示系统评论
     */
    private int type;

    private String nickname;

    private String avatar;

    /**
     * 1表示已点赞，0表示未点赞
     */
    private Integer status;
}
