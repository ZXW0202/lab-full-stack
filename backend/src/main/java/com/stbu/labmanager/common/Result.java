package com.stbu.labmanager.common;

import lombok.Data;

/**
 * 统一返回结果
 */
@Data
public class Result<T> {
    // 成功码 200，失败码 500
    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_CODE = "500";

    private String code;
    private String msg;
    private T data;

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(SUCCESS_CODE);
        r.setMsg("成功");
        r.setData(data);
        return r;
    }

    // 成功（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(ERROR_CODE);
        r.setMsg(msg);
        return r;
    }
}