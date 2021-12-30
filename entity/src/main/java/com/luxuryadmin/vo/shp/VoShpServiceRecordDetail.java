package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Classname VoShpServiceRecordDetail
 * @Description 【店铺服务】详情VO
 * @Date 2020/9/18 19:20
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpServiceRecordDetail {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "主键ID", required = false, value = "主键ID")
    private Integer id;

    /**
     * 商品图片链接URL列表，用英文分号分割
     */
    @ApiModelProperty(name = "prodImgUrls", required = false, value = "商品图片链接URL列表，用英文分号分割")
    private String prodImgUrls;

    /**
     * 商品图片URL
     */
    @ApiModelProperty(name = "prodImgUrlList", required = false, value = "商品图片URL列表")
    private List<String> prodImgUrlList;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "prodName", required = false, value = "商品名称")
    private String prodName;

    /**
     * 独立编码
     */
    @ApiModelProperty(name = "uniqueCode", required = false, value = "独立编码")
    private String uniqueCode;

    /**
     * 服务成本
     */
    @ApiModelProperty(name = "costAmount", required = false, value = "服务成本")
    private BigDecimal costAmount;

    /**
     * 实际收费
     */
    @ApiModelProperty(name = "realReceiveAmount", required = false, value = "实际收费")
    private BigDecimal realReceiveAmount;

    /**
     * 服务类型名称
     */
    @ApiModelProperty(name = "typeName", required = true, value = "服务类型名称")
    private String typeName;

    /**
     * 维修人员ID
     */
    @ApiModelProperty(name = "serviceShpUserId", required = false, value = "服务人员ID")
    private Integer serviceShpUserId;

    /**
     * 维修人员ID
     */
    @ApiModelProperty(name = "receiveShpUserId", required = false, value = "接单人员ID")
    private Integer receiveShpUserId;

    /**
     * 维修人员名称
     */
    @ApiModelProperty(name = "serviceShpUserName", required = false, value = "服务人员名称")
    private String serviceShpUserName;

    /**
     * 维修人员名称
     */
    @ApiModelProperty(name = "receiveShpUserName", required = false, value = "接单人员名称")
    private String receiveShpUserName;

    /**
     * 服务编号
     */
    @ApiModelProperty(name = "serviceNumber", required = false, value = "服务编号")
    private String serviceNumber;

    /**
     * 服务编号
     */
    @ApiModelProperty(name = "serviceStatus", required = false, value = "服务状态 inService|服务中 finish|完成 cancel|取消")
    private String serviceStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "insertTime", required = false, value = "创建时间")
    private Date insertTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "insertUserName", required = false, value = "创建人员姓名")
    private String insertUserName;

    /**
     * 完成时间/取消时间
     */
    @ApiModelProperty(name = "finishOrCancelTime", required = false, value = "完成时间/取消时间")
    private Date finishOrCancelTime;

    /**
     * 备注
     */
    @ApiModelProperty(name = "note", required = false, value = "备注")
    private String note;

    /**
     * 客户信息
     */
    @ApiModelProperty(name = "customerInfo", required = false, value = "客户信息")
    private String customerInfo;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "shopName", required = false, value = "店铺名称")
    private String shopName;

    /**
     * 备注
     */
    @ApiModelProperty(name = "shopAddress", required = false, value = "店铺地址")
    private String shopAddress;

    /**
     * 客户信息
     */
    @ApiModelProperty(name = "shopContact", required = false, value = "店铺联系方式")
    private String shopContact;
    @ApiModelProperty(name = "recordCosts", required = false, value = "服务成本集合")
    private List<VoServiceRecordCost> recordCosts;

    @ApiModelProperty(name = "totalCost", required = false, value = "服务成本总计金额")
    private String totalCost;
    @ApiModelProperty(name = "showType", required = false, value = "展示类型 all 全部 context:只展示内容 默认all")
    private String showType;

    @ApiModelProperty(name = "uPermUpdate", required = false, value = "维修保养编辑权限")
    private String uPermUpdate;
}
