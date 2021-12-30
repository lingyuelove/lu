package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 开店铺--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "开店铺--前端参数模型")
@Data
public class ParamAddShop extends ParamUpdateShopInfo {


    /**
     * 店铺昵称
     */
    @ApiModelProperty(name = "shopName", required = true,
            value = "店铺昵称;100个字符以内")
    @NotBlank(message = "店铺昵称不允许为空")
    @Length(max = 100, message = "店铺头像地址长度在100个字符以内")
    private String shopName;

    /**
     * 店铺省份
     */
    @ApiModelProperty(name = "province", required = true, value = "店铺省份;2~20个字符长度")
    @NotBlank(message = "店铺省份不允许为空")
    @Length(min = 2, max = 20, message = "省份长度在2~20个字符以内")
    private String province;

    /**
     * 店铺城市
     */
    @ApiModelProperty(name = "city", required = true, value = "店铺城市;2~20个字符长度")
    @NotBlank(message = "店铺城市不允许为空")
    @Length(min = 2, max = 20, message = "城市长度在2~20个字符以内")
    private String city;

    /**
     * 店铺地址
     */
    @ApiModelProperty(name = "address", required = true, value = "店铺地址;250个字符以内")
    @NotBlank(message = "店铺地址不允许为空")
    @Length(max = 250, message = "店铺地址长度在250个字符以内")
    private String address;

    /**
     * 校验值
     */
    @ApiModelProperty(name = "vid", required = false, value = "初始化注册店铺时下发的校验值")
    private String vid;


    /**
     * 封面图片地址(注册店铺时,不需要填写)
     */
    @ApiModelProperty(hidden = true)
    private String coverImgUrl;

    /**
     * 店铺头像地址(注册店铺时,不需要填写)
     */
    @ApiModelProperty(hidden = true)
    private String headImgUrl;

}
