package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductOrOrderForDeleteSearch
 * @Author: ZhangSai
 * Date: 2021/7/1 17:28
 */
@ApiModel(description = "删除模块--前端搜索类")
@Data
public class ParamProductOrOrderForDeleteSearch extends ParamToken {

    @ApiModelProperty(name = "searchName", value = "搜索名称")
    private String searchName;

    /**
     * 销售时间 起始
     */
    @ApiModelProperty(value = "查询条件中-删除时间范围-开始 yyyy-MM-dd HH:mm:ss  --2.6.2", name = "deleteTimeStart", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTimeStart;

    /**
     * 销售时间 结束
     */
    @ApiModelProperty(value = "查询条件中-删除时间范围-结束 yyyy-MM-dd HH:mm:ss  --2.6.2", name = "deleteTimeEnd", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTimeEnd;
    /**
     * 属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品)"
     */
    @ApiModelProperty(name = "attributeCode", required = false,
            value = "属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品);如有多个请用分号隔开拼接  --2.6.2")
    @Pattern(regexp = "^[12340;]{2,}$", message = "属性编码--参数错误")
    private String attributeCode;
    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);如有多个请用分号隔开拼接")
    private String classifyCode;

    @ApiModelProperty(name = "deleteUserId", required = false, value = "删除人员id;如有多个请用分号隔开拼接  --2.6.2")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "删除人员--参数错误")
    private String deleteUserId;

    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;updateTime(删除时间排序)  --2.6.2")
    //@Pattern(regexp = "^(normal)|(price)|(time)|(updateTime)|(notDown)|(finishTime)|(finishPrice)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)  --2.6.2",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;
    /**
     * 订单类型
     */
    @ApiModelProperty(name = "orderType", required = false, value = "订单类型;多个用英文分号分割拼接  --2.6.2")
    private String orderType;

    @ApiModelProperty(name = "deleteType", required = true, value = "删除类型 1:商品 2:订单")
    @NotBlank(message = "删除类型不能为空")
    private String deleteType;

    @ApiModelProperty(name = "shopId", hidden = true, value = "店铺id")
    private Integer shopId ;
    @ApiModelProperty(name = "userId", hidden = true, value = "用户id")
    private Integer userId ;
    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为20;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 20;
}
