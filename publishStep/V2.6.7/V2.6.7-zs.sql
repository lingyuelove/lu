DROP TABLE
IF EXISTS `pro_convey`;
CREATE TABLE `pro_convey` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_send_shop_id` int NOT NULL COMMENT '发送方店铺id字段,主键id',
  `fk_receive_shop_id` int DEFAULT NULL COMMENT '接收方店铺id',
  `number` varchar(20)  DEFAULT NULL COMMENT '编码',
  `name` varchar(50)  NOT NULL COMMENT '名称',
  `send_state` varchar(255)  NOT NULL COMMENT '发送状态 0待提取 1已提取 2已确认',
  `receive_state` varchar(255)  DEFAULT NULL COMMENT '接收状态',
  `send_del` varchar(1)  NOT NULL DEFAULT '0' COMMENT '发送方删除',
  `receive_del` varchar(1)  NOT NULL DEFAULT '0' COMMENT '接收方删除',
  `receive_user_id` int DEFAULT NULL COMMENT '提取人',
  `insert_admin` int NOT NULL COMMENT '创建人',
  `update_admin` int DEFAULT NULL COMMENT '更新人',
  `receive_time` timestamp NULL DEFAULT NULL COMMENT '提取时间',
  `insert_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=10000 COMMENT='商品传送表';
DROP TABLE
IF EXISTS `pro_convey_product`;
CREATE TABLE `pro_convey_product` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
	`fk_pro_product_id` INT NOT NULL COMMENT '商品id',
	`fk_pro_convey_id` INT NOT NULL COMMENT '商品传送表的id字段,主键id',
	`receive_product_id` INT COMMENT '接收商品id',
	`name` VARCHAR ( 255 ) DEFAULT NULL COMMENT '商品名称',
	`description` VARCHAR ( 255 ) DEFAULT NULL COMMENT '商品描述',
	`num` INT DEFAULT NULL COMMENT '商品数量',
	`old_num` int DEFAULT NULL COMMENT '商品数量 确定转移时进行保存',
	`finish_price` DECIMAL ( 15, 2 ) DEFAULT NULL COMMENT '结算价',
	`insert_time` TIMESTAMP NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT DEFAULT NULL COMMENT '更新人',
	`remark` VARCHAR ( 255 ) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY ( `id` ),
	UNIQUE KEY `uk_pro_convey_id_pro_product_id` ( `fk_pro_product_id`, `fk_pro_convey_id` ) USING BTREE COMMENT '一个寄卖转移不能有相同的两件商品',
    UNIQUE KEY `uk_product_id` ( `fk_pro_product_id` ) USING BTREE COMMENT '一个商品只能在一个寄卖转移'
) AUTO_INCREMENT = 10000 COMMENT='寄卖转移商品';
#商品添加是否为寄卖传送商品
ALTER TABLE `pro_product`
    ADD COLUMN `convey_state` varchar(10) DEFAULT 'warehouse' COMMENT '寄卖传送类型 convey寄卖传送 warehouse仓库商品' AFTER `retrieve_time`;

#寄卖传送接收的商品添加寄卖传送id
ALTER TABLE `pro_product`
    ADD COLUMN `fk_pro_convey_id` int  COMMENT '寄卖传送id' AFTER `convey_state`;

#锁单添加关联锁单id p判断这个锁单是自己新增还是关联新增
ALTER TABLE `pro_lock_record`
    ADD COLUMN `son_record_id` int COMMENT '锁单内部关联id 判断是否为寄卖传送关联锁单' AFTER `remark`;
#锁单添加关联锁单id 判断这个锁单是自己新增还是关联新增
ALTER TABLE `pro_lock_record`
    ADD COLUMN `convey_lock_type` varchar(10)  COMMENT '寄卖锁单关联类型 lock锁单关联 order：订单关联 ' AFTER `son_record_id`;
#商品添加是否为寄卖传送商品
ALTER TABLE `ord_order`
    ADD COLUMN `fk_pro_lock_record_id` int COMMENT '锁单id' AFTER `year_rate`;


