#分享小程序
DROP TABLE IF EXISTS `op_employee`;
CREATE TABLE `op_employee` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fk_shp_user_id` INT NOT NULL COMMENT '工作人员id',
  `type` VARCHAR (10) NOT NULL DEFAULT '1' COMMENT '预留字段;0:技术部门;1:运营部;默认为 1',
  `union_switch` VARCHAR (2) NOT NULL DEFAULT '0' COMMENT '商家联盟分享开关: 0:关 | 1:开',
  `insert_time` TIMESTAMP NOT NULL COMMENT '插入时间',
  `update_time` TIMESTAMP NULL COMMENT '修改时间',
  `insert_admin` INT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT NULL COMMENT '修改用户_管理员id',
  `del` VARCHAR (1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;',
  `remark` VARCHAR (255) NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '工作人员表' ;

DROP TABLE IF EXISTS `op_union_agent`;
CREATE TABLE `op_union_agent` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fk_op_employee_id` INT NOT NULL COMMENT '工作人员表id',
  `fk_shp_user_id` INT NOT NULL COMMENT '代理人员id',
  `state` VARCHAR (2) NOT NULL DEFAULT '1' COMMENT '0:未绑定 | 1:已绑定',
  `agent_switch` VARCHAR (2) NOT NULL DEFAULT '0' COMMENT '商家联盟分享开关: 0:关 | 1:开',
  `valid_day` INT not NULL default '1' COMMENT '有效时间（天）',
  `insert_time` TIMESTAMP NOT NULL COMMENT '插入时间',
  `update_time` TIMESTAMP NULL COMMENT '修改时间',
  `insert_admin` INT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT NULL COMMENT '修改用户_管理员id',
  `del` VARCHAR (1) NOT NULL DEFAULT 0 COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;',
  `remark` VARCHAR (255) NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '代理人员表' ;
