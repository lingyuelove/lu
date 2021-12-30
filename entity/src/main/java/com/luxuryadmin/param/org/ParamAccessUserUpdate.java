package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓访问用户列表更新", description="机构仓访问用户列表更新")
public class ParamAccessUserUpdate {

    /**
     * 主键id
     */
    @NotNull(message = "id不为空")
    @ApiModelProperty(value = "主键id", name = "id", required = true)
    private Integer id;

    private Integer userId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "organizationId", required = true)
    private Integer organizationId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    private String phone;
}
