package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.api.ParamWxUserPhone;
import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareSeeUserAdd
 * @Author: ZhangSai
 * Date: 2021/7/6 14:41
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "分享用户添加参数")
@Data
public class ParamShareSeeUserAdd extends ParamBasic {


    @ApiModelProperty(value = "code", name = "code", required = true)
    private String code;

    /**
     * 分享批次
     */
    @ApiModelProperty(value = "分享批次", required = true)
    private String shareBatch;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatarUrl;
    /**
     * 性别  0：未知、1：男、2：女
     */
    @ApiModelProperty(value = "性别  0：未知、1：男、2：女")
    private String gender;

    /**
     * 微信的openId
     */
    @ApiModelProperty(value = "微信的openId")
    private String openId;

    /**
     * 微信的openId
     */
    @ApiModelProperty(value = "微信的unionId")
    private String unionId;

    /**
     * 0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     */
    @ApiModelProperty(value = "分享类型", hidden = true)
    private String type;

    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty(value = "userPhone", name = "userPhone", required = false)
    private String userPhone;

    /**
     * 访问ip
     */
    @ApiModelProperty(hidden = true)
    private String ip;
}
