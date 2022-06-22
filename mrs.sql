/*
 Navicat Premium Data Transfer

 Source Server         : zjw
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Schema         : mrs

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 22/06/2022 10:58:20
*/

CREATE DATABASE mrs;
USE mrs;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `uid` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `mid` bigint(0) NULL DEFAULT NULL COMMENT '电影ID',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '短评',
  `score` int(0) NULL DEFAULT NULL COMMENT '评分',
  `time` timestamp(0) NULL DEFAULT NULL COMMENT '评价时间',
  `agree` int(0) NULL DEFAULT NULL COMMENT '赞同数',
  `type` tinyint(3) UNSIGNED ZEROFILL NULL DEFAULT 000 COMMENT '0表示系统 1表示豆瓣',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65239 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '电影ID',
  `did` int(0) NULL DEFAULT NULL COMMENT '豆瓣ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电影名',
  `year` smallint(0) NULL DEFAULT NULL COMMENT '上映年份',
  `directors` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导演',
  `writers` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '编剧',
  `actors` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '主演',
  `types` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `regions` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区',
  `languages` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语言',
  `release_date` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上映时间',
  `runtime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时长',
  `alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '别名',
  `imdb` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IMDb号',
  `score` double NULL DEFAULT NULL COMMENT '评分',
  `num` bigint(0) NULL DEFAULT NULL COMMENT '评价人数',
  `five` double NULL DEFAULT NULL COMMENT '5星百分比',
  `four` double NULL DEFAULT NULL COMMENT '4星百分比',
  `three` double NULL DEFAULT NULL COMMENT '3星百分比',
  `two` double NULL DEFAULT NULL COMMENT '2星百分比',
  `one` double NULL DEFAULT NULL COMMENT '1星百分比',
  `introduction` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '简介',
  `pic` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电影海报',
  `crawl_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '爬取时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9016 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for movie_feature
-- ----------------------------
DROP TABLE IF EXISTS `movie_feature`;
CREATE TABLE `movie_feature`  (
  `mid` bigint(0) NOT NULL COMMENT '电影id',
  `matrix` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电影特征矩阵',
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for movie_region
-- ----------------------------
DROP TABLE IF EXISTS `movie_region`;
CREATE TABLE `movie_region`  (
  `mid` bigint(0) NOT NULL COMMENT '电影ID',
  `rid` int(0) NOT NULL COMMENT '地区ID',
  `degree` int(0) NULL DEFAULT NULL COMMENT '程度'
) ENGINE = InnoDB AUTO_INCREMENT = 11277 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for movie_type
-- ----------------------------
DROP TABLE IF EXISTS `movie_type`;
CREATE TABLE `movie_type`  (
  `mid` bigint(0) NOT NULL COMMENT '电影ID',
  `tid` int(0) NOT NULL COMMENT '类型ID',
  `degree` int(0) NULL DEFAULT NULL COMMENT '程度'
) ENGINE = InnoDB AUTO_INCREMENT = 19755 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recommendation
-- ----------------------------
DROP TABLE IF EXISTS `recommendation`;
CREATE TABLE `recommendation`  (
  `uid` bigint(0) NOT NULL COMMENT '用户id',
  `mid` bigint(0) NOT NULL COMMENT '电影id',
  `idx` double NULL DEFAULT NULL COMMENT '推荐指数',
  `type` tinyint(3) UNSIGNED ZEROFILL NULL DEFAULT 000 COMMENT '推荐类型（1表示基于内容，2表示协同过滤，0表示随机推荐）',
  PRIMARY KEY (`uid`, `mid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for region
-- ----------------------------
DROP TABLE IF EXISTS `region`;
CREATE TABLE `region`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '地区ID',
  `region` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for region_like
-- ----------------------------
DROP TABLE IF EXISTS `region_like`;
CREATE TABLE `region_like`  (
  `uid` bigint(0) NOT NULL COMMENT '用户id',
  `rid` int(0) NOT NULL COMMENT '电影地区id',
  `degree` int(0) NULL DEFAULT NULL COMMENT '喜爱程度',
  PRIMARY KEY (`uid`, `rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for same_likes
-- ----------------------------
DROP TABLE IF EXISTS `same_likes`;
CREATE TABLE `same_likes`  (
  `did` int(0) NOT NULL COMMENT '电影ID',
  `sid` int(0) NOT NULL COMMENT '喜欢这部电影的人也喜欢的电影ID'
) ENGINE = InnoDB AUTO_INCREMENT = 88451 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `did` int(0) NULL DEFAULT NULL COMMENT '豆瓣ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2286 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for type_like
-- ----------------------------
DROP TABLE IF EXISTS `type_like`;
CREATE TABLE `type_like`  (
  `uid` bigint(0) NOT NULL COMMENT '用户id',
  `tid` int(0) NOT NULL COMMENT '电影类型id',
  `degree` int(0) NULL DEFAULT NULL COMMENT '喜爱程度',
  PRIMARY KEY (`uid`, `tid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` tinyint(0) NULL DEFAULT NULL COMMENT '性别（0表示女，1表示男，2表示保密）',
  `state` int(0) NULL DEFAULT NULL COMMENT '状态',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `mail` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18957 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_like
-- ----------------------------
DROP TABLE IF EXISTS `user_like`;
CREATE TABLE `user_like`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID号',
  `cid` bigint(0) NULL DEFAULT NULL COMMENT '被点赞的评论id',
  `uid` bigint(0) NULL DEFAULT NULL COMMENT '点赞的用户id',
  `status` tinyint(3) UNSIGNED ZEROFILL NULL DEFAULT 000 COMMENT '点赞状态（0表示未点赞，1表示已点赞）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
