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