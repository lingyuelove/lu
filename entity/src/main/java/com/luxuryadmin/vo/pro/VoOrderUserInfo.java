package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Author: taoqimin
 * Date: 2021-09-27
 */
@Data
@ApiModel(value="用户简信息", description="用户简信息")
public class VoOrderUserInfo{

    @ApiModelProperty(value = "id")
    private Integer userId;

    @ApiModelProperty(value = "名字")
    private String nickname;
}
