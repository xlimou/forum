package com.limou.forum.services;

import com.limou.forum.model.User;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
public interface IUserService {
    /**
     * 创建一个普通用户
     *
     * @param user 用户信息
     */
    void createNormalUser(User user);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);

    /**
     * 处理用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);

    /**
     * 根据Id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    User selectById(Long id);

    /**
     * 更新当前用户的发帖数
     *
     * @param id 用户id
     */
    void addOneArticleCountById(Long id);
}
