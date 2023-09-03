package com.limou.forum.services;

import com.limou.forum.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/9/2
 */
public interface IMessageService {

    /**
     * 发送站内信息
     *
     * @param message 要发送的站内信
     */
    void create(Message message);

    /**
     * 根据接收方id查询未读站内信数量
     *
     * @param receiveUserId 接收方id
     * @return 站内信数量
     */
    Integer selectUnreadCount(Long receiveUserId);

    /**
     * 根据接收者用户id查询用户所有站内信信息，包含发送者信息
     *
     * @param receiveUserId 接收者用户id
     * @return 站内信列表
     */
    List<Message> selectByReceiveUserId(Long receiveUserId);

    /**
     * 根据id更新站内信状态
     *
     * @param id    站内信id
     * @param state 状态（0未读，1已读，2已回复）
     */
    void updateStateById(Long id, Byte state);

    /**
     * 根据id查询站内信信息
     *
     * @param id 站内信id
     * @return 站内信信息
     */
    Message selectById(Long id);

    /**
     * 回复站内信
     *
     * @param repliedId 要回复的站内信id
     * @param message   回复的消息对象
     */
    @Transactional
    void reply(Long repliedId, Message message);
}
