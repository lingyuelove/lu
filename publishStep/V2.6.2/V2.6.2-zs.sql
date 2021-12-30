#分享表新增字段 仅执行此操作
ALTER TABLE `pro_share`
    ADD COLUMN `fk_pro_classify_code`  varchar(20)  DEFAULT '' COMMENT '产品分类表的code;默认'':无分类;' AFTER `fk_shp_user_id`;

  #商品补充信息分类表
    DROP TABLE
    IF EXISTS `pro_classify_type`;

    CREATE TABLE `pro_classify_type` (
    `id`  int NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联' ,
    `code`  varchar(50)  COMMENT '英文首字母大写名称' ,
    `name`  varchar(50)  COMMENT '分类名称;限长50个汉字' ,
    `description`  varchar(50)  DEFAULT '' COMMENT '对code进行详细的补充说明' ,
    `icon_url`  varchar(500)  DEFAULT '' COMMENT '图标地址' ,
    `fk_pro_classify_id`  int NOT NULL COMMENT '商品系列表主键id' ,
    `fk_pro_classify_code`  varchar(10)  DEFAULT NULL COMMENT '商品系列表name',
    `fk_shp_shop_id`  int NOT NULL COMMENT 'shp_shop的id字段,主键id; -9为公用系统分类;' ,
    `pro_classify_type_id` int NOT NULL COMMENT '商品补充信息分类表主键id,供二级三级使用' ,
    `type`  varchar(10)  DEFAULT '1' COMMENT '类型;1:一级分类;2:二级分类;3:三级分类' ,
    `chose_type`  varchar(10)  DEFAULT '1' COMMENT '类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:复选标签;' ,
    `state`  int NOT NULL DEFAULT 1 COMMENT '状态;-1:已删除;0:未使用;1:使用中' ,
    `sort`  int NOT NULL DEFAULT 0 COMMENT '序号排序' ,
    `insert_time`  timestamp NOT NULL  COMMENT '插入时间' ,
    `update_time`  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间' ,
    `insert_admin`  int NULL DEFAULT NULL COMMENT '添加用户_管理员id' ,
    `update_admin`  int NULL DEFAULT NULL COMMENT '修改用户_管理员id' ,
    `versions`  int NOT NULL DEFAULT 1 COMMENT '版本号;用于更新时对比操作;' ,
    `del`  varchar(2)  DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件' ,
    `remark`  varchar(255)  DEFAULT '' COMMENT '备注' ,
    PRIMARY KEY (`id`),
    INDEX `idx_del` (`del`) USING BTREE ,
    INDEX `idx_shop_id_name` (`name`, `fk_shp_shop_id`) USING BTREE
    )
    COMMENT='商品补充信息分类表' AUTO_INCREMENT=10000;

    #商品补充信息关联表
    DROP TABLE
    IF EXISTS `pro_product_classify`;

    CREATE TABLE `pro_product_classify` (
    `id`  int NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
    `fk_pro_product_id`  int NOT NULL COMMENT 'pro_product的主键Id,逻辑id,软件内部关联' ,
    `fk_shp_shop_id`  int NOT NULL COMMENT 'shp_shop的id字段,主键id' ,
    `fk_pro_classify_type_id`  int NOT NULL COMMENT '商品补充信息分类id' ,
    `classify_type_detail_name`  varchar(255) NULL COMMENT '补充信息类型名称' ,
    `type_detail_sub_name`  varchar(255) NULL COMMENT '补充信息类型名称内容' ,
    `insert_time`  timestamp NOT NULL  COMMENT '插入时间' ,
    `update_time`  timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间' ,
    `insert_admin`  int NULL DEFAULT NULL COMMENT '添加用户_管理员id' ,
    `update_admin`  int NULL DEFAULT NULL COMMENT '修改用户_管理员id' ,
    `del`  varchar(2)  DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件' ,
    `remark`  varchar(255)  DEFAULT '' COMMENT '备注' ,
    PRIMARY KEY (`id`),
    INDEX `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE ,
    INDEX `idx_del` (`del`) USING BTREE ,
    INDEX `idx_fk_pro_product_id` (`fk_pro_product_id`) USING BTREE
    )
    COMMENT='商品补充信息关联表' AUTO_INCREMENT=10000;


DROP TABLE
IF EXISTS pro_sub_series;
CREATE TABLE `pro_sub_series` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
  `name` varchar(50) NOT NULL COMMENT '分类名称;限长20个汉字',
  `fk_pro_classify_sub_name` varchar(50) NOT NULL COMMENT '商品品牌分类pro_classify_sub表品牌名称',
  `fk_shp_shop_id` int NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `type` varchar(10) COMMENT '类型;0:系统自带;1:用户自建',
  `state` int NOT NULL COMMENT '状态;-1:已删除;0:未使用;1:使用中',
  `sort` int NOT NULL COMMENT '序号排序',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin`  int NULL DEFAULT NULL COMMENT '添加用户_管理员id' ,
  `update_admin`  int NULL DEFAULT NULL COMMENT '修改用户_管理员id' ,
  `del` varchar(10) COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商品分类系列表' AUTO_INCREMENT = 10000;


DROP TABLE
IF EXISTS pro_series_model;
CREATE TABLE `pro_series_model` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
  `name` varchar(50) NOT NULL COMMENT '分类名称;限长20个汉字',
  `fk_pro_sub_series_name` varchar(50)  NULL COMMENT '商品系列分类pro_sub_series表分类名称',
  `fk_pro_classify_sub_name` varchar(50) NOT NULL COMMENT '商品品牌分类pro_classify_sub表品牌名称',
  `fk_shp_shop_id` int NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `type` varchar(10) COMMENT '类型;0:系统自带;1:用户自建',
  `state` int NOT NULL COMMENT '状态;-1:已删除;0:未使用;1:使用中',
  `sort` int NOT NULL COMMENT '序号排序',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin`  int NULL DEFAULT NULL COMMENT '添加用户_管理员id' ,
  `update_admin`  int NULL DEFAULT NULL COMMENT '修改用户_管理员id' ,
  `del` varchar(10) COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商品分类型号表' AUTO_INCREMENT = 10000;

#商品表新增字段 仅执行此操作
ALTER TABLE `pro_product`
    ADD COLUMN `fk_pro_sub_series_name`  varchar(100)  DEFAULT '' COMMENT '商品分类系列;默认'':无分类;' AFTER `fk_pro_classify_sub_name`;

#商品表新增字段 仅执行此操作
 ALTER TABLE `pro_product`
    ADD COLUMN `fk_pro_series_model_name`  varchar(100)  DEFAULT '' COMMENT '商品分类型号;默认'':无分类;' AFTER `fk_pro_sub_series_name`;
#商品表新增字段 仅执行此操作
 ALTER TABLE `pro_product`
    ADD COLUMN `public_price`  int NULL DEFAULT 0 COMMENT '国内公价(元)' AFTER `sale_price`;
DROP TABLE
IF EXISTS pro_classify_type_shop;
CREATE TABLE `pro_classify_type_shop` (
`id`  int NOT NULL AUTO_INCREMENT COMMENT '主键id' ,
`fk_shp_shop_id`  int NOT NULL COMMENT '主键id' ,
`fk_pro_classify_type_id`  int NOT NULL COMMENT '补充信息id' ,
`type`  varchar(255) NULL DEFAULT 1 COMMENT '类型;1:一级分类;2:二级分类;3:三级分类' ,
`state`  int NULL DEFAULT 0 COMMENT '默认 0 不适用' ,
`insert_time`  timestamp NOT NULL COMMENT '创建时间' ,
`update_time`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,
`insert_admin`  int NOT NULL COMMENT '创建人' ,
`update_admin`  int NULL COMMENT '更新人' ,
PRIMARY KEY (`id`)
)
COMMENT='单个店铺补充信息不适用关联表'  AUTO_INCREMENT = 10000;


#打印模板表新增字段 仅执行此操作
 ALTER TABLE `pro_print_tpl`
    ADD COLUMN `state`  varchar(50)  DEFAULT '0' COMMENT '模板类型 0普通模板 1快速模板' AFTER `content`;

#店铺微信表新增字段 仅执行此操作
 ALTER TABLE `shp_wechat`
    ADD COLUMN `type`  varchar(4)  DEFAULT '0' COMMENT '联系人类型 0:微信 1:手机号' AFTER `insert_admin`;

#店铺微信表新增字段 仅执行此操作
 ALTER TABLE `shp_wechat`
    ADD COLUMN `contact_responsible`  varchar(50)  DEFAULT '' COMMENT '负责内容' AFTER `type`;

#--截止2021-09-07 21:50:13 以上sql已经全部在正式环境执行过;如有改动,请在下面增加;

 ALTER TABLE `ord_order`
    ADD COLUMN `pre_money` decimal(15,0) DEFAULT '0' COMMENT '预付金额(分)' AFTER `finish_price`;
 ALTER TABLE `ord_order`
    ADD COLUMN `last_money` decimal(15,0) DEFAULT '0' COMMENT '尾款金额(分)' AFTER `pre_money`;

#锁单表需要去掉索引 pro_lock_record：idx_uk_pro_id_and_user_id

#商品补充信息分类表 pro_classify_type_id不是必传
DROP TABLE
   IF EXISTS `pro_classify_type`;
CREATE TABLE `pro_classify_type` (
	`id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
	`code` VARCHAR (50) COMMENT '英文首字母大写名称',
	`name` VARCHAR (50) COMMENT '分类名称;限长50个汉字',
	`description` VARCHAR (50) DEFAULT '' COMMENT '对code进行详细的补充说明',
	`icon_url` VARCHAR (500) DEFAULT '' COMMENT '图标地址',
	`fk_pro_classify_id` INT NOT NULL COMMENT '商品系列表主键id',
	`fk_pro_classify_code` VARCHAR (10) DEFAULT NULL COMMENT '商品系列表name',
	`fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id; -9为公用系统分类;',
	`pro_classify_type_id` INT NULL COMMENT '商品补充信息分类表主键id,供二级三级使用',
	`type` VARCHAR (10) DEFAULT '1' COMMENT '类型;1:一级分类;2:二级分类;3:三级分类',
	`chose_type` VARCHAR (10) DEFAULT '1' COMMENT '类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:复选标签;',
	`state` INT NOT NULL DEFAULT 1 COMMENT '状态;-1:已删除;0:未使用;1:使用中',
	`sort` INT NOT NULL DEFAULT 0 COMMENT '序号排序',
	`insert_time` TIMESTAMP NOT NULL COMMENT '插入时间',
	`update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
	`insert_admin` INT NULL DEFAULT NULL COMMENT '添加用户_管理员id',
	`update_admin` INT NULL DEFAULT NULL COMMENT '修改用户_管理员id',
	`versions` INT NOT NULL DEFAULT 1 COMMENT '版本号;用于更新时对比操作;',
	`del` VARCHAR (2) DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
	`remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
	PRIMARY KEY (`id`),
	INDEX `idx_del` (`del`) USING BTREE,
	INDEX `idx_shop_id_name` (`name`, `fk_shp_shop_id`) USING BTREE
) COMMENT = '商品补充信息分类表' AUTO_INCREMENT = 10000;
