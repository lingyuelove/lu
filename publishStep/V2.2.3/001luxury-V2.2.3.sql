CREATE TABLE `stat_total` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `count_date` varchar(20) NOT NULL COMMENT '统计日期',
  `count_month` varchar(10) NOT NULL COMMENT '统计月份',
  `count_year` varchar(10) NOT NULL COMMENT '统计年份',
  `reg_person_num` int NOT NULL COMMENT '注册人数',
  `reg_shop_num` int NOT NULL COMMENT '注册店铺数',
  `order_num` int NOT NULL COMMENT '订单量',
  `order_sell_amount` decimal(15,2) NOT NULL COMMENT '订单销售额',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_count_date` (`count_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='运营统计-大盘表';

CREATE TABLE `stat_shop` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
  `prod_num_upload` int NOT NULL COMMENT '上传商品数量',
  `prod_num_onsale` int NOT NULL COMMENT '在售商品数量',
  `total_sale_num` int NOT NULL COMMENT '总销售额',
  `total_sale_amount` decimal(15,2) NOT NULL COMMENT '总销售量',
  `shop_staff_num` int NOT NULL COMMENT '店铺员工数量',
  `shop_leaguer_num` int NOT NULL COMMENT '店铺友商数量',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='运营统计-店铺表';

CREATE TABLE `stat_prod_sale_classify` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pro_classify_code` varchar(20)   NOT NULL COMMENT '销售类别code',
  `pro_classify_name` varchar(20)   NOT NULL COMMENT '销售类别名称',
  `total_sale_num` int NOT NULL COMMENT '总销售量',
  `total_sale_amount` decimal(15,2) NOT NULL COMMENT '总销售额',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_pro_classify_code` (`pro_classify_code`) USING BTREE,
  UNIQUE KEY `idx_pro_classify_name` (`pro_classify_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='运营统计-销售分类表';

CREATE TABLE `pro_print_tpl` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `insert_admin` int NOT NULL COMMENT '创建人',
  `update_admin` int DEFAULT NULL COMMENT '修改人',
  `insert_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品打印模板表';

ALTER TABLE `pro_download_img`
ADD COLUMN `update_time`  timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP AFTER `insert_time`;

ALTER TABLE `ord_order`
ADD COLUMN `deduct_voucher_img_url`  varchar(2000) NULL DEFAULT '' COMMENT '扣款凭证图片URL' AFTER `cancel_time`;

ALTER TABLE `fin_shop_record`
ADD COLUMN `img_url_detail`  varchar(2000) NULL COMMENT '流水详情图片列表' AFTER `img_url`;
