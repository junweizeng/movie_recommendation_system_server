package cn.zjw.mrs.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.zjw.mrs.entity.Result;
import cn.zjw.mrs.mapper.UserMapper;
import cn.zjw.mrs.service.OssService;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.management.ObjectName;
import java.io.ByteArrayInputStream;

/**
 * @Classname OssServiceImpl
 * @Date 2022/4/17 13:51
 * @Created by zjw
 * @Description
 */
@Service
public class OssServiceImpl implements OssService {
    @Value("${aliyun.oss.maxSize}")
    private int maxSize;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.dir.prefix}")
    private String dirPrefix;

    @Resource
    private OSSClient ossClient;

    @Resource
    private UserMapper userMapper;

    private String getAvatarObjectName() {
        return  DateUtil.today() + '/' + IdUtil.simpleUUID() + ".webp";
    }

    private String formatPath(String objectName){
        return "https://"  + bucketName + "." + ossClient.getEndpoint().getHost() + "/" + objectName;
    }

    public static void main(String[] args) {
        System.out.println(new OssServiceImpl().getAvatarObjectName());
    }

    @Override
    public Result<?> updateAvatar(String username, MultipartFile uploadFile) {
        String dirSuffix = getAvatarObjectName();
        String objectName = dirPrefix + dirSuffix;
        try {
            String content = "Hello OSS";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return Result.error(-1, "头像上传失败(┬┬﹏┬┬)");
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return Result.error(-1, "头像上传失败(┬┬﹏┬┬)");
        }
        return Result.success("头像修改成功(‾◡◝)", null);
    }
}
