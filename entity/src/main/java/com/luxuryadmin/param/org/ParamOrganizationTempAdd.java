package com.luxuryadmin.param.org;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 机构临时仓新增
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓新增", description="机构临时仓新增")
public class ParamOrganizationTempAdd extends ParamToken {

    /**
     * 机构id
     */
    @NotNull(message = "机构参数不为空")
    @ApiModelProperty(value = "机构id", name = "organizationId", required = true)
    private Integer organizationId;

    /**
     * 临时仓id
     */
    @NotNull(message = "临时仓参数不为空")
    @ApiModelProperty(value = "临时仓id", name = "tempId", required = true)
    private Integer tempId;

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 创建人
     */
    private Integer insertAdmin;

    /**
     * 店铺机构仓排序位置分组名称
     */
    @NotBlank(message = "排序不为空")
    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName", required = true)
    private String tempSeatName;

    /**
     * 展会位置
     */
    @NotBlank(message = "展会位置不为空")
    @ApiModelProperty(value = "展会位置", name = "showSeat", required = true)
    private String showSeat;

    @NotBlank(message = "参数为空")
    @ApiModelProperty(value = "vid", name = "vid", required = true)
    private  String vid;
}
