package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.service.UserLikeRedisService;
import cn.zjw.mrs.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.zjw.mrs.entity.UserLike;
import cn.zjw.mrs.service.UserLikeService;
import cn.zjw.mrs.mapper.UserLikeMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
* @author 95758
* @description 针对表【user_like】的数据库操作Service实现
* @createDate 2022-05-22 16:43:36
*/
@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike>
    implements UserLikeService{

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserLikeRedisService userLikeRedisService;

    @Resource
    private UserLikeMapper userLikeMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    @Async("asyncServiceExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void transLikedFromRedis2Database() {
        List<UserLike> list = userLikeRedisService.getAllLikedDataFromRedis();
        for (UserLike like : list) {
            UserLike userLike = userLikeMapper.selectOne(new LambdaUpdateWrapper<UserLike>()
                    .eq(UserLike::getCid, like.getCid())
                    .eq(UserLike::getUid, like.getUid()));
            if (userLike == null){
                // 没有记录，直接存入
                userLikeMapper.insert(like);
            } else {
                // 有记录，需要更新
                userLikeMapper.update(null, new LambdaUpdateWrapper<UserLike>()
                        .eq(UserLike::getCid, like.getCid())
                        .eq(UserLike::getUid, like.getUid())
                        .set(UserLike::getStatus, like.getStatus()));
            }
        }
    }

    @Override
    @Async("asyncServiceExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void transLikedCountFromRedis2Database() {
        List<Comment> list = userLikeRedisService.getAllLikedCountFromRedis();
        for (Comment comment : list) {
            Comment pre = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().eq(Comment::getId, comment.getId()));
            Integer preAgree = pre.getAgree();
            // 评论总点赞数 = 数据库中原来的点赞数 + redis评论点赞数
            Integer totalAgree = preAgree + comment.getAgree();
            // 更新评论点赞数
            commentMapper.update(null, new LambdaUpdateWrapper<Comment>()
                    .eq(Comment::getId, comment.getId())
                    .set(Comment::getAgree, totalAgree));
        }
    }

    @Override
    public int getUserLikeStatus(long cid, long uid) {
        int redisStatus = userLikeRedisService.getUserLikeStatusFromRedis(cid, uid);
        // 如果查询redis，返回-1，表示无记录，则取数据库点赞表中查询
        if (redisStatus == -1) {
            UserLike userLike = userLikeMapper.selectOne(new LambdaQueryWrapper<UserLike>()
                    .eq(UserLike::getCid, cid)
                    .eq(UserLike::getUid, uid));
            // 如果点赞表中不存在记录，则说明未点赞，返回0
            if (Objects.isNull(userLike)) {
                return 0;
            }
            // 记录存在，则直接返回点赞状态值即可
            return userLike.getStatus();
        }
        return redisStatus;
    }
}




