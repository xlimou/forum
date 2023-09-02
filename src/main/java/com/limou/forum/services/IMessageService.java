package com.limou.forum.services;

import com.limou.forum.model.Message;

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
}
