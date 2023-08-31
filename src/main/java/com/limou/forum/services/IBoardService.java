package com.limou.forum.services;

import com.limou.forum.model.Board;

import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
public interface IBoardService {

    /**
     * 查询num条记录
     *
     * @param num 要查询的条数
     * @return 查询结果
     */
    List<Board> selectByNum(Integer num);

    /**
     * 查询所有有效记录，升序
     *
     * @return 查询结果
     */
    List<Board> selectAllBySort();

    /**
     * 根据id查询板块信息
     *
     * @param id 板块id
     * @return 板块信息
     */
    Board selectById(Long id);

    /**
     * 板块的帖子数量 + 1
     *
     * @param id 板块id
     */
    void addOneArticleCountById(Long id);

    /**
     * 板块的帖子数量 - 1
     *
     * @param id 板块id
     */
    void subOneArticleCountById(Long id);
}
