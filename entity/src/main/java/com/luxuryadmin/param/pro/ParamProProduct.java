package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author qwy
 * @Classname ParamProduct
 * @Description TODO
 * @Date 2020/6/28 13:59
 */
@ApiModel(description = "商品管理-一般商品列表查询参数实体")
@Data
public class ParamProProduct {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    /**
     * 插入时间 起始
     */
    @ApiModelProperty(value = "查询条件中-入库时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-入库时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 上架时间 起始
     */
    @ApiModelProperty(value = "查询条件中-上架时间范围-开始", name = "releaseTimeStart", required = false)
    private Date releaseTimeStart;

    /**
     * 上架时间 结束
     */
    @ApiModelProperty(value = "查询条件中-上架时间范围-结束", name = "releaseTimeEnd", required = false)
    private Date releaseTimeEnd;

    /**
     * 售罄时间 起始
     */
    @ApiModelProperty(value = "查询条件中-售罄时间范围-开始", name = "finishTimeStart", required = false)
    private Date finishTimeStart;

    /**
     * 售罄时间 结束
     */
    @ApiModelProperty(value = "查询条件中-售罄时间范围-结束", name = "finishTimeEnd", required = false)
    private Date finishTimeEnd;


    @ApiModelProperty(value = "商品ID", name = "id", required = false)
    @Pattern(regexp = "^[0-9,]+$", message = "商品ID--参数错误")
    private String id;

    @ApiModelProperty(value = "商品名称", name = "name", required = false)
    private String name;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = false)
    @Pattern(regexp = "^[0-9,]+$", message = "店铺id--参数错误")
    private String shopId;

    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "店铺编号--参数错误")
    private String shopNumber;

    @ApiModelProperty(value = "店铺名称", name = "shopName", required = false)
    private String shopName;

    @ApiModelProperty(value = "商品属性:10-自有商品,20-寄卖商品,30-质押商品,40-其它 一般包含了10/20/40，查询一般全部，不传值", name = "attributeCode", required = false)
    @Pattern(regexp = "[0-4]0+$", message = "商品属性--参数错误")
    private String attributeCode;

    @ApiModelProperty(value = "商品分类：WB-腕表；XB-箱包； ZB-珠宝； XX-鞋靴； PS-配饰； QT-其它；", name = "classifyCode", required = false)
    @Pattern(regexp = "[WB|XB|ZB|XX|PS|QT]+$", message = "商品属性--参数错误")
    private String classifyCode;

    @ApiModelProperty(value = "商品状态: 0-未上架；1-在售中；2-已售罄", name = "stateCode", required = false)
    @Pattern(regexp = "[012349]+$", message = "商品状态--参数错误")
    private String stateCode;


    @ApiModelProperty(value = "此状态不影响用户前端展示;商家联盟状态:0:不显示 | 1:显示", name = "unionState")
    @Pattern(regexp = "[012349]+$", message = "[unionState]参数错误")
    private String unionState;

    @ApiModelProperty(hidden = true)
    private String uniqueCode;

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
