package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Classname CommentController
 * @Date 2022/4/14 19:20
 * @Created by zjw
 * @Description
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping
    private Result<?> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/own")
    private Result<?> getOwnComment(@RequestParam Long mid) {
        return commentService.getOwnComment(mid);
    }

    @GetMapping("/all")
    private Result<?> getCommentsByMovieId(@RequestParam Long mid) {
        return commentService.getCommentsByMovieId(mid);
    }
}
