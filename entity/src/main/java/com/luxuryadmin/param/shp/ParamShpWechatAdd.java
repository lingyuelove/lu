package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 店铺微信添加对象类
 * @author sanjin
 * @Date 2020/08/31 16:08
 */
@Data
public class ParamShpWechatAdd extends ParamToken {

    /**
     * 联系人姓名
     */
    @ApiModelProperty(name = "contactPersonName", required = true, value = "联系人姓名;")
    @NotBlank(message = "联系人姓名不允许为空")
    private String contactPersonName;

    /**
     * 联系人微信号
     */
    @ApiModelProperty(name = "contactPersonWechat", required = true, value = "联系人微信号;")
    @NotBlank(message = "联系人微信号不允许为空")
    private String contactPersonWechat;
    /**
     * 负责内容
     */
    @ApiModelProperty(name = "contactResponsible", required = false, value = "负责内容;")
    private String contactResponsible;

    @ApiModelProperty(name = "shopId", hidden = true, value = "店铺id;")
    private Integer shopId;

    @ApiModelProperty(name = "userId", hidden = true, value = "用户id;")
    private Integer userId;
}
