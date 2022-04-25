package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zjw
 * @TableName same_likes
 */
@TableName(value ="same_likes")
@Data
public class SameLikes implements Serializable {
    /**
     * 电影ID
     */
    private Long did;

    /**
     * 喜欢这部电影的人也喜欢的电影ID
     */
    private Long sid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}