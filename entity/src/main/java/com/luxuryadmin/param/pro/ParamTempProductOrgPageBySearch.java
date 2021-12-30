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
 * 临时仓商品在机构集合搜索
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="临时仓商品在机构集合搜索", description="临时仓商品在机构集合搜索")
public class ParamTempProductOrgPageBySearch extends ParamToken {

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;

    @ApiModelProperty(value = "临时仓id", name = "tempId", required = false)
    private  Integer tempId;

    @ApiModelProperty(value = "机构仓id", name = "organizationId", required = true)
    private Integer organizationId ;

    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;normal(默认排序);price(价格排序);time(提取时间排序);updateTime(更新时间排序)")
    //@Pattern(regexp = "^(normal)|(price)|(time)|(updateTime)|(notDown)|(finishTime)|(finishPrice)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;
    /**
     * 最低价格
     */
    @ApiModelProperty(name = "priceMin", required = false, value = "最低价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最低价格--参数错误")
    private String priceMin;

    /**
     * 最高价格
     */
    @ApiModelProperty(name = "priceMax", required = false, value = "最高价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最高价格--参数错误")
    private String priceMax;

    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String  proName;

    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    private String  classifyCode;


    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName")
    private String tempSeatName;
}
