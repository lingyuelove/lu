package com.luxuryadmin.vo.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: taoqimin
 * Date: 2021-11-04
 */
@Data
@ApiModel(value="商品卖家审核列表返回", description="商品卖家审核列表返回")
public class VoBizUnionVerifyList {

    @ApiModelProperty(value = "审核id")
    private Integer id;
    @ApiModelProperty(value = "店铺id")
    private Integer shopId;
    @ApiModelProperty(value = "店铺名称")
    private String name;
    @ApiModelProperty(value = "店铺编号")
    private String number;

    @ApiModelProperty(value = "手机号")
    private String contact;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "经营类目")
    private String classifyCode;
    @ApiModelProperty(value = "经营类目")
    private String classifyCodeName;
    @ApiModelProperty(value = "执照")
    private String licenseImgUrl;

    @ApiModelProperty(value = "申请身份")
    private String shopUserType;

    @ApiModelProperty(value = "身份证")
    private String validImgUrl;

    @ApiModelProperty(value = "企查查股东截图")
    private String stockImgUrl;
    /**
     * 其他人员认证身份证
     */
    @ApiModelProperty(value = "其他人员认证身份证")
    private String otherUserImgUrl;
    @ApiModelProperty(value = "店铺授权图片")
    private String empowerImgUrl;

    @ApiModelProperty(value = "店铺注册时间")
    private String insertTime;

    @ApiModelProperty(value = "状态 0 未审核 1已通过 2未通过")
    private String state;
    @ApiModelProperty(value = "审核时间")
    private String examineTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "审核失败原因")
    private String failRemark;
}
