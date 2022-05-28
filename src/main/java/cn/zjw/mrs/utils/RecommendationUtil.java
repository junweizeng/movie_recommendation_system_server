package cn.zjw.mrs.utils;

import cn.zjw.mrs.enums.RecommendationTypeEnum;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zjw
 * @Classname RecommendationUtil
 * @Date 2022/5/28 20:29
 * @Description
 */
public class RecommendationUtil {

    /**
     * 随机指定范围内N个不重复的数；
     * 利用HashSet的特征，只能存放不同的值；
     * 生成的随机数在[min, max)之间。
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     * @param set 随机数结果集
     */
    public static void randomSet(int min, int max, int n, Set<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // [0, 1) => [min, max)
            int num = (int) (Math.random() * (max - min) + min) ;
            // 将不同的数存入HashSet中
            set.add(num);
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            // 递归
            randomSet(min, max, n - setSize, set);
        }
    }
}
