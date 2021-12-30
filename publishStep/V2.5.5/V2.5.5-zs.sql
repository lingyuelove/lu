
#店铺分享表;添加参数
ALTER TABLE `pro_share`
    ADD COLUMN `del`  varchar(10)  NOT NULL DEFAULT '0' COMMENT '删除状态 0 未删除 1已删除' AFTER `insert_time`;
ALTER TABLE `pro_share`
    ADD COLUMN `share_img`  varchar(500)   COMMENT '分享图片' AFTER `insert_time`;
ALTER TABLE `pro_share`
    ADD COLUMN `show_name`  varchar(255)   COMMENT '分享显示名字' AFTER `insert_time`;

#添加店铺权限表
CREATE TABLE `shp_shop_config` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`fk_shp_shop_id` INT NOT NULL COMMENT '店铺id',
	`shop_number` VARCHAR (10) NOT NULL COMMENT '店铺编号',
	`open_share_user` VARCHAR (10) NOT NULL COMMENT '是否开启小程序访客功能 0未开启 1已开启',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
	`insert_admin` INT NOT NULL COMMENT '创建人',
	`update_admin` INT NULL COMMENT '更新人',
	`remark` VARCHAR (255) NULL COMMENT '备注',
	PRIMARY KEY (`id`),
	INDEX `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE,
	INDEX `idx_shop_number` (`shop_number`) USING BTREE
) AUTO_INCREMENT = 10000 COMMENT = '店铺配置表';


#订单表新加参数
ALTER TABLE `ord_order`
    ADD COLUMN `delete_remark`  varchar(255)   COMMENT '删除备注' AFTER `del`;

#商品表添加参数
ALTER TABLE `pro_product`
    ADD COLUMN `delete_remark`  varchar(255)   COMMENT '删除备注' AFTER `del`;

#商品表添加参数
ALTER TABLE `pro_product`
    ADD COLUMN `retrieve_remark`  varchar(255)   COMMENT '取回备注' AFTER `recycle_admin`;
#商品表添加参数
ALTER TABLE `pro_product`
    ADD COLUMN `fk_shp_retrieve_user_id`  INT   COMMENT '取回用户' AFTER `retrieve_remark`;
#商品表添加参数
ALTER TABLE `pro_product`
    ADD COLUMN `retrieve_time`  TIMESTAMP NULL DEFAULT NULL COMMENT '取回时间' AFTER `fk_shp_retrieve_user_id`;



CREATE TABLE `pro_share_see_user` (
`id`  int NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`fk_shp_shop_id`  int NOT NULL COMMENT '店铺id' ,
`fk_pro_share_id`  int NOT NULL COMMENT '分享id' ,
`fk_pro_share_batch`  varchar(50) NOT NULL COMMENT '分享批次' ,
`nick_name`  varchar(255) NOT NULL COMMENT '昵称' ,
`avatar_url`  varchar(255) NOT NULL COMMENT '头像' ,
`gender`  varchar(2) NOT NULL COMMENT '性别  0：未知、1：男、2：女' ,
`open_id`  varchar(255) NOT NULL COMMENT '微信的openId' ,
`union_id`  varchar(255) NULL COMMENT '微信的unionId' ,
`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
PRIMARY KEY (`id`),
INDEX `idx_fk_pro_share_id` (`fk_pro_share_id`) USING BTREE ,
INDEX `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
)
COMMENT='小程序访客记录表'  AUTO_INCREMENT = 10000
;