package cn.zjw.mrs.vo.comment;

import cn.zjw.mrs.vo.movie.MovieStripVo;
import lombok.Data;

/**
 * @author zjw
 * @Classname CommentMovieVo
 * @Date 2022/4/18 15:21
 * @Description
 */
@Data
public class CommentMovieVo {

    private CommentStripVo commentStripVo;

    private MovieStripVo movieStripVo;

}
