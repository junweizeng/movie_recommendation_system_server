package cn.zjw.mrs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zjw
 * @Classname Result<T>
 * @Date 2022/4/7 16:18
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据
     */
    private Object data;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(200, msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(500, "失败", null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    public static <T> Result<T> error(String msg, T data) {
        return new Result<>(500, msg, data);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg);
    }
}