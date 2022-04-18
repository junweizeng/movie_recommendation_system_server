package cn.zjw.mrs.vo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Classname OwnCommentVo
 * @Date 2022/4/14 22:28
 * @Created by zjw
 * @Description
 */
@Data
public class CommentStripVo {
    private int score;

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //转化时间戳
    private Timestamp time;

    private int agree;

    private String nickname;

    private String avatar;
}
