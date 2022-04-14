package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.vo.comment.OwnCommentVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.service.CommentService;
import cn.zjw.mrs.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
* @author 95758
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-04-14 19:19:45
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private CommentMapper commentMapper;

    /**
     * 添加评价信息，如果评价信息已经存在，则更新评价信息
     * @param comment 评价信息
     * @return
     */
    @Override
    public Result<?> addComment(Comment comment) {
        // 获取SecurityHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String name = loginUser.getUser().getNickname();
        comment.setUid(userId);
        comment.setType(0);
        comment.setAgree(0);
        comment.setNickname(name);
        comment.setTime(new Timestamp(System.currentTimeMillis())); // 设置当前时间

        Comment isCommentExists = commentMapper.selectOne(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getUid, comment.getUid())
                        .eq(Comment::getMid, comment.getMid())
        );
        // 判断如果评价信息已经存在，则更新评论即可
        if (!Objects.isNull(isCommentExists)) {
            comment.setAgree(isCommentExists.getAgree());
            int update = commentMapper.update(comment,
                    new LambdaUpdateWrapper<Comment>()
                            .eq(Comment::getUid, comment.getUid())      // 更新数据条件，判断uid和mid是否匹配
                            .eq(Comment::getMid, comment.getMid()));
            if (update <= 0) {
                return Result.error(-1, "评价更新失败，请稍后重试");
            } else {
                return Result.success("评价更新成功", "");
            }
        }

        // 如果评价信息不存在，则插入评价信息
        int isInsert = commentMapper.insert(comment);
        if (isInsert <= 0) {
            return Result.error(-1, "评价失败");
        } else {
            return Result.success("评价成功", "");
        }
    }

    @Override
    public Result<?> getOwnComment(Long mid) {
        // 获取SecurityHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long uid = loginUser.getUser().getId();

        OwnCommentVo ownCommentVo = commentMapper.selectOwnCommentByUidAndMid(uid, mid);
        if (!Objects.isNull(ownCommentVo)) {
            return Result.success(ownCommentVo);
        } else {
            return Result.error(-1, "您还未评价");
        }
    }

    @Override
    public Result<?> getCommentsByMovieId(Long mid) {
        List<OwnCommentVo> ownCommentVos = commentMapper.selectCommentsByMovieId(mid);
        Map<String, List<OwnCommentVo>> res = new HashMap<>();
        res.put("comments", ownCommentVos);
        return Result.success(res);
    }
}




