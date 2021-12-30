CREATE TABLE `mp_banner` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片路径',
  `banner_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片名称',
  `skip_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转类型',
  `skip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '跳转地址',
  `app_id` varbinary(64) DEFAULT NULL,
  `sore` int DEFAULT '0' COMMENT '排序',
  `state` int DEFAULT '1' COMMENT '是否展示：0不展示。1展示',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 COMMENT='banner表';

CREATE TABLE `mp_pay_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `fk_mp_user_id` int NOT NULL COMMENT '用户id',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '第三方支付订单号',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台订单号',
  `total_money` decimal(15,0) NOT NULL DEFAULT '0' COMMENT '总金额(分)',
  `discount_money` decimal(15,0) NOT NULL DEFAULT '0' COMMENT '折扣金额(分)',
  `real_money` decimal(15,0) NOT NULL DEFAULT '0' COMMENT '实际支付金额(分)',
  `vip_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员类型：mpvip：小程序会员。sdjvip',
  `order_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单类型,vip:会员费,source:资源包',
  `state` int NOT NULL DEFAULT '10' COMMENT '-99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功',
  `pay_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付通道; weixin、alipay、other',
  `trade_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易类型:公众号支付,扫码支付,app支付,h5支付,刷脸支付,付款码支付',
  `create_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '创建类型; 0:用户自动创建,1:后台管理员手动创建',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '交易结束时间',
  `insert_admin` int DEFAULT NULL COMMENT '创建人用户id',
  `update_admin` int DEFAULT NULL COMMENT '修改人用户id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='支付订单';


CREATE TABLE `mp_union_verify` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fk_mp_user_id` int NOT NULL COMMENT '用户id',
  `fk_pro_classify_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类目 腕表 箱包 腕表和箱包',
  `license_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '营业执照路径',
  `valid_img_url` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '店铺认证图片',
  `stock_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '股东认证图片',
  `empower_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '店铺授权图片',
  `other_user_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其他人员认证身份证',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 0 未审核 1已通过 2未通过',
  `shop_user_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '店铺身份 法人 股东 都不是',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `insert_admin` int NOT NULL COMMENT '新增人',
  `update_admin` int DEFAULT NULL COMMENT '更新人',
  `del` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除 0 未删除 1已删除',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `fail_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审核失败原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='商家联盟审核表';

CREATE TABLE `mp_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `wx_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信名',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '昵称',
  `state` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '状态  0：禁用   1：正常',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '头像',
  `master_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '主体类型',
  `vip_type` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员类型:sdj,mp',
  `is_member` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'no' COMMENT '是否是会员 yes|是会员 no|不是会员',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '',
  `member_state` int DEFAULT '1' COMMENT '0:非会员; 1:体验会员;2:正式会员;3:靓号会员',
  `try_start_time` timestamp NULL DEFAULT NULL COMMENT '试用开始时间',
  `try_end_time` timestamp NULL DEFAULT NULL COMMENT '试用结束时间',
  `pay_start_time` timestamp NULL DEFAULT NULL COMMENT '付费使用开始时间',
  `pay_end_time` timestamp NULL DEFAULT NULL COMMENT '付费使用结束时间',
  `login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录ip',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '省',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '市',
  `district` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '区',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='小程序用户表';

CREATE TABLE `mp_user_invite` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_invite_mp_user_id` int NOT NULL COMMENT '邀请人id',
  `fk_be_invite_mp_user_id` int NOT NULL COMMENT '被邀请用户id',
  `state` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '邀请状态：0:禁用；1:正常; ',
  `reward_state` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '10' COMMENT '奖励状态：10:无奖励；20:已发放全部奖励',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='邀请记录';

CREATE TABLE `mp_visitor_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_be_mp_user_id` int DEFAULT '0' COMMENT '被访问人id',
  `fk_mp_user_id` int NOT NULL COMMENT '访客用户id',
  `master_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主体类型。什么小程序',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='访客记录';


CREATE TABLE `mp_add_vip_time` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fk_mp_user_id` int NOT NULL COMMENT '用户id',
  `fk_be_mp_user_id` int NOT NULL COMMENT '被邀请人id',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `final_end_time` timestamp NULL DEFAULT NULL COMMENT '最终到期时间',
  `add_day` int NOT NULL COMMENT '增加天数',
  `insert_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `insert_admin` int DEFAULT NULL COMMENT '添加用户_管理员id',
  `update_admin` int DEFAULT NULL COMMENT '修改用户_管理员id',
  `del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000  COMMENT='追加时长表';


INSERT INTO `mp_banner` ( `url`, `banner_name`, `skip_type`, `skip_address`, `app_id`, `sore`, `state`, `insert_time`, `update_time`, `insert_admin`, `update_admin`, `del` )
VALUES
	( '/banner/lQLPDhrwCigvXanNAXHNBFOw-ZwnMcaVDrcBtoYXewC8AA_1107_369.png', 'ceshi', 'h5', 'https://developers.weixin.qq.com/miniprogram/dev/component/navigator.html', NULL, 0, 1, NOW(), NULL, NULL, NULL, '0' );
INSERT INTO `mp_banner` ( `url`, `banner_name`, `skip_type`, `skip_address`, `app_id`, `sore`, `state`, `insert_time`, `update_time`, `insert_admin`, `update_admin`, `del` )
VALUES
	( '/product/picture/12265/20211201/16340/1638325481774.png', NULL, 'mp', 'exhibition/index/index?organizationId=&shopNumber=', 0x777830633938623833373730393865343833, 1, 1, NOW(), NULL, NULL, NULL, '0' );
INSERT INTO `mp_banner` ( `url`, `banner_name`, `skip_type`, `skip_address`, `app_id`, `sore`, `state`, `insert_time`, `update_time`, `insert_admin`, `update_admin`, `del` )
VALUES
	( '/product/picture/12207/20211201/12657/1638329343883.png', NULL, 'native', '/pages/user-rights/user-rights', NULL, 2, 1, NOW(), NULL, NULL, NULL, '0' );