package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 14:33
 * @Description: 二级分类检索类
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "二级分类检索类")
@Data
public class ParamProClassifySubQuery extends ParamToken {

    @ApiModelProperty(name = "classifyId", required = false, value = "产品一级分类主键id,全部分类不传")
    private String classifyId;

    @ApiModelProperty(name = "classifyCode", required = false, value = "产品一级分类code,全部分类不传")
    private String classifyCode;

    @ApiModelProperty(name = "shopId", required = false, value = "商铺主键id,后端直接获取")
    private Integer shopId;

    @ApiModelProperty(name = "name", required = false, value = "二级分类名称")
    private String name;

    @ApiModelProperty(value = "状态;0:隐藏;1:显示")
    private Integer state;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;


}
