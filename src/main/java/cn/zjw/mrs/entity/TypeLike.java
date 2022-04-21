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
 * 
 * @author zjw
 * @TableName type_like
 */
@TableName(value ="type_like")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeLike implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 电影类型id
     */
    private Integer tid;

    /**
     * 喜爱程度
     */
    private Integer degree;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}