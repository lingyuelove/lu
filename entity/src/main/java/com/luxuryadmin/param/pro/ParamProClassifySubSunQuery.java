package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProClassifySubSunQuery
 * @Author: ZhangSai
 * Date: 2021/8/4 16:13
 */
@ApiModel(description = "品牌，系列分类检索类")
@Data
public class ParamProClassifySubSunQuery  extends ParamToken {

    /**
     * 商品品牌分类pro_classify_sub表品牌名称
     */
    @ApiModelProperty(name = "classifySubName", required = true,value = "商品品牌分类pro_classify_sub表品牌名称")
    private String classifySubName;

    @ApiModelProperty(name = "name", hidden = true , value = "分类名称")
    private String name;

    @ApiModelProperty(name = "shopId", hidden = true, value = "商铺主键id,后端直接获取")
    private Integer shopId;


    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;
}
