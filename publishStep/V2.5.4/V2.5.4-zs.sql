
#店铺表添加活动添加总时长;添加参数
ALTER TABLE `shp_shop`
    ADD COLUMN `total_hours`  decimal(12,2)  DEFAULT '0.00' COMMENT '活动添加的总时长' AFTER `admin_add_time`;
ALTER TABLE `shp_shop`
    ADD COLUMN `pay_end_time_old`  timestamp   NULL COMMENT '会员加活动结束时间' AFTER `pay_end_time`;


DROP TABLE IF EXISTS `shp_share_type`;
CREATE TABLE `shp_share_type` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`code` VARCHAR (50)  DEFAULT NULL COMMENT '时长规则类型 1 分享添加时长',
	`hours` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '时长',
	`add_num` INT DEFAULT NULL COMMENT '可添加的次数',
	`insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
	`update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
	`insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
	`update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
	`versions` INT NOT NULL DEFAULT '1' COMMENT '版本号;用于更新时对比操作;',
	`remark` VARCHAR (255)  DEFAULT '' COMMENT '备注',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 10001 COMMENT = '商铺添加时长类型表';

INSERT INTO `shp_share_type` (
	`id`,
	`code`,
	`hours`,
	`add_num`,
	`insert_time`,
	`update_time`,
	`insert_admin`,
	`update_admin`,
	`versions`,
	`remark`
)
VALUES
	(
		'10000',
		'1',
		'1.00',
		'24',
		'2021-06-08 20:02:58',
		NULL,
		'-1',
		NULL,
		'1',
		''
	);

#更新商品库中还有数量的临时仓商品数量
update pro_temp_product tp left join pro_product pp on tp.fk_pro_product_id = pp.id set tp.num = 1 where tp.num is null and pp.total_num >0;
#更新商品库中没有数量的临时仓商品数量
update pro_temp_product tp left join pro_product pp on tp.fk_pro_product_id = pp.id set num = 0 where tp.num is null and (pp.total_num is null or pp.total_num= 0 )
