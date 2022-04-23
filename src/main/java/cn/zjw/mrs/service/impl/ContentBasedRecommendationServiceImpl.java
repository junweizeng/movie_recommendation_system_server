package cn.zjw.mrs.service.impl;

import cn.zjw.mrs.entity.*;
import cn.zjw.mrs.mapper.*;
import cn.zjw.mrs.service.ContentBasedRecommendationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import javafx.util.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * @author zjw
 * @Classname ContentBasedRecommendationServiceImpl
 * @Date 2022/4/21 22:07
 * @Description
 */
@Service
public class ContentBasedRecommendationServiceImpl implements ContentBasedRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(ContentBasedRecommendationServiceImpl.class);

    /**
     * 表示类型 或 地区 的数量都为21
     */
    private static final int TYPES_OR_REGIONS_LENGTH = 21;

    /**
     * 表示用户喜好权重，
     * 即用户喜欢的电影类型或电影地区的标签 加入用户偏好矩阵的次数
     */
    private static final int USER_LIKE_WEIGHT = 3;

    @Resource
    private TypeLikeMapper typeLikeMapper;

    @Resource
    private RegionLikeMapper regionLikeMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private MovieTypeMapper movieTypeMapper;

    @Resource
    private MovieRegionMapper movieRegionMapper;

    @Resource
    private MovieFeatureMapper movieFeatureMapper;

    @Resource
    private RecommendationMapper recommendationMapper;

    @Override
    @Async("asyncServiceExecutor")
    public void updateRecommendation(Long uid) {
        logger.info("start executeAsync");

        Timestamp t1 = Timestamp.valueOf(LocalDateTime.now());

        double[] userPreferenceMatrix = computeUserPreferenceMatrix(uid);
        System.out.println("userPreferenceMatrix:" + Arrays.toString(userPreferenceMatrix));

        List<Pair<Long, Double>> movieRecommendations = new ArrayList<>();
        List<MovieFeature> movieFeatures = movieFeatureMapper.selectList(null);
        for (MovieFeature movieFeature : movieFeatures) {
            double[] movieFeatureMatrix = movieFeatureMatrixFormat(movieFeature.getMatrix());
            double dist = calculateTheUsersPreferenceForMovies(userPreferenceMatrix, movieFeatureMatrix);
            if (dist > 0) {
                movieRecommendations.add(new Pair<>(movieFeature.getMid(), dist));
            }
        }
        movieRecommendations.sort(new Comparator<Pair<Long, Double>>() {
            @Override
            public int compare(Pair<Long, Double> o1, Pair<Long, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int len = Math.min(movieRecommendations.size(), 30);
        recommendationMapper.delete(new LambdaQueryWrapper<Recommendation>().eq(Recommendation::getUid, uid));
        System.out.println("\n排序结果：");
        for (int i = 0; i < len; i ++ ) {
            System.out.println(movieRecommendations.get(i).getKey() + "   " + movieRecommendations.get(i).getValue());
            recommendationMapper.insert(new Recommendation(uid,
                    movieRecommendations.get(i).getKey(),
                    movieRecommendations.get(i).getValue()));
        }

        Timestamp t2 = Timestamp.valueOf(LocalDateTime.now());
        logger.info("begin time:" + t1);
        logger.info("end time:" + t2);

        logger.info("end executeAsync");
    }

    /**
     * 将电影特征矩阵由字符串转为数组
     * @param feature 电影特征矩阵字符串（例："10100000000..."）
     * @return 数组形式电影特征矩阵（例：[1.0, 0.0, 1.0, 0.0, ...]
     */
    private double[] movieFeatureMatrixFormat(String feature) {
        double[] movieFeatureMatrix = new double[42];
        String[] features = feature.split("");
        for (int i = 0; i < feature.length(); i ++ ) {
            movieFeatureMatrix[i] = Double.parseDouble(features[i]);
        }
        return movieFeatureMatrix;
    }

    /**
     * 计算用户对电影的喜好程度
     * 即计算 用户的偏好矩阵 与 电影的特征矩阵 之间的距离（运用余弦相似度公式）
     * @param userPreferenceMatrix 用户偏好矩阵
     * @param movieFeatureMatrix 电影特征矩阵
     * @return 用户对电影的喜好程度
     */
    private double calculateTheUsersPreferenceForMovies(double[] userPreferenceMatrix, double[] movieFeatureMatrix) {
        INDArray user = Nd4j.create(userPreferenceMatrix, new int[]{1, userPreferenceMatrix.length});
        INDArray movie = Nd4j.create(movieFeatureMatrix, new int[]{1, movieFeatureMatrix.length});
        INDArray mulRes = user.mul(movie);
        // sum()计算矩阵所有元素和，toDoubleVector()将INDArray转为double[]并取第一个值，即矩阵中元素和
        double numerator = mulRes.sum().toDoubleVector()[0];

        double userPowSum = user.mul(user).sum().toDoubleVector()[0];
        double moviePowSum = movie.mul(movie).sum().toDoubleVector()[0];
        double denominator = Math.sqrt(userPowSum) * Math.sqrt(moviePowSum);

        return numerator / denominator;
    }

    public static void main(String[] args) {
        Timestamp t1 = Timestamp.valueOf(LocalDateTime.now());
        new ContentBasedRecommendationServiceImpl().calculateTheUsersPreferenceForMovies(new double[]{1.0, 2.0, 3.0, 4.0}, new double[]{-1, 2, -1, 2});
        Timestamp t2 = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(t1);
        System.out.println(t2);
    }

    /**
     * 计算用户的偏好矩阵
     * @param uid 用户id
     * @return 用户偏好矩阵
     */
    private double[] computeUserPreferenceMatrix(Long uid) {
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getUid, uid)
                .orderByDesc(Comment::getTime));
        // 如果用户评价过的电影超过10部，则只取最近评价的5部电影
        int commentsLen = Math.min(comments.size(), 5);

        // typeMap用于存储键值对（电影类型id => 评分）
        Map<Integer, List<Integer>> typeMap = new HashMap<>(21);
        // regionMap用于存储键值对（电影地区id => 评分）
        Map<Integer, List<Integer>> regionMap = new HashMap<>(21);

        double avg = 0;
        for (int i = 0; i < commentsLen; i ++ ){
            Comment comment = comments.get(i);
            avg += comment.getScore();

            // 获取每部电影的电影类型id列表
            List<MovieType> movieTypes = movieTypeMapper.selectList(
                    new LambdaQueryWrapper<MovieType>().eq(MovieType::getMid, comment.getMid()));
            // 获取每部电影的电影地区id列表
            List<MovieRegion> movieRegions = movieRegionMapper.selectList(
                    new LambdaQueryWrapper<MovieRegion>().eq(MovieRegion::getMid, comment.getMid()));

            for (MovieType movieType: movieTypes) {
                if (typeMap.containsKey(movieType.getTid())) {
                    typeMap.get(movieType.getTid()).add(comment.getScore());
                } else {
                    List<Integer> scores = new ArrayList<>();
                    scores.add(comment.getScore());
                    typeMap.put(movieType.getTid(), scores);
                }
            }
            for (MovieRegion movieRegion: movieRegions) {
                if (regionMap.containsKey(movieRegion.getRid())) {
                    regionMap.get(movieRegion.getRid()).add(comment.getScore());
                } else {
                    List<Integer> scores = new ArrayList<>();
                    scores.add(comment.getScore());
                    regionMap.put(movieRegion.getRid(), scores);
                }
            }
        }
        avg = (avg + USER_LIKE_WEIGHT * 10) / (comments.size() + USER_LIKE_WEIGHT);

        // 用户自己选择的类型或地区喜好标签，每个标签都以10分影响程度加入用户偏好矩阵
        // 为了提高用户自选标签的推荐价值，这里加入特征矩阵的次数为USER_LIKE_WEIGHT次
        addTypeLikesIntoUserMatrix(uid, typeMap);
        addRegionLikesIntoUserMatrix(uid, regionMap);

        List<Double> res = new ArrayList<>();
        // 计算用户对每个类型和每个地区的喜好程度，并将最终结果加入到用户偏好矩阵
        makeUserMatrix(typeMap, avg, res);
        makeUserMatrix(regionMap, avg, res);

        // 将res（ArrayList）转为数组
        double[] userPreferenceMatrix = new double[res.size()];
        for (int i = 0; i < res.size(); i ++ ) {
            userPreferenceMatrix[i] = res.get(i);
        }
        return userPreferenceMatrix;
    }

    /**
     * 用户自己选择的类型喜好标签，每个标签都以10分影响程度加入用户偏好矩阵。
     * 为了提高用户自选标签的推荐价值，这里加入特征矩阵的次数为USER_LIKE_WEIGHT次
     * @param uid 用户id
     * @param typeMap typeMap用于存储键值对（电影类型id => 评分）
     */
    private void addTypeLikesIntoUserMatrix(Long uid, Map<Integer, List<Integer>> typeMap) {
        List<TypeLike> typeLikes = typeLikeMapper.selectList(
                new LambdaQueryWrapper<TypeLike>().eq(TypeLike::getUid, uid));
        for (TypeLike typeLike: typeLikes) {
            if (typeMap.containsKey(typeLike.getTid())) {
                for (int i = 0; i < USER_LIKE_WEIGHT; i ++ ) {
                    typeMap.get(typeLike.getTid()).add(10);
                }
            } else {
                List<Integer> scores = new ArrayList<>();
                for (int i = 0; i < USER_LIKE_WEIGHT; i ++ ) {
                    scores.add(10);
                }
                typeMap.put(typeLike.getTid(), scores);
            }
        }
    }

    /**
     * 用户自己选择的地区喜好标签，每个标签都以10分影响程度加入用户偏好矩阵。
     * 为了提高用户自选标签的推荐价值，这里加入特征矩阵的次数为USER_LIKE_WEIGHT次
     * @param uid 用户id
     * @param regionMap regionMap用于存储键值对（电影地区id => 评分）
     */
    private void addRegionLikesIntoUserMatrix(Long uid, Map<Integer, List<Integer>> regionMap) {
        List<RegionLike> regionLikes = regionLikeMapper.selectList(
                new LambdaQueryWrapper<RegionLike>().eq(RegionLike::getUid, uid));

        for (RegionLike regionLike: regionLikes) {
            if (regionMap.containsKey(regionLike.getRid())) {
                for (int i = 0; i < USER_LIKE_WEIGHT; i ++ ) {
                    regionMap.get(regionLike.getRid()).add(10);
                }
            } else {
                List<Integer> scores = new ArrayList<>();
                for (int i = 0; i < USER_LIKE_WEIGHT; i ++ ) {
                    scores.add(10);
                }
                regionMap.put(regionLike.getRid(), scores);
            }
        }
    }

    /**
     * 计算用户对每个类型和每个地区的喜好程度，并将最终结果加入到用户偏好矩阵
     * @param map 类型 or 地区 键值对（类型 or 地区，评分列表）
     * @param avg 用户平均打分
     * @param res 用户的偏好矩阵
     */
    private void makeUserMatrix(Map<Integer, List<Integer>> map, double avg, List<Double> res) {
        for (int i = 0; i < TYPES_OR_REGIONS_LENGTH; i ++ ) {
            double totalScore = 0.0;
            if (map.containsKey(i)) {
                List<Integer> scores = map.get(i);
                for (Integer score : scores) {
                    totalScore += (score - avg);
                }
                res.add(totalScore / scores.size());
            } else {
                res.add(0.0);
            }
        }
    }
}
