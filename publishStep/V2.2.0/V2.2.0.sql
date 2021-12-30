ALTER TABLE `shp_order_daily_count`
ADD COLUMN `onshelves_prod_num`  int NULL DEFAULT '0' COMMENT '上架商品数量' AFTER `in_repo_amount_total`;

ALTER TABLE `ord_receipt_share`
    ADD COLUMN `receipt_type`  varchar(20) NOT NULL DEFAULT 'dzpz' COMMENT '收据类型 dzpz|电子凭证 service|店铺服务' AFTER `insert_time`;

ALTER TABLE `biz_leaguer`
    MODIFY COLUMN `is_can_see_sale_price`  tinyint NOT NULL DEFAULT 0 COMMENT '是否可以看销售价 1|可以 0|不可以' AFTER `remark`;

ALTER TABLE `shp_shop`
ADD COLUMN `is_member`  varchar(20) NOT NULL DEFAULT 'no' COMMENT '是否是会员 yes|是会员 no|不是会员' AFTER `total_month`,
ADD COLUMN `mini_program_cover_img_url`  varchar(255) NULL COMMENT '小程序封面图片地址' AFTER `is_member`;


CREATE TABLE `shp_service_record` (
      `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
      `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
      `prod_img_urls` varchar(5000)  NOT NULL COMMENT '商品图片链接URL列表，用英文分号分割',
      `prod_name` varchar(255)  NOT NULL COMMENT '商品名称',
      `unique_code` varchar(255)  DEFAULT NULL COMMENT '独立编码',
      `cost_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '维修成本',
      `real_receive_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '实际收费',
      `type_name` varchar(50)  NOT NULL COMMENT '服务类型名称',
      `service_shp_user_id` int NOT NULL DEFAULT '0' COMMENT '维修人员ID',
      `note` varchar(500)  DEFAULT NULL COMMENT '备注',
      `customer_info` varchar(255)  DEFAULT NULL COMMENT '客户信息',
      `service_status` varchar(50)  NOT NULL COMMENT '服务状态 inService|服务中 finish|完成 cancel|取消 ',
      `service_number` varchar(50)  NOT NULL COMMENT '服务编号',
      `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
      `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
      `insert_time` datetime NOT NULL COMMENT '创建时间',
      `update_time` datetime NOT NULL COMMENT '更新时间',
      `insert_admin` int DEFAULT NULL COMMENT '创建者',
      `update_admin` int DEFAULT NULL COMMENT '修改者',
      `del` char(1)  NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
      PRIMARY KEY (`id`),
      KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺服务记录表';

CREATE TABLE `shp_service_type` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
    `service_name` varchar(200) NOT NULL COMMENT '服务名称',
    `insert_admin` int NOT NULL COMMENT '创建者',
    `update_admin` int DEFAULT NULL COMMENT '修改者',
    `insert_time` datetime NOT NULL COMMENT '创建时间',
    `update_time` datetime NOT NULL COMMENT '修改时间',
    `del` char(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺服务类型表';

CREATE TABLE `ord_modify_record` (
     `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
     `fk_ord_order_id` int NOT NULL COMMENT '订单ID',
     `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
     `fk_pro_product_id` int NOT NULL COMMENT '商品ID',
     `create_user_id` int NOT NULL COMMENT '创建订单用户ID',
     `update_user_id` int NOT NULL COMMENT '修改订单用户ID',
     `unique_code_before` varchar(50)  DEFAULT NULL COMMENT '独立编码（修改前）',
     `unique_code_after` varchar(50)  DEFAULT NULL COMMENT '独立编码（修改后）',
     `type_before` varchar(50)  DEFAULT NULL COMMENT '订单类型（修改前）',
     `type_after` varchar(50)  DEFAULT NULL COMMENT '订单类型（修改后）',
     `finish_price_before` decimal(15,2) DEFAULT NULL COMMENT '成交价格（修改前）',
     `finish_price_after` decimal(15,2) DEFAULT NULL COMMENT '成交价格（修改后）',
     `sale_user_id_before` int NOT NULL COMMENT '销售人员ID(修改前)',
     `sale_user_id_after` int NOT NULL COMMENT '销售人员ID(修改后)',
     `after_sale_guarantee_before` varchar(255)  DEFAULT NULL COMMENT '售后保障（修改前）',
     `after_sale_guarantee_after` varchar(255)  DEFAULT NULL COMMENT '售后保障（修改后）',
     `remark_before` varchar(255)  DEFAULT NULL COMMENT '订单备注（修改前）',
     `remark_after` varchar(255)  DEFAULT NULL COMMENT '订单备注（修改后）',
     `address_before` varchar(255)  DEFAULT NULL COMMENT '收货信息（修改前）',
     `address_after` varchar(255)  DEFAULT NULL COMMENT '收货信息（修改后）',
     `insert_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '修改时间',
      PRIMARY KEY (`id`),
      KEY `idx_order_id_shop_id` (`fk_ord_order_id`,`fk_shp_shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单修改记录表';

-- 财务账单流水
CREATE TABLE `fin_shop_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
  `inout_type` varchar(20) NOT NULL COMMENT '出入类型 in|收入 out|支出',
  `inout_sub_type` varchar(50) NOT NULL COMMENT '流水子类型 中文',
  `record_type` varchar(50) NOT NULL COMMENT '流水类型 system|系统生成 manual|人工记录',
  `img_url` varchar(255) NOT NULL COMMENT '图片地址',
  `stream_no` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流水号',
  `change_amount` decimal(15,2) NOT NULL COMMENT '变动金额 收入为正，支出为负(单位为分)',
  `fk_order_id` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流水关联的订单ID',
  `happen_time` datetime NOT NULL COMMENT '流水发生时间',
  `happen_date` varchar(20) NOT NULL COMMENT '流水发生日期',
  `happen_month` varchar(20) NOT NULL COMMENT '流水发生月份',
  `happen_year` varchar(10) NOT NULL COMMENT '流水发生年份',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `insert_admin` int NOT NULL COMMENT '创建者用户ID',
  `update_admin` int DEFAULT NULL COMMENT '修改者用户ID',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `del` char(1) NOT NULL COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺财务流水表';

CREATE TABLE `fin_shop_record_type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
  `fin_record_type_name` varchar(200) NOT NULL COMMENT '店铺流水类型名称',
  `inout_type` varchar(50) DEFAULT NULL COMMENT '出入类型 in|收入 out|支出',
  `insert_admin` int NOT NULL COMMENT '创建者',
  `update_admin` int DEFAULT NULL COMMENT '修改者',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `del` char(1) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_shop_id_name_inout_type` (`fk_shp_shop_id`,`fin_record_type_name`,`inout_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='店铺财务流水类型表';

CREATE TABLE `shp_operate_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` int NOT NULL COMMENT '操作店铺ID',
  `shop_name` varchar(255) NOT NULL COMMENT '操作店铺名称',
  `operate_user_id` int NOT NULL COMMENT '操作人用户ID',
  `operate_user_shop_name` varchar(255) NOT NULL COMMENT '操作人所在店铺用户名',
  `module_name` varchar(50) NOT NULL COMMENT '模块名称',
  `operate_type_name` varchar(50) NOT NULL COMMENT '操作业务类型名称',
  `operate_content` varchar(500) NOT NULL COMMENT '操作内容',
  `operate_date` varchar(255) NOT NULL COMMENT '操作日期',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `request_domain` varchar(500) NOT NULL COMMENT '请求域名',
  `request_uri` varchar(500) NOT NULL COMMENT '请求URI',
  `request_param` varchar(2000) NOT NULL COMMENT '请求参数',
  `request_ip` varchar(20) NOT NULL COMMENT '请求IP',
  `request_method` varchar(50) NOT NULL COMMENT '请求方法 POST|GET',
  `fk_pro_product_id` int DEFAULT NULL COMMENT '操作的商品ID',
  `fk_ord_order_id` int DEFAULT NULL COMMENT '操作订单ID',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺操作日志表';

INSERT INTO `op_platform` (`id`, `type`, `state`, `insert_time`, `update_time`, `insert_admin`, `update_admin`, `versions`, `del`, `remark`)
VALUES ('2', 'android', '10', NOW(), NULL, '1', '1', '1', '0', '');

