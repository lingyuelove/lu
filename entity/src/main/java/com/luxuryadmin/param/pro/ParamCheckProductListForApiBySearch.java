package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ParamCheckProductListForApiBySearch {
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

    @ApiModelProperty(value = "搜索商品名称", name = "pageSize", required = false)
    private String productName;

    @ApiModelProperty(value = "盘点类型 缺失:0  存在:1", name = "checkState")
    private String checkType;

    /**
     * 商品分类id; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = "商品分类id; 和分类列表对应; 默认0:无分类;", name = "checkState")
    private String fkProClassifyCode;

    @ApiModelProperty(value = "已盘点yes 未盘点no", name = "checkState")
    private String checkState;

    /**
     * pro_check的主键Id
     */
    @ApiModelProperty(value = "pro_check的主键Id", name = "fkProCheckId", required = true)
    @NotBlank(message = "pro_check的主键Id不为空")
    private String fkProCheckId;
    @ApiModelProperty(value = "店铺id", name = "shopId", hidden = true )
    private int shopId;
    @ApiModelProperty(value = "tempId", name = "tempId", hidden = true )
    private int tempId;
    @ApiModelProperty(value = "uniqueCode", name = "uniqueCode", hidden = true )
    private String uniqueCode;
}
