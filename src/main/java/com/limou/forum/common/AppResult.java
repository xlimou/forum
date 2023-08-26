package com.limou.forum.common;

/**
 * 全局返回结果
 *
 * @author 小李哞哞
 * @date 2023/8/27
 */
public class AppResult<T> {
    // 状态码
    private int code;
    // 描述信息
    private String message;
    // 具体的数据
    private T data;

    public AppResult(int code, String message) {
        this(code, message, null);
    }

    public AppResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @return
     */
    public static AppResult success() {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功
     *
     * @param message 自定义消息内容
     * @return
     */
    public static AppResult success(String message) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message);
    }

    public static <T> AppResult<T> success(T data) {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> AppResult<T> success(String message, T data) {
        return new AppResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     *
     * @return
     */
    public static AppResult failed() {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * 失败
     *
     * @param message 自定义消息内容
     * @return
     */
    public static AppResult failed(String message) {
        return new AppResult(ResultCode.FAILED.getCode(), message);
    }

    public static AppResult failed(ResultCode resultCode) {
        return new AppResult(resultCode.getCode(), resultCode.getMessage());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
