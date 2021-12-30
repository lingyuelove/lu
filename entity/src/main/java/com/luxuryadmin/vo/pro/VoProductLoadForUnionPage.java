package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProductLoadForUnionPage
 * @Author: ZhangSai
 * Date: 2021/7/20 17:22
 */
@Data
@ApiModel(value="商家联盟商品page admin端显示类", description="商家联盟商品page admin端显示类")
public class VoProductLoadForUnionPage {

    private List<VoProductLoad> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private long total;
    @ApiModelProperty(value = "商品总数 商品*库存", name = "totalNum")
    private String totalNum;

    @ApiModelProperty(value = "商品总额", name = "totalPrice")
    private String totalPrice;


}
