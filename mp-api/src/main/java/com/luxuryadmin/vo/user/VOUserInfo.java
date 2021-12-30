package com.luxuryadmin.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class VOUserInfo {

    @ApiModelProperty(value = "用户id", name = "id")
    private Integer id;

    /**
     * 微信名
     */
    @ApiModelProperty(value = "微信名")
    private String wxNickname;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 状态  0：禁用   1：正常
     */
    @ApiModelProperty(value = "状态  0：禁用   1：正常")
    private String state;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "会员类型:sdj,mp")
    private String vipType;
    /**
     * 主体类型
     */
    @ApiModelProperty(value = "主体类型")
    private String masterType;
    /**
     * 是否是会员 yes|是会员 no|不是会员
     */
    @ApiModelProperty(value = "是否是会员 yes|是会员 no|不是会员")
    private String isMember;
    /**
     * open_id
     */
    @ApiModelProperty(value = "open_id")
    private String openId;
    /**
     * 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     */
    @ApiModelProperty(value = "0:非会员; 1:体验会员;2:正式会员;3:靓号会员")
    private Integer memberState;
    /**
     * 试用开始时间
     */
    @ApiModelProperty(value = "试用开始时间")
    private Date tryStartTime;
    /**
     * 试用结束时间
     */
    @ApiModelProperty(value = "试用结束时间")
    private Date tryEndTime;
    /**
     * 付费使用开始时间
     */
    @ApiModelProperty(value = "付费使用开始时间")
    private Date payStartTime;
    /**
     * 付费使用结束时间
     */
    @ApiModelProperty(value = "付费使用结束时间")
    private Date payEndTime;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否展示配置文件信息,1：展示。0：不展示")
    private Integer showConfig;


    @ApiModelProperty(value = "云仓编号", required = true, name = "cloudWarehouse")
    @NotBlank(message = "云仓编号")
    private String cloudWarehouseNum;

    @ApiModelProperty(value = "店铺编号", required = true, name = "shopNum")
    @NotBlank(message = "店铺编号")
    private String shopNum;


    @ApiModelProperty(value = "首页地址", required = true, name = "homeUrl")
    private String homeUrl;

    @ApiModelProperty(value = "appId", required = true, name = "appId")
    private String appId;
}
