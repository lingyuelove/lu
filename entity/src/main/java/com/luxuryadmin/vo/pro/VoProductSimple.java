package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProductSimple {

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * bizId
     */
    private String bizId;

}
