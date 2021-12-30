package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: taoqimin
 * Date: 2021-11-04
 */
@Data
@ApiModel(value = "商家联盟审核 --修改", description = "商家联盟审核 --修改")
public class ParamBizUnionVerifyUpdate {

    @ApiModelProperty(value = "审核id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "经营类目")
    private String classifyCode;

    @ApiModelProperty(value = "执照")
    @NotBlank(message = "营业执照不能为空")
    private String licenseImgUrl;

    @ApiModelProperty(value = "申请身份")
    private String shopUserType;

    @ApiModelProperty(value = "身份证")
    private String validImgUrl;

    @ApiModelProperty(value = "企查查股东截图")
    private String stockImgUrl;

    @ApiModelProperty(value = "店铺授权图片")
    private String empowerImgUrl;
    /**
     * 其他人员认证身份证
     */
    @ApiModelProperty(value = "其他人员认证身份证")
    private String otherUserImgUrl;
    @ApiModelProperty(value = "状态 0 未审核 1已通过 2未通过")
    private String state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "失败原因", name = "failRemark")
    private String failRemark;
}
