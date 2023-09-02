package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
import com.limou.forum.model.Message;
import com.limou.forum.model.User;
import com.limou.forum.services.IMessageService;
import com.limou.forum.services.IUserService;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 小李哞哞
 * @date 2023/9/2
 */
@Slf4j
@Tag(name = "站内信模块")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private IMessageService messageService;

    @Resource
    private IUserService userService;

    /**
     * 发送站内信
     *
     * @param receiveUserId 接收方id
     * @param content       消息内容
     * @return AppResult
     */
    @Operation(summary = "发送站内信")
    @Parameters({
            @Parameter(name = "receiveUserId", description = "接收方id", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "content", description = "消息内容", required = true, in = ParameterIn.DEFAULT)
    })
    @PostMapping("/send")
    public AppResult send(HttpServletRequest request,
                          @RequestParam("receiveUserId") @NonNull Long receiveUserId,
                          @RequestParam("content") @NonNull String content) {

        // 获取当前登录用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);

        // 校验是否给自己发私信, 不可以给自己发私信
        if (ObjUtil.equals(user.getId(), receiveUserId)) {
            // 打印日志
            log.warn(ResultCode.FAILED_MESSAGE_SELF.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_MESSAGE_SELF);
        }

        // 校验当前发送用户是否被禁言
        userService.checkState(request);

        // 校验接收方是否存在
        User receiveUser = userService.selectById(receiveUserId);
        if (ObjUtil.isEmpty(receiveUser)) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS);
        }

        // 构造消息对象
        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);

        // 调用Service
        messageService.create(message);

        // 返回成功结果
        return AppResult.success();
    }
}
