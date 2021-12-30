package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 店铺机构仓排序位置分组新增/修改
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="店铺机构仓排序位置分组新增/修改", description="店铺机构仓排序位置分组新增/修改")
public class ParamTempSeatAddOrUpdate {
    /**
     * 分组名称 逗号分隔
     */
    @ApiModelProperty(value = "分组名称 List", name = "nameList", required = true)
    private String name;

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;
}
