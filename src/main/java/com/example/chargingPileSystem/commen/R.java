package com.example.chargingPileSystem.commen;
import com.example.chargingPileSystem.enums.ErrorEnum;
import lombok.Data;
import java.io.Serializable;
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 42221335446390L;
    private static final String REQUEST_SUCCESS = "success";

    private int code;
    private String msg;
    private T data;

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //修饰符 <泛型变量> 返回值类型 方法名(参数类型 参数列表){ }
    public static <T> R<T> ok(T data) {
        return new R<>(0, REQUEST_SUCCESS, data);
    }

    public static R<Object> ok() {
        return new R<>(0, REQUEST_SUCCESS, null);
    }

    public static <T> R<T> fail(ErrorEnum errorEnum, T data) {
        return new R<>(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    public static <T> R<T> fail(ErrorEnum errorEnum) {
        return new R<>(errorEnum.getCode(), errorEnum.getMsg(), null);
    }
}
