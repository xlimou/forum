package com.limou.forum.services;

import com.limou.forum.model.Article;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 查询帖子列表
     *
     * @return 帖子列表
     */
    List<Article> selectAll();

    /**
     * 根据板块id查询帖子列表
     *
     * @param boardId 板块id
     * @return 帖子列表
     */
    List<Article> selectByBoardId(Long boardId);
}
