package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 保存分享产品--查询参数实体
 *
 * @author monkey king
 * @date 2020-06-13 17:14:05
 */
@ApiModel(description = "保存分享产品--查询参数实体")
@Data
public class ParamShareProductSave extends ParamBasic {
    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;


    /**
     * 商品id,多个用英文逗号隔开;例如: 10001,10002
     */
    @ApiModelProperty(name = "proIds", required = true,
            value = "商品id,多个用英文逗号隔开;例如: 10001,10002 全点分享则填写'all' ")
    @Pattern(regexp = "^([0-9,]{5,})|(all)$", message = "proIds--参数错误")
    private String proIds;

    /**
     * 分享出去可显示的价格;
     */
    @ApiModelProperty(name = "token", required = false,
            value = "分享出去可显示的价格;tradePrice:友商价;agencyPrice:代理价格; salePrice:销售价格; initPrice:成本价 默认:salePrice; ")
    @Pattern(regexp = "^((tradePrice)|(agencyPrice)|(salePrice)|(initPrice)|,)*$", message = "showPrice--参数错误")
    private String showPrice;

    /**
     * 临时商品分享
     */
    @ApiModelProperty(name = "tempPro", required = false, value = "临时商品分享时,此参数需要加上,值为'yes'")
    @Length(max = 10, message = "tempPro--格式错误")
    private String tempPro;

    /**
     * 临时仓id
     */
    @ApiModelProperty(name = "tempId", required = false, value = "临时仓id")
    @Pattern(regexp = "^[0-9]{5,}$", message = "tempId--参数错误")
    private String tempId;
    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(name = "shopId", hidden = true, value = "shp_shop的id")
    private Integer shopId;

    /**
     * shp_user的id字段,主键id
     */
    @ApiModelProperty(name = "userId", hidden = true, value = "shp_user的id")
    private Integer userId;

    /**
     * 店铺编号;对外显示
     */
    @ApiModelProperty(name = "shopNumber", hidden = true, value = "店铺编号")
    private String shopNumber;

    /**
     * 员工编号;对外显示
     */
    @ApiModelProperty(name = "userNumber", hidden = true, value = "员工编号")
    private Integer userNumber;
    @ApiModelProperty(name = "type", required = false, value = "0店铺分享 1商品分享 2商品详情分享")
    private String type;
    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器)，逗号分隔 --2.6.2;")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;
}
