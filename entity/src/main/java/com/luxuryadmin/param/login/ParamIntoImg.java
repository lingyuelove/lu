package com.luxuryadmin.param.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 密码登录--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "图片导入")
@Data
public class ParamIntoImg {


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "用户名/手机号", name = "username", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "No.1 format error!")
    @NotBlank(message = "No.1 input not allowed empty!")
    private String username;


    /**
     * md5格式的密码
     */
    @ApiModelProperty(value = "密码", name = "password", required = true)
    @NotBlank(message = "No.2 input not allowed empty!")
    private String password;

    /**
     * 店铺经营者的手机号
     */
    @ApiModelProperty(value = "phone", name = "phone", required = true)
    @NotBlank(message = "No.3 input not allowed empty!")
    @Pattern(regexp = "^[0-9]{11}$", message = "No.3 format error!")
    private String phone;

    /**
     * 标签;
     */
    @ApiModelProperty(value = "微商相册标签; 格式包含@xx | 不包含@xxx", name = "tag", required = false)
    private String tag;


    /**
     * 筛选商品名称
     */
    @ApiModelProperty(value = "包含该关键字的名称才被导入", name = "filterName")
    private String filterName;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = true)
    @Pattern(regexp = "^[0-9]{4,9}$", message = "No.4 format error!")
    @NotBlank(message = "No.4 input not allowed empty!")
    private String shopNumber;

    @ApiModelProperty(value = "当前页", name = "page", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "No.5 format error!")
    private String page;

    @ApiModelProperty(value = "cookie", name = "cookie", required = false)
    private String cookie;

    @ApiModelProperty(value = "timestamp", name = "timestamp", required = true)
    @NotBlank(message = "timestamp not allowed empty!")
    private String timestamp;

    @ApiModelProperty(value = "token", name = "token", required = false)
    private String token;

    @ApiModelProperty(value = "token", name = "wareHouseId", required = false)
    private String wareHouseId;

    @ApiModelProperty(value = "0:未上架 | 1:已上架", name = "downOrUp", required = false)
    private String downOrUp;
}
