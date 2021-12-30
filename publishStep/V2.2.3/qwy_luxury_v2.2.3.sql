DROP TABLE IF EXISTS `pro_temp_product`;
CREATE TABLE `pro_temp_product` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;',
  `fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `fk_pro_temp_id` INT NOT NULL COMMENT 'pro_temp的id字段,主键id',
  `fk_pro_product_id` INT NOT NULL COMMENT 'pro_product的id字段,主键id',
  `name` VARCHAR (255) DEFAULT null COMMENT '名称',
  `num` INT DEFAULT NULL COMMENT '临时仓数量',
  `description` VARCHAR (255) DEFAULT null COMMENT '产品描述',
  `trade_price` DECIMAL (15, 2) DEFAULT null COMMENT '友商价(分)',
  `agency_price` DECIMAL (15, 2) DEFAULT null COMMENT '代理价(分)',
  `sale_price` DECIMAL (15, 2) DEFAULT null COMMENT '零售价(分)',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pro_temp_id_pro_product_id` (
    `fk_pro_temp_id`,
    fk_pro_product_id
  ) USING BTREE COMMENT '一个临时仓不能有相同的两件商品',
  KEY `idx_fk_shp_shop_id_pro_temp_id` (
    `fk_shp_shop_id`,
    fk_pro_temp_id
  ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '临时仓产品表;' ;



DROP TABLE IF EXISTS `pro_temp`;
CREATE TABLE `pro_temp` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;',
  `fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `state` VARCHAR (10) DEFAULT '10' COMMENT '名称',
  `name` VARCHAR (255) DEFAULT '' COMMENT '名称',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
  `sort` INT NOT NULL DEFAULT '99' COMMENT '排序',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '临时仓;' ;



项目启动后, 需要执行以下链接:
http://192.168.0.250:8080/sys/initVipExpire?token=15112304365

//锁单消息通知权限; msg:pro:lock

//添加临时仓权限;


delete from shp_bind_count;
  ALTER TABLE `shp_bind_count`
  DROP INDEX `uk_username`,
  ADD  UNIQUE INDEX `uk_type_open_id` (`type`, `open_id`);