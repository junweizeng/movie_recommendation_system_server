package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.UserLike;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 95758
* @description 针对表【user_like】的数据库操作Service
* @createDate 2022-05-22 16:43:36
*/
public interface UserLikeService extends IService<UserLike> {
    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transLikedFromRedis2Database();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2Database();

    /**
     * 判断用户（uid）是否点赞过评论（cid）
     * 先查redis记录，如果为空（即-1），则继续查数据库，如果还是没有记录，则返回0；否则返回1
     * @param cid 评论id
     * @param uid 用户id
     * @return 点赞状态（1表示已点赞，0表示未点赞）
     */
    int getUserLikeStatus(long cid, long uid);
}
