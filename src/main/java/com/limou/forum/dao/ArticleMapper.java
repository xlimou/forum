package com.limou.forum.dao;

import com.limou.forum.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article row);

    int insertSelective(Article row);

    Article selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Article row);

    int updateByPrimaryKeyWithBLOBs(Article row);

    int updateByPrimaryKey(Article row);

    /**
     * 查询帖子列表，每条帖子记录包含帖子信息、作者信息
     *
     * @return 帖子列表
     */
    List<Article> selectAll();

    /**
     * 按照板块id查询帖子列表，每条帖子记录包含帖子信息、作者信息
     *
     * @param boardId 板块id
     * @return 帖子列表
     */
    List<Article> selectByBoardId(@Param("boardId") Long boardId);

    /**
     * 按照id查询帖子详细信息(包含作者、板块信息)
     *
     * @param id 帖子id
     * @return 帖子详细信息
     */
    Article selectDetailById(@Param("id") Long id);
}