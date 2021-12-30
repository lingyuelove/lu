package com.luxuryadmin.param.shp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Classname ParamShpServiceQuery
 * @Description 店铺服务列表查询参数
 * @Date 2020/9/18 18:11
 * @Created by sanjin145
 */
@Data
public class ParamShpServiceQuery {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 当前页
     */
    @ApiModelProperty(name = "pageNum", required = true, value = "当前页;默认为1")
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 20;

    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "fkShpShopId", required = false, value = "店铺ID")
    private Integer fkShpShopId;

    /**
     * 服务状态
     */
    @ApiModelProperty(name = "serviceStatus", required = true, value = "服务状态,inService|服务中,finish|完成,cancel|取消",
            allowableValues = "inService,finish,cancel")
    @NotBlank(message = "serviceStatus不允许为空")
    private String serviceStatus;

    /**
     * 客户信息里的手机号
     */
//    @ApiModelProperty(name = "customerPhone", required = false, value = "客户信息里的手机号")
//    private String customerPhone;
//
//    @ApiModelProperty(name = "uniqueCode", required = false, value = "独立编码")
//    private String uniqueCode;

    @ApiModelProperty(name = "searchContent", required = false, value = "查询条件")
    private String searchContent;

    @ApiModelProperty(name = "orderClause", required = false, value = "排序子句",hidden = true)
    private String orderClause;

    @ApiModelProperty(name = "startTime", required = false, value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(name = "endTime", required = false, value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(name = "serviceShpUserId", required = false, value = "服务人员ID")
    private String serviceShpUserId;

    @ApiModelProperty(name = "receiveShpUserId", required = false, value = "接单人员ID")
    private String receiveShpUserId;

    @ApiModelProperty(name = "typeName", required = false, value = "类型名称")
    private String typeName;

    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;updateTime(更新时间排序)")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;
}
