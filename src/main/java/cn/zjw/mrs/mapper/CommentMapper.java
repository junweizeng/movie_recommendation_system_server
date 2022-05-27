package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.entity.Preference;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 95758
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2022-04-14 19:19:45
* @Entity cn.zjw.mrs.entity.Comment
*/
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 获取某个用户评价某部电影的评价信息
     * @param uid 用户id
     * @param mid 电影id
     * @return 评价信息
     */
    CommentStripVo selectOwnCommentByUidAndMid(Long uid, Long mid);

    /**
     * 获取某部电影下的所有评价信息
     * @param mid 电影id
     * @param currentPage 当前页数
     * @param pageSize 每页评论数
     * @return 所有评价信息
     */
    List<CommentStripVo> selectMoreCommentsByMovieId(Long mid, int currentPage, int pageSize);

    /**
     * 获取某个用户的所有评价动态
     * @param uid 用户id
     * @param currentIndex 从第几条开始取
     * @param pageSize 每页条数
     * @return 所有评价动态
     */
    List<CommentMovieVo> selectOwnCommentMovieMoments(Long uid, Integer currentIndex, Integer pageSize);

    /**
     * 获取用户所有的偏好
     * @return
     */
    List<Preference> selectAllPreferences();
}




