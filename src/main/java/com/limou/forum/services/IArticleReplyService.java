package com.limou.forum.services;

import com.limou.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/31
 */
public interface IArticleReplyService {

    /**
     * 添加评论
     *
     * @param articleReply 要添加的评论
     */
    @Transactional
    void create(ArticleReply articleReply);

    /**
     * 根据帖子id查询评论信息，其中包含评论发布者信息
     *
     * @param articleId 帖子id
     * @return 评论列表
     */
    List<ArticleReply> selectByArticleId(Long articleId);
}
