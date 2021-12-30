DROP TABLE
IF EXISTS `pro_deliver`;
CREATE TABLE `pro_deliver` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_pro_lock_record_id` int NOT NULL DEFAULT '0' COMMENT '锁单id',
  `fk_ord_order_id` int NOT NULL DEFAULT '0' COMMENT '订单id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `fk_shp_user_id` int DEFAULT NULL COMMENT '发货人id',
  `number` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '发货编号(用时间戳)',
  `state` int NOT NULL DEFAULT '0' COMMENT '0：未发货，1：已发货',
  `logistics_number` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物流单号',
  `logistics_company` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物流公司',
  `deliver_imgs` text COLLATE utf8mb4_general_ci COMMENT '发货凭证图',
  `deliver_time` timestamp NULL DEFAULT NULL COMMENT '发货时间',
  `deliver_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发货方式(目前五种)：ME_CLAIM:自取，FLASH_SEND:闪送，SF_EXPRESS:顺丰，OTHER_PEOPLE_TAKE:他人代取，OTHER:其他',
  `deliver_source` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '发货订单来源：ORDER(订单)、LOCK_RECORD（锁单）',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='发货表';