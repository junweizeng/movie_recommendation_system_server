package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName movie_feature
 */
@TableName(value ="movie_feature")
@Data
public class MovieFeature implements Serializable {
    /**
     * 电影id
     */
    @TableId
    private Long mid;

    /**
     * 电影特征矩阵
     */
    private String matrix;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}