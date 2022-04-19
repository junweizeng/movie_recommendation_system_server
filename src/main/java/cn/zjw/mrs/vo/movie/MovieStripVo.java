package cn.zjw.mrs.vo.movie;

import lombok.Data;

/**
 * @author zjw
 * @Classname MovieStripVo
 * @Date 2022/4/18 14:46
 * @Description 电影长条
 */
@Data
public class MovieStripVo {
    private Long id;

    private Long did;

    private String name;

    private double score;

    private String pic;

    private String directors;

    private String actors;

    private String regions;

    private String types;
}
