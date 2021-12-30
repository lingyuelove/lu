package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 机构仓编辑
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓编辑", description="机构仓编辑")
public class ParamOrganizationUpdate {
    /**
     * 主键id
     */
    @NotNull(message = "参数不为空")
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private Integer id;

    private Integer shopId;

    /**
     * 机构临时仓名称
     */
    @NotBlank(message = "机构仓名称不为空")
    @ApiModelProperty(value = "机构仓名称", name = "name", required = true)
    private String name;

    /**
     * 展会状态 10 不开启限制 | 20 开启限制
     */
    @ApiModelProperty(value = "机构仓名称", name = "name", required = false)
    private String state;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime", required = false)
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime", required = false)
    private String endTime;

    /**
     * 更新人
     */
    private Integer updateAdmin;
}
