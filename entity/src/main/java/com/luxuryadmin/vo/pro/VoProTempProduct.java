package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 临时仓VO层
 *
 * @author monkey king
 * @date 2021-01-17 14:10:42
 */
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoProTempProduct extends VoProductLoad {


    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 临时仓库商品id
     */
    private String proTempProductId;

    /**
     *临时仓库id
     */
    private String tempId;


    /**
     * 临时仓数量
     */
    private Integer num;


    private Integer proId;

}
