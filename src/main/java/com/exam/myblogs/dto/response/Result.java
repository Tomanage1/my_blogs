package com.exam.myblogs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Result<T> {
    private Integer errorCode;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setErrorCode(200);
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(Integer errorCode) {
        Result<T> result = new Result<>();
        result.setErrorCode(errorCode);
        result.setMsg("error");
        result.setData(null);
        return result;
    }

    public static <T> Result<T> error(String msg, T data) {
        Result<T> result = new Result<>();
        result.setErrorCode(400);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setErrorCode(400);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    public static <T> Result<T> error(Integer errorCode, String msg, T data) {
        Result<T> result = new Result<>();
        result.setErrorCode(errorCode);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}