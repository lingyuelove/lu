package com.luxuryadmin.vo.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoRecommendAdminList
 * @Author: ZhangSai
 * Date: 2021/5/27 20:50
 */
@Data
@ApiModel(value="推荐友商List admin端显示类", description="推荐友商List admin端显示类")
public class VoRecommendAdminList {
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "店铺id")
    private Integer fkShpShopId;

    @ApiModelProperty(value = "友商店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺地址")
    private String address;

    @ApiModelProperty(value = "店铺头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "创建时间")
    private String insertTime;
    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号")
    private String shopNumber;
    /**
     * 状态  0 不推荐 1 推荐
     */
    @ApiModelProperty(value = "状态  0 不推荐 1 推荐")
    private String state;
}
