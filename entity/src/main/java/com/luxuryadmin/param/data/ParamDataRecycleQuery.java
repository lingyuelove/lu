package com.luxuryadmin.param.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: ParamDataRecycleQuery
 * @Author: ZhangSai
 * Date: 2021/6/11 10:53
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "广告管理-消息推送")
@Data
public class ParamDataRecycleQuery extends ParamToken {

    @ApiModelProperty(value = "店铺ID", name = "shopId", required = false, hidden = true)
    private Integer shopId;

    /**
     * 销售时间 起始
     */
    @ApiModelProperty(value = "查询条件中-回收时间-开始 yyyy-MM-dd HH:mm:ss", name = "recycleTimeStart", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recycleTimeStart;
    @ApiModelProperty(value = "查询条件中-回收时间-开始 yyyy-MM-dd HH:mm:ss", name = "finishTimeStart", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTimeStart;
    /**
     * 销售时间 结束
     */
    @ApiModelProperty(value = "查询条件中-回收时间-结束 yyyy-MM-dd HH:mm:ss", name = "recycleTimeStart", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recycleTimeEnd;

    @ApiModelProperty(value = "排序字段", name = "sortColumn", required = true, allowableValues = "数量 recycleCount,总成本initAllPrice ,回收时间 insertTime,回收成本 initPrice")
    @Pattern(regexp = "^(recycleCount)|(initAllPrice)|(insertTime)|(initPrice)$", message = "排序字段--参数错误")
    private String sortColumn;

    @ApiModelProperty(value = "排序类型", name = "sortType", required = true, allowableValues = "asc,desc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortType;


    /**
     * 属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品)"
     */
    @ApiModelProperty(name = "attributeCode", required = false,
            value = "属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品);如有多个请用分号隔开拼接")
    @Pattern(regexp = "^[12340;]{2,}$", message = "属性编码--参数错误")
    private String attributeCode;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);多个请用分号隔开")
    @Length(min = 2, max = 100, message = "分类编码长度在2~100个字符")
    private String classifyCode;

    @ApiModelProperty(name = "recycleAdmin", value = "回收人员")
    private String recycleAdmin;
    @ApiModelProperty(name = "uniqueCode", value = "独立编码")
    private String uniqueCode;
    @ApiModelProperty(name = "proName", value = "商品名称")
    private String proName;
}
