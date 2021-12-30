package com.luxuryadmin.entity.shp;

import com.luxuryadmin.entity.sys.SysSalt;
import lombok.Data;

import java.util.Date;

/**
 * 店铺管理员
 *
 * @author monkey king
 * @date   2019/12/01 04:55:22
 */
@Data
public class ShpUser {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码;
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号;对称加密存储
     */
    private String phone;

    /**
     * -2:超级管理员(店长)；-1：管理员；0：普通人员；1：访客；
     */
    private String type;

    /**
     * 状态  0：禁用   1：正常
     */
    private String state;

    /**
     * 会员号
     */
    private Integer number;

    /**
     * 默认登录上一次店铺;0:不默认登录; 1:默认登录该店铺
     */
    private String defaultLogin;

    /**
     * 用户头像图片地址
     */
    private String headImgUrl;

    /**
     * shp_shop的id字段,主键id;默认直接登录店铺;0为没有关联店铺
     */
    private Integer fkShpShopId;

    /**
     * 运营模块--渠道的主键id
     */
    private Integer fkOpChannelId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

    private ShpUserDetail shpUserDetail;
    private SysSalt sysSalt;

    /**
     * 极光推送【Registration ID】
     */
    private String jgPushRegId;

    /**
     * app版本
     */
    private String appVersion;


    /**
     * 绑定微信登录 0:未绑定 | 1:已绑定
     */
    private Integer bindWeChat;

    /**
     * 绑定苹果登录 0:未绑定 | 1:已绑定
     */
    private Integer bindApple;
}