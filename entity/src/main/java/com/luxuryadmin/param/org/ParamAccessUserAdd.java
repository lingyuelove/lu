package com.luxuryadmin.param.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓访问用户列表新增", description="机构仓访问用户列表新增")
public class ParamAccessUserAdd {

    /**
     * shp_shop的id字段,主键id
     */
    private Integer shopId;

    private Integer userId;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "organizationId", required = true)
    private Integer organizationId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "帐号--参数错误")
    private String phone;

    /**
     * 获取类型 -90 已删除 | 10白名单 | 20黑名单
     */
    @ApiModelProperty(value = "获取类型 10白名单 | 20黑名单", name = "accessType", required = true)
    private String accessType;
}
