package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 更新店铺信息--前端参数模型
 *
 * @author monkey king
 * @Date 2020-05-31 23:28:04
 */
@ApiModel(description = "更新店铺信息--前端参数模型")
@Data
public class ParamUpdateShopInfo {

    /**
     * 店铺昵称
     */
    @ApiModelProperty(name = "shopName", required = true, value = "token")
    @Length(max = 50, message = "token在50个字符以内")
    private String token;

    /**
     * 店铺昵称
     */
    @ApiModelProperty(name = "shopName", required = false,
            value = "店铺昵称;长度不得超过100个字符")
    @Length(max = 100, message = "店铺昵称长度在100个字符以内")
    private String shopName;

    /**
     * 店铺省份
     */
    @ApiModelProperty(name = "province", required = false, value = "店铺省份;2~20个字符长度")
    @Length(min = 2, max = 20, message = "省份长度在2~20个字符以内")
    private String province;

    /**
     * 店铺城市
     */
    @ApiModelProperty(name = "city", required = false, value = "店铺城市;2~20个字符长度")
    @Length(min = 2, max = 20, message = "城市长度在2~20个字符以内")
    private String city;

    /**
     * 店铺地址
     */
    @ApiModelProperty(name = "address", required = false, value = "店铺地址;250个字符以内")
    @Length(max = 250, message = "店铺地址长度在250个字符以内")
    private String address;

    /**
     * 封面图片地址
     */
    @ApiModelProperty(name = "coverImgUrl", required = false, value = "封面图片地址;250个字符以内")
    @Length(max = 250, message = "封面图片地址长度在250个字符以内")
    private String coverImgUrl;

    /**
     * 店铺头像地址
     */
    @ApiModelProperty(name = "shopHeadImgUrl", required = false, value = "店铺头像地址;250个字符以内")
    @Length(max = 250, message = "店铺头像地址长度在250个字符以内")
    private String shopHeadImgUrl;

    /**
     * 封面图片地址
     */
    @ApiModelProperty(name = "miniProgramCoverImgUrl", required = false, value = "微信小程序封面图片地址;250个字符以内")
    @Length(max = 250, message = "微信小程序封面图片地址长度在250个字符以内")
    private String miniProgramCoverImgUrl;

    /**
     * 邀请码
     */
    @ApiModelProperty(name = "inviteCode", required = false, value = "邀请码")
    @Pattern(regexp = "^\\d{5,9}$", message = "邀请码格式错误")
    private String inviteCode;
}
