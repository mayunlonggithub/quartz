/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50646
Source Host           : localhost:3306
Source Database       : quartz

Target Server Type    : MYSQL
Target Server Version : 50646
File Encoding         : 65001

Date: 2019-12-02 13:31:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for job_entity
-- ----------------------------
DROP TABLE IF EXISTS `job_entity`;
CREATE TABLE `job_entity` (
  `id` int(11) NOT NULL COMMENT 'id',
  `name` varchar(255) DEFAULT '' COMMENT 'job名称',
  `job_group` varchar(255) DEFAULT NULL COMMENT 'job组名',
  `cron` varchar(255) DEFAULT NULL COMMENT '执行的cron表达式',
  `parameter` varchar(255) DEFAULT NULL COMMENT 'job的参数',
  `description` varchar(255) DEFAULT NULL COMMENT 'job描述信息',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '截止时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of job_entity
-- ----------------------------
INSERT INTO `job_entity` VALUES ('1', 'first', 'helloworld', '0/3 * * * * ?', '1', '第一个', '2019-11-05 22:05:47', '2019-12-12 22:05:49', '2');
INSERT INTO `job_entity` VALUES ('2', 'second', 'helloworld', '0/5 * * * * ? ', '2', '第二个', '2019-11-13 22:05:53', '2019-12-19 22:05:55', '2');
INSERT INTO `job_entity` VALUES ('4', 'third', 'helloworld', '0/15 * * * * ? ', '3', '第三个', '2019-12-12 22:05:59', '2019-11-29 22:06:02', '3');
INSERT INTO `job_entity` VALUES ('5', 'four', 'helloworld', '0 0/1 * * * ? *', '4', '第四个', '2019-12-11 22:06:05', '2019-12-26 22:06:07', '3');
