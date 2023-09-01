package com.limou.forum.services.impl;

import com.limou.forum.model.ArticleReply;
import com.limou.forum.services.IArticleReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 小李哞哞
 * @date 2023/8/31
 */
@SpringBootTest
class ArticleReplyServiceImplTest {

    @Resource
    private IArticleReplyService articleReplyService;

    @Transactional
    @Test
    void create() {
        // 构造一条帖子评论
        ArticleReply articleReply = new ArticleReply();
        articleReply.setArticleId(19L);
        articleReply.setPostUserId(2L);
        articleReply.setContent("单元测试时间");
        articleReplyService.create(articleReply);
    }

    @Test
    void selectByArticleId() {
        List<ArticleReply> articleReplies = articleReplyService.selectByArticleId(4L);
        articleReplies.forEach(System.out::println);
    }
}