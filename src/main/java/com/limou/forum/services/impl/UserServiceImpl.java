package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
import com.limou.forum.dao.UserMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.User;
import com.limou.forum.services.IUserService;
import com.limou.forum.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author 小李哞哞
 * @date 2023/8/28
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User selectByUsername(String username) {
        // 非空校验
        if (StrUtil.isBlank(username)) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 返回查询结果
        return userMapper.selectByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        // 非空校验
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 按用户名查询用户信息
        User user = selectByUsername(username);
        // 对查询结果做非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.FAILED_LOGIN.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 校验密码
        boolean checked = MD5Util.checkPwd(password, user.getPassword(), user.getSalt());
        if (!checked) {
            // 打印日志
            log.warn(ResultCode.FAILED_LOGIN.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
        // 登录成功，返回用户信息
        log.info("登录成功, username = {}", user.getUsername());
        return user;
    }

    @Override
    public User selectById(Long id) {
        // 非空校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 查询数据库并返回结果
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void createNormalUser(User user) {
        // 非空校验
        if (ObjUtil.isEmpty(user)
                || StrUtil.isBlank(user.getUsername())
                || StrUtil.isBlank(user.getPassword())
                || StrUtil.isBlank(user.getNickname())
                || StrUtil.isBlank(user.getSalt())) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }

        // 按用户名查询用户信息
        User existsUser = selectByUsername(user.getUsername());
        // 判断用户是否存在
        if (ObjUtil.isNotEmpty(existsUser)) {
            log.warn(ResultCode.FAILED_USER_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }

        // 新增用户流程，设置默认值(虽然数据库设置了默认值，但是这里还是要写，双保险)
        user.setGender((byte) 2);
        user.setArticleCount(0);
        user.setIsAdmin((byte) 0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);
        // 当前日期
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);

        // 写入数据库
        int row = userMapper.insertSelective(user);
        // 结果校验
        if (row != 1) {
            log.info(ResultCode.FAILED_CREATE.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }

        // 新增成功
        log.info("新增用户成功，username = {}", user.getUsername());
    }

    @Override
    public void checkState(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 获取当前登录的用户
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_BANNED));
        }
    }

    @Override
    public void addOneArticleCountById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_ARTICLE_COUNT.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT));
        }
        // 查询对应的用户
        User user = userMapper.selectByPrimaryKey(id);
        // 非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString() + ", user id = {}", id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 更新用户的发帖数量，注意要创建一个新对象，设置关键的值即可，如果直接使用查出来的对象，那么将会修改所有值
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() + 1);
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_ARTICLE_COUNT.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_ARTICLE_COUNT));
        }
        // 查询对应的用户
        User user = userMapper.selectByPrimaryKey(id);
        // 非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString() + ", user id = {}", id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 更新用户的发帖数量，注意要创建一个新对象，设置关键的值即可，如果直接使用查出来的对象，那么将会修改所有值
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setArticleCount(user.getArticleCount() - 1);
        // 判断减1之后是否小于0
        if (updateUser.getArticleCount() < 0) {
            // 小于0就设置为0
            updateUser.setArticleCount(0);
        }
        // 调用DAO
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
