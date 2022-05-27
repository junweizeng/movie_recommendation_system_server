package cn.zjw.mrs;

import cn.zjw.mrs.entity.Preference;
import cn.zjw.mrs.mapper.CommentMapper;
import cn.zjw.mrs.vo.comment.CommentMovieVo;
import cn.zjw.mrs.vo.comment.CommentStripVo;
import net.sf.jsqlparser.Model;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericItemPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Classname CommentTest
 * @Date 2022/4/14 22:09
 * @Created by zjw
 * @Description
 */
@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void getOwnCommentTest() {
        CommentStripVo commentStripVo = commentMapper.selectOwnCommentByUidAndMid((long) 11, (long) 5);
        System.out.println(commentStripVo);
    }

    @Test
    public void getCommentsByMovieId() {
        List<CommentStripVo> commentStripVo = commentMapper.selectMoreCommentsByMovieId((long) 500, 5, 5);
        System.out.println(commentStripVo);
    }

    @Test
    public void getCommentMovieMoments() {
        List<CommentMovieVo> commentMovieVos = commentMapper.selectOwnCommentMovieMoments((long) 10, 0, 10);
        for (CommentMovieVo commentStripVo: commentMovieVos) {
            System.out.println(commentStripVo.getCommentStripVo().toString());
            System.out.println(commentStripVo.getMovieStripVo().toString());
            System.out.println();
        }
        System.out.println(commentMovieVos);
    }

    @Test
    public void getAllPreferences() {
        List<Preference> preferences = commentMapper.selectAllPreferences();
        for (Preference preference : preferences) {
            System.out.println(preference);
        }
        for (int i = 0; i < preferences.size(); i ++ ) {
            int cnt = preferences.get(i).getCnt();
            PreferenceArray preferenceArray = new GenericUserPreferenceArray(cnt);
            preferenceArray.setUserID(0, preferences.get(i).getUid());
            for (int j = 0; j < cnt; j ++ ) {
                System.out.println(preferences.get(i + j).getMid() + " " + preferences.get(i + j).getScore());
                preferenceArray.setItemID(j, preferences.get(i + j).getMid());
                preferenceArray.setValue(j, preferences.get(i + j).getScore());
            }
            System.out.println(preferenceArray);

            i += cnt - 1;
        }
    }
}
