package com.limou.forum.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 全局返回结果
 *
 * @author 小李哞哞
 * @date 2023/8/27
 */
@Schema(name = "返回结果类")
public class AppResult {
    // 状态码
    @Schema(title = "状态码")
    @JsonInclude(JsonInclude.Include.ALWAYS) // 无论任何情况，都参与JSON序列化
    private int code;
    // 描述信息
    @Schema(title = "描述信息")
    @JsonInclude(JsonInclude.Include.ALWAYS) // 无论任何情况，都参与JSON序列化
    private String message;
    // 具体的数据
    @Schema(title = "返回的具体数据")
    @JsonInclude(JsonInclude.Include.ALWAYS) // 无论任何情况，都参与JSON序列化
    private Object data;

    public AppResult(int code, String message) {
        this(code, message, null);
    }

    public AppResult(int code, String message, Object data) {
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

    public static AppResult success(Object data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static AppResult success(String message, Object data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message, data);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
