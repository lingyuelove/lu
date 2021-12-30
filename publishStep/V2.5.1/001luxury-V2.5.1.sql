
#记账单
DROP TABLE
IF EXISTS fin_bill;

CREATE TABLE `fin_bill` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shopId',
	`name` VARCHAR (100) NOT NULL COMMENT '账单名称',
	`money` DECIMAL NOT NULL COMMENT '账单金额',
	`total_money` DECIMAL (10, 0) NULL DEFAULT NULL COMMENT '总金额',
	`types` VARCHAR (255) NOT NULL COMMENT '对账类型逗号分隔',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT NULL COMMENT '更新人',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	`state` VARCHAR (4) NOT NULL DEFAULT '10' COMMENT '对账状态 10：进行中 -99 已删除',
	PRIMARY KEY (`id`)
) COMMENT = '帐单表' AUTO_INCREMENT = 10000;

#记账单新增表
DROP TABLE
IF EXISTS fin_bill_day;

CREATE TABLE `fin_bill_day` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shopId',
	`fk_fin_bill_id` INT NOT NULL COMMENT '账单id',
	`total_money` DECIMAL (12, 0) NOT NULL COMMENT '总成本',
	`cash_money` DECIMAL (12, 0) NOT NULL COMMENT '现金',
	`product_money` DECIMAL (12, 0) NOT NULL COMMENT '商品成本',
	`profit_money` DECIMAL (12, 0) NOT NULL COMMENT '销售利润',
	`salary_money` DECIMAL (12, 0) NOT NULL COMMENT '薪资支出',
	`other_money` DECIMAL (12, 0) NOT NULL COMMENT '其他收支',
	`service_money` DECIMAL (12, 0) NOT NULL COMMENT '维修收支',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`biz_time` TIMESTAMP NOT NULL COMMENT '统计时间(业务,统计前一天)',
	`remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
	PRIMARY KEY (`id`)
) COMMENT = '账单每日统计' AUTO_INCREMENT = 10000;

#商品过期表的添加
DROP TABLE
IF EXISTS pro_expired_notice;

CREATE TABLE `pro_expired_notice` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop店铺id',
	`attribute_name` VARCHAR (50) NULL COMMENT '过期提醒名称',
	`classify_name` VARCHAR (50) NULL COMMENT '过期提醒类型名称',
	`fk_pro_attribute_codes` VARCHAR (50) NOT NULL COMMENT '产品属性表的code集合',
	`fk_pro_classify_codes` VARCHAR (50) NULL COMMENT '商品分类名称; 多个逗号分隔',
	`expired_day` INT NOT NULL COMMENT '设置过期天数',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT NULL COMMENT '更新人',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	PRIMARY KEY (`id`)
) COMMENT = '商品过期提醒表' AUTO_INCREMENT = 10000;

#在订单表新增结款字段
ALTER TABLE `ord_order`
    ADD COLUMN `entrust_img` varchar(1000) NULL COMMENT '结款凭证 3张图' AFTER `deduct_voucher_img_url`;

ALTER TABLE `ord_order`
    ADD COLUMN `entrust_remark` varchar(255) NULL COMMENT '结款备注' AFTER `entrust_img`;

#在盘点表新增商品分类名称字段
ALTER TABLE `pro_check`
    ADD COLUMN `fk_pro_classify_codes` varchar(60) NULL COMMENT '商品分类名称; 多个逗号分隔' AFTER `fk_pro_attribute_codes`;

ALTER TABLE `pro_check`
    ADD COLUMN `start_time` datetime NULL COMMENT '开始时间' AFTER `update_time`;

ALTER TABLE `pro_check`
    ADD COLUMN `end_time` datetime NULL COMMENT '结束时间' AFTER `start_time`;

ALTER TABLE `fin_fund_record`
    ADD COLUMN `init_price` decimal(15,2) NULL COMMENT '卖出价格' AFTER `money`;

ALTER TABLE `fin_fund_record`
    ADD COLUMN `fk_fin_bill_id` int NOT NULL COMMENT '账单id' AFTER `id`;

ALTER TABLE `fin_fund_record`
    ADD COLUMN `fund_type` varchar(20) NOT NULL COMMENT '分类名称 10:商品销售; 20:维修服务;30薪资;40:其他收支;50:质押商品' AFTER `fin_classify_name`;


