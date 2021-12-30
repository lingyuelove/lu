package com.luxuryadmin.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 机构临时仓app端显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓app端显示", description="机构临时仓app端显示")
public class VoOrganizationTempPageByApp {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "organizationId")
    private Integer organizationId;

    /**
     * 临时仓id
     */
    @ApiModelProperty(value = "临时仓id", name = "tempId")
    private Integer tempId;

    /**
     * 店铺机构仓排序位置分组名称
     */
    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName")
    private String tempSeatName;

    /**
     * 展会位置
     */
    @ApiModelProperty(value = "展会位置", name = "showSeat")
    private String showSeat;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "商品数量", name = "productCount")
    private Integer productCount;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;


    @ApiModelProperty(value = "提取人", name = "insertAdminName")
    private String insertAdminName;

}
