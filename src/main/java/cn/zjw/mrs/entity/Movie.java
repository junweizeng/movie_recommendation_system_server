package cn.zjw.mrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * @author zjw
 * @TableName movie
 */
@TableName(value ="movie")
@Data
public class Movie implements Serializable {
    /**
     * 电影ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 豆瓣ID
     */
    private Long did;

    /**
     * 电影名
     */
    private String name;

    /**
     * 上映年份
     */
    private Integer year;

    /**
     * 导演
     */
    private String directors;

    /**
     * 编剧
     */
    private String writers;

    /**
     * 主演
     */
    private String actors;

    /**
     * 类型
     */
    private String types;

    /**
     * 地区
     */
    private String regions;

    /**
     * 语言
     */
    private String languages;

    /**
     * 上映时间
     */
    private String releaseDate;

    /**
     * 时长
     */
    private String runtime;

    /**
     * 别名
     */
    private String alias;

    /**
     * IMDb号
     */
    private String imdb;

    /**
     * 评分
     */
    private Double score;

    /**
     * 评价人数
     */
    private String num;

    /**
     * 5星百分比
     */
    private Double five;

    /**
     * 4星百分比
     */
    private Double four;

    /**
     * 3星百分比
     */
    private Double three;

    /**
     * 2星百分比
     */
    private Double two;

    /**
     * 1星百分比
     */
    private Double one;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 电影海报
     */
    private String pic;

    /**
     * 最近一次爬取时间
     */
    private Timestamp crawlTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Movie other = (Movie) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDid() == null ? other.getDid() == null : this.getDid().equals(other.getDid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getYear() == null ? other.getYear() == null : this.getYear().equals(other.getYear()))
            && (this.getDirectors() == null ? other.getDirectors() == null : this.getDirectors().equals(other.getDirectors()))
            && (this.getWriters() == null ? other.getWriters() == null : this.getWriters().equals(other.getWriters()))
            && (this.getActors() == null ? other.getActors() == null : this.getActors().equals(other.getActors()))
            && (this.getTypes() == null ? other.getTypes() == null : this.getTypes().equals(other.getTypes()))
            && (this.getRegions() == null ? other.getRegions() == null : this.getRegions().equals(other.getRegions()))
            && (this.getLanguages() == null ? other.getLanguages() == null : this.getLanguages().equals(other.getLanguages()))
            && (this.getReleaseDate() == null ? other.getReleaseDate() == null : this.getReleaseDate().equals(other.getReleaseDate()))
            && (this.getRuntime() == null ? other.getRuntime() == null : this.getRuntime().equals(other.getRuntime()))
            && (this.getAlias() == null ? other.getAlias() == null : this.getAlias().equals(other.getAlias()))
            && (this.getImdb() == null ? other.getImdb() == null : this.getImdb().equals(other.getImdb()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()))
            && (this.getNum() == null ? other.getNum() == null : this.getNum().equals(other.getNum()))
            && (this.getFive() == null ? other.getFive() == null : this.getFive().equals(other.getFive()))
            && (this.getFour() == null ? other.getFour() == null : this.getFour().equals(other.getFour()))
            && (this.getThree() == null ? other.getThree() == null : this.getThree().equals(other.getThree()))
            && (this.getTwo() == null ? other.getTwo() == null : this.getTwo().equals(other.getTwo()))
            && (this.getOne() == null ? other.getOne() == null : this.getOne().equals(other.getOne()))
            && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
            && (this.getPic() == null ? other.getPic() == null : this.getPic().equals(other.getPic()))
            && (this.getCrawlTime() == null ? other.getCrawlTime() == null : this.getCrawlTime().equals(other.getCrawlTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDid() == null) ? 0 : getDid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getYear() == null) ? 0 : getYear().hashCode());
        result = prime * result + ((getDirectors() == null) ? 0 : getDirectors().hashCode());
        result = prime * result + ((getWriters() == null) ? 0 : getWriters().hashCode());
        result = prime * result + ((getActors() == null) ? 0 : getActors().hashCode());
        result = prime * result + ((getTypes() == null) ? 0 : getTypes().hashCode());
        result = prime * result + ((getRegions() == null) ? 0 : getRegions().hashCode());
        result = prime * result + ((getLanguages() == null) ? 0 : getLanguages().hashCode());
        result = prime * result + ((getReleaseDate() == null) ? 0 : getReleaseDate().hashCode());
        result = prime * result + ((getRuntime() == null) ? 0 : getRuntime().hashCode());
        result = prime * result + ((getAlias() == null) ? 0 : getAlias().hashCode());
        result = prime * result + ((getImdb() == null) ? 0 : getImdb().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        result = prime * result + ((getNum() == null) ? 0 : getNum().hashCode());
        result = prime * result + ((getFive() == null) ? 0 : getFive().hashCode());
        result = prime * result + ((getFour() == null) ? 0 : getFour().hashCode());
        result = prime * result + ((getThree() == null) ? 0 : getThree().hashCode());
        result = prime * result + ((getTwo() == null) ? 0 : getTwo().hashCode());
        result = prime * result + ((getOne() == null) ? 0 : getOne().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getPic() == null) ? 0 : getPic().hashCode());
        result = prime * result + ((getCrawlTime() == null) ? 0 : getCrawlTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", did=").append(did);
        sb.append(", name=").append(name);
        sb.append(", year=").append(year);
        sb.append(", directors=").append(directors);
        sb.append(", writers=").append(writers);
        sb.append(", actors=").append(actors);
        sb.append(", types=").append(types);
        sb.append(", regions=").append(regions);
        sb.append(", languages=").append(languages);
        sb.append(", releaseDate=").append(releaseDate);
        sb.append(", runtime=").append(runtime);
        sb.append(", alias=").append(alias);
        sb.append(", imdb=").append(imdb);
        sb.append(", score=").append(score);
        sb.append(", num=").append(num);
        sb.append(", five=").append(five);
        sb.append(", four=").append(four);
        sb.append(", three=").append(three);
        sb.append(", two=").append(two);
        sb.append(", one=").append(one);
        sb.append(", introduction=").append(introduction);
        sb.append(", pic=").append(pic);
        sb.append(", crawlTime=").append(crawlTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


}