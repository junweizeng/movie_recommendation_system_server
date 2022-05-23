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

    private String nickname;

    private String avatar;

    private Integer status;
}
