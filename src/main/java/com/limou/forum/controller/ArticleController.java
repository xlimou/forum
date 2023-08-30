package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
import com.limou.forum.model.Article;
import com.limou.forum.model.Board;
import com.limou.forum.model.User;
import com.limou.forum.services.IArticleService;
import com.limou.forum.services.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/30
 */
@Tag(name = "帖子模块")
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private IArticleService articleService;

    @Resource
    private IBoardService boardService;

    @Operation(summary = "发布帖子")
    @Parameters({
            @Parameter(name = "boardId", description = "板块id", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "title", description = "帖子标题", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "content", description = "帖子内容", required = true, in = ParameterIn.DEFAULT),
    })
    @PostMapping("/create")
    public AppResult create(HttpServletRequest request,
                            @RequestParam("boardId") @NonNull Long boardId,
                            @RequestParam("title") @NonNull String title,
                            @RequestParam("content") @NonNull String content) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 校验发贴用户是否被禁言
        if (user.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_BANNED.toString());
            // 返回结果
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }

        // 校验板块是否可用
        Board board = boardService.selectById(boardId);
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", 发帖失败, 板块不存在, board id = {}", boardId);
            // 返回结果
            return AppResult.failed(ResultCode.ERROR_IS_NULL);
        }
        if (board.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_BANNED.toString());
            // 返回结果
            return AppResult.failed(ResultCode.FAILED_BOARD_BANNED);
        }

        // 封装帖子对象
        Article article = new Article();
        article.setUserId(user.getId());
        article.setBoardId(boardId);
        article.setTitle(title);
        article.setContent(content);

        // 调用Service发布帖子
        articleService.create(article);

        // 返回结果
        return AppResult.success();
    }

    /**
     * 获取帖子列表
     *
     * @param boardId 板块id
     * @return AppResult
     */
    @Operation(summary = "获取帖子列表", description = "若参数boardId为空，则查询所有帖子；否则查询指定板块的帖子")
    @Parameter(name = "boardId", description = "板块id", in = ParameterIn.PATH)
    @GetMapping({"/getArticleList", "/getArticleList/{boardId}"}) // boardId可为空
    public AppResult getArticleList(@PathVariable(value = "boardId", required = false) Long boardId) {
        // 调用Service查询帖子列表
        List<Article> articles = articleService.selectAll();
        // 非空校验
        if (ObjUtil.isEmpty(articles)) {
            // 如果不赋值一个空对象，那么将来JSON字符串里的data就是"null"这个字符串，如果赋值空对象了就是一个空数组[]
            articles = new ArrayList<>();
        }
        // 返回结果
        return AppResult.success(articles);
    }
}