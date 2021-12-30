package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 员工参数--前端参数模型
 *
 * @author monkey king
 * @date 2019-12-26 02:15:20
 */
@Data
@ApiModel(description = "员工参数--前端参数模型")
public class ParamEmployee extends ParamToken {

    /**
     * 状态  0：禁用(或辞职)   1：正常
     */
    @ApiModelProperty(name = "state", required = true, value = "状态 -9删除 0：禁用(或辞职)   1：正常")
    @Pattern(regexp = "^(-9)|0|1$", message = "state--参数错误")
    @NotBlank(message = "state--参数错误")
    private String state;

    /**
     * 排序字段;normal(默认排序);time(加入时间);name(昵称排序);
     */
    @ApiModelProperty(name = "sortKey",
            value = "排序字段;normal(默认排序);time(加入时间);name(昵称排序);")
    @Pattern(regexp = "^(time)|(name)|(normal)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;

    /**
     * 搜索关键字
     */
    @ApiModelProperty(name = "queryKey", value = "搜索关键字,目前支持手机号和店铺昵称搜索")
    private String queryKey;

    /**
     * 店铺id
     */
    @ApiModelProperty(hidden = true)
    private int shopId;

    /**
     * 加密查询的手机号
     */
    @ApiModelProperty(hidden = true)
    private String username;


    /**
     * 数据库排序
     */
    @ApiModelProperty(hidden = true)
    private String sortKeyDb;


}
