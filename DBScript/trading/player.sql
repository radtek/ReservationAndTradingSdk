CREATE DATABASE IF NOT EXISTS `trading`;
USE `trading`;
DROP TABLE IF EXISTS PLAYER;
create table PLAYER(
   PLAYER_ID BIGINT(15) unsigned NOT NULL COMMENT '用户编号',
   PLAYER_NAME VARCHAR(60) NOT NULL COMMENT '用户名称',
   PLAYER_ROLE_ID BIGINT(15) unsigned COMMENT '用户角色ID',
   primary Key(PLAYER_ID) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

   
