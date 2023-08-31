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

    /**
     * 根据帖子id查询帖子信息
     *
     * @param id 帖子id
     * @return 帖子信息
     */
    Article selectById(Long id);

    /**
     * 根据帖子id查询帖子详细信息(包含帖子信息、作者信息和板块信息)
     *
     * @param id 帖子id
     * @return 帖子详细信息
     */
    Article selectDetailById(Long id);

    /**
     * 编辑帖子
     *
     * @param id      帖子id
     * @param title   帖子标题
     * @param content 帖子正文
     */
    void modify(Long id, String title, String content);

    /**
     * 点赞帖子
     *
     * @param id 帖子id
     */
    void thumbsById(Long id);

    /**
     * 根据帖子id删除帖子(逻辑删除)
     *
     * @param id 帖子id
     */
    @Transactional
    void deleteById(Long id);

    /**
     * 帖子的评数量 + 1
     *
     * @param id 帖子id
     */
    void addOneReplyCountById(Long id);
}
