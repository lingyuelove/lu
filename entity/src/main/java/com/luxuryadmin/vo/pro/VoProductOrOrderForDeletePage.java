package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProductOrOrderForDeletePage
 * @Author: ZhangSai
 * Date: 2021/7/1 17:41
 */
@Data
@ApiModel(value="删除列表类", description="机构仓app端显示大类")
public class VoProductOrOrderForDeletePage {
    @ApiModelProperty(value = "删除列表类", name = "list")
    private List<VoProductOrOrderForDelete> list;

    @ApiModelProperty(value = "pageNum", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "pageSize", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "hasNextPage", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "商品总数 --2.6.2", name = "totalProduct")
    private Long totalProduct;

    @ApiModelProperty(value = "订单总数 --2.6.2", name = "totalOrder")
    private Long totalOrder;

    @ApiModelProperty(name = "uPermHistoryDelete", value = "已删除订单删除权限")
    private String uPermHistoryDelete;

}
