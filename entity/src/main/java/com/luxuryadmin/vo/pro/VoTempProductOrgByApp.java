package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
@ApiModel(value="临时仓商品在机构集合显示app端", description="临时仓商品在机构集合显示app端")
public class VoTempProductOrgByApp {

    @ApiModelProperty(value = "临时仓商品在机构集合显示app端", name = "list")
    private List<VoTempProductOrgPageByApp> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    @ApiModelProperty(value = "店铺数", name = "shopNum")
    private Integer shopNum;

    @ApiModelProperty(value = "商品数", name = "productNum")
    private Integer productNum;

    @ApiModelProperty(value = "商品价值", name = "productPrice")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;
}
