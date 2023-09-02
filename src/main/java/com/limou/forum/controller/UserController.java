package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            @Parameter(name = "passwordRepeat", description = "重复密码", required = true, in = ParameterIn.DEFAULT),})
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

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Operation(summary = "用户登录")
    @Parameters({
            @Parameter(name = "username", description = "用户名", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "password", description = "密码", required = true, in = ParameterIn.DEFAULT)})
    @PostMapping("/login")
    public AppResult login(HttpServletRequest request,
                           @RequestParam("username") @NonNull String username,
                           @RequestParam("password") @NonNull String password) {

        // 调用Service中的登录方法，返回User对象
        User user = userService.login(username, password);
        if (ObjUtil.isEmpty(user)) {
            log.warn(ResultCode.FAILED_LOGIN.toString());
            return AppResult.failed(ResultCode.FAILED_LOGIN);
        }

        // 如果登录成功把User对象设置到Session作用域中
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 返回结果
        return AppResult.success("登录成功");
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id，为空表示查询当前登录用户信息，否则查询指定id用户信息
     * @return AppResult
     */
    @Operation(summary = "获取用户信息", description = "id不为空则查询当前登录用户信息，否则查询指定id用户信息")
    @Parameter(name = "id", description = "用户id", in = ParameterIn.PATH)
    @GetMapping({"/info", "/info/{id}"})
    public AppResult getUserInfo(HttpServletRequest request,
                                 @PathVariable(value = "id", required = false) Long id) {

        User user = null;

        if (ObjUtil.isEmpty(id)) {
            // 如果id为空，从Session中获取当前登录的用户信息，不需要进行非空校验，因为拦截器已经处理了
            HttpSession session = request.getSession(false);
            user = (User) session.getAttribute(AppConfig.USER_SESSION);
        } else {
            // 如果id不为空，从数据库中按Id查询用户信息
            user = userService.selectById(id);
            if (ObjUtil.isEmpty(user)) {
                log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
                return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
            }
        }

        // 返回结果
        return AppResult.success(user);
    }

    /**
     * 退出登录
     *
     * @return AppResult
     */
    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public AppResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (ObjUtil.isNotEmpty(session)) {
            log.info("销毁用户Session，退出成功！");
            session.invalidate();
        }
        return AppResult.success("退出成功");
    }

    /**
     * 修改用户信息
     *
     * @param username    用户名(登录名)
     * @param nickname    昵称
     * @param gender      性别
     * @param email       邮箱
     * @param phoneNumber 电话
     * @param remark      个人简介
     * @return AppResult
     */
    @Operation(summary = "修改用户信息")
    @Parameters({
            @Parameter(name = "username", description = "用户名(登录名)", in = ParameterIn.DEFAULT),
            @Parameter(name = "nickname", description = "昵称", in = ParameterIn.DEFAULT),
            @Parameter(name = "gender", description = "性别", in = ParameterIn.DEFAULT),
            @Parameter(name = "email", description = "邮箱", in = ParameterIn.DEFAULT),
            @Parameter(name = "phoneNumber", description = "电话", in = ParameterIn.DEFAULT),
            @Parameter(name = "remark", description = "个人简介", in = ParameterIn.DEFAULT),
    })
    @PostMapping("/modifyInfo")
    public AppResult modifyInfo(HttpServletRequest request,
                                @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "nickname", required = false) String nickname,
                                @RequestParam(value = "gender", required = false) Byte gender,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                @RequestParam(value = "remark", required = false) String remark) {

        // 参数校验
        if (StrUtil.isBlank(username)
                && StrUtil.isBlank(nickname)
                && ObjUtil.isEmpty(gender)
                && StrUtil.isBlank(email)
                && StrUtil.isBlank(phoneNumber)
                && StrUtil.isBlank(remark)) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE);
        }
        // 获取当前登录用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 构造修改对象
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(username);
        updateUser.setNickname(nickname);
        updateUser.setGender(gender);
        updateUser.setEmail(email);
        updateUser.setPhoneNumber(phoneNumber);
        updateUser.setRemark(remark);
        // 调用Service
        userService.modifyInfo(updateUser);
        // 打印成功日志
        log.info("修改成功, user id = {}", user.getId());
        // 查询修改后的用户信息
        user = userService.selectById(user.getId());
        // 将session中的用户信息修改为最新的
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 返回成功结果
        return AppResult.success(user);
    }

    /**
     * 修改密码
     *
     * @param oldPassword    旧密码
     * @param newPassword    新密码
     * @param passwordRepeat 确认密码
     * @return AppResult
     */
    @Operation(summary = "修改密码")
    @Parameters({
            @Parameter(name = "oldPassword", description = "旧密码", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "newPassword", description = "新密码", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "passwordRepeat", description = "确认密码", required = true, in = ParameterIn.DEFAULT),
    })
    @PostMapping("/modifyPwd")
    public AppResult modifyPwd(HttpServletRequest request,
                               @RequestParam("oldPassword") @NonNull String oldPassword,
                               @RequestParam("newPassword") @NonNull String newPassword,
                               @RequestParam("passwordRepeat") @NonNull String passwordRepeat) {

        // 判断密码与确认密码是否相同
        if (!StrUtil.equals(newPassword, passwordRepeat)) {
            // 打印日志
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        // 获取当前登录用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);

        // 调用Service
        userService.modifyPassword(user.getId(), newPassword, oldPassword);
        // 打印日志
        log.info("密码修改成功, user id = {}", user.getId());
        // 让当前session失效，强迫用户使用新密码重新登录
        session.invalidate();
        // 返回成功结果
        return AppResult.success();
    }

}
