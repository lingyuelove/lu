package com.luxuryadmin.param.admin.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author monkey king
 * @date 2020-12-18 20:39:58
 */
@ApiModel(description = "用户管理-用户列表")
@Data
public class ParamLoginRecord {
    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = true)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "用户主键Id", name = "userId", required = true)
    private String userId;
}
