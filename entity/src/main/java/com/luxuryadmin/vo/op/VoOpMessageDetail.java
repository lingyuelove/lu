package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-17 17:57
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpMessageDetail {
    /**
     * 消息ID
     */
    @ApiModelProperty(name = "id", required = false, value = "记录编号")
    private Long id;

    /**
     * 消息标题
     */
    @ApiModelProperty(name = "title", required = false, value = "消息标题")
    private String title;

    /**
     * 消息内容
     */
    @ApiModelProperty(name = "content", required = false, value = "消息内容")
    private String content;

    /**
     * 消息图标
     */
    @ApiModelProperty(name = "titleImgUrl", required = false, value = "消息图标")
    private String titleImgUrl;

    /**
     * 消息点击状态
     */
    @ApiModelProperty(name = "clickState", required = false, value = "消息点击状态")
    private String clickState;

    /**
     * 消息新增时间
     */
    @ApiModelProperty(name = "insertTime", required = false, value = "消息新增时间")
    private Date insertTime;

    /**
     * 消息类型
     */
    @ApiModelProperty(name = "type", required = false, value = "消息类型")
    private String type;

    /**
     * 跳转类型 nojump|不跳转(默认) h5|跳转H5页面 native|跳转原生页面
     */
    @ApiModelProperty(name = "jumpType", required = false, value = "跳转类型 nojump|不跳转(默认) h5|跳转H5页面 " +
            "native|跳转原生页面 externalPage|外部App")
    private String jumpType;

    /**
     * 点击消息跳转H5链接
     */
    @ApiModelProperty(name = "clickH5Url", required = false, value = "点击消息跳转H5链接")
    private String clickH5Url;

    /**
     * 跳转的原生页面 orderDetail|订单详情
     */
    @ApiModelProperty(name = "nativePage", required = false, value = "跳转的原生页面 orderDetail|订单详情")
    private String nativePage;

    /**
     * 跳转的原生页面 orderDetail|订单详情
     */
    @ApiModelProperty(name = "extraParam", required = false, value = "消息参数")
    private String extraParam;


}
