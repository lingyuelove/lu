DROP TABLE
IF EXISTS `biz_union_verify`;
CREATE TABLE `biz_union_verify` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
   `fk_shp_shop_id` int NOT NULL COMMENT '店铺id',
   `fk_pro_classify_code` varchar(20) DEFAULT NULL COMMENT '类目 腕表 箱包 腕表和箱包',
   `license_img_url` varchar(255)  NOT NULL COMMENT '营业执照路径',
   `valid_img_url` varchar(2000)  NOT NULL COMMENT '店铺认证图片',
   `stock_img_url` varchar(255)  DEFAULT NULL COMMENT '股东认证图片',
   `empower_img_url` varchar(255)  DEFAULT NULL COMMENT '店铺授权图片',
   `other_user_img_url` varchar(255)  DEFAULT NULL COMMENT '其他人员认证身份证',
   `state` varchar(1)  NOT NULL COMMENT '状态 0 未审核 1已通过 2未通过',
   `shop_user_type` varchar(255)  NOT NULL COMMENT '店铺身份 法人 股东 都不是',
   `insert_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `insert_admin` int NOT NULL COMMENT '新增人',
   `update_admin` int DEFAULT NULL COMMENT '更新人',
   `del` varchar(1)  NOT NULL DEFAULT '0' COMMENT '删除 0 未删除 1已删除',
   `remark` varchar(255) DEFAULT NULL COMMENT '备注',
   `fail_remark` varchar(255) DEFAULT NULL COMMENT '审核失败原因',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=10000  COMMENT='商家联盟审核表';
#盘点表添加盘点类型字段 仅执行此操作
ALTER TABLE `ord_receipt_share`
    ADD COLUMN `show_deliver` varchar(10) DEFAULT '0' COMMENT '是否显示分享物流信息;0:不显示; 1:显示' AFTER `show_type`;

alter table pro_product modify column init_price decimal(19,2);
alter table pro_product modify column trade_price decimal(19,2);
alter table pro_product modify column agency_price decimal(19,2);
alter table pro_product modify column sale_price decimal(19,2);
alter table pro_product modify column finish_price decimal(19,2);
alter table fin_fund_record modify column money decimal(19,2);
alter table fin_fund_record modify column init_price decimal(19,2);
alter table ord_order modify column finish_price decimal(19,2);
alter table ord_order modify column pre_money decimal(19,2);
alter table ord_order modify column last_money decimal(19,2);
