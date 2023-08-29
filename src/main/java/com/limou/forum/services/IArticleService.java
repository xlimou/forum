package com.limou.forum.services;

import com.limou.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
public interface IArticleService {

    /**
     * 发布帖子
     *
     * @param article 要发布的帖子
     */
    @Transactional
    void create(Article article);
}
