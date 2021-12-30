package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Classname VoShpServiceRecord
 * @Description 店铺服务记录VO
 * @Date 2020/9/18 18:08
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpServiceRecord {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * 商品图片URL
     */
    @ApiModelProperty(name = "prodImgUrl", required = false, value = "商品图片URL")
    private String prodImgUrl;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "prodName", required = false, value = "商品名称")
    private String prodName;

    /**
     * 备注
     */
    @ApiModelProperty(name = "note", required = false, value = "备注")
    private String note;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "insertTime", required = false, value = "创建时间")
    private String insertTime;

    /**
     *
     */
    @ApiModelProperty(name = "serviceTypeName", required = false, value = "服务类型名称")
    private String serviceTypeName;

    /**
     * 服务成本
     */
    @ApiModelProperty(name = "costAmount", required = false, value = "服务成本")
    private String costAmount;

    /**
     * 实际收费
     */
    @ApiModelProperty(name = "realReceiveAmount", required = false, value = "实际收费")
    private String realReceiveAmount;
    @ApiModelProperty(name = "showTime", required = false, value = "展示时间")
    private String showTime;

    @ApiModelProperty(name = "serviceStatus", required = false, value = "服务状态,inService|服务中,finish|完成,cancel|取消")
    private String serviceStatus;
    /**
     * 完成时间
     */
    @ApiModelProperty(name = "finishTime", hidden = true, value = "完成时间")
    private String finishTime;

    /**
     * 取消时间
     */
    @ApiModelProperty(name = "cancelTime", hidden = true, value = "取消时间")
    private String cancelTime;

    @ApiModelProperty(name = "showUserName", required = false, value = "展示人员名称")
    private String showUserName;

    /**
     * 维修人员ID
     */
    @ApiModelProperty(name = "serviceShpUserId", hidden = true, value = "服务人员ID")
    private Integer serviceShpUserId;

    /**
     * 维修人员ID
     */
    @ApiModelProperty(name = "receiveShpUserId", hidden = true, value = "接单人员ID")
    private Integer receiveShpUserId;

    /**
     * 维修人员名称
     */
    @ApiModelProperty(name = "serviceShpUserName", hidden = true, value = "服务人员名称")
    private String serviceShpUserName;

    /**
     * 维修人员名称
     */
    @ApiModelProperty(name = "receiveShpUserName", hidden = true, value = "接单人员名称")
    private String receiveShpUserName;

    @ApiModelProperty(name = "totalCost", required = false, value = "服务成本")
    private String totalCost;
}
