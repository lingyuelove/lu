package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinShopRecordDetail {

    /**
     * 流水收入支出类型
     */
    @ApiModelProperty(value = "流水收入支出类型", name = "inoutSubType", required = false)
    private String inoutType;

    /**
     * 流水类型名称
     */
    @ApiModelProperty(value = "流水类型名称", name = "inoutSubType", required = false)
    private String inoutSubType;

    /**
     * 流水类型 system|系统生成 manual|人工记录
     */
    @ApiModelProperty(value = "流水类型 system|系统生成 manual|人工记录", name = "recordType", required = false)
    private String recordType;


    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号", name = "streamNo", required = false)
    private String streamNo;

    /**
     * 变动金额 收入为正，支出为负
     */
    @ApiModelProperty(value = "变动金额 收入为正，支出为负", name = "changeAmount", required = false)
    private String changeAmount;

    /**
     * 流水关联的订单ID
     */
    @ApiModelProperty(value = "关联订单号", name = "fkOrderId", required = false)
    private Integer fkOrderId;

    /**
     * 流水发生时间
     */
    @ApiModelProperty(value = "流水发生时间", name = "happenTime", required = false)
    private String happenTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "记录创建时间", name = "insertTime", required = false)
    private String insertTime;

    /**
     * 创建者用户ID
     */
    @ApiModelProperty(value = "创建人", name = "insertAdmin", required = false)
    private String insertAdmin;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "insertAdmin", required = false)
    private String note;

    /**
     * 流水详情图片地址列表
     */
    @ApiModelProperty(value = "流水详情图片地址列表", name = "imgUrlDetailList", required = false)
    private List<String> imgUrlDetailList;
}
