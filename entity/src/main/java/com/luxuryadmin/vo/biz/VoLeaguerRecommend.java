package com.luxuryadmin.vo.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoLeaguerList
 * @Author: ZhangSai
 * Date: 2021/5/27 18:22
 */
@Data
@ApiModel(value="推荐友商集合显示类", description="推荐友商集合显示类")
public class VoLeaguerRecommend {

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(value = "友商店铺名称")
    private String shopName;

    @ApiModelProperty(value = "友商店铺名称")
    private String shopNumber;
    @ApiModelProperty(value = "店铺地址")
    private String address;

    @ApiModelProperty(value = "店铺头像地址")
    private String headImgUrl;

    /**
     * 友商状态: -90: 拉黑; -10:已删除; 10:已发请求(待确认); 20:已成为好友
     */
    @ApiModelProperty(value = "友商状态:0未发送请求 10:已发请求")
    private String leaguerState;
}
