package cn.zjw.mrs;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.vo.comment.OwnCommentVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

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
        OwnCommentVo ownCommentVo = commentMapper.selectOwnCommentByUidAndMid((long) 11, (long) 5);
        System.out.println(ownCommentVo);
    }

    @Test
    public void getCommentsByMovieId() {
        List<OwnCommentVo> ownCommentVo = commentMapper.selectCommentsByMovieId((long) 5);
        System.out.println(ownCommentVo);
    }
}
