DROP TABLE
IF EXISTS pro_classify_sub;
CREATE TABLE `pro_classify_sub` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
  `code` varchar(50) NOT NULL COMMENT '模板id+店铺编号+本店增加id个数(字符串拼接);eg:WB100001',
  `name` varchar(50) NOT NULL COMMENT '分类名称;限长20个汉字',
  `description` varchar(50) COMMENT '对code进行详细的补充说明',
  `fk_pro_classify_id` int NOT NULL COMMENT '商品一级分类pro_classify表主键id',
  `fk_pro_classify_code` varchar(10) NOT NULL COMMENT '商品一级分类pro_classify表的code',
  `fk_shp_shop_id` int NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `type` varchar(10) COMMENT '类型;0:系统自带;1:用户自建',
  `state` int NOT NULL COMMENT '状态;-1:已删除;0:未使用;1:使用中',
  `sort` int NOT NULL COMMENT '序号排序',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del` varchar(10) COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商品二级分类表' AUTO_INCREMENT = 10000;

DROP TABLE
IF EXISTS shp_share_total;
CREATE TABLE `shp_share_total` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `fk_shp_shop_id` int NOT NULL COMMENT '商铺表shp_shop主键id',
  `fk_shp_share_type_code` varchar(50) NOT NULL COMMENT '商铺添加时长类型表shp_share_type类型code',
  `fk_shp_user_id` int NOT NULL COMMENT '商铺用户表shp_user主键id',
  `total_count` int COMMENT '操作记录次数',
  `total_hours` decimal(12,2) COMMENT '当日操作累计时长',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int COMMENT '添加用户_管理员id',
  `update_admin` int COMMENT '修改用户_管理员id',
  `versions` int COMMENT '版本号;用于更新时对比操作;',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商铺分享进度时长累计表' AUTO_INCREMENT = 10000;


DROP TABLE
IF EXISTS shp_share_type;
CREATE TABLE `shp_share_type` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `code` varchar(50) COMMENT '时长规则类型 1 分享同行价获得时常',
  `hours` decimal(12,2) COMMENT '类型基础时长(累计时长=hours*操作记录次数)',
  `add_num` int COMMENT '可添加的次数',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int COMMENT '添加用户_管理员id',
  `update_admin` int COMMENT '修改用户_管理员id',
  `versions` int COMMENT '版本号;用于更新时对比操作;',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商铺添加时长类型表' AUTO_INCREMENT = 10000;



DROP TABLE
IF EXISTS shp_alter_history;
CREATE TABLE `shp_alter_history` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_shp_shop_id` int DEFAULT NULL COMMENT '商铺表shp_shop表主键id',
  `code` varchar(10) COMMENT '商铺添加会员时间类型；0：管理员添加；1：分享添加',
  `hours` decimal(12,2) COMMENT '会员添加时间（以小时为单位）',
  `insert_time` timestamp NOT NULL COMMENT '插入时间',
  `update_time` timestamp NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `insert_admin` int COMMENT '添加用户_管理员id',
  `update_admin` int COMMENT '修改用户_管理员id',
  `remark` varchar(255) COMMENT '备注',
  PRIMARY KEY (`id`)
) COMMENT = '商铺会员变动表' AUTO_INCREMENT = 10000;




#枚举表添加服饰分类
INSERT INTO sys_enum (code,name,description,type)VALUES('FS','服饰','服饰','pro_classify');

#店铺添加服饰分类
INSERT INTO pro_classify (code,name,description,fk_shp_shop_id,type,insert_admin)
SELECT 'FS','服饰','服饰',f.fk_shp_shop_id,'0',f.fk_shp_shop_id FROM pro_classify f GROUP BY f.fk_shp_shop_id ;


#产品表;添加参数
ALTER TABLE `pro_product`
    ADD COLUMN `fk_pro_classify_sub_id`  int DEFAULT NULL COMMENT '二级分类pro_classify_sub主键id' AFTER `fk_pro_classify_code`;

#临时仓添加筛选价格类型字段
ALTER TABLE `pro_temp`
    ADD COLUMN `price_type` varchar(50) COMMENT '筛选价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）' AFTER `name`;




#修改枚举表其它分类的排序
UPDATE sys_enum SET sort=10 WHERE type='pro_classify' AND `name`='其它';

#修改枚举表服饰分类的排序
UPDATE sys_enum SET sort=7 WHERE type='pro_classify' AND `name`='服饰';

#修改分类表其它分类的排序
UPDATE pro_classify SET sort=10 WHERE `name`='其它';


#修改分类表服饰分类的排序
UPDATE pro_classify SET sort=7 WHERE `name`='服饰';