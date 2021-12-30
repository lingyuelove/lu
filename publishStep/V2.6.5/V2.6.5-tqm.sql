	ALTER TABLE `pro_deliver`
ADD COLUMN `fk_pro_product_id` int(0) NULL COMMENT '商品id' ;

	ALTER TABLE `pro_deliver`
ADD COLUMN `num` int(0) NULL COMMENT '数量' ;