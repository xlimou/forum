package com.limou.forum.services;

import com.limou.forum.model.ArticleReply;
import org.springframework.transaction.annotation.Transactional;

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
}
