package cn.zjw.mrs.utils;

import cn.zjw.mrs.entity.LoginUser;
import org.apache.logging.log4j.util.Strings;

/**
 * @author zjw
 * @Classname PicUrlUtil
 * @Date 2022/4/18 13:30
 * @Description
 */
public class PicUrlUtil {
    /**
     * 获取完整图片路径
     * 由于数据库中的图片url路径为oss存储路径的后缀，所以返回给前端前需要将路径补全
     * @param avatar 头像路径后缀
     * @return 完整头像url
     */
    public static String getFullAvatarUrl(String avatar) {
        String avatarUrl = "";
        if (Strings.isNotBlank(avatar)) {
            avatarUrl = "https://mrs-zjw.oss-cn-hangzhou.aliyuncs.com/mrs/avatar/" + avatar;
        }
        return avatarUrl;
    }

    /**
     * 获取完整图片路径
     * 由于数据库中的图片url路径为oss存储路径的后缀，所以返回给前端前需要将路径补全
     * @param pic 电影海报路径后缀
     * @return 完整电影海报url
     */
    public static String getFullMoviePicUrl(String pic) {
        String avatarUrl = "";
        if (Strings.isNotBlank(pic)) {
            avatarUrl = "https://mrs-zjw.oss-cn-hangzhou.aliyuncs.com/mrs/movie/" + pic;
        }
        return avatarUrl;
    }
}
