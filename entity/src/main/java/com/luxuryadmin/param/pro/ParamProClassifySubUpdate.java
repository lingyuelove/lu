package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author:     Mong
 * @Date:    2021/5/27 14:33
 * @Description: 二级分类更新类
 */
@ApiModel(description = "二级分类更新类")
@Data
public class ParamProClassifySubUpdate {

    /**
     * 主键id
     */
    @ApiModelProperty(name = "id", required = true, value = "主键id")
    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    @NotBlank(message = "id不能为空!")
    private String id;

    @ApiModelProperty(name = "ClassifyCode", required = false, value = "产品一级分类主键id,全部分类为传0")
    private String ClassifyCode;

    @ApiModelProperty(name = "shopId", required = false, value = "商铺主键id")
    @Pattern(regexp = "^[0-9]+$", message = "id--参数错误")
    private String shopId;

    @ApiModelProperty(name = "name", required = false, value = "二级分类名称")
    @NotBlank(message = "分类名称不能为空!")
    private String name;
    @ApiModelProperty(name = "name", required = false, value = "iconUrl")
    private String iconUrl;


    /**
     * 状态;-1:已删除;0:未使用;1:使用中
     */
    @ApiModelProperty(name = "state", required = false, value = "状态;-1:已删除;0:未使用;1:使用中")
    private Integer state;
    /**
     * 序号排序
     */
    @ApiModelProperty(value = "序号排序")
    private Integer sort;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
