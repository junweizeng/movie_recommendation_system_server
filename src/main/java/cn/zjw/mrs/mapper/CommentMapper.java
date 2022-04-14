package cn.zjw.mrs.mapper;

import cn.zjw.mrs.entity.Comment;
import cn.zjw.mrs.vo.comment.OwnCommentVo;
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
    OwnCommentVo selectOwnCommentByUidAndMid(Long uid, Long mid);

    List<OwnCommentVo> selectCommentsByMovieId(Long mid);
}




