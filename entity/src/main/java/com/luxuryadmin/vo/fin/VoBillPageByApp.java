package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 帐单列表集合显示
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value="帐单列表集合显示", description="帐单列表集合显示")
public class VoBillPageByApp {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "billId")
    private Integer billId;

    /**
     * 账单名称
     */
    @ApiModelProperty(value = "账单名称", name = "name")
    private String name;

    /**
     * 账单金额
     */
    @ApiModelProperty(value = "账单金额", name = "money")
    private String money;

    /**
     * 总金额
     */
    @ApiModelProperty(value = "总金额", name = "totalMoney")
    private String totalMoney;
    /**
     * 对账类型逗号分隔
     */
    @ApiModelProperty(value = "对账类型逗号分隔", name = "types")
    private String types;

    @ApiModelProperty(value = "对账类型 中文名称", name = "types")
    private String attributeShortCn;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "insertAdmin")
    private Integer insertAdmin;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", name = "insertAdminName")
    private String insertAdminName;

}
