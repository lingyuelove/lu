package com.luxuryadmin.param.org;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 机构临时仓编辑
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓编辑", description="机构临时仓编辑")
public class ParamOrganizationTempUpdate extends ParamToken {

    /**
     * 主键id
     */
    @NotNull(message = "参数不为空")
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private Integer id;


    /**
     * 更新人
     */
    private Integer updateAdmin;

    /**
     * 店铺机构仓排序位置分组名称
     */
    @NotBlank(message = "排序位置不为空")
    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName", required = true)
    private String tempSeatName;

    /**
     * 展会位置
     */
    @NotBlank(message = "展会位置不为空")
    @ApiModelProperty(value = "展会位置", name = "showSeat", required = true)
    private String showSeat;
}
