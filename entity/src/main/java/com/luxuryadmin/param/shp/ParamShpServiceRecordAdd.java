package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname ParamAddShpService
 * @Description 添加店铺服务参数类
 * @Date 2020/9/18 15:50
 * @Created by sanjin145
 */
@Data
public class ParamShpServiceRecordAdd extends ParamToken {
    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID,修改时必填")
    private Integer id;

    /**
     * 商品图片链接URL列表，用英文分号分割
     */
    @ApiModelProperty(name = "prodImgUrls", required = false, value = "商品图片链接URL列表，用英文分号分割")
    private String prodImgUrls;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "prodName", required = true, value = "商品名称")
    @NotBlank(message = "prodName不允许为空")
    private String prodName;

    /**
     * 独立编码
     */
    @ApiModelProperty(name = "uniqueCode", required = false, value = "独立编码")
    private String uniqueCode;

    /**
     * 维修成本
     */
    @ApiModelProperty(name = "costAmount", required = false, value = "维修成本")
    private BigDecimal costAmount;

    /**
     * 实际收费
     */
    @ApiModelProperty(name = "realReceiveAmount", required = false, value = "实际收费")
    private BigDecimal realReceiveAmount;

    /**
     * 服务类型ID 暂未使用该字段 所以2.6.4此字段改为非必填
     */
    @ApiModelProperty(name = "typeId", required = false, value = "服务类型ID")
    private String typeId;

    /**
     * 服务类型名称
     */
    @ApiModelProperty(name = "typeName", required = true, value = "服务类型名称")
    @NotBlank(message = "typeName不允许为空")
    private String typeName;

    /**
     * 维修人员ID
     */
    @ApiModelProperty(name = "serviceShpUserId", required = false, value = "服务人员ID")
    private Integer serviceShpUserId;

    @ApiModelProperty(name = "receiveShpUserId", required = false, value = "接单人员ID")
    private Integer receiveShpUserId;

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

    @ApiModelProperty(name = "costAddOrUps", required = false, value = "服务成本新增或编辑")
    private List<ParamServiceRecordCostAddOrUp> costAddOrUps;
    @ApiModelProperty(name = "costAddOrUpList", required = false, value = "服务成本新增或编辑")
    private String costAddOrUpList;
    @ApiModelProperty( hidden = true, value = "店铺id")
    private Integer shopId ;
    @ApiModelProperty( hidden = true, value = "用户id")
    private Integer userId;

}
