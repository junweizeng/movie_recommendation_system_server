package cn.zjw.mrs.vo.movie;

import lombok.Data;

/**
 * @Classname MovieCardVo
 * @Date 2022/4/13 14:35
 * @Created by zjw
 * @Description 电影卡片
 */
@Data
public class MovieCardVo {
    private Long id;

    private String name;

    private double score;
    
    private String pic;
}
