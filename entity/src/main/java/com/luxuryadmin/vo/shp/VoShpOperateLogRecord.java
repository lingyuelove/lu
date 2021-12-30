package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname VoShpOperateLogRecord
 * @Description 店铺操作日志记录VO
 * @Date 2020/9/18 18:08
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpOperateLogRecord {

    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * 商品图片URL
     */
    @ApiModelProperty(name = "operateUserName", required = false, value = "操作人")
    private String operateUserName;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "operateTime", required = false, value = "操作时间")
    private String operateTime;

    /**
     * 备注
     */
    @ApiModelProperty(name = "operateModule", required = false, value = "操作模块")
    private String operateModule;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "operateContent", required = false, value = "操作内容")
    private String operateContent;

}
