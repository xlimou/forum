package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.dao.MessageMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.Message;
import com.limou.forum.model.User;
import com.limou.forum.services.IMessageService;
import com.limou.forum.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 小李哞哞
 * @date 2023/9/2
 */
@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private IUserService userService;

    @Override
    public void create(Message message) {
        // 参数校验
        if (ObjUtil.isEmpty(message)
                || ObjUtil.isEmpty(message.getPostUserId())
                || message.getPostUserId() <= 0
                || ObjUtil.isEmpty(message.getReceiveUserId())
                || message.getReceiveUserId() <= 0
                || StrUtil.isBlank(message.getContent())) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 设置默认值(双保险)
        message.setState((byte) 0); // 表示未读状态
        message.setDeleteState((byte) 0);
        Date date = new Date();
        message.setCreateTime(date);
        message.setUpdateTime(date);

        // 校验发送者和接收者是否存在
        User postUser = userService.selectById(message.getPostUserId());
        User receiveUser = userService.selectById(message.getReceiveUserId());
        if (ObjUtil.isEmpty(postUser) || ObjUtil.isEmpty(receiveUser)) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 调用DAO
        int row = messageMapper.insertSelective(message);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

    }
}
