

#商品修改记录表添加来源字段 仅执行此操作
ALTER TABLE `pro_modify_record`
    ADD COLUMN `source` varchar(20) DEFAULT NULL COMMENT '来源 ios；android；pc' AFTER `insert_time`;

#盘点表添加临时仓id字段 仅执行此操作
ALTER TABLE `pro_check`
    ADD COLUMN `fk_pro_temp_id` int DEFAULT NULL COMMENT 'pro_temp的id字段,主键id' AFTER `fk_shp_shop_id`;
#盘点表添加盘点类型字段 仅执行此操作
ALTER TABLE `pro_check`
    ADD COLUMN `type` varchar(10) DEFAULT 'warehouse' COMMENT '盘点类型 temp:临时仓； warehouse:仓库' AFTER `fk_pro_temp_id`;
#盘点表编辑字段为可为空
ALTER TABLE pro_check MODIFY fk_pro_attribute_codes varchar(20) DEFAULT NULL;

DROP TABLE
IF EXISTS `pro_deliver_logistics`;
CREATE TABLE `pro_deliver_logistics` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `logistics_state` varchar(50) NOT NULL COMMENT '发货状态 1:物流异常 2:暂无物流信息 10:已发货 20:已揽件 30:运输中 40:派件中 41:派件异常 50:已签收 60:已退回/转寄',
  `logistics_number` varchar(50)  NOT NULL COMMENT '物流单号',
  `phone` varchar(10)  DEFAULT NULL COMMENT '手机号后四位  物流查询使用',
  `context` text  COMMENT '物流信息',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
)  AUTO_INCREMENT=10000  COMMENT='发货物流表';

DROP TABLE
IF EXISTS `shp_service_record_cost`;
CREATE TABLE `shp_service_record_cost`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺ID',
  `fk_shp_service_record_id` int NOT NULL COMMENT '店铺服务记录ID',
  `repair_content` varchar(255)  DEFAULT NULL COMMENT '维修内容',
  `service_cost` decimal(15, 2) NULL DEFAULT NULL COMMENT '服务成本',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) AUTO_INCREMENT=10000   COMMENT = '服务成本表' ;

#维修保养表新增接单人员字段 不可为空
ALTER TABLE `shp_service_record`
    ADD COLUMN `receive_shp_user_id` int DEFAULT NULL COMMENT '接单人员ID' AFTER `service_shp_user_id`;
#分享电子凭证表新增展示类型字段 不可为空
ALTER TABLE `ord_receipt_share`
    ADD COLUMN `show_type` varchar(10)  DEFAULT 'all' COMMENT '展示类型 all 全部显示 no:全部不显示 context:只展示内容' AFTER `receipt_type`;