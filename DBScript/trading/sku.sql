CREATE DATABASE  IF NOT EXISTS `trading`;
USE `trading`;
DROP TABLE IF EXISTS SKU;
create table SKU( 
   SKU_ID BIGINT(15) unsigned NOT NULL COMMENT '������',
   SKU_NAME VARCHAR(60) NOT NULL COMMENT '��������',
   primary Key (SKU_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='������Ϣ��';

   