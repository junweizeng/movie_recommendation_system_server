package cn.zjw.mrs.vo.movie.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjw
 * @Classname CategoryVo
 * @Date 2022/4/23 18:16
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {

    private String name;

    private String base;

    public CategoryVo(String name) {
        this.name = name;
    }
}
