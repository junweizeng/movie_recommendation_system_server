package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.User;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.service.CommentService;
import cn.zjw.mrs.service.UserLikeRedisService;
import cn.zjw.mrs.service.UserLikeService;
import cn.zjw.mrs.utils.FileUtil;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

/**
* @author zjw
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

    @Resource
    private UserLikeRedisService userLikeRedisService;

    @Resource
    private UserLikeService userLikeService;

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
        if (!Objects.isNull(commentStripVo)) {
            commentStripVo.setAvatar(PicUrlUtil.getFullAvatarUrl(commentStripVo.getAvatar()));
        }
        return commentStripVo;
    }

    @Override
    public List<CommentStripVo> getMoreCommentsByMovieId(Long mid, int currentPage, int pageSize) {
        currentPage = currentPage * pageSize;
        List<CommentStripVo> commentStripVos = commentMapper.selectMoreCommentsByMovieId(mid, currentPage, pageSize);
        for (CommentStripVo comment: commentStripVos) {
            if (comment.getType() == 0) {
                comment.setAvatar(PicUrlUtil.getFullAvatarUrl(comment.getAvatar()));
            }
            // 更新点赞数：总点赞数 = 数据库中的点赞数 + redis中的点赞数
            comment.setAgree(comment.getAgree() + userLikeRedisService.getUserLikeCountFromRedis(comment.getId()));
        }
        return commentStripVos;
    }

    @Override
    public Map<String, Object> getMoreOwnCommentMovieMoments(Long uid, Integer currentPage, Integer pageSize) {
        Integer currentIndex = (currentPage - 1) * pageSize;
        List<CommentMovieVo> commentMovieVos = commentMapper.selectOwnCommentMovieMoments(uid, currentIndex, pageSize);
        for (CommentMovieVo commentMovieVo: commentMovieVos) {
            // 获取头像和电影海报完整的url
            String avatar = commentMovieVo.getCommentStripVo().getAvatar();
            commentMovieVo.getCommentStripVo().setAvatar(PicUrlUtil.getFullAvatarUrl(avatar));
            String pic = commentMovieVo.getMovieStripVo().getPic();
            commentMovieVo.getMovieStripVo().setPic(PicUrlUtil.getFullMoviePicUrl(pic));

            // 更新点赞数：总点赞数 = 数据库中的点赞数 + redis中的点赞数
            long cid = commentMovieVo.getCommentStripVo().getId();
            int dbAgree = commentMovieVo.getCommentStripVo().getAgree();
            int redisAgree = userLikeRedisService.getUserLikeCountFromRedis(cid);
            commentMovieVo.getCommentStripVo().setAgree(dbAgree + redisAgree);
        }
        for (CommentMovieVo moment: commentMovieVos) {
            // 获取用户点赞状态
            int status = userLikeService.getUserLikeStatus(moment.getCommentStripVo().getId(), uid);
            moment.getCommentStripVo().setStatus(status);
        }

        // 获取用户动态总条目数
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getUid, uid));
        Integer total = comments.size();

        Map<String, Object> page = new HashMap<>(2);
        page.put("total", total);
        page.put("records", commentMovieVos);
        return page;
    }

    @Override
    public int removeOwnComment(Long uid, Long mid) {
        return commentMapper.delete(
                new LambdaQueryWrapper<Comment>().eq(Comment::getUid, uid).eq(Comment::getMid, mid));
    }

    @Override
    public List<Map<String, String>> getCommentsWordCloudData(long mid) {
        List<String> stopWords = FileUtil.readStopWords();

        List<Map<String, String>> res = new ArrayList<>();
        List<CommentStripVo> comments = commentMapper.selectMoreCommentsByMovieId(mid, 0, 100);
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

        Map<String, Integer> counts = new HashMap<>(20);
        for (CommentStripVo comment : comments) {
            // 去掉两边的空格
            String sentence = comment.getComment().trim();
            // 将所有标点符号换成空格
            // 小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀。
            // 中括号内的“P”表示Unicode 字符集七个字符属性之一：标点字符。
            sentence = sentence.replaceAll("\\p{P}", "");
            sentence = sentence.replaceAll("\\s+", "");

            // 结巴分词，得到分词结果（包含 词语、截取起始位置、截取终止位置）
            List<String> words = jiebaSegmenter.sentenceProcess(sentence);
            words.removeAll(stopWords);
            System.out.println(words);
            for (String word: words) {
                // 词频统计，如果哈希表中已有word，则加1；否则word的value值置为1
                if (counts.containsKey(word)) {
                    counts.put(word, counts.get(word) + 1);
                } else {
                    counts.put(word, 1);
                }
            }
        }

        // 将词频数据封装成{"name":"词语", "value": "1"}形式，词云图数据所需形式
        counts.forEach((word, count) -> {
            Map<String, String> data = new HashMap<>(2);
            data.put("name", word);
            data.put("value", String.valueOf(count));
            res.add(data);
        });

        return res;
    }
}




