package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 商品锁单查询--前端接收参数模型
 *
 * @author monkey king
 * @date 2021-08-24 19:18:52
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品锁单查询--前端接收参数模型")
@Data
public class ParamLockProductQuery extends ParamToken {


    /**
     * 查询关键字
     */
    @ApiModelProperty(name = "keyword", value = "查询关键字")
    private String keyword;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode",  value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    /**
     * 锁单人员id
     */
    @ApiModelProperty(name = "lockUserId", value = "锁单人员id")
    private String lockUserId;

    /**
     * 锁单金额小
     */
    @ApiModelProperty(name = "minPrice", value = "锁单金额小(分)")
    private String minPrice;


    /**
     * 锁单金额大
     */
    @ApiModelProperty(name = "maxPrice", value = "锁单金额大(分)")
    private String maxPrice;


    /**
     * 锁单时间--开始
     */
    @ApiModelProperty(name = "lockTimeSt", value = "锁单时间--开始")
    private String lockTimeSt;


    /**
     * 锁单时间--结束
     */
    @ApiModelProperty(name = "lockTimeEt", value = "锁单时间--结束")
    private String lockTimeEt;

    /**
     * 排序字段
     * price(价格排序);time(时间排序);
     */
    @ApiModelProperty(name = "sortKey", required = true, value = "price(价格排序);time(时间排序);"
            , allowableValues = "time,price")
    @Pattern(regexp = "^(price)|(time)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;

    /**
     * 数据库的排序
     */
    @ApiModelProperty(hidden = true)
    private String sortKeyDb;

    /**
     * 唯一编码
     */
    @ApiModelProperty(hidden = true)
    private String uniqueCode;

    @ApiModelProperty(hidden = true)
    private int shopId;


}
