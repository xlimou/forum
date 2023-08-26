package com.limou.forum.exception;

import com.limou.forum.common.AppResult;

/**
 * 业务异常类
 *
 * @author 小李哞哞
 * @date 2023/8/27
 */
public class ApplicationException extends RuntimeException {

    // 在异常中持有一个错误结果对象
    protected AppResult errorResult;

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(AppResult errorResult) {
        super(errorResult.getMessage());
        this.errorResult = errorResult;
    }

    public ApplicationException(String message, AppResult errorResult) {
        super(message);
        this.errorResult = errorResult;
    }

    public ApplicationException(String message, Throwable cause, AppResult errorResult) {
        super(message, cause);
        this.errorResult = errorResult;
    }

    public ApplicationException(Throwable cause, AppResult errorResult) {
        super(cause);
        this.errorResult = errorResult;
    }

    public AppResult getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(AppResult errorResult) {
        this.errorResult = errorResult;
    }
}
