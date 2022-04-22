package cn.zjw.mrs.service;

/**
 * @author zjw
 * @Classname ContentBasedRecommendationService
 * @Date 2022/4/21 22:07
 * @Description
 */
public interface ContentBasedRecommendationService {

    /**
     * 更新指定用户推荐结果
     * @param uid 用户id
     */
    public void updateRecommendation(Long uid);
}
