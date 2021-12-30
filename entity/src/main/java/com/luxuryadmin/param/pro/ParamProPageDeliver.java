package com.luxuryadmin.param.pro;

import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 发货列表查询
 *
 * @author tqm
 * @date 2021-09-25
 */
@ApiModel(description = "发货列表查询")
@Data
public class ParamProPageDeliver extends ParamToken {


    @ApiModelProperty(name = "state", value = "0：未发货，1：已发货")
    @NotNull(message = "发货状态不允许为空")
    private String state;

    @ApiModelProperty(name = "deliverSource", value = "发货订单来源：ORDER(订单)、LOCK_RECORD（锁单）")
    private String deliverSource;

    @ApiModelProperty(name = "timeSort", value = "排序方式(updateTime)")
    @NotBlank(message = "排序方式不能为空")
    private String sortKey;

    @ApiModelProperty(name = "timeSort", value = "时间排序：asc(升序)、desc（降序）")
    @NotBlank(message = "排序方式值不能为空")
    private String sortValue;

    @ApiModelProperty(name = "orderOrLockUserId", value = "开单或者锁单人员userId")
    private String orderOrLockUserId;

    @ApiModelProperty(name = "type", value = "订单类型")
    private String orderType;

    @ApiModelProperty(name = "startTime", value = "时间范围筛选(开始时间)")
    private String startTime;

    @ApiModelProperty(name = "endTime", value = "时间范围筛选(结束时间)")
    private String endTime;

    @ApiModelProperty(name = "pageSize", value = "每页显示条数")
    @Pattern(regexp = "^\\d{2,}$", message = "[pageSize]格式错误")
    private String pageSize;

    @ApiModelProperty(name = "proName", value = "搜索条件")
    private String proName;

    @ApiModelProperty(name = "shpUserId", value = "发货人员id")
    private String shpUserId;

    @ApiModelProperty(name = "deliverStartTime", value = "发货开始时间")
    private String deliverStartTime;

    @ApiModelProperty(name = "deliverEndTime", value = "发货结束时间")
    private String deliverEndTime;

    @ApiModelProperty(hidden = true)
    private Integer shopId;

    @ApiModelProperty(hidden = true)
    private Integer currentUserId;

    @ApiModelProperty(hidden = true)
    private List<String> type;

}
