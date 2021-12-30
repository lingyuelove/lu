package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.shp
 * @ClassName: VoShpServiceRecordForPage
 * @Author: ZhangSai
 * Date: 2021/6/21 15:01
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="店铺服务记录VO大类", description="店铺服务记录VO大类")
public class VoShpServiceRecordForPage {

    @ApiModelProperty(value = "分页参数", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "分页参数", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "是否有下一页", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "店铺服务记录list", name = "list")
    private List<VoShpServiceRecord> list;

    @ApiModelProperty(value = "查看权限", name = "uPermServiceProfit")
    private String uPermServiceProfit;

    @ApiModelProperty(value = "维修数量", name = "serviceNum")
    private String serviceNum;

    @ApiModelProperty(value = "维修成本", name = "serviceInitPrice")
    private String serviceInitPrice;

    @ApiModelProperty(value = "维修利润", name = "serviceProfitPrice")
    private String serviceProfitPrice;

    @ApiModelProperty(value = "维修数量名", name = "countName")
    private String countName;

    @ApiModelProperty(value = "维修利润名", name = "profitPriceName")
    private String profitPriceName;

    @ApiModelProperty(value = "维修金额名", name = "servicePriceName")
    private String servicePriceName;

}
