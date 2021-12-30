package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Description: 店铺售后保障VO
 * @author: walkingPotato
 * @date: 2020-08-20 15:51
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpAfterSaleGuarantee {

    /**
     * 售后保障ID
     */
    private Integer guaranteeId;

    /**
     * 售后保障名称
     */
    private String guaranteeName;

}
