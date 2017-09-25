CREATE DATABASE  IF NOT EXISTS `trading`;
USE `trading`;
DROP TABLE IF EXISTS PLAYER_SHOPPING_CART_SKU;
create table PLAYER_SHOPPING_CART_SKU( 
   PLAYER_ID BIGINT(15) unsigned NOT NULL COMMENT '用户编号',
   SHOP_ID BIGINT(15)unsigned NOT NULL COMMENT  '商店编号',
   SKU_ID BIGINT(15) unsigned NOT NULL COMMENT '货物编号',
   SHOP_SKU_AMOUNT BIGINT(15) COMMENT '货物数量',
   primary Key(PLAYER_ID,SHOP_ID,SKU_ID) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户购物车信息表';

   
