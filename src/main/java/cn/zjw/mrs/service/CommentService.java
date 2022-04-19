package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 95758
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-04-14 19:19:45
*/
@Repository
public interface CommentService extends IService<Comment> {
    /**
     * 添加评价信息，如果评价信息已经存在，则更新评价信息
     * @param comment 评价信息
     * @param username 用户账号（唯一标识）
     * @return
     * -1表示评价更新失败；
     * 1表示评论更新成功；
     * -2表示评价失败；
     * 2表示评论成功
     */
    Integer addComment(Comment comment, String username);

    /**
     * 获取某个用户对某部电影的评价信息
     * @param uid 用户id
     * @param mid 电影id
     * @return 对电影的评价信息
     */
    CommentStripVo getOwnComment(Long uid, Long mid);

    /**
     * 获取某部电影下的若干评价信息
     * @param mid 电影id
     * @return 若干电影评价信息
     */
    List<CommentStripVo> getCommentsByMovieId(Long mid);

    /**
     * 获取指定用户的电影评价动态
     * @param uid 用户id
     * @return 电影评价动态条目
     */
    List<CommentMovieVo> getOwnCommentMovieMoments(Long uid);
}
