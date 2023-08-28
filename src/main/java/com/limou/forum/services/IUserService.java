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
}
