
#用户角色关联表添加数据 fk_sys_user_id换成15112304365用户的id
INSERT INTO `sys_user_role_ref` (`id`, `fk_sys_user_id`, `fk_sys_role_id`, `insert_time`, `update_time`, `insert_admin`, `update_admin`, `versions`, `del`, `remark`) VALUES (10001, 10002, 10001, '2021-10-27 17:00:54', NULL, NULL, NULL, 1, '0', '');

#同步sys_role、sys_role_permission_ref、sys_permission 三个表的数据
