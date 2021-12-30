package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifyTypeSearch
 * @Author: ZhangSai
 * Date: 2021/8/3 11:31
 */
@Data
@ApiModel(value="补充信息分类实体参数", description="新增补充信息分类实体参数")
public class ParamClassifyTypeSearch extends ParamToken {

    @ApiModelProperty(name = "shopId", hidden = true, value = "商铺主键id 后端自己获取")
    private Integer shopId;

    @ApiModelProperty(name = "classifyCode", required = false,value = "商品系列表英文拼写，现只有箱包和腕表")
    private String classifyCode;

    @ApiModelProperty(name = "bizId", hidden = true, value = "商品主键id 后端自己获取")
    private String bizId;


    @ApiModelProperty(name = "bizId", hidden = true, value = "商品主键id 后端自己获取")
    private Integer productId;

    @ApiModelProperty(name = "classifyTypeId", hidden = true, value = "商品二级id 后端自己获取")
    private Integer classifyTypeId;

    private String state;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;
}
