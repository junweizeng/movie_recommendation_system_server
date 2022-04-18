package cn.zjw.mrs.vo.comment;

import cn.zjw.mrs.vo.movie.MovieStripVo;
import lombok.Data;

/**
 * @Classname CommentMovieVo
 * @Date 2022/4/18 15:21
 * @Created by zjw
 * @Description
 */
@Data
public class CommentMovieVo {

    private CommentStripVo commentStripVo;

    private MovieStripVo movieStripVo;

}
