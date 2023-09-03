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
import java.util.List;

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

    @Override
    public Integer selectUnreadCount(Long receiveUserId) {
        // 参数校验
        if (ObjUtil.isEmpty(receiveUserId) || receiveUserId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 调用DAO
        Integer count = messageMapper.selectUnreadCount(receiveUserId);
        // 结果校验, 正常的查询是不可能出现null这个结果的，倘若出现就抛出服务器异常
        if (ObjUtil.isEmpty(count)) {
            // 打印日志
            log.warn(ResultCode.ERROR_SERVICES.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_SERVICES));
        }
        // 返回结果
        return count;
    }

    @Override
    public List<Message> selectByReceiveUserId(Long receiveUserId) {
        // 参数校验
        if (ObjUtil.isEmpty(receiveUserId) || receiveUserId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 调用DAO并返回结果
        return messageMapper.selectByReceiveUserId(receiveUserId);
    }

    @Override
    public void updateStateById(Long id, Byte state) {
        // 参数校验
        if (ObjUtil.isEmpty(id)
                || id <= 0
                || ObjUtil.isEmpty(state)
                || state < 0
                || state > 2) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 构造更新对象
        Message updateMessage = new Message();
        updateMessage.setId(id);
        updateMessage.setState(state);
        updateMessage.setUpdateTime(new Date());
        // 调用DAO
        int row = messageMapper.updateByPrimaryKeySelective(updateMessage);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public Message selectById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 调用DAO并返回
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public void reply(Long repliedId, Message message) {
        // 调用已有方法查询站内信信息
        Message existsMsg = selectById(repliedId);
        // 非空校验
        if (ObjUtil.isEmpty(existsMsg)) {
            // 打印日志
            log.warn(ResultCode.FAILED_MESSAGE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_MESSAGE_NOT_EXISTS));
        }
        // 更新状态为2(已回复)
        updateStateById(repliedId, (byte) 2);

        // 将回复的消息写入数据库
        create(message);

        // 测试事务
        // throw new ApplicationException("测试事务");
    }
}
