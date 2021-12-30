package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoShareUserList
 * @Author: ZhangSai
 * Date: 2021/6/30 16:07
 */
@Data
@ApiModel(value = "小程序访客list", description = "小程序访客list")
public class VoShareUserList {
    @ApiModelProperty(value = "小程序分享用户id", name = "id")
    private Integer id;
    @ApiModelProperty(value = "商品名称", name = "userName")
    private String userName;
    @ApiModelProperty(value = "用户头像", name = "userImg")
    private String userImg;
    @ApiModelProperty(value = "用户性别", name = "sex")
    private String sex;
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;
}
