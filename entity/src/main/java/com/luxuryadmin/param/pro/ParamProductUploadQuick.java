package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 上传快速开单商品--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "上传快速开单商品参数实体")
@Data
public class ParamProductUploadQuick {

    /**
     * token;登录标识符
     */
//    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
//    private String token;

    /**
     * 商品属性
     */
    @ApiModelProperty(value = "商品属性;10:自有商品; 20:寄卖商品; 30:质押(典当)商品", name = "attribute", required = false)
    @NotBlank(message = "商品属性不允许为空")
    private String attribute;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类;例如: WB,SS,XB,XX,PS,QT", name = "classify", required = false)
    @NotBlank(message = "商品分类不允许为空")
    private String classify;

    @ApiModelProperty(value = "商品二级分类id",name = "classifySub",required = false)
    private String classifySub;
//
//
//    /**
//     * 商品状态
//     */
//    @ApiModelProperty(value = "商品状态;10:保存到库存;20:立即上架", name = "state", required = false)
//    @Pattern(regexp = "^\\d{2}$", message = "商品状态校验错误")
//    @NotBlank(message = "商品状态不允许为空")
//    private String state;

//    /**
//     * 防重复提交vid
//     */
//    @ApiModelProperty(value = "初始化上传商品时下发的vid;", name = "vid", required = true)
//    @Length(min = 32, max = 32, message = "vid--参数错误")
//    private String vid;

    /**
     * 商品图片;
     */
    @ApiModelProperty(value = "商品图片地址,多个用分号隔开;eg:/product/abc.png;/product/efg.png", name = "proImgUrl", required = true)
    @Length(max = 1000, message = "商品图片长度必须≤1000个字符")
    private String proImgUrl;

//    /**
//     * 商品业务逻辑id
//     */
//    @ApiModelProperty(value = "商品业务逻辑id;更新商品时,请赋值;", name = "bizId", required = false)
//    private String bizId;
//
    /**
     * 缩略图
     */
    @ApiModelProperty(value = "商品缩略图片;eg:/product/efg.png", name = "smallImgUrl", required = false)
    @Length(max = 250, message = "缩略图长度必须≤250个字符")
    private String smallImgUrl;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称(传中文到服务器)", name = "name", required = true)
    @Length(max = 50, message = "商品名称长度必须≤50个字符")
    private String name;
//
//    /**
//     * 商品描述
//     */
//    @ApiModelProperty(value = "商品描述", name = "description", required = false)
//    @Length(max = 250, message = "商品描述长度必须≤250个字符")
//    private String description;
//
//    /**
//     * 商品来源
//     */
//    @ApiModelProperty(value = "商品来源", name = "source", required = false)
//    @Length(max = 20, message = "商品来源长度必须≤20个字符")
//    private String source;
//
//
    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价;不填默认为0", name = "initPrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "成本价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String initPrice;
//
//    /**
//     * 友商价
//     */
//    @ApiModelProperty(value = "友商价;不填默认为0", name = "tradePrice", required = false)
//    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
//    @Length(max = 11,message = "价格超出范围!")
//    private String tradePrice;
//
//    /**
//     * 代理价
//     */
//    @ApiModelProperty(value = "代理价;不填默认为0", name = "agencyPrice", required = false)
//    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "代理价-格式错误")
//    @Length(max = 11,message = "价格超出范围!")
//    private String agencyPrice;
//
    /**
     * 销售价(卖客价)
     */
    @ApiModelProperty(value = "卖客售价(卖客价);不填默认为0", name = "salePrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "销售价-格式错误")
    @Length(max = 11,message = "价格超出范围!")
    private String salePrice;

    /**
     * 商品委托方--信息
     */
    @ApiModelProperty(value = "商品委托方--信息", name = "customerInfo", required = false)
    @Length(max = 250, message = "委托方信息必须≤250个字符")
    private String customerInfo;

    /**
     * 回收人员
     */
    @ApiModelProperty(value = "回收人员", name = "recycleAdmin", required = false)
    private Integer recycleAdmin;

    @Override
    public String toString() {
        return "name:" + name + ";proImgUrl:" + proImgUrl;
    }
}
