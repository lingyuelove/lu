
DROP TABLE
IF EXISTS `pro_temp_product_move`;
CREATE TABLE `pro_temp_product_move`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `fk_pro_product_id` int NOT NULL COMMENT '商品id',
  `fk_remove_pro_temp_id` int NOT NULL COMMENT '移出仓库id',
  `fk_enter_pro_temp_id` int NOT NULL COMMENT '移入仓库id',
  `remove_num` int NOT NULL DEFAULT 0 COMMENT '转出数量',
  `surplus_num` int NOT NULL DEFAULT 0 COMMENT '超出数量 默认0',
  `insert_time` timestamp NOT NULL ON DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `insert_admin` int NOT NULL COMMENT '创建人',
  `remark` varchar(255) NULL COMMENT '备注',
  PRIMARY KEY (`id`)
)AUTO_INCREMENT=10000 COMMENT='临时仓商品移动历史表';

#锁单表添加来源字段 仅执行此操作
ALTER TABLE `pro_lock_record`
    ADD COLUMN `fk_ord_address_id` int NULL COMMENT '收货地址id' AFTER `unlock_user_id`;

#帮助中心问题表添加视频帮助中心字段 仅执行此操作
ALTER TABLE `op_problem`
    ADD COLUMN `type` varchar(1) NOT NULL  DEFAULT '0' COMMENT '分类 0:图文教程 1:视频教程' AFTER `sort`;
ALTER TABLE `op_problem`
    ADD COLUMN `video_url` varchar(200) NULL COMMENT '视频链接' AFTER `problem_answer`;
ALTER TABLE `op_problem`
    ADD COLUMN `play_num` int NOT NULL DEFAULT 0 COMMENT '播放次数' AFTER `video_url`;
ALTER TABLE `op_problem`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `del`;


CREATE TABLE `ord_print_tpl`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
  `context` text NULL COMMENT '导出文本',
  `insert_time` timestamp NOT NULL ON DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  `insert_admin` int NOT NULL COMMENT '创建人',
  `update_admin` int NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
)AUTO_INCREMENT=10000 COMMENT='订单打印模板表';
