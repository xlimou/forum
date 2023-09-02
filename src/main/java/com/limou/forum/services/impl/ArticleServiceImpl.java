package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.dao.ArticleMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.Article;
import com.limou.forum.model.Board;
import com.limou.forum.model.User;
import com.limou.forum.services.IArticleService;
import com.limou.forum.services.IBoardService;
import com.limou.forum.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private IUserService userService;
    @Resource
    private IBoardService boardService;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void create(Article article) {
        // 参数校验
        if (ObjUtil.isEmpty(article)
                || ObjUtil.isEmpty(article.getBoardId())
                || ObjUtil.isEmpty(article.getUserId())
                || StrUtil.isBlank(article.getTitle())
                || StrUtil.isBlank(article.getContent())) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }

        // 设置默认值(与数据库默认值构成双保险机制，使程序更健壮)
        article.setLikeCount(0);
        article.setDeleteState((byte) 0);
        article.setState((byte) 0);
        article.setReplyCount(0);
        article.setVisitCount(0);
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);

        // 写入数据库
        int row = articleMapper.insertSelective(article);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

        // 查询对应用户信息
        User user = userService.selectById(article.getUserId());
        // 非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", 发帖失败, user id = {}", article.getUserId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新用户发帖数
        userService.addOneArticleCountById(article.getUserId());

        // 查询对应板块信息
        Board board = boardService.selectById(article.getBoardId());
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", 发帖失败, 板块不存在, board id = {}", article.getBoardId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新板块帖子数
        boardService.addOneArticleCountById(article.getBoardId());

        // 打印日志
        log.info(ResultCode.SUCCESS.toString() + ", article id = {}, user id = {}, board id = {}, 发帖成功！", article.getId(), article.getUserId(), article.getBoardId());

        // 测试事务是否正常生效
        // throw new ApplicationException("测试事务");
    }

    @Override
    public List<Article> selectAll() {
        return articleMapper.selectAll();
    }

    @Override
    public List<Article> selectByBoardId(Long boardId) {
        // 参数校验
        if (ObjUtil.isEmpty(boardId) || boardId <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 查询板块信息
        Board board = boardService.selectById(boardId);
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString() + ", board id = {}", boardId);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        // 查询数据库并返回结果
        return articleMapper.selectByBoardId(boardId);
    }

    @Override
    public Article selectDetailById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 调用DAO查询帖子信息
        Article article = articleMapper.selectDetailById(id);
        // 因为要更新访问量，所以必须进行非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString() + ", article id = {}", id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 访问量 + 1
        article.setVisitCount(article.getVisitCount() + 1);
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setVisitCount(article.getVisitCount());
        // 调用DAO，更新帖子访问量
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
        // 打印日志
        log.info("article id = {} 的帖子访问量 + 1 .", id);
        // 返回数据
        return article;
    }

    @Override
    public void modify(Long id, String title, String content) {
        // 参数校验
        if (ObjUtil.isEmpty(id)
                || id <= 0
                || StrUtil.isBlank(title)
                || StrUtil.isBlank(content)) {

            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 构造article对象
        Article updateArticle = new Article();
        updateArticle.setId(id);
        updateArticle.setTitle(title);
        updateArticle.setContent(content);
        updateArticle.setUpdateTime(new Date());
        // 调用DAO修改帖子
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public Article selectById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 查询数据库并返回结果
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void thumbsById(Long id) {
        // 直接调用定义好的Service方法，并且该方法里已经进行了参数校验
        Article article = selectById(id);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 状态校验 - 已归档
        if (article.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED));
        }
        // 构造要更新的对象
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setLikeCount(article.getLikeCount() + 1);
        updateArticle.setUpdateTime(new Date());
        // 调用DAO执行更新操作
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void deleteById(Long id) {
        // 直接调用定义好的Service方法，并且该方法里已经进行了参数校验
        Article article = selectById(id);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 构造要更新的对象
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setDeleteState((byte) 1);
        updateArticle.setUpdateTime(new Date());
        // 调用DAO修改帖子deleteState字段
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }

        // 帖子的作者发帖数 - 1
        userService.subOneArticleCountById(article.getUserId());
        // 帖子所在板块的帖子数量 - 1
        boardService.subOneArticleCountById(article.getBoardId());

        // 测试事务是否正常生效
        // throw new ApplicationException("测试事务");
    }

    @Override
    public void addOneReplyCountById(Long id) {
        // 直接调用定义好的Service方法，并且该方法里已经进行了参数校验
        Article article = selectById(id);
        // 非空校验
        if (ObjUtil.isEmpty(article)) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_NOT_EXISTS));
        }
        // 状态校验 - 已归档
        if (article.getState() == 1) {
            // 打印日志
            log.warn(ResultCode.FAILED_ARTICLE_BANNED.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_ARTICLE_BANNED));
        }
        // 构造要更新的对象
        Article updateArticle = new Article();
        updateArticle.setId(article.getId());
        updateArticle.setReplyCount(article.getReplyCount() + 1);
        updateArticle.setUpdateTime(new Date());
        // 调用DAO执行更新操作
        int row = articleMapper.updateByPrimaryKeySelective(updateArticle);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public List<Article> selectByUserId(Long userId) {
        // 调用Service查询用户信息，里面已经进行数据校验了
        User user = userService.selectById(userId);
        // 非空校验
        if (ObjUtil.isEmpty(user)) {
            // 打印日志
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }
        // 调用DAO根据userId查询帖子列表
        return articleMapper.selectByUserId(userId);
    }
}
