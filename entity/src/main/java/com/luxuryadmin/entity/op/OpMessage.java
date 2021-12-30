package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 消息中心记录
 *
 * @author monkey king
 * @date   2020/07/13 15:33:16
 */
@Data
public class OpMessage {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 类型 shop|店铺消息 friendBusiness|友商消息 system|系统消息，参考EnumOpMessageType类
     */
    private String type;

    /**
     * 子类型 参考EnumOpMessageSubType类
     */
    private String subType;

    /**
     * 标题缩略图url地址
     */
    private String titleImgUrl;

    /**
     * 点击消息跳转H5链接
     */
    private String clickH5Url;


    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否逻辑删除;0:不删除;1:逻辑删除;所有查询sql都要带上del=0这个条件;
     */
    private String del;

    /**
     * 推送平台 all|所有平台(默认) ios|苹果 android|安卓
     */
    private String pushPlatform;

    /**
     * 发送方式 right_now|立即发送（默认） timer|定时发送
     */
    private String sendType;

    /**
     * 预发送时间  send_type为right_now|立即发送时可为空
     */
    private Date preSendTime;

    /**
     * 真实发送时间，定时发送的消息该字段初始为Null,待发送后填入真实发送时间
     */
    private Date sendTime;

    /**
     * 是否推送所有用户 yes|是 no|否
     */
    private String isPushAllUser;

    /**
     * 推送状态 no_push|未推送 have_push|已推送
     */
    private String pushState;

    /**
     * 创建来源 code|代码调用接口创建(默认) cms|后台管理系统创建
     */
    private String createSource;

    /**
     * 跳转类型 nojump|不跳转(默认) h5|跳转H5页面 native|跳转原生页面
     */
    private String jumpType;

    /**
     * 跳转的原生页面
     */
    private String nativePage;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 推送用户Excel文件URL地址
     */
    private String pushUserExcelUrl;

}