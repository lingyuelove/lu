package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 创建临时仓的参数模型
 *
 * @author by Administrator
 * @Classname ParamProduct
 * @Description TODO
 * @date 2021-01-18 01:51:01
 */
@ApiModel(description = "临时仓-一般商品列表查询参数实体")
@Data
public class ParamProTempCreate {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;


    @ApiModelProperty(value = "仓库名称", name = "name", required = true)
    @NotBlank(message = "仓库名称不能为空")
    @Length(max = 10)
    private String name;

    /**
     * 防重复提交vid
     */
    @ApiModelProperty(value = "vid;从listProTemp接口获取;", name = "vid", required = true)
    @Length(min = 32, max = 32, message = "vid--参数错误")
    @NotBlank(message = "vid--格式错误")
    private String vid;



}
