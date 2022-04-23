package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}