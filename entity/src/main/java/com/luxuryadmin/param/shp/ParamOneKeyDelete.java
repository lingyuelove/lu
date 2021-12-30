package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 一键物理删除
 * @author Administrator
 */
@ApiModel(description = "一键物理删除")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParamOneKeyDelete {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime", required = true)
    @DateTime(format = "yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime", required = true)
    @DateTime(format = "yyyy-MM-dd")
    private String endTime;


    /**
     * 删除类型
     */
    @ApiModelProperty(value = "删除类型,多个用英文分号隔开(order;salary;message;service)", name = "deleteType", required = true)
    @NotBlank(message = "删除类型不能为空")
    private String deleteType;


    /**
     * 短信验证码
     */
    @ApiModelProperty(value = "短信验证码", name = "smsCode", required = true)
    private String smsCode;

}
