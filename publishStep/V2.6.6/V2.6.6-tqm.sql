ALTER TABLE `pro_deliver`
ADD COLUMN `fk_ord_address_id` int(0) NULL COMMENT '收货地址id' AFTER `fk_pro_product_id`;

UPDATE pro_deliver
SET fk_pro_product_id = ( SELECT fk_pro_product_id FROM ord_order WHERE id = pro_deliver.fk_ord_order_id ),
fk_ord_address_id = ( SELECT id FROM ord_address WHERE ord_address.fk_ord_order_id = pro_deliver.fk_ord_order_id )
WHERE
	del = 0
	AND deliver_source = 'ORDER';


UPDATE pro_deliver
SET fk_pro_product_id = ( SELECT fk_pro_product_id FROM pro_lock_record WHERE id = pro_deliver.fk_pro_lock_record_id ),
fk_ord_address_id = ( SELECT fk_ord_address_id FROM pro_lock_record WHERE pro_deliver.fk_pro_lock_record_id = id )
WHERE
	del = 0
	AND deliver_source = 'LOCK_RECORD';



CREATE TABLE `pro_dynamic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `is_initialize` int NOT NULL DEFAULT '0' COMMENT '是否初始化，1是，0否',
  `sort` int DEFAULT NULL COMMENT '排序。只对初始化数据有效',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品动态列表';


CREATE TABLE `pro_dynamic_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_pro_dynamic_id` int DEFAULT NULL COMMENT '列表id',
  `fk_pro_product_id` int NOT NULL COMMENT '商品id',
  `state` int NOT NULL DEFAULT '10' COMMENT '状态：10：正常，20已售罄，30已锁单，40已删除',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='动态列表商品信息';




INSERT INTO pro_dynamic ( NAME, is_initialize, sort, fk_shp_shop_id, insert_time, update_time, insert_admin, update_admin, del, url ) SELECT
'在途',
1,
1,
shp_shop.id,
NOW(),
NOW(),
NULL,
NULL,
'0',
'/default/pro/dynamic/intransit.png'
FROM
	shp_shop
WHERE
	del = 0;
INSERT INTO pro_dynamic ( NAME, is_initialize, sort, fk_shp_shop_id, insert_time, update_time, insert_admin, update_admin, del, url ) SELECT
'在店',
1,
2,
shp_shop.id,
NOW(),
NOW(),
NULL,
NULL,
'0',
'/default/pro/dynamic/shop.png'
FROM
	shp_shop
WHERE
	del = 0;
INSERT INTO pro_dynamic ( NAME, is_initialize, sort, fk_shp_shop_id, insert_time, update_time, insert_admin, update_admin, del, url ) SELECT
'维修养护',
1,
3,
shp_shop.id,
NOW(),
NOW(),
NULL,
NULL,
'0',
'/default/pro/dynamic/service.png'
FROM
	shp_shop
WHERE
	del = 0;

#更新字段状态 11.17新增需求
update pro_dynamic set is_initialize = 0;

update pro_dynamic set is_initialize = 1 where sort is null;