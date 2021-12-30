package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoShareUserPage
 * @Author: ZhangSai
 * Date: 2021/6/30 16:10
 */
@Data
@ApiModel(value = "商家联盟小程序访客", description = "商家联盟小程序访客")
public class VoUnionAccess {


    @ApiModelProperty(value = "分享批次号", name = "id")
    private String shareBatch;

    /**
     * 奢当家用户昵称
     */
    private String nickname;

    @ApiModelProperty(value = "微信昵称", name = "nickname")
    private String wxNickname;

    @ApiModelProperty(value = "微信头像", name = "userImg")
    private String userImg;

    @ApiModelProperty(value = "微信性别", name = "sex")
    private String sex;

    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    @ApiModelProperty(value = "过期时间", name = "expireTime")
    private String expireTime;

    @ApiModelProperty(value = "访问状态", name = "accessState")
    private String accessState;

    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;

}
