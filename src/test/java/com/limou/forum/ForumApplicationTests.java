package com.limou.forum;

import com.limou.forum.dao.UserMapper;
import com.limou.forum.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class ForumApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DataSource dataSource;

    @Test
    void contextLoads() {
        System.out.println("基于Spring的论坛系统-前后端分离");
    }

    @Test
    void testDruid() throws SQLException {
        System.out.println(dataSource.getClass());
        System.out.println(dataSource.getConnection());
    }

    @Test
    void testMybatis() {
        User user = userMapper.selectByPrimaryKey(1L);
        System.out.println(user);
        System.out.println(user.getUsername());
    }

}
