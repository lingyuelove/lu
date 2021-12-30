

CREATE TABLE `pro_check` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `fk_shp_shop_id` int NOT NULL COMMENT 'shp_shop店铺id',
  `check_name` varchar (255) NOT NULL COMMENT '盘点名称',
  `check_state` varchar (255) NULL DEFAULT '10' COMMENT '盘点状态 10:进行中 | 20:取消 | 30:完成',
  `sale_price_start` decimal (15, 0) NULL DEFAULT 0 COMMENT '盘点开始金额',
  `sale_price_end` decimal (15, 0) NULL DEFAULT 0 COMMENT '盘点结束金额',
  `fk_pro_attribute_codes` varchar (20) NOT NULL COMMENT '产品属性表的code集合',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '盘点时间',
  `insert_admin` int NOT NULL DEFAULT 0 COMMENT '创建盘点的用户',
  `update_admin` int NULL DEFAULT 0 COMMENT '更新用户id',
  `remark` varchar (255) NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) COMMENT = '盘点表' AUTO_INCREMENT = 10000 ;


CREATE TABLE `pro_check_product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `fk_pro_product_id` int NOT NULL COMMENT 'pro_product商品id',
  `fk_pro_check_id` int NOT NULL COMMENT 'pro_check的主键Id',
  `check_state` varchar (8) NOT NULL DEFAULT 'no' COMMENT '状态设置 已盘点yes 未盘点no',
  `check_type` varchar (8) NULL DEFAULT '' COMMENT '盘点类型 缺失:0  存在:1',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '盘点时间',
  `insert_admin` int NOT NULL DEFAULT 0 COMMENT '盘点人',
  `update_admin` int NULL DEFAULT NULL COMMENT '更新用户id',
  `remark` varchar (255) NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_fk_pro_product_id` (`fk_pro_product_id`) USING BTREE,
  INDEX `idx_fk_pro_check_id` (`fk_pro_check_id`) USING BTREE
) COMMENT = '盘点商品表' AUTO_INCREMENT = 10000 ;


#在订单表新增结款字段
ALTER TABLE `ord_order`
    ADD COLUMN `entrust_state` varchar(20) NULL COMMENT '结款状态 -1:未填写| 0:未结款 | 1:已结款' AFTER `open_type`;

#在订单表新增结款字段之后给之前的订单设置借款状态的默认值
update ord_order a left join pro_product b on a.fk_pro_product_id = b.id set a.entrust_state = "-1"  where b.fk_pro_attribute_code = "20";

