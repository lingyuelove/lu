package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamShareUser
 * @Author: ZhangSai
 * Date: 2021/6/30 16:11
 */
@ApiModel(description = "点击单个分享所有用户的搜索类")
@Data
public class ParamUnionAccess extends ParamToken {


    @ApiModelProperty(value = "分享人id", name = "userId")
    private String userId;

    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;

    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;

}
