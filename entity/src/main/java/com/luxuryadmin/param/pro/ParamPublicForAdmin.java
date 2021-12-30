package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamPublicForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/18 15:42
 */
@Data
@ApiModel(value="公价商品搜索后台", description="公价商品搜索后台")
public class ParamPublicForAdmin {


    @ApiModelProperty(name = "id",required = false, value = "主键ID")
    private Integer id;


    /**
     * -1:删除 | 0:停用 | 1:启用; 默认为 1
     */
    @ApiModelProperty(name = "state", value = " -1:删除 | 0:停用 | 1:启用; 默认为 1")
    private String state;

    /**
     * 产品分类表的code;默认':无分类;
     */
    @ApiModelProperty(name = "classifyCode", value = "产品分类表的code;默认':无分类;")
    private String classifyCode;

    /**
     * 品牌名称
     */
    @ApiModelProperty(name = "name", value = "品牌名称")
    private String name;

    /**
     * 型号
     */
    @ApiModelProperty(name = "typeNo", value = "型号")
    private String typeNo;

    /**
     * 系列
     */
    @ApiModelProperty(name = "serialNo", value = "系列")
    private String serialNo;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    @ApiModelProperty(name = "pageNum", required = false, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
//    @Max(value = 999, message = "当前页最大为999")
//    @Pattern(regexp = "^\\d+$", message = "[pageNum]格式错误")
    private int pageNum= 1;
}
