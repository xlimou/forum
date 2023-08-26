package com.limou.forum.exception;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 *
 * @author 小李哞哞
 * @date 2023/8/27
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public AppResult applicationExceptionHandler(ApplicationException e) {
        // 开发环境打印异常信息
        e.printStackTrace();
        // 线上环境打印日志
        log.error(e.getMessage());
        // 有错误结果对象就直接返回
        if (ObjUtil.isNotEmpty(e.getErrorResult())) {
            return e.getErrorResult();
        }
        // 无错误结果对象，有异常信息的返回
        if (StrUtil.isNotBlank(e.getMessage())) {
            return AppResult.failed(e.getMessage());
        }
        // 无错误结果对象，也没有异常信息的返回
        return AppResult.failed(ResultCode.ERROR_SERVICES);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AppResult exceptionHandler(Exception e) {
        // 开发环境打印异常信息
        e.printStackTrace();
        // 线上环境打印日志
        log.error(e.getMessage());
        // 有异常信息的返回
        if (StrUtil.isNotBlank(e.getMessage())) {
            return AppResult.failed(e.getMessage());
        }
        // 无异常信息的返回
        return AppResult.failed(ResultCode.ERROR_SERVICES);
    }
}
