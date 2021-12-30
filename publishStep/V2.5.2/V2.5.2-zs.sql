DROP TABLE
IF EXISTS biz_shop_recommend;
CREATE TABLE `biz_shop_recommend` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `fk_shp_shop_number` varchar(10) NOT NULL COMMENT '店铺编号',
  `state` varchar(10) NOT NULL COMMENT '状态  0 不推荐 1 推荐',
  `recommend_num` int NOT NULL COMMENT '推荐次数上限',
  `insert_time` timestamp NOT NULL  COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insert_admin` int NOT NULL COMMENT '创建人',
  `update_admin` int DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '添加友商推荐' AUTO_INCREMENT = 10000;

DROP TABLE
IF EXISTS biz_shop_see;
CREATE TABLE `biz_shop_see` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT 'shopId',
  `day_count` int NOT NULL COMMENT '每日查看次数',
  `fk_be_seen_shop_id` int NOT NULL COMMENT '被查看的店铺id',
  `insert_time` timestamp NOT NULL COMMENT '创建时间',
  `insert_admin` int NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) COMMENT='店铺查看次数表' AUTO_INCREMENT=10000;

DROP TABLE
IF
	EXISTS pro_modify_record;
CREATE TABLE `pro_modify_record` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
	`fk_shp_user_id` INT NOT NULL COMMENT 'shp_user的id字段,主键id',
	`fk_pro_product_id` INT NOT NULL COMMENT 'pro_product的id字段,主键id',
	`type` VARCHAR ( 20 ) COMMENT '类型;入库,上架,锁单,下架,修改,开单,退货,下载图片,分享,删除',
	`attribute_name` VARCHAR ( 20 ) COMMENT '属性名称;',
	`before_value` VARCHAR ( 1000 ) COMMENT '类型;入库,上架,锁单,下架,修改,开单,退货,下载图片,分享,删除',
	`after_value` VARCHAR ( 1000 ) COMMENT '类型;入库,上架,锁单,下架,修改,开单,退货,下载图片,分享,删除',
	`insert_time` TIMESTAMP NOT NULL COMMENT '插入时间',
	`remark` VARCHAR ( 255 ) COMMENT '备注',
PRIMARY KEY ( `id` )
) COMMENT = '商品修改记录表' AUTO_INCREMENT = 10000;
#商务模块--友商配置表添加参数
ALTER TABLE `biz_leaguer_config`
    ADD COLUMN `recommend` varchar(10) NOT NULL DEFAULT '1' COMMENT '优质友商推荐 0 不推荐 1 推荐' AFTER `only_show_top_leaguer`;

#临时仓产品表;添加参数
ALTER TABLE `pro_temp_product`
    ADD COLUMN `init_price`  decimal(15,2) NULL  COMMENT '成本价(分)' AFTER `trade_price`;

#订单表添加临时仓id;添加参数
ALTER TABLE `ord_order`
    ADD COLUMN `fk_pro_temp_id`  int  NULL COMMENT '临时仓id' AFTER `fk_shp_shop_id`;