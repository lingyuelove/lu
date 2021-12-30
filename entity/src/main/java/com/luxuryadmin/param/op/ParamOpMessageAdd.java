package com.luxuryadmin.param.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-17 15:15
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamOpMessageAdd {

    /**
     * 消息标题
     */
    @ApiModelProperty(name = "title", required = true, value = "消息标题",example="奢当家测试消息标题")
    @NotNull(message="消息标题不能为空")
    private String title;

    /**
     * 消息内容
     */
    @ApiModelProperty(name = "content", required = true, value = "消息内容",example="奢当家测试消息内容")
    @NotNull(message="消息内容不能为空")
    private String content;

    /**
     * 店铺ID，【消息类型:msgType】为【系统消息:system】时【店铺ID】可为空，其它条件下必填。
     */
    @ApiModelProperty(name = "shopId", required = false, value = "店铺ID</br>【消息类型:msgType】为【系统消息:system】时【店铺ID】可为空，其它条件下必填")
    private Integer shopId;

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "userId", required = true, value = "用户ID")
    @NotNull(message="用户注册手机号不能为空")
    private String phone;

    /**
     * 消息类型
     * shop|店铺消息 friendBusiness|友商消息 system|系统消息，参考EnumOpMessageType类'
     */
    @ApiModelProperty(name = "msgType", required = true, allowableValues="shop,friendBusiness,system,other",
            value = "消息类型</br>shop|店铺消息</br> friendBusiness|友商消息</br> system|系统消息")
    @NotNull(message="消息类型不能为空")
    private String msgType;

}
