package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author monkey king
 * @date 2020-09-28 19:42:20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoSalaryOrdType {

    private String proAttr;

    private String orderType;

    private BigDecimal money;

    private Integer num;

}
