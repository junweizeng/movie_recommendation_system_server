package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.UserLike;

import java.util.List;

/**
 * @author zjw
 * @Classname RedisService
 * @Date 2022/5/22 16:33
 * @Description
 */
public interface UserLikeRedisService {

    /**
     * 获取用户（uid）是否点赞过评论（cid）
     * @param cid 评论id
     * @param uid 用户id
     * @return 点赞状态（1表示已点赞，0表示未点赞，redis中不存在则返回-1）
     */
    int getUserLikeStatusFromRedis(long cid, long uid);

    /**
     * 点赞。状态为1
     * @param cid 被点赞的评论id
     * @param uid 点赞的用户id
     */
    void likeComment(long cid, long uid);

    /**
     * 取消点赞。将状态改变为0
     * @param cid 被点赞的评论id
     * @param uid 点赞的用户id
     */
    void unlikeComment(long cid, long uid);

    /**
     * 从Redis中删除一条点赞数据
     * @param cid 被点赞的评论id
     * @param uid 点赞的用户id
     */
    void deleteLikedFromRedis(long cid, long uid);

    /**
     * 获取redis中的某条评论的点赞数
     * @param cid 评论id
     * @return 点赞数
     */
    int getUserLikeCountFromRedis(long cid);

    /**
     * 该评论的点赞数加1
     * @param cid 被点赞的评论id
     */
    void increaseLikedCount(long cid);

    /**
     * 该用户的点赞数减1
     * @param cid 被点赞的评论id
     */
    void decreaseLikedCount(long cid);

    /**
     * 获取Redis中存储的所有点赞数据
     * @return Redis中存储的所有点赞数据
     */
    List<UserLike> getAllLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return Redis中存储的所有点赞数量
     */
    List<Comment> getAllLikedCountFromRedis();
}
