package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
public class ParamProTempQuery {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;


    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private String pageNum;


    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

}
