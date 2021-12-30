package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoConvey
 * @Author: ZhangSai
 * Date: 2021/11/22 18:21
 */
@Data
@ApiModel(value="商品传送表实体参数", description="商品传送表实体参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoConvey {
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String number;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer insertAdmin;

    @ApiModelProperty(value = "创建人名称")
    private String insertAdminName;
    /**
     * 发送状态
     */
    @ApiModelProperty(value = "发送状态 0待提取 1已提取 2已确认")
    private String sendState;
    /**
     * 接收状态
     */
    @ApiModelProperty(value = "接收状态 0待确认 1已确认")
    private String receiveState;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "展示时间")
    private String showTime;

    @ApiModelProperty(value = "展示人名称")
    private String showUserName;

    @ApiModelProperty(value = "商品总数")
    private Integer totalCount;

    @ApiModelProperty(value = "商品总估值")
    private BigDecimal totalPrice;

}
