package com.luxuryadmin.vo.visitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class VOVisitorRecordList {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "头像")
    private String headImgUrl;

    @ApiModelProperty(value = "微信名称")
    private String nickname;

    @ApiModelProperty(value = "用户名（手机号）")
    private String username;

    @ApiModelProperty(value = "分享人昵称")
    private String visitorNickname;

    @ApiModelProperty(value = "访问时间")
    private String visitorTime;

    @ApiModelProperty(value = "过期时间")
    private String pastTime;

    @ApiModelProperty(value = "访问状态")
    private Integer memberState;

    private Date tryEndTime;

    private Date payEndTime;

}
