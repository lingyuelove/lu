package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 接受前台传来的Banner对象
 */
@Data
@ApiModel(description = "Banner管理")
public class ParamOpBanner {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * banner标题
     */
    @ApiModelProperty(name = "title", required = true, value = "banner标题")
    @NotNull(message="banner标题不能为空")
    private String title;

    /**
     * banner图片地址
     */
    @ApiModelProperty(name = "imgUrl", required = true, value = "banner图片地址")
    @NotNull(message="banner图片地址不能为空")
    private String imgUrl;

    /**
     * 发布渠道 ios|苹果 android|安卓 all|所有平台
     */
    @ApiModelProperty(name = "publishPlatform", required = true, value = "发布渠道 ios|苹果 android|安卓 all|所有平台",
            allowableValues="all,ios,android")
    @NotNull(message="发布渠道不能为空")
    private String publishPlatform;

    /**
     * 安卓支持最小版本
     */
    @ApiModelProperty(name = "androidMinVersion", required = false, value = "安卓支持最小版本")
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "安卓最小版本号格式错误")
    private String androidMinVersion;

    /**
     * 安卓支持最大版本
     */
    @ApiModelProperty(name = "androidMaxVersion", required = false, value = "安卓支持最大版本")
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "安卓最大版本号格式错误")
    private String androidMaxVersion;

    /**
     * ios支持最小版本
     */
    @ApiModelProperty(name = "iosMinVersion", required = false, value = "ios支持最小版本")
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "ios最小版本号格式错误")
    private String iosMinVersion;

    /**
     * ios支持最大版本
     */
    @ApiModelProperty(name = "iosMaxVersion", required = false, value = "ios支持最大版本")
    @Pattern(regexp = "^([1-9]\\d|[1-9])(\\.([1-9]\\d|\\d)){2}$", message = "ios最大版本号格式错误")
    private String iosMaxVersion;

    /**
     * banner位置 indexCarousel|首页轮播 indexPopupWindow|首页弹窗 myCarousel|我的页面轮播
     */
    @ApiModelProperty(name = "pos", required = true, value = "banner位置")
    @NotNull(message="banner位置不能为空")
    private String pos;

    /**
     * 跳转类型 noJump|不跳转 h5|跳转到h5页面 native|跳转到原生页面
     */
    @ApiModelProperty(name = "jumpType", required = true, value = "跳转类型 noJump|不跳转 h5|跳转到h5页面 " +
            "native|跳转到原生页面 externalPage|外部App", allowableValues="noJump,h5,native,externalPage")
    @NotNull(message="跳转类型不能为空")
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
    @ApiModelProperty(name = "sortNum", required = true, value = "排序号,数字越大的排在前面")
    @NotNull(message="排序编号不能为空")
    private Integer sortNum;

    /**
     * Banner状态 on|启用 off|停用
     */
    @ApiModelProperty(name = "state", required = true, value = "Banner状态 on|启用 off|停用",allowableValues="on,off")
    @NotNull(message="Banner状态不能为空")
    private String state;

    /**
     * 显示开始时间
     */
    @ApiModelProperty(name = "showStartTime", required = true, value = "显示开始时间")
    @NotNull(message="显示开始时间不能为空")
    private Date showStartTime;

    /**
     * 显示结束时间
     */
    @ApiModelProperty(name = "showEndTime", required = true, value = "显示结束时间")
    @NotNull(message="显示结束时间不能为空")
    private Date showEndTime;

}
