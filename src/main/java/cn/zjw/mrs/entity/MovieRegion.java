package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zjw
 * @TableName movie_region
 */
@TableName(value ="movie_region")
@Data
public class MovieRegion implements Serializable {
    /**
     * 电影ID
     */
    private Long mid;

    /**
     * 地区ID
     */
    private Integer rid;

    /**
     * 程度
     */
    private Integer degree;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}