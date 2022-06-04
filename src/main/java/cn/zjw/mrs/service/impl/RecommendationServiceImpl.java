package cn.zjw.mrs.service.impl;

import cn.hutool.core.lang.Pair;
import cn.zjw.mrs.entity.*;
import cn.zjw.mrs.enums.RecommendationTypeEnum;
import cn.zjw.mrs.mapper.*;
import cn.zjw.mrs.service.RecommendationService;
import cn.zjw.mrs.utils.PicUrlUtil;
import cn.zjw.mrs.utils.RecommendationUtil;
import cn.zjw.mrs.vo.movie.MovieCardVo;
import cn.zjw.mrs.vo.movie.RecommendedMovieVo;
import cn.zjw.mrs.vo.movie.ReviewedMovieStripVo;
import cn.zjw.mrs.vo.movie.relation.CategoryVo;
import cn.zjw.mrs.vo.movie.relation.LinkVo;
import cn.zjw.mrs.vo.movie.relation.NodeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author 95758
 * @description 针对表【user_recommendation】的数据库操作Service实现
 * @createDate 2022-04-24 00:04:20
 */
@Component
@Service
@Slf4j
public class RecommendationServiceImpl extends ServiceImpl<RecommendationMapper, Recommendation>
        implements RecommendationService{

    /**
     * 基于用户的协同过滤的电影推荐器
     */
    private static Recommender userCfRecommender;

    /**
     * 基于用户的协同过滤推荐，重新计算获取推荐器
     * 定时每20分钟执行一次，后续根据数据量的变化，执行频率可做调整
     */
    @Async("asyncServiceExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 20)
    public void updateUserBasedCollaborativeFilteringRecommendationRecommender() throws TasteException {
        log.info("开始：基于用户的协同过滤推荐，重新计算获取推荐器");

        try {
            // 准备数据
            FastByIDMap<PreferenceArray> preferences = new FastByIDMap<>();
            List<Preference> myPreferences = commentMapper.selectAllPreferences();
            long idx = 0;
            for (int i = 0; i < myPreferences.size(); i ++ ) {
                int cnt = myPreferences.get(i).getCnt();
                PreferenceArray preferenceArray = new GenericUserPreferenceArray(cnt);
                preferenceArray.setUserID(0, myPreferences.get(i).getUid());
                for (int j = 0; j < cnt; j ++ ) {
                    preferenceArray.setItemID(j, myPreferences.get(i + j).getMid());
                    preferenceArray.setValue(j, myPreferences.get(i + j).getScore());
                }
                preferences.put(idx ++, preferenceArray);
                i += cnt - 1;
            }
            DataModel model = new GenericDataModel(preferences);

            // 计算用户间的皮尔逊系数
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            // 对每个用户取固定数量30个最近邻居
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(30, similarity, model);
            // 基于用户协同过滤推荐的推荐器
            userCfRecommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        } catch (TasteException e) {
            e.printStackTrace();
        }

        log.info("结束：基于用户的协同过滤推荐，重新计算获取推荐器");
    }

    /**
     * 表示类型 或 地区 的数量都为21
     */
    private static final int TYPES_OR_REGIONS_LENGTH = 21;

    /**
     * 表示用户喜好权重，
     * 即用户喜欢的电影类型或电影地区的标签 加入用户偏好矩阵的次数
     */
    private static final int USER_LIKE_WEIGHT = 3;

    /**
     * 推荐电影数量
     */
    private static final int THIRTY_RECOMMENDATIONS = 30;

    @Resource
    private RecommendationMapper recommendationMapper;

    @Resource
    private MovieTypeMapper movieTypeMapper;

    @Resource
    private MovieRegionMapper movieRegionMapper;

    @Resource
    private MovieFeatureMapper movieFeatureMapper;

    @Resource
    private MovieMapper movieMapper;

    @Resource
    private TypeLikeMapper typeLikeMapper;

    @Resource
    private RegionLikeMapper regionLikeMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Recommendation> randomRecommended(Long uid, Integer num) {
        List<Recommendation> newRecommendations = new ArrayList<>();
        List<MovieCardVo> movieCardVos = movieMapper.selectHighestRatedMovies(100);
        Set<Integer> randomIndexSet = new HashSet<>();
        // 获取num部随机高分电影
        RecommendationUtil.randomSet(0, 100, 50, randomIndexSet);
        int cnt = 0;
        for (Integer randomIndex: randomIndexSet) {
            MovieCardVo movie = movieCardVos.get(randomIndex);

            Comment comment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getUid, uid)
                    .eq(Comment::getMid, movie.getId()));
            // 如果评论不存在，即用户未看过，加入推荐列表
            if (Objects.isNull(comment)) {
                Recommendation recommendation = new Recommendation(uid, movie.getId(), 0.6,
                        RecommendationTypeEnum.RANDOM.getTypeCode());
                newRecommendations.add(recommendation);
                cnt ++;
                if (cnt >= num) {
                    break;
                }
            }
        }
        return newRecommendations;
    }

    @Override
    @Async("asyncServiceExecutor")
    public void solveColdStart(long uid) {
        List<Recommendation> newRecommendations = randomRecommended(uid, 30);
        saveBatch(newRecommendations);
    }

    @Override
    public List<RecommendedMovieVo> getRecommendedMoviesByUserId(Long uid) {
        List<RecommendedMovieVo> recommendedMovies = recommendationMapper.selectRecommendedMoviesByUserId(uid, 30);

        for (RecommendedMovieVo movie: recommendedMovies) {
            movie.setPic(PicUrlUtil.getFullMoviePicUrl(movie.getPic()));
        }
        return recommendedMovies;
    }

    @Override
    public Map<String, List<?>> getLinksBetweenWatchedMoviesAndRecommendedMovies(Long uid) {
        Map<String, List<?>> res = new HashMap<>(3);
        List<CategoryVo> categories = new ArrayList<>();
        categories.add(new CategoryVo("看过的电影"));
        categories.add(new CategoryVo("推荐的电影"));

        List<MovieFeature> watchedMoviesFeatures = movieFeatureMapper.selectWatchedMoviesFeaturesByUserId(uid, 20);
        List<MovieFeature> recommendedMoviesFeatures = movieFeatureMapper.selectRecommendedMoviesFeaturesByUserId(uid, 20);
        List<MovieFeature> totalMoviesFeatures = new ArrayList<>();
        totalMoviesFeatures.addAll(watchedMoviesFeatures);
        totalMoviesFeatures.addAll(recommendedMoviesFeatures);

        List<LinkVo> links = new ArrayList<>();
        for (int i = 0; i < totalMoviesFeatures.size(); i ++ ) {
            MovieFeature movieFeature1 = totalMoviesFeatures.get(i);
            double[] first = this.formatMovieFeatureMatrix(movieFeature1.getMatrix());

            List<Pair<Long, Double>> distances = new ArrayList<>();
            for (int j = i + 1; j < totalMoviesFeatures.size(); j ++ ) {
                MovieFeature movieFeature2 = totalMoviesFeatures.get(j);
                double[] second = this.formatMovieFeatureMatrix(movieFeature2.getMatrix());

                // 计算两部电影之间的距离
                double distance = calculateTheUsersPreferenceForMovies(first, second);
                // 如果两部电影距离小于0.4，说明基本没有相似点，则不加入结果列表distances中
                if (distance > 0.4) {
                    distances.add(new Pair<>(movieFeature2.getMid(), distance));
                }
            }
            // 对distances列表进行排序，排序根据距离（相似度）大小排序
            distances.sort(new Comparator<Pair<Long, Double>>() {
                @Override
                public int compare(Pair<Long, Double> o1, Pair<Long, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            // 获取相似度最高的2部电影，如果没有2部就全部获取
            int len = Math.min(distances.size(), 2);
            for (int j = 0; j < len; j ++ ) {
                System.out.println(distances.get(j).getValue());
                // 两部相似度很高的电影之间连接一条线
                links.add(new LinkVo(
                        String.valueOf(movieFeature1.getMid()),
                        String.valueOf(distances.get(j).getKey()),
                        distances.get(j).getValue()
                ));
            }
        }

        List<NodeVo> nodes = new ArrayList<>();
        List<ReviewedMovieStripVo> watchedMovies = movieMapper.selectSomeReviewedMoviesByUserId(uid, 30);
        List<RecommendedMovieVo> recommendedMovies = recommendationMapper.selectRecommendedMoviesByUserId(uid, 30);

        for (ReviewedMovieStripVo movie: watchedMovies) {
            nodes.add(new NodeVo(
                    movie.getId(),
                    movie.getId(),
                    movie.getName(),
                    String.valueOf(movie.getScore()),
                    0,
                    movie.getTypes(),
                    movie.getRegions()));
        }
        for (RecommendedMovieVo movie: recommendedMovies) {
            nodes.add(new NodeVo(
                    movie.getId(),
                    movie.getId(),
                    movie.getName(),
                    String.valueOf(movie.getScore()),
                    1,
                    movie.getTypes(),
                    movie.getRegions()));
        }

        res.put("categories", categories);
        res.put("links", links);
        res.put("nodes", nodes);
        return res;
    }

    @Override
    @Async("asyncServiceExecutor")
    public void updateRecommendation(Long uid) {
        log.info("start executeAsync");

        Timestamp t1 = Timestamp.valueOf(LocalDateTime.now());

        // 删除该用户之前的推荐结果
        recommendationMapper.delete(new LambdaQueryWrapper<Recommendation>().eq(Recommendation::getUid, uid));

        // 基于内容推荐
        List<Pair<Long, Double>> contentBasedResult = getContentBasedMovieRecommendationResult(uid, 30);
        int cnt = 0;
        System.out.println("\n排序结果：");
        for (int i = 0; i < contentBasedResult.size(); i ++ ) {
            System.out.println(contentBasedResult.get(i).getKey() + "   " + contentBasedResult.get(i).getValue());
            // 重新插入系统新推荐的电影
            int insert = recommendationMapper.insert(new Recommendation(uid,
                    contentBasedResult.get(i).getKey(),
                    contentBasedResult.get(i).getValue(),
                    RecommendationTypeEnum.CONTENT_BASED.getTypeCode()));
            cnt += insert;
            if (cnt >= 15) {
                break;
            }
        }

        // 协同过滤推荐
        try {
            List<RecommendedItem> recommendedMovies = userCfRecommender.recommend(uid, 30);

            for (int i = 0; i < recommendedMovies.size(); i ++ ) {
                // 判断电影是否已经看过，如果没有则插入推荐表中（去重）
                if (commentMapper.selectOne(
                        new LambdaQueryWrapper<Comment>()
                                .eq(Comment::getMid, recommendedMovies.get(i).getItemID())
                                .eq(Comment::getUid, uid)) == null) {
                    recommendationMapper.insert(new Recommendation(uid,
                            recommendedMovies.get(i).getItemID(),
                            recommendedMovies.get(i).getValue() / 10.0,
                            RecommendationTypeEnum.USER_BASED_CF.getTypeCode()));
                    cnt ++;
                }
                if (cnt >= 30) {
                    break;
                }
            }
        } catch (TasteException e) {
            e.printStackTrace();
        }

        List<Recommendation> recommendations =
                recommendationMapper.selectList(new LambdaQueryWrapper<Recommendation>().eq(Recommendation::getUid, uid));
        int size = recommendations.size();
        
        if (size < THIRTY_RECOMMENDATIONS) {
            List<Recommendation> recommendations1 = randomRecommended(uid, 30 - size);
            saveBatch(recommendations1);
        }

        Timestamp t2 = Timestamp.valueOf(LocalDateTime.now());
        log.info("begin time:" + t1);
        log.info("end time:" + t2);

        log.info("end executeAsync");
    }

    /**
     * 基于内容电影推荐
     * @param uid 用户id
     * @param size 推荐电影数目
     * @return 推荐电影结果
     */
    public List<Pair<Long, Double>> getContentBasedMovieRecommendationResult(long uid, int size) {
        // 计算用户的偏好矩阵
        double[] userPreferenceMatrix = computeUserPreferenceMatrix(uid);
        System.out.println("userPreferenceMatrix:" + Arrays.toString(userPreferenceMatrix));

        List<Pair<Long, Double>> movieRecommendations = new ArrayList<>();
        // 所有电影特征信息矩阵都提前预处理过并存入数据库中，这里查询未被用户评价过且评分高于5分的电影特征矩阵列表
        List<MovieFeature> movieFeatures =
                movieFeatureMapper.selectAllMovieFeaturesWhereUserNotWatchedAndScoreMoreThanFive(uid);
        for (MovieFeature movieFeature : movieFeatures) {
            double[] movieFeatureMatrix = formatMovieFeatureMatrix(movieFeature.getMatrix());
            // 计算 用户的偏好矩阵 与 电影的特征信息矩阵 的相似度（运用余弦相似度计算公式）
            double dist = calculateTheUsersPreferenceForMovies(userPreferenceMatrix, movieFeatureMatrix);
            // 若相似度大于0，则加入结果集
            if (dist > 0) {
                movieRecommendations.add(new Pair<>(movieFeature.getMid(), dist));
            }
        }
        // 经过排序，将相似度高的电影排在前面
        movieRecommendations.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        // 取结果集中相似度最高的size部电影，如果结果集没有size部，则取结果集中所有电影
        int len = Math.min(movieRecommendations.size(), size);
        List<Pair<Long, Double>> res = new ArrayList<>();
        for (int i = 0; i < len; i ++ ) {
            res.add(movieRecommendations.get(i));
        }
        return res;
    }

    /**
     * 将电影特征矩阵由字符串转为数组
     * @param feature 电影特征矩阵字符串（例："10100000000..."）
     * @return 数组形式电影特征矩阵（例：[1.0, 0.0, 1.0, 0.0, ...]
     */
    private double[] formatMovieFeatureMatrix(String feature) {
        double[] movieFeatureMatrix = new double[42];
        String[] features = feature.split("");
        for (int i = 0; i < feature.length(); i ++ ) {
            movieFeatureMatrix[i] = Double.parseDouble(features[i]);
        }
        return movieFeatureMatrix;
    }

    /**
     * 计算用户对电影的喜好程度
     * 即计算 用户的偏好矩阵 与 电影的特征矩阵 的相似度（运用余弦相似度公式）
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

    /**
     * 计算用户的偏好矩阵
     * @param uid 用户id
     * @return 用户偏好矩阵
     */
    private double[] computeUserPreferenceMatrix(Long uid) {
        List<Comment> comments = commentMapper.selectList(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getUid, uid)
                .orderByDesc(Comment::getTime));
        // 如果用户评价过的电影超过5部，则只取最近评价的5部电影
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
        if (commentsLen == 0) {
            avg = 5;
        }
        avg = 5;

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




