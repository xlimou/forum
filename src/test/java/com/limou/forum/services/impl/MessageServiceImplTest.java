package com.limou.forum.services.impl;

import com.limou.forum.model.Message;
import com.limou.forum.services.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 小李哞哞
 * @date 2023/9/2
 */
@SpringBootTest
class MessageServiceImplTest {

    @Resource
    private IMessageService messageService;

    @Transactional
    @Test
    void create() {
        // 构造消息对象
        Message message = new Message();
        message.setPostUserId(2L);
        message.setReceiveUserId(1L);
        message.setContent("测试消息111");
        messageService.create(message);
    }

    @Test
    void selectUnreadCount() {
        Integer count = messageService.selectUnreadCount(2L);
        System.out.println(count);
    }

    @Test
    void selectByReceiveUserId() {
        List<Message> messages = messageService.selectByReceiveUserId(2L);
        messages.forEach(System.out::println);
    }

    @Transactional
    @Test
    void updateStateById() {
        messageService.updateStateById(2L, (byte) 1);
    }

    @Test
    void selectById() {
        Message message = messageService.selectById(1L);
        System.out.println(message);
    }
}