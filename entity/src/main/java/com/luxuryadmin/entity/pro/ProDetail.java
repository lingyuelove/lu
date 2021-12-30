package com.luxuryadmin.entity.pro;

import lombok.Data;

import java.util.Date;

/**
 * 商品详情表;该表和(pro_product)一对一关系
 *
 * @author monkey king
 * @date 2019/12/01 04:55:08
 */
@Data
public class ProDetail {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * pro_product的id字段;主键id
     */
    private Integer fkProProductId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 商品附件
     */
    private String accessory;

    /**
     * 商品来源
     */
    private String source;

    /**
     * 委托方--姓名
     */
    private String customerName;

    /**
     * 委托方--手机号码
     */
    private String customerPhone;

    /**
     * 委托方--备注
     */
    private String customerRemark;


    /**
     * 保卡;0:没有; 1:有;
     */
    private String repairCard;

    /**
     * 保卡有效时间
     */
    private String repairCardTime;

    /**
     * 商品独立(唯一)编码,用户自填;
     */
    private String uniqueCode;

    /**
     * 保卡图片地址或者独立编码图片地址
     */
    private String cardCodeImg;

    /**
     * 商品图片地址,多个用英文分号隔开
     */
    private String productImg;

    /**
     * 视频地址
     */
    private String videoUrl;

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

    /**
     * 备注图片地址;
     */
    private String remarkImgUrl;

    /**
     * 商品委托人信息
     */
    private String customerInfo;

    /**
     * 商品货号; 由商品分类前缀+商品id组合而成; <br/>
     * eg:WD10001
     */
    private String autoNumber;
}