package com.luxuryadmin.param.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.sys
 * @ClassName: ParamJobWx
 * @Author: ZhangSai
 * Date: 2021/11/11 10:34
 */
@ApiModel(description = "获取工作微信统计列表 --搜索类")
@Data
public class ParamJobWxCensuses {

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;
    @ApiModelProperty(value = "搜索关联id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;
}
