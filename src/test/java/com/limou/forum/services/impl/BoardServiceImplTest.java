package com.limou.forum.services.impl;

import com.limou.forum.model.Board;
import com.limou.forum.services.IBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@SpringBootTest
class BoardServiceImplTest {

    @Resource
    private IBoardService boardService;

    @Test
    void selectByNum() {
        List<Board> boards = boardService.selectByNum(3);
        boards.forEach(System.out::println);
    }

    @Transactional
    @Test
    void addOneArticleCountById() {
        boardService.addOneArticleCountById(1L);
    }
}