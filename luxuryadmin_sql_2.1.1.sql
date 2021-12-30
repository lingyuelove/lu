
-- 用户店铺关系表;
ALTER TABLE `luxury211`.`shp_user_shop_ref`
  ADD COLUMN `fk_shp_user_type_id` INT DEFAULT 0  NULL   COMMENT '用户类型表的id' AFTER `name`;


--更新所有code值变成id值
UPDATE shp_user_shop_ref ref SET ref.fk_shp_user_type_id = IFNULL((SELECT id FROM shp_user_type WHERE fk_shp_shop_id = ref.`fk_shp_shop_id` AND CODE = ref.`fk_shp_user_type_id`),ref.`fk_shp_user_type_id`);


-- 用户类型, 插入经营者的通用模板
insert into `shp_user_type` (
  `id`,
  `code`,
  `name`,
  `description`,
  `fk_shp_shop_id`,
  `type`,
  `state`,
  `sort`,
  `insert_time`,
  `update_time`,
  `insert_admin`,
  `update_admin`,
  `versions`,
  `del`,
  `remark`
)
values
  (
    '-9',
    '-9',
    '经营者',
    '经营者',
    '-9',
    '2',
    '1',
    '-9',
    now(),
    null,
    null,
    null,
    1,
    '0',
    '系统创建,不允许删除,所有店铺关联'
  ) ;


-- 修改用户类型表; 去掉code字段
ALTER TABLE `shp_user_type`
  ADD  UNIQUE INDEX `uk_name_shp_shop_id` (`fk_shp_shop_id`, `name`);


-- 枚举类 唯一索引
ALTER TABLE `sys_enum`
  ADD  UNIQUE INDEX `uk_code_name_type` (`code`, `name`, `type`);

-- 把"职员" 改成 "员工"
update shp_user_type set name = '员工' where name = '职员';

-- 更新boss与店铺的关系;改变域名;
http://localhost:8080/sys/restartBossShpUserShopRef?token=15112304365