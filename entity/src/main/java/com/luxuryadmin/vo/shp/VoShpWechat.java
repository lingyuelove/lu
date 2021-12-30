package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 店铺微信VO
 * @author sanjin
 * @Date 2020/08/31 16:26
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpWechat {

    /**
     * 联系人姓名
     */
    @ApiModelProperty(name = "id", required = false, value = "店铺微信ID;")
    private String id;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(name = "contactPersonName", required = false, value = "联系人姓名;")
    private String contactPersonName;

    /**
     * 联系人微信号
     */
    @ApiModelProperty(name = "contactPersonWechat", required = false, value = "联系人微信号;")
    private String contactPersonWechat;

    /**
     * 负责内容
     */
    @ApiModelProperty(name = "contactResponsible", required = false, value = "负责内容;")
    private String contactResponsible;

    /**
     * 联系人类型 0:微信 1:手机号
     */
    @ApiModelProperty(name = "type", required = false, value = "联系人类型 0:微信 1:手机号;")
    private String type;

    @ApiModelProperty(name = "showName", required = false, value = "展示名称;")
    private String showName;

}
