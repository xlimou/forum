package com.limou.forum.controller;

import com.limou.forum.common.AppResult;
import com.limou.forum.exception.ApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小李哞哞
 * @date 2023/8/26
 */
@Tag(name = "测试模块")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "项目测试接口", description = "测试整个项目是否正常运行")
    @GetMapping("/hello")
    public String hello() {
        return "Hello forum";
    }

    @Operation(summary = "普通异常测试接口", description = "测试项目中发生除业务异常外的异常处理情况")
    @GetMapping("/exception")
    public AppResult testException() throws Exception{
        throw new Exception("这是一个Exception...");
    }

    @Operation(summary = "业务异常测试接口", description = "测试项目中发生业务异常的处理情况")
    @GetMapping("/appException")
    public AppResult testApplicationException() {
        throw new ApplicationException("这是一个ApplicationException...");
    }
}
