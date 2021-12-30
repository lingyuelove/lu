package com.luxuryadmin.service.util.mongo;

import com.luxuryadmin.common.aop.check.DateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.service.util.mongo 默认搜索类
 * @ClassName: TenantBaseStatisticsGetDTO
 * @Author: ZhangSai
 * Date: 2021/11/9 14:38
 */
@Data
public class TenantBaseStatistics {
    @ApiModelProperty(value = "搜索关联id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;
    @ApiModelProperty(value = "时间参数", name = "dateFieldName")
    private String dateFieldName;

    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;
    @ApiModelProperty(value = "搜索关联id名称", name = "userIdName")
    private String userIdName;
}
