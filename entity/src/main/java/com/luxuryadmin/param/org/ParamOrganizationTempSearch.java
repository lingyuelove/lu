package com.luxuryadmin.param.org;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 机构临时仓新增
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓集合搜索", description="机构临时仓集合搜索")
public class ParamOrganizationTempSearch extends ParamToken {



    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @NotNull(message = "参数为空")
    @ApiModelProperty(value = "机构仓id", name = "organizationId", required = true)
    private Integer organizationId;

    private Integer shopId ;

    private Integer userId;

    @ApiModelProperty(value = "手机号", name = "phone", required = false)
    private String phone;

    @ApiModelProperty(value = "搜索名称", name = "searchName", required = false)
    private String searchName;
}
