package com.luxuryadmin.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class VOAddVipUserList {

    @ApiModelProperty(value = "用户id", name = "id")
    private Integer id;

    @ApiModelProperty(value = "手机号", name = "username")
    private String username;

    @ApiModelProperty(value = "开通时间", name = "insertTime")
    private String insertTime;

    @ApiModelProperty(value = "开通人", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "有效期", name = "payEndTime")
    private String payEndTime;
}
