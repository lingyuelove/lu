package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoShareList
 * @Author: ZhangSai
 * Date: 2021/6/30 14:07
 */
@Data
@ApiModel(value="小程序分享list", description="小程序分享list")
public class VoShareList {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "小程序分享id", name = "id")
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(value = "店铺主键id", name = "shopId")
    private Integer shopId;

    /**
     * shp_user的id字段,主键id
     */
    @ApiModelProperty(value = "用户id", name = "userId")
    private Integer userId;

    /**
     * 插入时间
     */
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    /**
     * 名称
     */
    @ApiModelProperty(value = "分享显示名字", name = "showName")
    private String showName;

    /**
     * 分享图片
     */
    @ApiModelProperty(value = "分享图片", name = "shareImg")
    private String shareImg;

    @ApiModelProperty(value = "分享人名称", name = "userName")
    private String userName;


}
