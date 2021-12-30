package com.luxuryadmin.vo.share;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShareShopProduct {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;
    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;
    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 名称
     */
    private String name;


    /**
     * 适用人群
     */
    @ApiModelProperty(value = "适用人群", name = "targetUser")
    private String targetUser;

    /**
     * 销售价(分)
     */
    private BigDecimal salePrice;

    /**
     * 缩略图地址
     */
    private String smallImg;
}
