package com.luxuryadmin.param.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamUnionVerifyAdd
 * @Author: ZhangSai
 * Date: 2021/11/3 18:00
 */
@Data
@ApiModel(description = "商家联盟认证信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamUnionVerifyAdd extends ParamToken {

    @ApiModelProperty(value = "店铺id",hidden = false)
    private Integer shopId;
    @ApiModelProperty(value = "用户id",hidden = true)
    private Integer userId;
    @ApiModelProperty(value = "类目 腕表 箱包 腕表和箱包", required = false, name = "classifyCode")
    private String classifyCode;

    /**
     * 营业执照路径
     */
    @ApiModelProperty(value = "营业执照路径", required = true, name = "licenseImgUrl")
    private String licenseImgUrl;

    /**
     * 店铺认证图片
     */
    @ApiModelProperty(value = "店铺认证图片", required = true, name = "validImgUrl")
    private String validImgUrl;
    /**
     * 其他人员认证身份证
     */
    @ApiModelProperty(value = "其他人员认证身份证")
    private String otherUserImgUrl;
    /**
     * 股东认证图片
     */
    @ApiModelProperty(value = "股东认证图片")
    private String stockImgUrl;
    /**
     * 店铺授权图片
     */
    @ApiModelProperty(value = "店铺授权图片")
    private String empowerImgUrl;

    @ApiModelProperty(value = "店铺身份 0法人 1股东 2都不是", required = true, name = "shopUserType")
    @NotBlank(message = "店铺身份不允许为空!")
    private String shopUserType;
}
