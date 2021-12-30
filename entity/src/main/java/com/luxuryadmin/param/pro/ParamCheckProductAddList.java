package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="盘点新增参数", description="盘点新增参数")
public class ParamCheckProductAddList extends ParamCheckAddList{

    /**
     * pro_check的主键id
     */
    private Integer fkProCheckId;

    /**
     * 盘点人id
     */
    @ApiModelProperty(value = "盘点人id", name = "fkShpUserId", required = false)
    private Integer fkShpUserId;

    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(value = " shp_shop的id字段,主键id", name = "fkShpShopId", required = false)
    private Integer fkShpShopId;

    @ApiModelProperty(value = "商品属性表的code集合", name = "fkProAttributeCodes", required = true)
    private List<String> fkProAttributeCodeList;

    @ApiModelProperty(value = "商品分类名称集合", name = "fkProAttributeCodes", required = true)
    private List<String> fkProClassifyCodeList;
}
