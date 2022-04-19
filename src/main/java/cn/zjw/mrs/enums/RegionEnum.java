package cn.zjw.mrs.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author zjw
 * @Classname RegionEnum
 * @Date 2022/4/12 22:48
 * @Description
 */
@Getter
public enum RegionEnum {
    // 地区代码 —— 地区名称
    MAINLAND_CHINA(1, "中国大陆"),
    USA(2, "美国"),
    HONG_KONG(3, "中国香港"),
    CHINESE_TAIWAN(4, "中国台湾"),
    JAPAN(5, "日本"),
    KOREA(6, "韩国"),
    UK(7, "英国"),
    FRANCE(8, "法国"),
    GERMANY(9, "德国"),
    ITALY(10, "意大利"),
    SPAIN(11, "西班牙"),
    INDIA(12, "印度"),
    THAILAND(13, "泰国"),
    RUSSIA(14, "俄罗斯"),
    IRAN(15, "伊朗"),
    CANADA(16, "加拿大"),
    AUSTRALIA(17, "澳大利亚"),
    IRELAND(18, "爱尔兰"),
    SWEDEN(19, "瑞典"),
    BRAZIL(20, "巴西"),
    DENMARK(21, "丹麦");

    /**
     * 地区名称代码
     * 将注解所标识的属性的值存储到数据库中
     */
    @EnumValue
    private final Integer region;
    /**
     * 地区名称
     */
    private final String regionName;

    RegionEnum(Integer region, String regionName) {
        this.region = region;
        this.regionName = regionName;
    }

}
