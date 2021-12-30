#机构临时仓
DROP TABLE
IF EXISTS org_organization;

CREATE TABLE `org_organization` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
	`name` VARCHAR (50) NOT NULL COMMENT '机构临时仓名称',
	`state` VARCHAR (20) NULL DEFAULT '20' COMMENT '展会状态 10 不开启限制 | 20 开启限制',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL COMMENT '更新时间',
	`start_time` datetime NULL COMMENT '开始时间',
	`end_time` datetime NULL COMMENT '结束时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT NULL COMMENT '更新人',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	PRIMARY KEY (`id`)
) COMMENT = '机构仓' AUTO_INCREMENT = 10000;

DROP TABLE
IF EXISTS org_organization_temp;

CREATE TABLE `org_organization_temp` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_org_organization_id` INT NOT NULL COMMENT '机构id',
	`fk_pro_temp_id` INT NOT NULL COMMENT '临时仓id',
	`fk_shp_shop_id` INT NOT NULL COMMENT '店铺id',
	`temp_seat_name` VARCHAR (50) NOT NULL COMMENT '店铺机构仓排序位置分组名称',
	`show_seat` VARCHAR (50) NOT NULL COMMENT '展会位置',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT NULL COMMENT '更新人',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	PRIMARY KEY (`id`)
) COMMENT = '机构临时仓' AUTO_INCREMENT = 10000;

DROP TABLE
IF EXISTS org_access_user;

CREATE TABLE `org_access_user` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shopId',
	`fk_org_organization_id` INT NOT NULL COMMENT '机构仓id',
	`phone` VARCHAR (20) NOT NULL COMMENT '手机号',
	`access_type` VARCHAR (10) NULL DEFAULT '10' COMMENT '用户类型 -90 已删除 | 10白名单 | 20黑名单 ',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
	`insert_admin` INT DEFAULT NULL COMMENT '创建人用户id',
	`update_admin` INT DEFAULT NULL COMMENT '修改人用户id',
	`remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
	PRIMARY KEY (`id`)
) COMMENT = '机构仓访问用户列表' AUTO_INCREMENT = 10000;

DROP TABLE
IF EXISTS org_temp_seat;

CREATE TABLE `org_temp_seat` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT '店铺id',
	`name` VARCHAR (20) NOT NULL COMMENT '分组名称',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`insert_admin` INT NOT NULL COMMENT '添加用户_管理员id',
	PRIMARY KEY (`id`)
) COMMENT = '店铺机构仓排序位置分组表' AUTO_INCREMENT = 10000;

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