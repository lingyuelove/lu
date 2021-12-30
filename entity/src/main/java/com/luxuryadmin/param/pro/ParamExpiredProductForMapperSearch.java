package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品过期提醒搜索类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品过期提醒搜索类", description="商品过期提醒搜索类")
public class ParamExpiredProductForMapperSearch extends ParamExpiredProductSearch{
    /**
     * 产品属性表的code集合
     */
    private List<String> attributeCodeList;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    private List<String> classifyCodeList;

    /**
     * 设置过期天数
     */
    private Integer expiredDay;

    //独立编码不支持模糊查询

    @ApiModelProperty(hidden = true)
    private String uniqueCode;
}
