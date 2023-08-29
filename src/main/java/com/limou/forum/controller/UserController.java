package com.limou.forum.controller;

import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.model.User;
import com.limou.forum.services.IUserService;
import com.limou.forum.utils.MD5Util;
import com.limou.forum.utils.UUIDUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
@Tag(name = "用户模块")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 用户注册
     *
     * @param username       用户名
     * @param nickname       用户昵称
     * @param password       密码
     * @param passwordRepeat 重复密码
     * @return
     */
    @Operation(summary = "用户注册")
    @Parameters({
            @Parameter(name = "username", description = "用户名", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "nickname", description = "昵称", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "password", description = "密码", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "passwordRepeat", description = "重复密码", required = true, in = ParameterIn.DEFAULT),
    })
    @PostMapping("/register")
    public AppResult register(@RequestParam("username") @NonNull String username,
                              @RequestParam("nickname") @NonNull String nickname,
                              @RequestParam("password") @NonNull String password,
                              @RequestParam("passwordRepeat") @NonNull String passwordRepeat) {

        // 校验密码与重复密码是否相同
        if (!StrUtil.equals(password, passwordRepeat)) {
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        // 准备数据
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        // 生成盐
        String salt = UUIDUtil.UUID_32();
        user.setSalt(salt);
        // 加盐加密
        String encryptedPwd = MD5Util.md5Salt(password, salt);
        user.setPassword(encryptedPwd);

        // 调用Service层
        userService.createNormalUser(user);
        // 返回成功
        return AppResult.success("注册成功");

    }
}