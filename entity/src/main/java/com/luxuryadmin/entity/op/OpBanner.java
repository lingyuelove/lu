package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author sanjin145
 * @date   2020/08/27 14:14:28
 */
@Data
public class OpBanner {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * banner标题
     */
    private String title;

    /**
     * banner图片地址
     */
    private String imgUrl;

    /**
     * 发布渠道 ios|苹果 android|安卓 all|所有平台
     */
    private String publishPlatform;

    /**
     * 安卓支持最小版本
     */
    private String androidMinVersion;

    /**
     * 安卓支持最大版本
     */
    private String androidMaxVersion;

    /**
     * ios支持最小版本
     */
    private String iosMinVersion;

    /**
     * ios支持最大版本
     */
    private String iosMaxVersion;

    /**
     * banner位置 indexCarousel|首页轮播 indexPopupWindow|首页弹窗 myCarousel|我的页面轮播 shopUnion|商家联盟
     */
    private String pos;

    /**
     * 跳转类型 noJump|不跳转 h5|跳转到h5页面 native|跳转到原生页面
     */
    private String jumpType;

    /**
     * 跳转H5页面URL
     */
    private String jumpH5Url;

    /**
     * 跳转原生地址
     */
    private String jumpNativePage;

    /**
     * 排序号,数字越大的排在前面
     */
    private Integer sortNum;

    /**
     * Banner状态 on|启用 off|停用
     */
    private String state;

    /**
     * 显示开始时间
     */
    private Date showStartTime;

    /**
     * 显示结束时间
     */
    private Date showEndTime;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人ID
     */
    private Integer insertAdmin;

    /**
     * 更新人ID
     */
    private Integer updateAdmin;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;
}