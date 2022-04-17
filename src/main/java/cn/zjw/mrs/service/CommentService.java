package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

/**
* @author 95758
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-04-14 19:19:45
*/
@Repository
public interface CommentService extends IService<Comment> {
    Result<?> addComment(Comment comment, String username);

    Result<?> getOwnComment(Long mid);

    Result<?> getCommentsByMovieId(Long mid);
}
