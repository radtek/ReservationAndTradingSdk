CREATE DATABASE  IF NOT EXISTS `trading`;
USE `trading`;
DROP TABLE IF EXISTS SKU;
create table SKU( 
   SKU_ID BIGINT(15) unsigned NOT NULL COMMENT '货物编号',
   SKU_NAME VARCHAR(60) NOT NULL COMMENT '货物名称',
   primary Key (SKU_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='货物信息表';

   
