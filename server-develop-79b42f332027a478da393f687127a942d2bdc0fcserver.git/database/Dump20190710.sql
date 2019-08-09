-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: 47.244.194.202    Database: ted
-- ------------------------------------------------------
-- Server version	5.6.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '地址',
  `nickname` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '昵称',
  `head_picture` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '头像地址',
  `nation` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname` (`nickname`),
  UNIQUE KEY `address` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=100011 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `address_info`
--

DROP TABLE IF EXISTS `address_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ETC/ETH 地址',
  `phase` tinyint(4) DEFAULT NULL COMMENT '期，1为第一期，2为第二期',
  `eth_payment` decimal(30,18) DEFAULT NULL COMMENT '共振的eth',
  `etc_payment` decimal(30,18) DEFAULT NULL COMMENT '共振的etc',
  `inviter_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邀请人地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0待确定，1已有第一笔交易，2未有第一笔交易',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `team_id` int(11) DEFAULT NULL COMMENT '所在骑士团id',
  `etc_rewarded` tinyint(1) DEFAULT NULL COMMENT 'etc  0 还没领奖，1已领奖',
  `eth_rewarded` tinyint(1) DEFAULT NULL COMMENT ' eth 0 还没领奖，1已领奖',
  PRIMARY KEY (`id`),
  UNIQUE KEY `address` (`address`,`phase`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block_info`
--

DROP TABLE IF EXISTS `block_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `block_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hash` char(66) COLLATE utf8_bin NOT NULL COMMENT 'block hash',
  `type` char(3) COLLATE utf8_bin NOT NULL COMMENT 'etc/eth',
  `height` int(10) unsigned DEFAULT NULL COMMENT 'block高度',
  `previous` char(66) COLLATE utf8_bin NOT NULL COMMENT '前一个block hash',
  `size` int(10) unsigned NOT NULL COMMENT 'block体积',
  `timestamp` int(10) unsigned NOT NULL COMMENT '出块时间',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`),
  KEY `height` (`height`),
  KEY `idx_height` (`height`)
) ENGINE=InnoDB AUTO_INCREMENT=364 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='block记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'ETC/ETH地址',
  `type` varchar(4) CHARACTER SET utf8 COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '类型 ETC/ETH',
  `status` smallint(6) DEFAULT NULL COMMENT '状态 已使用3 已失效2 未生效1 有效0',
  `discount` float DEFAULT NULL COMMENT '折扣',
  `payment` decimal(30,18) DEFAULT NULL COMMENT '额度限制',
  `reward` decimal(30,0) DEFAULT NULL COMMENT '优惠券的TED回报',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `left_time` int(11) DEFAULT NULL COMMENT '优惠券剩余有效时间,单位s',
  `transaction_id` int(11) DEFAULT NULL COMMENT '生成该券的交易id',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更改时间',
  `begin_block` int(11) DEFAULT NULL COMMENT '生效区块',
  `end_block` int(11) DEFAULT NULL COMMENT '失效区块',
  `left_block` int(11) DEFAULT NULL COMMENT '剩余有效区块',
  `v` varchar(225) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `r` varchar(225) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `s` varchar(225) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `period`
--

DROP TABLE IF EXISTS `period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `period` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_change_time` datetime DEFAULT NULL COMMENT '第一期结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '骑士团id',
  `phase` tinyint(4) DEFAULT NULL COMMENT '阶段',
  `highest_rank` int(11) DEFAULT NULL COMMENT '历史最高排名',
  `leader_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '团长地址',
  `etc_fund` decimal(30,18) DEFAULT NULL COMMENT 'etc金额',
  `eth_fund` decimal(30,18) DEFAULT NULL COMMENT 'eth金额',
  `number` int(11) DEFAULT NULL COMMENT '人数',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'ETC/ETH地址',
  `type` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '类型 ETC/ETH',
  `payment` decimal(30,18) DEFAULT NULL COMMENT '付款',
  `reward` decimal(30,18) DEFAULT NULL COMMENT '预期奖励',
  `coupon_id` int(11) DEFAULT NULL COMMENT '优惠券id',
  `status` smallint(6) DEFAULT NULL COMMENT '状态 处理中0 成功1 失败2',
  `created_at` datetime DEFAULT NULL COMMENT '提交时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `transaction_hash` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '链上交易返回的hash',
  `phase` tinyint(3) DEFAULT NULL COMMENT '1 第一期的交易 2 第二期的交易',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-10 14:49:55
