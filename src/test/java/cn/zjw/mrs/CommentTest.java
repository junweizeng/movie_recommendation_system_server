package cn.zjw.mrs;

import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Classname CommentTest
 * @Date 2022/4/14 22:09
 * @Created by zjw
 * @Description
 */
@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void getOwnCommentTest() {
        CommentStripVo commentStripVo = commentMapper.selectOwnCommentByUidAndMid((long) 11, (long) 5);
        System.out.println(commentStripVo);
    }

    @Test
    public void getCommentsByMovieId() {
        List<CommentStripVo> commentStripVo = commentMapper.selectCommentsByMovieId((long) 5);
        System.out.println(commentStripVo);
    }

    @Test
    public void getCommentMovieMoments() {
        List<CommentMovieVo> commentMovieVos = commentMapper.selectOwnCommentMovieMoments((long) 10);
        for (CommentMovieVo commentStripVo: commentMovieVos) {
            System.out.println(commentStripVo.getCommentStripVo().toString());
            System.out.println(commentStripVo.getMovieStripVo().toString());
            System.out.println();
        }
        System.out.println(commentMovieVos);
    }
}
