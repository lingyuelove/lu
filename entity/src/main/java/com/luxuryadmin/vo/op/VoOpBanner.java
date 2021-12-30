package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Banner前台展示VO类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpBanner {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * banner标题
     */
    @ApiModelProperty(name = "title", required = false, value = "banner标题")
    private String title;

    /**
     * banner图片地址
     */
    @ApiModelProperty(name = "imgUrl", required = false, value = "banner图片地址")
    private String imgUrl;

    /**
     * 发布渠道 ios|苹果 android|安卓 all|所有平台
     */
    @ApiModelProperty(name = "publishPlatform", required = false, value = "发布渠道 ios|苹果 android|安卓 all|所有平台",
            allowableValues="all,ios,android")
    private String publishPlatform;

    /**
     * 安卓支持最小版本
     */
    @ApiModelProperty(name = "androidMinVersion", required = false, value = "安卓支持最小版本")
    private String androidMinVersion;

    /**
     * 安卓支持最大版本
     */
    @ApiModelProperty(name = "androidMaxVersion", required = false, value = "安卓支持最大版本")
    private String androidMaxVersion;

    /**
     * ios支持最小版本
     */
    @ApiModelProperty(name = "iosMinVersion", required = false, value = "ios支持最小版本")
    private String iosMinVersion;

    /**
     * ios支持最大版本
     */
    @ApiModelProperty(name = "iosMaxVersion", required = false, value = "ios支持最大版本")
    private String iosMaxVersion;

    /**
     * banner位置 indexCarousel|首页轮播 indexPopupWindow|首页弹窗 myCarousel|我的页面轮播
     */
    @ApiModelProperty(name = "pos", required = false, value = "banner位置")
    private String pos;

    /**
     * 跳转类型 noJump|不跳转 h5|跳转到h5页面 native|跳转到原生页面
     */
    @ApiModelProperty(name = "jumpType", required = false, value = "跳转类型 noJump|不跳转 h5|跳转到h5页面 " +
            "native|跳转到原生页面 externalPage|外部APP shopUnion|商家联盟",
            allowableValues="noJump,h5,native,externalPage,shopUnion")
    private String jumpType;

    /**
     * 跳转H5页面URL
     */
    @ApiModelProperty(name = "jumpH5Url", required = false, value = "跳转H5页面URL")
    private String jumpH5Url;

    /**
     * 跳转原生地址
     */
    @ApiModelProperty(name = "jumpNativePage", required = false, value = "跳转原生地址")
    private String jumpNativePage;

    /**
     * 排序号,数字越大的排在前面
     */
    @ApiModelProperty(name = "sortNum", required = false, value = "排序号,数字越大的排在前面")
    private Integer sortNum;

    /**
     * Banner状态 on|启用 off|停用
     */
    @ApiModelProperty(name = "state", required = false, value = "Banner状态 on|启用 off|停用",allowableValues="on,off")
    private String state;

    /**
     * 显示开始时间
     */
    @ApiModelProperty(name = "showStartTime", required = false, value = "显示开始时间")
    private Date showStartTime;

    /**
     * 显示结束时间
     */
    @ApiModelProperty(name = "showEndTime", required = false, value = "显示结束时间")
    private Date showEndTime;

    /**
     * 显示结束时间
     */
    @ApiModelProperty(name = "insertTime", required = false, value = "创建时间")
    private Date insertTime;

    /**
     * 更新者名称
     */
    @ApiModelProperty(name = "updateAdminName", required = false, value = "更新者名称")
    private String updateAdminName;

    /**
     * 是否弹窗时间戳
     */
    @ApiModelProperty(name = "timestamp", required = false, value = "是否弹窗时间戳")
    private String timestamp;
}
