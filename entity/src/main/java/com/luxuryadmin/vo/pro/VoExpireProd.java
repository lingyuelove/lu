package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 到期商品VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoExpireProd {

    /**
     * 商品bizId
     */
    private String bizId;

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     * 店铺名称
     */
    private String proName;

}
