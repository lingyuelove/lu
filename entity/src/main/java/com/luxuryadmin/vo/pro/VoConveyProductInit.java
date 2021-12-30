package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.enums.shp.EnumSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamConveyInit
 * @Author: ZhangSai
 * Date: 2021/11/24 17:46
 */
@Data
@ApiModel(value="商品传送表集合显示实体参数", description="商品传送表集合显示实体参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoConveyProductInit {
    @ApiModelProperty(value = "商品类型集合显示", name = "classifyList")
    private List<VoProClassify> classifyList;
    @ApiModelProperty(value = "商品价格类型（initPrice：成本价，tradePrice：友商价，agencyPrice：代理价，salePrice：销售价）", name = "defaultPrice")
    private String defaultPrice;
    /**
     * 发送状态
     */
    @ApiModelProperty(value = "发送状态 0待提取 1已提取 2已确认")
    private String sendState;
    /**
     * 接收状态
     */
    @ApiModelProperty(value = "接收状态 0待确认 1已确认")
    private String receiveState;

}
