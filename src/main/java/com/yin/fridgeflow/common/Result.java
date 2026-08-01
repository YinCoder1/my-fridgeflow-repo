package com.yin.fridgeflow.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    //成功（无数据）
    public static <T> Result<T> success(){
        return new Result<>(200,"success",null);
    }

    //成功（有数据）
    public static <T> Result<T> success(T data){
        return new Result<>(200,"success",data);
    }

    //成功（自定义消息+数据）
    public static <T> Result<T> success(String message,T data){
        return new Result<>(200,message,data);
    }

    //失败
    public static <T> Result<T> error(int code,String message){
        return new Result<>(code,message,null);
    }

    // 失败（快捷常用状态码）
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    public static <T> Result<T> conflict(String message) {
        return new Result<>(409, message, null);
    }

}
