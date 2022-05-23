package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author zjw
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 电影ID
     */
    private Long mid;

    /**
     * 短评
     */
    private String comment;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 评价时间
     * 注解用于转化时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp time;

    /**
     * 赞同数
     */
    private Integer agree;

    /**
     * 0表示系统 1表示豆瓣
     */
    private Integer type;

    /**
     * 用户名
     */
    private String nickname;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}