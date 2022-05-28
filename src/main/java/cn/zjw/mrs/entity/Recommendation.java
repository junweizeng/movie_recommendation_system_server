package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zjw
 * @TableName user_recommendation
 */
@TableName(value ="recommendation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 电影id
     */
    private Long mid;

    /**
     * 推荐指数
     */
    private Double idx;

    /**
     * 推荐类型（1表示基于内容，2表示协同过滤，0表示随机推荐）
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}