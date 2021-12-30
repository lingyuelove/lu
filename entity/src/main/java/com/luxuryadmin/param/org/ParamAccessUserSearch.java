package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
/**
 * 机构仓访问用户列表搜索
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓访问用户列表搜索", description="机构仓访问用户列表搜索")
public class ParamAccessUserSearch {

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @NotNull(message = "参数为空")
    @ApiModelProperty(value = "机构仓id", name = "organizationId", required = true)
    private Integer organizationId;

    /**
     * 获取类型 -90 已删除 | 10白名单 | 20黑名单
     */
    @ApiModelProperty(value = "获取类型 10白名单 | 20黑名单", name = "accessType", required = true)
    private String accessType;

}
