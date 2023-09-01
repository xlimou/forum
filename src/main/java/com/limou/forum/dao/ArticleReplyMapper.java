package com.limou.forum.dao;

import com.limou.forum.model.ArticleReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleReplyMapper {
    int insert(ArticleReply row);

    int insertSelective(ArticleReply row);

    ArticleReply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleReply row);

    int updateByPrimaryKey(ArticleReply row);

    /**
     * 根据帖子id查询评论信息，其中包含评论发布者信息
     *
     * @param articleId 帖子id
     * @return 评论列表
     */
    List<ArticleReply> selectByArticleId(@Param("articleId") Long articleId);
}