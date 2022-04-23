package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zjw
 * @TableName movie_type
 */
@TableName(value ="movie_type")
@Data
public class MovieType implements Serializable {
    /**
     * 电影ID
     */
    private Long mid;

    /**
     * 类型ID
     */
    private Integer tid;

    /**
     * 程度
     */
    private Integer degree;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}