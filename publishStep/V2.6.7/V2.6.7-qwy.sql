DROP TABLE IF EXISTS `shp_perm_index`;
CREATE TABLE `shp_perm_index` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
  `parent_id` INT NOT NULL COMMENT '父权限ID，根目录为0',
  `name` VARCHAR (50) DEFAULT '' COMMENT '权限名称',
  `code` VARCHAR (50) DEFAULT '' COMMENT '权限编码(页面跳转时的判断值)',
  `http_url` VARCHAR (500) DEFAULT '' COMMENT 'webView的url',
  `url` VARCHAR (255) DEFAULT '' COMMENT '权限相对路径URL',
  `permission` VARCHAR (500) DEFAULT '' COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `new_state` VARCHAR (10) DEFAULT '0' COMMENT '0:普通 | 1:新上线',
  `cost_state` VARCHAR (10) DEFAULT '0' COMMENT '0:所有可以访问 | 1:仅限会员访问',
  `type` INT NOT NULL COMMENT '类型   0：一级   1：二级   2：三级',
  `display` INT DEFAULT '0' COMMENT '是否在app首页的全部功能里显示:  0:不显示; 1:显示',
  `is_private` VARCHAR(2) DEFAULT '0' COMMENT '敏感权限 0:否 | 1:是',
  `icon_url` VARCHAR (255) DEFAULT '' COMMENT '权限图片地址',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `color` VARCHAR (10) DEFAULT '' COMMENT '16进制的颜色编码; FFFFFF',
  `ios_version` INT NOT NULL DEFAULT '1' COMMENT 'ios线上版本号;权限版本号小于等于端上版本号都显示,向下兼容',
  `android_version` INT NOT NULL DEFAULT '1' COMMENT 'android线上版本号;权限版本号小于等于端上版本号都显示,向下兼容',
  `only_shop_id` VARCHAR(500) DEFAULT '' COMMENT '指定店铺id可见;多个用逗号隔开;',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
  `versions` INT NOT NULL DEFAULT '1' COMMENT '版本号;用于更新时对比操作;',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_fk_parent_id` (`name`, `parent_id`) USING BTREE COMMENT '同一目录下不允许重名',
  KEY `idx_type_display` (`type`, `display`),
  KEY `idx_versions_type` (`versions`, `type`)
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '员工权限索引表(新版权限)' ;

DROP TABLE IF EXISTS `shp_perm_user_ref`;
CREATE TABLE `shp_perm_user_ref` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `fk_shp_user_id` INT NOT NULL COMMENT '店铺--用户表ID',
  `fk_shp_perm_index_id` INT NOT NULL COMMENT '员工权限索引表ID',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shop_id_user_id_perm_id` (
    `fk_shp_shop_id`,
    `fk_shp_user_id`,
    `fk_shp_perm_index_id`
  ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '用户与权限对应关系(新版权限)' ;



DROP TABLE IF EXISTS `shp_perm_tpl`;
CREATE TABLE `shp_perm_tpl` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `fk_shp_shop_id` INT NOT NULL COMMENT 'shp_shop的id字段,主键id',
  `fk_shp_user_type_id` INT NOT NULL COMMENT '用户类型id',
  `fk_shp_perm_index_id` INT NOT NULL COMMENT '员工权限索引表ID',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_shopId_userTypeId_permId` (
    `fk_shp_shop_id`,
    `fk_shp_user_type_id`,
    `fk_shp_perm_index_id`
  ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '用户与权限对应关系--模板(新版权限)' ;


DROP TABLE IF EXISTS `shp_index`;
CREATE TABLE `shp_index` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联',
  `parent_id` INT NOT NULL COMMENT '父权限ID，根目录为0',
  `name` VARCHAR (50) DEFAULT '' COMMENT '权限名称',
  `fk_shp_perm_index_id` INT NOT NULL DEFAULT 0 COMMENT '权限id',
  `type` INT NOT NULL COMMENT '类型   0：一级   1：二级   2：三级',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
  `versions` INT NOT NULL DEFAULT '1' COMMENT '版本号;用于更新时对比操作;',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_fk_shp_perm_index_id` (`fk_shp_perm_index_id`)
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '首页功能列表' ;