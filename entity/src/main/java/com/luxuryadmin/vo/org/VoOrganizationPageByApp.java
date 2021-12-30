package com.luxuryadmin.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 机构仓app端显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓app端显示", description="机构仓app端显示")
public class VoOrganizationPageByApp {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;
    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "商品数量", name = "productCount")
    private Integer productCount;

    @ApiModelProperty(value = "店铺数量", name = "shopCount")
    private Integer shopCount;

    @ApiModelProperty(value = "结算总价", name = "totalPrice")
    private BigDecimal totalPrice;

    /**
     * 机构临时仓名称
     */
    @ApiModelProperty(value = "机构临时仓名称", name = "name")
    private String name;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    @ApiModelProperty(value = "创建人id", name = "insertAdmin")
    private Integer insertAdmin;

    @ApiModelProperty(value = "创建人", name = "insertAdminName")
    private String insertAdminName;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty(value = "开始时间", name = "startTime", required = false)
    private String startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty(value = "结束时间", name = "endTime", required = false)
    private String endTime;

}
