package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
import com.limou.forum.model.Article;
import com.limou.forum.model.ArticleReply;
import com.limou.forum.model.User;
import com.limou.forum.services.IArticleReplyService;
import com.limou.forum.services.IArticleService;
import com.limou.forum.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 小李哞哞
 * @date 2023/8/31
 */
@Tag(name = "评论模块")
@Slf4j
@RestController
@RequestMapping("/reply")
public class ArticleReplyController {

    @Resource
    private IArticleReplyService articleReplyService;

    @Resource
    private IUserService userService;

    @Resource
    private IArticleService articleService;

    /**
     * 新增评论
     *
     * @param articleId 帖子id
     * @param content   评论内容
     * @return AppResult
     */
    @Operation(summary = "新增评论")
    @Parameters({
            @Parameter(name = "articleId", description = "帖子id", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "content", description = "评论内容", required = true, in = ParameterIn.DEFAULT)
    })
    @PostMapping("/create")
    public AppResult create(HttpServletRequest request,
                            @RequestParam("articleId") @NonNull Long articleId,
                            @RequestParam("content") @NonNull String content) {

        // 获取当前操作的用户
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 校验用户是否被禁言
        userService.checkState(request);
        // 获取帖子信息
        Article article = articleService.selectById(articleId);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 校验帖子状态 - 已归档
        if (article.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }
        // 构造评论对象
        ArticleReply articleReply = new ArticleReply();
        articleReply.setPostUserId(user.getId());
        articleReply.setArticleId(articleId);
        articleReply.setContent(content);
        // 调用Service新增评论
        articleReplyService.create(articleReply);
        // 返回成功结果
        return AppResult.success();
    }
}
