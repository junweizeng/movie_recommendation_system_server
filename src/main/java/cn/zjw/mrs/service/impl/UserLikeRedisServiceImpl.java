package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.UserLike;
import cn.zjw.mrs.enums.LikedStatusEnum;
import cn.zjw.mrs.service.UserLikeRedisService;
import cn.zjw.mrs.utils.RedisCache;
import cn.zjw.mrs.utils.RedisKeyUtil;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zjw
 * @Classname RedisServiceImpl
 * @Date 2022/5/22 16:44
 * @Description
 */
@Service
public class UserLikeRedisServiceImpl implements UserLikeRedisService {
    @Resource
    private RedisCache redisCache;

    @Override
    public int getUserLikeStatusFromRedis(long cid, long uid) {
        String key = RedisKeyUtil.getLikedKey(cid, uid);
        Integer status = redisCache.getCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
        if (!Objects.isNull(status)) {
            return status;
        } else {
            return -1;
        }
    }

    @Override
    public void likeComment(long cid, long uid) {
        String key = RedisKeyUtil.getLikedKey(cid, uid);
        redisCache.setCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
    }

    @Override
    public void unlikeComment(long cid, long uid) {
        String key = RedisKeyUtil.getLikedKey(cid, uid);
        redisCache.setCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
    }

    @Override
    public void deleteLikedFromRedis(long cid, long uid) {
        String key = RedisKeyUtil.getLikedKey(cid, uid);
        redisCache.delCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public int getUserLikeCountFromRedis(long cid) {
        Integer count = redisCache.getCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, String.valueOf(cid));
        if (!Objects.isNull(count)) {
            return count;
        }
        return 0;
    }

    @Override
    public void increaseLikedCount(long cid) {
        redisCache.increaseCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, String.valueOf(cid), 1);
    }

    @Override
    public void decreaseLikedCount(long cid) {
        redisCache.increaseCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, String.valueOf(cid), -1);
    }

    @Override
    public List<UserLike> getAllLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisCache.getAllCacheMapValueCursor(RedisKeyUtil.MAP_KEY_USER_LIKED);
        List<UserLike> res = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> next = cursor.next();
            String key = (String) next.getKey();
            // 分离出cid 和 uid
            String[] split = key.split("::");
            Long cid = Long.valueOf(split[0]);
            Long uid = Long.valueOf(split[1]);
            Integer value = (Integer) next.getValue();

            // 组装UserLike对象，并放入res中
            UserLike userLike = new UserLike(cid, uid, value);
            res.add(userLike);

            // 存到res后，从Redis中删除
            redisCache.delCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
        }
        return res;
    }

    @Override
    public List<Comment> getAllLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor =
                redisCache.getAllCacheMapValueCursor(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT);
        List<Comment> res = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> next = cursor.next();
            String key = (String) next.getKey();

            // 组装Comment对象，并放入res中
            Comment comment = new Comment();
            comment.setId(Long.valueOf(key));
            comment.setAgree((Integer) next.getValue());
            res.add(comment);

            // 从redis中删除这条记录
            redisCache.delCacheMapValue(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return res;
    }
}
