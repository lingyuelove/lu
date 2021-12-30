package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 商品过期提醒搜索类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品过期提醒搜索类", description="商品过期提醒搜索类")
public class ParamExpiredProductSearch extends ParamToken {
    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;price(价格排序);time(已过提醒时间排序);updateTime(更新时间排序)")
    @Pattern(regexp = "^(price)|(time)|(updateTime)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    @ApiModelProperty(name = "expiredNoticeId", required = true,
            value = "过期id")
    private Integer expiredNoticeId;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;


    @ApiModelProperty(name = "shopId", required = false,
            value = "店铺id；后端自己获取 前段不传")
    private Integer shopId;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;
}
