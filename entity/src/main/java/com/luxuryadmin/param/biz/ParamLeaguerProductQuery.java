package com.luxuryadmin.param.biz;


import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 商品(友商)页面查询参数实体--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-08-03 20:04:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品(友商)页面查询参数实体")
public class ParamLeaguerProductQuery extends ParamBasic {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private String pageNum;

    /**
     * 排序字段
     * price(价格排序);time(入库时间排序);normal(默认排序)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;price(价格排序);time(入库时间排序);normal(默认排序)")
    @Pattern(regexp = "^(price)|(time)|(normal)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;


    /**
     * 适用人群
     */
    @ApiModelProperty(name = "targetUser", required = false, value = "适用人群;(通用;男;女)可多选,分号隔开")
    @Pattern(regexp = "^[通用男女;]{1,7}$", message = "适用人群--参数错误")
    private String targetUser;


    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;

    @ApiModelProperty(name = "priceType", required = false,
            value = "价格类型;切换筛选价格时,需要把此类型传到服务器(tradePrice:同行价格;salePrice:销售价格)")
    @Pattern(regexp = "^(tradePrice)|(salePrice)$", message = "价格类型--参数错误")
    private String priceType;

    /**
     * 最低价格
     */
    @ApiModelProperty(name = "priceMin", required = false, value = "最低价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最低价格--参数错误")
    private String priceMin;

    /**
     * 最高价格
     */
    @ApiModelProperty(name = "priceMax", required = false, value = "最高价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最高价格--参数错误")
    private String priceMax;

    /**
     * 查看星标友商
     */
    @ApiModelProperty(name = "onlyShowTopLeaguer", required = false, value = "查看星标友商;0:查看全部友商商品 | 1:只查看星标友商商品")
    @Pattern(regexp = "^[01]?$", message = "onlyShowTopLeaguer--参数错误")
    private String onlyShowTopLeaguer;

    @ApiModelProperty(value = "商品二级分类", name = "classifySub", required = false)
    private String classifySub;


    @ApiModelProperty(value = "小程序用的标识符", name = "flag", required = false)
    private String flag;
    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    /**
     * 在xml里的排序
     */
    @ApiModelProperty(hidden = true)
    private String sortKeyDb;

    /**
     * 最小显示金额
     */
    @ApiModelProperty(hidden = true)
    private String showMinPrice;

    /**
     * 最大显示金额
     */
    @ApiModelProperty(hidden = true)
    private String showMaxPrice;

    /**
     * 控制显示时间;大于等于该时间才显示
     */
    @ApiModelProperty(hidden = true)
    private String  showTimeSt;

}
