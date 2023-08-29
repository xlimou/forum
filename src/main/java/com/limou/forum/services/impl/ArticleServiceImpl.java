package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.dao.ArticleMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.Article;
import com.limou.forum.model.Board;
import com.limou.forum.model.User;
import com.limou.forum.services.IArticleService;
import com.limou.forum.services.IBoardService;
import com.limou.forum.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private IUserService userService;
    @Resource
    private IBoardService boardService;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void create(Article article) {
        // 参数校验
        if (ObjUtil.isEmpty(article)
                || ObjUtil.isEmpty(article.getBoardId())
                || ObjUtil.isEmpty(article.getUserId())
                || StrUtil.isBlank(article.getTitle())
                || StrUtil.isBlank(article.getContent())) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }

        // 设置默认值(与数据库默认值构成双保险机制，使程序更健壮)
        article.setLikeCount(0);
        article.setDeleteState((byte) 0);
        article.setState((byte) 0);
        article.setReplyCount(0);
        article.setVisitCount(0);
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);

        // 写入数据库
        int row = articleMapper.insertSelective(article);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

        // 查询对应用户信息
        User user = userService.selectById(article.getUserId());
        // 非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", 发帖失败, user id = {}", article.getUserId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新用户发帖数
        userService.addOneArticleCountById(article.getUserId());

        // 查询对应板块信息
        Board board = boardService.selectById(article.getBoardId());
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", 发帖失败, board id = {}", article.getBoardId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新板块帖子数
        boardService.addOneArticleCountById(article.getBoardId());

        // 打印日志
        log.info(ResultCode.SUCCESS.toString() + ", article id = {}, user id = {}, board id = {}, 发帖成功！", article.getId(), article.getUserId(), article.getBoardId());

        // 测试事务是否正常生效
        // throw new ApplicationException("测试事务");
    }
}
