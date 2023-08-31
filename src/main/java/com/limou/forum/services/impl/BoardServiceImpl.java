package com.limou.forum.services.impl;

import cn.hutool.core.util.ObjUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.dao.BoardMapper;
import com.limou.forum.exception.ApplicationException;
import com.limou.forum.model.Board;
import com.limou.forum.services.IBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Slf4j
@Service
public class BoardServiceImpl implements IBoardService {

    @Resource
    private BoardMapper boardMapper;

    @Override
    public List<Board> selectByNum(Integer num) {
        // 参数校验
        if (ObjUtil.isEmpty(num) || num <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 查询数据库并返回结果
        return boardMapper.selectByNum(num);
    }

    @Override
    public List<Board> selectAllBySort() {
        return boardMapper.selectAllBySort();
    }

    @Override
    public Board selectById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_PARAMS_INVALIDATE.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_INVALIDATE));
        }
        // 查询数据库并返回结果
        return boardMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOneArticleCountById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        // 查询对应的板块
        Board board = boardMapper.selectByPrimaryKey(id);
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.ERROR_IS_NULL.toString() + ", board id = {}", id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 更新板块的帖子数量，注意要创建一个新对象，设置关键的值即可，如果直接使用查出来的对象，那么将会修改所有值
        Board updateBoard = new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount() + 1);
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }

    @Override
    public void subOneArticleCountById(Long id) {
        // 参数校验
        if (ObjUtil.isEmpty(id) || id <= 0) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_ARTICLE_COUNT.toString());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_ARTICLE_COUNT));
        }
        // 查询对应的板块
        Board board = boardMapper.selectByPrimaryKey(id);
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString() + ", board id = {}", id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS));
        }
        // 更新板块的帖子数量，注意要创建一个新对象，设置关键的值即可，如果直接使用查出来的对象，那么将会修改所有值
        Board updateBoard = new Board();
        updateBoard.setId(board.getId());
        updateBoard.setArticleCount(board.getArticleCount() - 1);
        // 判断减1之后是否小于0
        if (updateBoard.getArticleCount() < 0) {
            // 小于0就设置为0
            updateBoard.setArticleCount(0);
        }
        // 调用DAO
        int row = boardMapper.updateByPrimaryKeySelective(updateBoard);
        // 结果校验
        if (row != 1) {
            // 打印日志
            log.warn(ResultCode.FAILED.toString() + ", 预期受影响行数为 1, 实际受影响行数为: {}", row);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
    }
}
