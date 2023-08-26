package com.limou.forum.controller;

import com.limou.forum.common.AppResult;
import com.limou.forum.exception.ApplicationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小李哞哞
 * @date 2023/8/26
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello forum";
    }

    @GetMapping("/exception")
    public AppResult testException() throws Exception{
        throw new Exception("这是一个Exception...");
    }

    @GetMapping("/appException")
    public AppResult testApplicationException() {
        throw new ApplicationException("这是一个ApplicationException...");
    }
}
