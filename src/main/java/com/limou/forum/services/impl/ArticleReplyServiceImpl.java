package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.dao.ArticleReplyMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.ArticleReply;
import com.limou.forum.services.IArticleReplyService;
import com.limou.forum.services.IArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/31
 */
@Slf4j
@Service
public class ArticleReplyServiceImpl implements IArticleReplyService {

    @Resource
    private ArticleReplyMapper articleReplyMapper;

    @Resource
    private IArticleService articleService;

    @Override
    public void create(ArticleReply articleReply) {
        // 非空校验
        if (ObjUtil.isEmpty(articleReply)
                || ObjUtil.isEmpty(articleReply.getArticleId())
                || ObjUtil.isEmpty(articleReply.getPostUserId())
                || StrUtil.isBlank(articleReply.getContent())) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }

        // 设置默认值(与数据库默认值构成双保险机制，使程序更健壮)
        articleReply.setReplyId(null);
        articleReply.setReplyUserId(null);
        articleReply.setState((byte) 0);
        articleReply.setDeleteState((byte) 0);
        articleReply.setLikeCount(0);
        Date date = new Date();
        articleReply.setCreateTime(date);
        articleReply.setUpdateTime(date);
        // 写入数据库
        int row = articleReplyMapper.insertSelective(articleReply);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
        // 更新帖子的评论数
        articleService.addOneReplyCountById(articleReply.getArticleId());
        // 打印日志
        log.info("评论成功, article reply id = {}, article id = {}, user id = {}", articleReply.getId(), articleReply.getArticleId(), articleReply.getPostUserId());

        // 测试事务是否正常生效
        // throw new ApplicationException("测试事务");
    }

    @Override
    public List<ArticleReply> selectByArticleId(Long articleId) {
        // 参数校验
        if (ObjUtil.isEmpty(articleId) || articleId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 调用DAO并返回结果
        return articleReplyMapper.selectByArticleId(articleId);
    }
}
