package com.limou.forum.controller;

import cn.hutool.core.util.ObjUtil;
import com.limou.forum.common.AppResult;
import com.limou.forum.common.ResultCode;
import com.limou.forum.model.Board;
import com.limou.forum.services.IBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小李哞哞
 * @date 2023/8/29
 */
@Tag(name = "板块模块")
@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

    @Resource
    private IBoardService boardService;

    /**
     * 从配置文件中读取值，如果没有配置，默认值是9
     */
    @Value("${forum.index.board-num:9}")
    private Integer indexBoardNum;

    /**
     * 获取首页板块列表
     *
     * @return AppResult
     */
    @Operation(summary = "获取首页板块列表")
    @GetMapping("/topList")
    public AppResult topList() {
        log.info("首页板块个数为: {}", indexBoardNum);
        // 调用Service查询结果
        List<Board> boards = boardService.selectByNum(indexBoardNum);
        // 非空校验
        if (ObjUtil.isEmpty(boards)) {
            boards = new ArrayList<>();
        }
        // 返回结果
        return AppResult.success(boards);
    }

    /**
     * 获取所有板块记录，根据sort升序排列
     *
     * @return AppResult
     */
    @Operation(summary = "获取所有板块记录")
    @GetMapping("/allList")
    public AppResult allList() {
        // 调用Service查询结果
        List<Board> boards = boardService.selectAllBySort();
        // 非空校验
        if (ObjUtil.isEmpty(boards)) {
            boards = new ArrayList<>();
        }
        // 返回结果
        return AppResult.success(boards);
    }

    @Operation(summary = "根据id获取板块信息")
    @Parameter(name = "id", description = "板块id", required = true, in = ParameterIn.PATH)
    @GetMapping("/getById/{id}")
    public AppResult getById(@PathVariable("id") @NonNull Long id) {
        Board board = boardService.selectById(id);
        // 非空校验
        if (ObjUtil.isEmpty(board)) {
            // 打印日志
            log.warn(ResultCode.FAILED_BOARD_NOT_EXISTS.toString() + ", board id = {}", id);
            // 返回结果
            return AppResult.failed(ResultCode.FAILED_BOARD_NOT_EXISTS);
        }
        // 返回结果
        return AppResult.success(board);
    }
}
