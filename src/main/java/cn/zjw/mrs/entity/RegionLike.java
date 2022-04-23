package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zjw
 * @TableName region_like
 */
@TableName(value ="region_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionLike implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 电影地区id
     */
    private Integer rid;

    /**
     * 喜爱程度
     */
    private Integer degree;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}