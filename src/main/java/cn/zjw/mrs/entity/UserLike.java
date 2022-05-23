package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @author zjw
 * @TableName user_like
 */
@TableName(value ="user_like")
@Data
public class UserLike implements Serializable {
    /**
     * ID号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被点赞的评论id
     */
    private Long cid;

    /**
     * 点赞的用户id
     */
    private Long uid;

    /**
     * 点赞状态（0表示未点赞，1表示已点赞）
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public UserLike(Long cid, Long uid, Integer status) {
        this.cid = cid;
        this.uid = uid;
        this.status = status;
    }
}