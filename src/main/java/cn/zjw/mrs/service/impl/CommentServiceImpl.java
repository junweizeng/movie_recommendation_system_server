package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.LoginUser;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.service.CommentService;
import cn.zjw.mrs.mapper.CommentMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private UserMapper userMapper;

    @Override
    public Integer addComment(Comment comment, String username) {
        // 获取当前登录用户的基本信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        Long uid = user.getId();
        String nickname = user.getNickname();
        comment.setUid(uid);
        comment.setType(0);
        comment.setAgree(0);
        comment.setNickname(nickname);
        // 设置当前时间
        comment.setTime(new Timestamp(System.currentTimeMillis()));

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
                            // 更新数据条件，判断uid和mid是否匹配
                            .eq(Comment::getUid, comment.getUid())
                            .eq(Comment::getMid, comment.getMid()));
            if (update <= 0) {
                return -1;
            } else {
                return 1;
            }
        }

        // 如果评价信息不存在，则插入评价信息
        int isInsert = commentMapper.insert(comment);
        if (isInsert <= 0) {
            return -2;
        } else {
            return 2;
        }
    }

    @Override
    public CommentStripVo getOwnComment(Long uid, Long mid) {
        CommentStripVo commentStripVo = commentMapper.selectOwnCommentByUidAndMid(uid, mid);
        commentStripVo.setAvatar(PicUrlUtil.getFullAvatarUrl(commentStripVo.getAvatar()));
        return commentStripVo;
    }

    @Override
    public List<CommentStripVo> getCommentsByMovieId(Long mid) {
        List<CommentStripVo> commentStripVos = commentMapper.selectCommentsByMovieId(mid);
        for (CommentStripVo comment: commentStripVos) {
            comment.setAvatar(PicUrlUtil.getFullAvatarUrl(comment.getAvatar()));
        }
        return commentStripVos;
    }

    @Override
    public List<CommentMovieVo> getOwnCommentMovieMoments(Long uid) {
        List<CommentMovieVo> commentMovieVos = commentMapper.selectOwnCommentMovieMoments(uid);
        // 获取头像和电影海报完整的url
        for (CommentMovieVo commentMovieVo: commentMovieVos) {
            String avatar = commentMovieVo.getCommentStripVo().getAvatar();
            commentMovieVo.getCommentStripVo().setAvatar(PicUrlUtil.getFullAvatarUrl(avatar));
            String pic = commentMovieVo.getMovieStripVo().getPic();
            commentMovieVo.getMovieStripVo().setPic(PicUrlUtil.getFullMoviePicUrl(pic));
        }
        return commentMovieVos;
    }
}




