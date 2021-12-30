DROP TABLE
IF EXISTS `biz_shop_union`;

CREATE TABLE `biz_shop_union` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT '店铺id',
	`state` INT NOT NULL DEFAULT 10 COMMENT '添加状态 -10 已退出 10已加入',
	`type`  varchar(2) NOT NULL COMMENT '添加类型 10 买家 20 卖家' ,
	`insert_time` TIMESTAMP NOT NULL COMMENT '创建时间',
	`update_time` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人' ,
	`update_admin` INT NULL COMMENT '更新人',
	`del` CHAR (1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	PRIMARY KEY (`id`),
	INDEX `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
	INDEX `idx_del` (`del`) USING BTREE
) AUTO_INCREMENT = 10000 COMMENT = '商家联盟配置表';


#对账单新增字段 仅执行此操作
ALTER TABLE `fin_bill`
    ADD COLUMN `old_money`  decimal(10,0) NOT NULL COMMENT '初始账单金额' AFTER `money`;



CREATE TABLE `sys_job_wx` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_sys_user_id` INT default 0 COMMENT '用户id,关联的后台用户id',
  `state` INT NOT NULL DEFAULT 0 COMMENT '绑定状态 -1:已解绑 | 0:未绑定 |  1:已绑定',
  `type` varchar (10) default '' COMMENT '类型;预留字段',
  `wx_account` varchar (50) default '' COMMENT '微信帐号',
  `nickname` varchar (50) default '' COMMENT '微信昵称',
  `phone` VARCHAR (50) DEFAULT '' COMMENT '微信绑定手机号',
  `insert_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
  `update_time` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insert_admin` INT NOT NULL COMMENT '创建人',
  `update_admin` INT NULL COMMENT '更新人',
  `del` varchar (1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` VARCHAR (255) NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  INDEX `idx_state_fk_sys_user_id` (`state`,`fk_sys_user_id`) USING BTREE,
  INDEX `idx_del` (`del`) USING BTREE
) AUTO_INCREMENT = 10000 COMMENT = '微信客服号(线上运营部门的工作号);' ;
