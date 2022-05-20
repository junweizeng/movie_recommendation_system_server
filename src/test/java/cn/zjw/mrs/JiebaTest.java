package cn.zjw.mrs;

import com.huaban.analysis.jieba.JiebaSegmenter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zjw
 * @Classname JiebaTest
 * @Date 2022/5/20 10:48
 * @Description
 */
@SpringBootTest
public class JiebaTest {

    @Test
    public void testDemo() {
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
    }

    public static void main(String[] args) {
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            // 去掉两边的空格
            sentence = sentence.trim();
            // 将所有标点符号换成空格
            sentence = sentence.replaceAll("\\p{P}", " ");
            System.out.println(jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).toString());
        }
    }
}
