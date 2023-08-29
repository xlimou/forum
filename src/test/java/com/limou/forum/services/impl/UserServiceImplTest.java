package com.limou.forum.services.impl;

import com.limou.forum.model.User;
import com.limou.forum.services.IUserService;
import com.limou.forum.utils.MD5Util;
import com.limou.forum.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
@SpringBootTest
class UserServiceImplTest {

    @Resource
    private IUserService userService;

    @Transactional
    @Test
    void createNormalUser() {
        // 构造user对象
        User user = new User();
        user.setUsername("testuser333");
        user.setNickname("testuser333");
        // 定义原始密码
        String password = "123123";
        // 生成盐
        String salt = UUIDUtil.UUID_32();
        // 设置盐
        user.setSalt(salt);
        // 生成一个加盐加密之后的密码密文
        String encryptedPwd = MD5Util.md5Salt(password, salt);
        // 设置密码密文
        user.setPassword(encryptedPwd);
        userService.createNormalUser(user);
        // 打印结果
        System.out.println(user);
    }

    @Test
    void selectByUsername() {
        User user = userService.selectByUsername("bitboy");
        System.out.println(user);
    }

    @Test
    void login() {
        User bitboy = userService.login("bitboy", "123456");
        System.out.println(bitboy);
    }

    @Test
    void selectById() {
        User user = userService.selectById(1L);
        System.out.println(user);
    }

    @Transactional
    @Test
    void addOneArticleCountById() {
        userService.addOneArticleCountById(1L);
    }
}