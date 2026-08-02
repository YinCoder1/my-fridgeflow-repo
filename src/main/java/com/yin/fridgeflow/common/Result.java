package com.yin.fridgeflow.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应结果包装类。
 * <p>所有 Controller 接口统一返回 {@code Result<T>}，前端据 code 判断成败，
 * message 为提示文案，data 为业务数据（可为 null）。</p>
 *
 * <p><b>常用状态码：</b></p>
 * <ul>
 *   <li>200 - 成功</li>
 *   <li>401 - 未认证</li>
 *   <li>403 - 无权限</li>
 *   <li>404 - 资源不存在</li>
 *   <li>409 - 冲突（如已加入家庭）</li>
 *   <li>500 - 服务器内部错误</li>
 * </ul>
 *
 * @param <T> 业务数据类型
 * @author yin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /** 状态码，200 表示成功 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 业务数据，无数据时为 null */
    private T data;

    /**
     * 成功（无数据）。
     * @return code=200, message="success", data=null
     */
    public static <T> Result<T> success(){
        return new Result<>(200,"success",null);
    }

    /**
     * 成功（有数据）。
     * @param data 业务数据
     * @return code=200, message="success"
     */
    public static <T> Result<T> success(T data){
        return new Result<>(200,"success",data);
    }

    /**
     * 成功（自定义消息+数据）。
     * @param message 提示信息
     * @param data    业务数据，可为 null
     * @return code=200
     */
    public static <T> Result<T> success(String message,T data){
        return new Result<>(200,message,data);
    }

    /**
     * 失败（自定义状态码+消息）。
     * @param code    状态码
     * @param message 提示信息
     * @return data=null
     */
    public static <T> Result<T> error(int code,String message){
        return new Result<>(code,message,null);
    }

    /**
     * 失败（快捷常用，code=500）。
     * @param message 提示信息
     * @return code=500, data=null
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 未认证（401）。
     * @param message 提示信息
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /**
     * 无权限（403）。
     * @param message 提示信息
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /**
     * 资源不存在（404）。
     * @param message 提示信息
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 冲突（409）。
     * @param message 提示信息
     */
    public static <T> Result<T> conflict(String message) {
        return new Result<>(409, message, null);
    }
}
