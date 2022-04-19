package cn.zjw.mrs.service;

import cn.zjw.mrs.entity.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zjw
 * @Classname OssService
 * @Date 2022/4/17 13:50
 * @Description
 */
public interface OssService {
    /**
     * 上传头像到oss服务器上，并将图片路径存入数据库中
     * @param username 用户名
     * @param uploadFile 待上传头像
     * @return 上传结果
     */
    boolean updateAvatar(String username, MultipartFile uploadFile);
}
