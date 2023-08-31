package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.config.AppConfig;
import com.limou.forum.model.Article;
import com.limou.forum.model.Board;
import com.limou.forum.model.User;
import com.limou.forum.services.IArticleService;
import com.limou.forum.services.IBoardService;
import com.limou.forum.services.IUserService;
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

    @Resource
    private IUserService userService;

    /**
     * 发布帖子
     *
     * @param boardId 板块id
     * @param title   帖子标题
     * @param content 帖子内容
     * @return AppResult
     */
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
        userService.checkState(request);

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
        List<Article> articles = null;
        // 参数校验
        if (ObjUtil.isEmpty(boardId)) {
            // 查询所有帖子
            articles = articleService.selectAll();
        } else {
            // 查询指定板块下的帖子
            articles = articleService.selectByBoardId(boardId);
        }
        // 非空校验
        if (ObjUtil.isEmpty(articles)) {
            // 如果不赋值一个空对象，那么将来JSON字符串里的data就是"null"这个字符串，如果赋值空对象了就是一个空数组[]
            articles = new ArrayList<>();
        }
        // 返回结果
        return AppResult.success(articles);
    }

    /**
     * 根据帖子id获取帖子详细信息(包含帖子信息、作者信息以及板块信息)
     *
     * @param id 帖子id
     * @return 帖子详细信息
     */
    @Operation(summary = "获取帖子详细信息", description = "包含帖子信息、作者信息以及板块信息")
    @Parameter(name = "id", description = "帖子id", required = true, in = ParameterIn.PATH)
    @GetMapping("/getDetails/{id}")
    public AppResult getDetails(HttpServletRequest request,
                                @PathVariable("id") @NonNull Long id) {

        // 从Session当中获取用户信息
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);

        // 调用Service查询帖子详细信息
        Article article = articleService.selectDetailById(id);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 返回结果
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }

        // 设置是否为作者访问帖子详情
        article.setOwn(ObjUtil.equals(user.getId(), article.getUserId()));
        // 返回结果
        return AppResult.success(article);
    }

    /**
     * 编辑帖子
     *
     * @param id      帖子id
     * @param title   帖子标题
     * @param content 帖子内容
     * @return AppResult
     */
    @Operation(summary = "编辑帖子")
    @Parameters({
            @Parameter(name = "id", description = "帖子id", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "title", description = "帖子标题", required = true, in = ParameterIn.DEFAULT),
            @Parameter(name = "content", description = "帖子内容", required = true, in = ParameterIn.DEFAULT)
    })
    @PostMapping("/modify")
    public AppResult modify(HttpServletRequest request,
                            @RequestParam("id") @NonNull Long id,
                            @RequestParam("title") @NonNull String title,
                            @RequestParam("content") @NonNull String content) {

        HttpSession session = request.getSession(false);
        // 获取当前操作的用户
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 判断用户是否被禁言
        userService.checkState(request);
        // 获取帖子信息
        Article article = articleService.selectById(id);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS);
        }
        // 判断当前编辑用户是否是作者
        if (ObjUtil.notEqual(user.getId(), article.getUserId())) {
            // 打印日志
            log.warn(ResultCode.FAILED_FORBIDDEN.toString() + ", 并非本帖子的作者进行编辑操作, 操作失败");
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_FORBIDDEN);
        }
        // 判断帖子的状态是否正常 - 已归档
        if (article.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            // 返回失败结果
            return AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED);
        }

        // 调用Service进行修改
        articleService.modify(id, title, content);
        // 打印日志
        log.info("编辑成功, user id = {}, article id = {}", user.getId(), article.getId());
        // 返回成功结果
        return AppResult.success();
    }

    /**
     * 点赞帖子
     *
     * @param id 帖子id
     * @return AppResult
     */
    @Operation(summary = "点赞帖子")
    @Parameter(name = "id", description = "帖子id", required = true, in = ParameterIn.PATH)
    @GetMapping("/thumbsUp/{id}")
    public AppResult thumbsUp(HttpServletRequest request,
                              @PathVariable("id") @NonNull Long id) {

        // 判断用户是否被禁言
        userService.checkState(request);
        // 调用Service层执行业务
        articleService.thumbsById(id);
        // 打印日志
        log.info("点赞成功，article id = {}", id);
        return AppResult.success();
    }
}
