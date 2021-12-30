package com.luxuryadmin.vo.biz;

import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: BizLeaguerShop
 * @Author: ZhangSai
 * Date: 2021/5/27 14:30
 */
@Data
@ApiModel(value="友商相册个人店铺信息", description="机构仓访问用户列表更新")
public class VoBizLeaguerShop {

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;
    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号")
    private String shopNumber;

    /**
     * 封面图片地址
     */
    @ApiModelProperty(value = "封面图片地址")
    private String coverImgUrl;

    @ApiModelProperty(value = "友商店铺名称")
    private String leaguerShopName;

    @ApiModelProperty(value = "店铺地址")
    private String address;

    /**
     * 店铺头像地址
     */
    @ApiModelProperty(value = "店铺头像地址")
    private String headImgUrl;

    @ApiModelProperty(value = "友商数")
    private Integer leaguerShopCount;

    @ApiModelProperty(value = "友商商品总数")
    private Integer leaguerShopProductCount;

    @ApiModelProperty(value = "本周访问量")
    private Integer seeWeekCount;

    @ApiModelProperty(value = "历史访问量")
    private Integer seeAllCount;

    @ApiModelProperty(value = "类型")
    private List<VoProClassify> classifyList;
}
