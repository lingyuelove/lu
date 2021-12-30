package com.luxuryadmin.vo.shp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.entity.shp.ShpUserDetail;
import com.luxuryadmin.entity.sys.SysSalt;
import lombok.Data;

import java.util.Date;

/**
 * @author Administrator
 * @Classname VoShpUser
 * @Description TODO
 * @Date 2020/6/22 18:06
 * @Created by Administrator
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpUser {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "姓名", index = 0)
    private String username;

    /**
     * 密码;
     */
    private String password;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称", index = 1)
    private String nickname;

    /**
     * 手机号;对称加密存储
     */
    @ExcelProperty(value = "手机号", index = 2)
    private String phone;

    /**
     * -2:超级管理员(店长)；-1：管理员；0：普通人员；1：访客；
     */
    @ExcelProperty(value = "员工类型", index = 3)
    private String type;

    /**
     * 状态  0：禁用   1：正常
     */
    @ExcelProperty(value = "账号状态", index = 4)
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
    @ExcelProperty(value = "注册时间", index = 5)
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
     * 修改人的昵称
     */
    private String updateAdminNickname;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;

    /**
     * 邀请人编码
     */
    private String inviteUserId;

    /**
     * 邀请人编码
     */
    private String inviteUserNumber;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户身份id
     */
    private String detailId;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 店铺编号
     */
    private String shopId;

    /**
     * 店铺编号
     */
    private String shopNumber;

    /**
     * 店铺名字
     */
    private String shopName;

    /**
     * 角色名称，逗号隔开
     */
    private String roleName;

    private ShpUserDetail shpUserDetail;

    private SysSalt sysSalt;

}
