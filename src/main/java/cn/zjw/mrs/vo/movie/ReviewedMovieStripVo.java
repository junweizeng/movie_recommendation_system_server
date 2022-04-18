package cn.zjw.mrs.vo.movie;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @Classname ReviewedMovieStripVo
 * @Date 2022/4/18 21:32
 * @Created by zjw
 * @Description 评价过的电影
 */
@Data
public class ReviewedMovieStripVo {
    private Long id;

    private Long did;

    private String name;

    /**
     * 电影评分
     */
    private String score;

    /**
     * 用户评分
     */
    private double userScore;

    private String pic;

    private String directors;

    private String actors;

    private String regions;

    private String types;

    /**
     * 评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")    //转化时间戳
    private Timestamp time;
}
