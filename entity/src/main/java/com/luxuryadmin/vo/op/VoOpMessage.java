package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 广告管理-消息推送VO
 *
 * @author sanjin
 * @date   2020/07/22 17:19:30
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpMessage {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(name = "id", required = true, value = "记录编号")
    private Long id;

    /**
     * 消息标题
     */
    @ApiModelProperty(name = "title", required = true, value = "消息标题")
    private String title;

    /**
     * 消息内容
     */
    @ApiModelProperty(name = "content", required = true, value = "消息内容")
    private String content;

    /**
     * 推送平台 all|所有平台(默认) ios|苹果 android|安卓
     */
    @ApiModelProperty(name = "pushPlatform", required = true, value = "推送平台 all|所有平台(默认) ios|苹果 android|安卓",allowableValues = "all,ios,android")
    private String pushPlatform;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", required = true, value = "消息类型 system|系统消息 other|其它消息",allowableValues = "system,other")
    private String type;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "insertTime", required = true, value = "创建时间")
    private Date insertTime;

    /**
     * 发送方式 right_now|立即发送（默认） timer|定时发送
     */
    @ApiModelProperty(name = "sendType", required = true, value = "发送方式 right_now|立即发送（默认）timer|定时发送",allowableValues = "right_now,timer")
    private String sendType;

    /**
     * 预发送时间  send_type为right_now|立即发送时可为空
     */
    @ApiModelProperty(name = "preSendTime", required = false,
            value = "预发送时间  swagger中时间参数格式:  【Sat, 22 July 2020 16:36:48 GMT】 代码中和原先一样")
    private Date preSendTime;

    /**
     * 预发送时间  send_type为right_now|立即发送时可为空
     */
    @ApiModelProperty(name = "preSendTime", required = false,
            value = "发送时间")
    private Date sendTime;

    /**
     * 跳转类型 nojump|不跳转(默认) h5|跳转H5页面 native|跳转原生页面
     */
    @ApiModelProperty(name = "jumpType", required = true, value = "跳转类型 nojump|不跳转(默认) h5|跳转H5页面 " +
            "native|跳转原生页面 externalPage|外部APP",
            allowableValues = "nojump,h5,native,externalPage")
    private String jumpType;

    /**
     * 点击消息跳转H5链接
     */
    @ApiModelProperty(name = "clickH5Url", required = false, value = "点击消息跳转H5链接")
    private String clickH5Url;

    /**
     * 跳转的原生页面
     */
    @ApiModelProperty(name = "nativePage", required = false, value = "跳转的原生页面",allowableValues = "orderDetail")
    private String nativePage;

    /**
     * 是否推送所有用户 yes|是 no|否
     */
    @ApiModelProperty(name = "isPushAllUser", required = true, value = "是否推送全部用户 先测试全部用户 暂时不考虑部分用户",allowableValues = "yes,no")
    private String isPushAllUser;

    /**
     * 推送状态 no_push|未推送 have_push|已推送
     */
    @ApiModelProperty(name = "pushState", required = false, value = "推送状态 no_push|未推送have_push|已推送",allowableValues = "no_push,have_push",hidden = true)
    private String pushState;

    /**
     * 创建来源 code|代码调用接口创建(默认) cms|后台管理系统创建
     */
    @ApiModelProperty(name = "createSource", required = false, value = "创建来源 code|代码调用接口创建(默认)cms|后台管理系统创建",allowableValues = "code,cms",hidden = true)
    private String createSource;

    /**
     * 跳转的原生页面
     */
    @ApiModelProperty(name = "pushUserExcelUrl", required = false, value = "推送用户Excel地址")
    private String pushUserExcelUrl;


}