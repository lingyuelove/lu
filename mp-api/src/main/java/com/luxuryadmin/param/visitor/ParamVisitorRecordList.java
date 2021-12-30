package com.luxuryadmin.param.visitor;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 访客记录
 *
 * @author taoqimin
 */
@Data
public class ParamVisitorRecordList extends ParamBasic {

    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "用户名(手机号)", name = "username")
    private String username;

    @ApiModelProperty(value = "分享人", name = "inviteId")
    private Integer inviteId;

    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    @ApiModelProperty(value = "开始时间", name = "endTime")
    private String endTime;

}
