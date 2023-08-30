package com.limou.forum.services.impl;

import com.limou.forum.model.Article;
import com.limou.forum.services.IArticleService;
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
class ArticleServiceImplTest {

    @Resource
    private IArticleService articleService;


    @Transactional
    @Test
    void create() {
        Article article = new Article();
        article.setUserId(1L); // bitboy
        article.setBoardId(1L); // Java板块
        article.setTitle("单元测试");
        article.setContent("测试内容");
        articleService.create(article);
    }

    @Test
    void selectAll() {
        List<Article> articles = articleService.selectAll();
        articles.forEach(System.out::println);
    }

    @Test
    void selectByBoardId() {
        List<Article> articles = articleService.selectByBoardId(1L);
        articles.forEach(System.out::println);
    }

    @Test
    void selectDetailById() {
        Article article = articleService.selectDetailById(8L);
        System.out.println(article);
    }
}