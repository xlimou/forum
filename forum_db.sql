/*
 Navicat MySQL Data Transfer

 Source Server         : local_mysql_8.0
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : forum_db

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 28/08/2023 18:59:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键自增',
  `boardId` bigint(0) NOT NULL COMMENT '关联板块编号',
  `userId` bigint(0) NOT NULL COMMENT '发帖人，关联用户编号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子正文',
  `visitCount` int(0) NOT NULL DEFAULT 0 COMMENT '访问量',
  `replyCount` int(0) NOT NULL DEFAULT 0 COMMENT '回复数',
  `likeCount` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除，0否，1是',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，精确到秒',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_reply
-- ----------------------------
DROP TABLE IF EXISTS `t_article_reply`;
CREATE TABLE `t_article_reply`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键自增',
  `articleId` bigint(0) NOT NULL COMMENT '关联帖子编号',
  `postUserId` bigint(0) NOT NULL COMMENT '楼主用户，关联用户编号',
  `replyId` bigint(0) NULL DEFAULT NULL COMMENT '关联回复编号，支持楼中楼',
  `replyUserId` bigint(0) NULL DEFAULT NULL COMMENT '楼主下的回复用户编号，支持楼中楼',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回帖内容',
  `likeCount` int(0) NOT NULL DEFAULT 0 COMMENT '点赞数',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除，0否，1是',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，精确到秒',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_board
-- ----------------------------
DROP TABLE IF EXISTS `t_board`;
CREATE TABLE `t_board`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键自增',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '板块名',
  `articleCount` int(0) NOT NULL DEFAULT 0 COMMENT '帖子数量',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序优先级，升序',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除，0否，1是',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，精确到秒',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键自增',
  `postUserId` bigint(0) NOT NULL COMMENT '发送者，关联用户编号',
  `receiveUserId` bigint(0) NOT NULL COMMENT '接收者，关联用户编号',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0正常，1禁用',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除，0否，1是',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，精确到秒',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号，主键自增',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，唯一',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密后的密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `phoneNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `gender` tinyint(0) NOT NULL DEFAULT 2 COMMENT '性别，0女，1男，2保密',
  `salt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '为密码加盐',
  `avatarUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `articleCount` int(0) NOT NULL DEFAULT 0 COMMENT '发帖数量',
  `isAdmin` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否管理员，0否，1是',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注，自我介绍',
  `state` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态，0正常，1禁言',
  `deleteState` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否删除，0否，1是',
  `createTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间，精确到秒',
  `updateTime` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间，经确到秒',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
