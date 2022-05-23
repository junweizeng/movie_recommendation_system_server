package cn.zjw.mrs.utils;

/**
 * @author zjw
 * @Classname RedisKeyUtil
 * @Date 2022/5/22 16:46
 * @Description
 */
public class RedisKeyUtil {
    /**
     * 保存用户点赞数据的key
     */
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    /**
     * 保存用户被点赞数量的key
     */
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    /**
     * 拼接被点赞的用户id和点赞的人的id作为key。格式 222222::333333
     * @param cid 被点赞的评论id
     * @param uid 点赞的用户id
     * @return cid::uid
     */
    public static String getLikedKey(long cid, long uid){
        return cid + "::" + uid;
    }
}
