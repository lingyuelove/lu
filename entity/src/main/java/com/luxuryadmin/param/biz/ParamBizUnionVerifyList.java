package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: tqm
 * Date: 2021-11-04
 */
@ApiModel(description = "获取商家联盟卖家审核列表")
@Data
public class ParamBizUnionVerifyList extends ParamToken {


    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;
    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String number;


    @ApiModelProperty(value = "手机号", name = "contact")
    private String contact;

    @ApiModelProperty(value = "经营类目", name = "fkProClassifyCode")
    private String fkProClassifyCode;

    @ApiModelProperty(value = "审核状态", name = "state")
    private String state;

    @ApiModelProperty(value = "页数", name = "state")
    private Integer pageSize;
}
