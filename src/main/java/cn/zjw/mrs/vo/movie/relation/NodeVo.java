package cn.zjw.mrs.vo.movie.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjw
 * @Classname NodeVo
 * @Date 2022/4/23 18:01
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeVo {
    private Long id;
    private Long mid;
    private String name;
    private String value;
    private Integer category;
    private String types;
    private String regions;
}
