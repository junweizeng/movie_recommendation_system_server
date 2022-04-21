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
    // 地区标识 —— 地区名称
    MAINLAND_CHINA(0, "中国大陆"),
    USA(1, "美国"),
    HONG_KONG(2, "中国香港"),
    CHINESE_TAIWAN(3, "中国台湾"),
    JAPAN(4, "日本"),
    KOREA(5, "韩国"),
    UK(6, "英国"),
    FRANCE(7, "法国"),
    GERMANY(8, "德国"),
    ITALY(9, "意大利"),
    SPAIN(10, "西班牙"),
    INDIA(11, "印度"),
    THAILAND(12, "泰国"),
    RUSSIA(13, "俄罗斯"),
    IRAN(14, "伊朗"),
    CANADA(15, "加拿大"),
    AUSTRALIA(16, "澳大利亚"),
    IRELAND(17, "爱尔兰"),
    SWEDEN(18, "瑞典"),
    BRAZIL(19, "巴西"),
    DENMARK(20, "丹麦");

    /**
     * 地区标识
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
