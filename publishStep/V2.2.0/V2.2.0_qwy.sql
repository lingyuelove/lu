DROP TABLE IF EXISTS `fin_salary`;
CREATE TABLE `fin_salary` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` INT NOT NULL COMMENT '店铺ID',
  `fk_shp_user_id` INT NOT NULL COMMENT '用户id',
  `salary_state` VARCHAR (4) NOT NULL COMMENT '0:未发放; 1:已发放;',
  `salary_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '薪资总额',
  `salary_name` VARCHAR (50) NOT NULL COMMENT '工资条名称',
  `salary_st_time` DATETIME NOT NULL COMMENT '工资条统计的开始时间',
  `salary_et_time` DATETIME NOT NULL COMMENT '工资条统计的结束时间',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '创建人用户id',
  `update_admin` INT DEFAULT NULL COMMENT '修改人用户id',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
  KEY `idx_fk_shp_user_id` (`fk_shp_user_id`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '工资条记录表' ;


DROP TABLE IF EXISTS `fin_salary_detail`;
CREATE TABLE `fin_salary_detail` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` INT NOT NULL COMMENT '店铺ID',
  `fk_shp_user_id` INT NOT NULL COMMENT '用户id',
  `fk_fin_salary_id` INT NOT NULL COMMENT '工资条记录表id',
  `product_attr` VARCHAR (10) NOT NULL COMMENT '统计的商品属性类型; 多个用逗号隔开; 10:自有商品; 20:寄卖商品',
  `sale_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '销售总额(分)',
  `gross_profit_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '毛利润总额(分)',
  `recycle_cost_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '回收成本总额(分)',
  `recycle_profit_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT 'TA回收产生的利润(分)',
  `service_profit_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '服务利润总额(分)',
  `sale_result_json` TEXT DEFAULT null COMMENT '销售结果json字符串',
  `scheme_type` VARCHAR (500) NOT NULL COMMENT '方案类型; 格式: 提成模块-方案,订单类型:值,订单类型2:值2;',
  `basic_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '底薪(分)',
  `else_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '其它款项(分)',
  `sale_push_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '销售总提成(分)',
  `recycle_push_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '回收总提成(分)',
  `service_push_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '服务总提成(分)',
  `salary_total_money` DECIMAL (15, 0) NOT NULL DEFAULT '0' COMMENT '总计(分)',
  `recycle_init_price_percent` DECIMAL (15, 2) NOT NULL DEFAULT '0' COMMENT '回收成本百分比(%)',
  `recycle_unit_price` DECIMAL (15, 2) NOT NULL DEFAULT '0' COMMENT '回收单价(分)/每件',
  `recycle_profit_percent` DECIMAL (15, 2) NOT NULL DEFAULT '0' COMMENT 'TA回收产生的利润提点(%)',
  `service_profit_percent` DECIMAL (15, 2) NOT NULL DEFAULT '0' COMMENT '服务利润提点(%)',
  `salary_st_time` DATETIME NOT NULL COMMENT '工资条统计的开始时间',
  `salary_et_time` DATETIME NOT NULL COMMENT '工资条统计的结束时间',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '创建人用户id',
  `update_admin` INT DEFAULT NULL COMMENT '修改人用户id',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
  KEY `idx_fk_shp_user_id` (`fk_shp_user_id`) USING BTREE,
  UNIQUE KEY `uk_shopId_userId_stTime` (`fk_shp_shop_id`, `fk_shp_user_id`, `salary_st_time`)
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '工资条详情表' ;


DROP TABLE IF EXISTS `fin_salary_scheme`;
CREATE TABLE `fin_salary_scheme` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` INT NOT NULL COMMENT '店铺ID',
  `fk_shp_user_id` INT NOT NULL COMMENT '用户id',
  `fk_fin_salary_detail_id` INT NOT NULL COMMENT '工资条详情表id',
  `scheme_type` VARCHAR (10) NOT NULL COMMENT '方案类型;2-1; 2-2; 2-3; 3-1',
  `scheme_percent` DECIMAL (15, 2) NOT NULL COMMENT '百分比',
  `scheme_num` DECIMAL (15, 0) NOT NULL COMMENT '件数',
  `scheme_money` DECIMAL (15, 0) NOT NULL COMMENT '合计(分)',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '创建人用户id',
  `update_admin` INT DEFAULT NULL COMMENT '修改人用户id',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
  KEY `idx_fk_shp_user_id` (`fk_shp_user_id`) USING BTREE,
  KEY `idx_fk_fin_salary_detail_id` (`fk_fin_salary_detail_id`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '工资方案表' ;


DROP TABLE IF EXISTS `fin_salary_order_profit`;
CREATE TABLE `fin_salary_order_profit` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` INT NOT NULL COMMENT '店铺ID',
  `fk_shp_user_id` INT NOT NULL COMMENT '用户id',
  `fk_fin_salary_detail_id` INT NOT NULL COMMENT '工资条详情表id',
  `fk_fin_salary_scheme_id` INT NOT NULL COMMENT '工资方案表id',
  `product_attr` VARCHAR (10) DEFAULT '' COMMENT '统计的商品属性类型;10:自有商品; 20:寄卖商品; 30:其它商品',
  `order_type` VARCHAR (20) DEFAULT '' COMMENT '订单类型;',
  `total_cost_money` DECIMAL (15, 0) NOT NULL COMMENT '总成本',
  `total_sale_money` DECIMAL (15, 0) NOT NULL COMMENT '销售总额',
  `total_profit_money` DECIMAL (15, 0) NOT NULL COMMENT '毛利润总额',
  `scheme_num` DECIMAL (15, 0) NOT NULL COMMENT '件数',
  `scheme_money` DECIMAL (15, 0) NOT NULL COMMENT '合计(分)',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '创建人用户id',
  `update_admin` INT DEFAULT NULL COMMENT '修改人用户id',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
  KEY `idx_fk_shp_user_id` (`fk_shp_user_id`) USING BTREE,
  KEY `idx_fk_fin_salary_detail_id` (`fk_fin_salary_detail_id`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '工资的订单利润表' ;