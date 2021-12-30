package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 店铺详情表;
 *
 * @author monkey king
 * @date 2019/12/01 04:55:22
 */
@Data
public class ShpDetail {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 店铺表的主键id
     */
    private Integer fkShpShopId;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 店长名称
     */
    private String shopkeeper;

    /**
     * 手机号
     */
    private String shopkeeperPhone;

    /**
     * 店铺图片;多张用英文分号隔开
     */
    private String moreImg;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 组织机构代码(税号)
     */
    private String companyTax;

    /**
     * 营业执照路径
     */
    private String licenseImgUrl;

    /**
     * 图片认证地址
     */
    private String validImgUrl;

    /**
     * 视频认证地址
     */
    private String validVideoUrl;

    /**
     * 中文存储: 未上传 | 部分上传 | 已上传
     */
    private String uploadState;

    /**
     * qq
     */
    private String qq;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

}