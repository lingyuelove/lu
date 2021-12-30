package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author monkey king
 * @date 2020-09-23 23:59:09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinSalary {

    private Integer salaryId;

    private Integer userId;

    private String nickname;

    private String headImgUrl;

    private String userTypeName;

    /**
     *  0:未发放; 1:已发放;
     */
    private String salaryState;

    /**
     * 薪资总额
     */
    private Long salaryMoney;

    /**
     * 工资条名称
     */
    private String salaryName;

    /**
     * 创建时间
     */
    private String insertTime;

}
