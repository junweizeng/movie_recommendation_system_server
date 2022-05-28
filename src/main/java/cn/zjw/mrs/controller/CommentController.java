package cn.zjw.mrs.controller;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.service.CommentService;
import cn.zjw.mrs.service.RecommendationService;
import cn.zjw.mrs.service.UserLikeRedisService;
import cn.zjw.mrs.service.UserLikeService;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;

/**
 * @author zjw
 * @Classname CommentController
 * @Date 2022/4/14 19:20
 * @Description
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    @Resource
    private RecommendationService recommendationService;

    @Resource
    private UserLikeService userLikeService;

    @Resource
    private UserLikeRedisService userLikeRedisService;

    @PostMapping
    private Result<?> addComment(@RequestBody Comment comment, Principal principal, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();

        int update = commentService.addComment(comment, principal.getName());
        switch (update) {
            case -1: return Result.error("评价更新失败，请稍后重试(┬┬﹏┬┬)");
            case 1: {
                recommendationService.updateRecommendation(uid);
                return Result.success("评价更新成功(‾◡◝)");
            }
            case -2: return Result.error("评价失败(┬┬﹏┬┬)");
            default: {
                recommendationService.updateRecommendation(uid);
                return Result.success("评价成功(‾◡◝)");
            }
        }
    }

    @GetMapping("/own")
    private Result<?> getOwnComment(@RequestParam Long mid, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        CommentStripVo ownComment = commentService.getOwnComment(uid, mid);
        if (Objects.isNull(ownComment)) {
            return Result.error("该用户还未评论");
        }
        return Result.success(ownComment);
    }

    @GetMapping("/more")
    private Result<?> getMoreCommentsByMovieId(@RequestParam Long mid,
                                               @RequestParam(defaultValue = "0") int currentPage,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               Authentication authentication) {
        // 限制请求数量，防止懂技术的人，通过接口一次性获取到所有的记录
        pageSize = Math.min(20, pageSize);

        List<CommentStripVo> comments = commentService.getMoreCommentsByMovieId(mid, currentPage, pageSize);
        if (Objects.isNull(comments)) {
            return Result.error("该电影下暂无评论");
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long uid = loginUser.getUser().getId();
        for (CommentStripVo comment: comments) {
            // 获取点赞状态
            int status = userLikeService.getUserLikeStatus(comment.getId(), uid);
            comment.setStatus(status);
        }
        return Result.success(comments);
    }

    @GetMapping("/movie/moments")
    private Result<?> getMoreOwnCommentMovieMoments(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Authentication authentication) {
        // 限制请求数量，防止懂技术的人，通过接口一次性获取到所有的记录
        pageSize = Math.min(20, pageSize);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();

        Map<String, Object> page = commentService.getMoreOwnCommentMovieMoments(uid, currentPage, pageSize);

        return Result.success(page);
    }

    @DeleteMapping("/remove")
    private Result<?> removeOwnComment(@RequestBody String mid, Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();
        int delete = commentService.removeOwnComment(uid, Long.valueOf(mid));
        if (delete == 0) {
            return Result.error("短评删除失败(┬┬﹏┬┬)");
        }

        recommendationService.updateRecommendation(uid);
        return Result.success("短评删除成功(‾◡◝)");
    }

    @GetMapping("/word/cloud/data")
    private Result<?> getCommentsWordCloudData(@RequestParam long mid) {
        List<Map<String, String>> res = commentService.getCommentsWordCloudData(mid);
        return Result.success(res);
    }

    @PutMapping("/like")
    private Result<?> likeComment(@RequestBody Map<String, String> data, Authentication authentication) {
        long cid = Long.parseLong(data.get("cid"));
        long status = Long.parseLong(data.get("status"));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();

        if (status == 1) {
            userLikeRedisService.likeComment(cid, uid);
            userLikeRedisService.increaseLikedCount(cid);
        } else {
            userLikeRedisService.unlikeComment(cid, uid);
            userLikeRedisService.decreaseLikedCount(cid);
        }
        return Result.success();
    }
}
