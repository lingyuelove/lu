package com.luxuryadmin.param.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Classname ParamOpProblemQuery
 * @Description TODO
 * @Date 2020/7/9 16:23:25
 * @Created by Administrator
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "广告管理-消息推送")
@Data
public class ParamDataSaleRankQuery extends ParamToken {

    @ApiModelProperty(value = "店铺ID", name = "shopId", required = false,hidden = true)
    private Integer shopId;

    /**
     * 销售时间 起始
     */
    @ApiModelProperty(value = "查询条件中-订单时间范围-开始 yyyy-MM-dd HH:mm:ss", name = "finishTimeStart", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTimeStart;

    /**
     * 销售时间 结束
     */
    @ApiModelProperty(value = "查询条件中-订单时间范围-结束 yyyy-MM-dd HH:mm:ss", name = "finishTimeEnd", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTimeEnd;

    @ApiModelProperty(value = "排序字段", name = "sortColumn", required = false,allowableValues = "saleCount,saleAmount,grossProfit")
    private String sortColumn;

    @ApiModelProperty(value = "排序类型", name = "sortType", required = false,allowableValues = "asc,desc")
    private String sortType;

    //2020-12-09 新增筛选字段
    /**
     * 销售人员id
     */
    @ApiModelProperty(name = "saleUserId", required = false, value = "销售人员id;all代表全部")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "销售人员--参数错误")
    private String saleUserId;

    /**
     * 订单类型
     */
    @ApiModelProperty(name = "orderType", required = false, value = "YS;DL;KH;QT订单类型;多个用英文分号分割拼接")
    private String orderType;

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
}
