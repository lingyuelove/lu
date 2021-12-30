# 增加分享有效时间
ALTER TABLE `pro_share`
  ADD COLUMN `end_time` TIMESTAMP NULL   COMMENT '结束时间;如果为空,则长期有效' AFTER `insert_time`;


ALTER TABLE `pro_share`
  ADD COLUMN `union_user_id` INT DEFAULT 0  NULL   COMMENT '商家联盟小程序关联的代理id(ShpUser.id)' AFTER `type`;



DROP TABLE IF EXISTS `op_upload_download`;
CREATE TABLE `op_upload_download` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;',
  `state` INT NOT NULL DEFAULT '10' COMMENT '-90:删除 | 10:进行中 | 20:已取消； | 30：已完成；',
  `name` VARCHAR (50) DEFAULT '' COMMENT '任务名称',
  `total_ms` INT DEFAULT 0 NULL COMMENT '任务总消耗毫秒数',
  `download_times` INT DEFAULT 0 COMMENT '下载次数',
  `type` VARCHAR (50) NOT NULL COMMENT '任务类型：in：导入，out：导出',
  `module` VARCHAR (50) NOT NULL COMMENT '模块；订单模块；商品模块；账单模块，其它模块',
  `fk_shp_shop_id` INT NOT NULL COMMENT '店铺id；shp_shop的id字段,主键id;',
  `fk_shp_user_id` INT NOT NULL COMMENT '下载用户；shp_user的id字段,主键id;',
  `start_time` TIMESTAMP NULL DEFAULT NULL COMMENT '下载内容选择得时间范围：开始时间',
  `end_time` TIMESTAMP NULL DEFAULT NULL COMMENT '下载内容选择得时间范围：结束时间',
  `url` VARCHAR (500) DEFAULT '' NULL COMMENT '下载地址全路径;http开头',
  `insert_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `finish_time` TIMESTAMP NULL DEFAULT NULL COMMENT '任务结束时间',
  `update_time` TIMESTAMP NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` INT DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` INT DEFAULT NULL COMMENT '修改用户_管理员id',
  `versions` INT NOT NULL DEFAULT 1 COMMENT '版本号;用于更新时对比操作;',
  `remark` VARCHAR (255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_state` (`state`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE,
  KEY `idx_module` (`module`) USING BTREE,
  KEY `idx_fk_shp_shop_id` (`fk_shp_shop_id`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 10000 COMMENT = '上传下载任务列表' ;


-- 添加锁单权限;
-- 查看全部锁单
"pro:check:allLockProduct"
