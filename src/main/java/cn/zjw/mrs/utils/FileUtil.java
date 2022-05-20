package cn.zjw.mrs.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjw
 * @Classname FileUtil
 * @Date 2022/5/20 22:45
 * @Description
 */
public class FileUtil {
    /**
     * 读取StopWords.txt文件中的停用词
     * @return 读取结果
     */
    public static List<String> readStopWords() {
        List<String> stopWords = new ArrayList<>();

        ClassPathResource classPathResource = new ClassPathResource("static/assets/StopWords.txt");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while(reader.ready()) {
                String line = reader.readLine();
                stopWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }
}
