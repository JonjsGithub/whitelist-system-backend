package cn.jonjs.whitelistsystembackend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    private Integer code; //0失败 1成功
    private String msg; //提示文本
    private T data; //数据

    public static <E> Result<E> success(E data) {
        return new Result<>(1, "操作成功", data);
    }
    public static <E> Result<E> success(String msg, E data) {
        return new Result<>(1, msg, data);
    }
    public static <E> Result<E> success() {
        return new Result<>(1, "操作成功", null);
    }
    public static <E> Result<E> success(String msg) {
        return new Result<>(1, msg, null);
    }

    public static <E> Result<E> error(E data) {
        return new Result<>(0, "操作失败", data);
    }
    public static <E> Result<E> error(String msg, E data) {
        return new Result<>(0, msg, data);
    }
    public static <E> Result<E> error() {
        return new Result<>(0, "操作失败", null);
    }
    public static <E> Result<E> error(String msg) {
        return new Result<>(0, msg, null);
    }


}
