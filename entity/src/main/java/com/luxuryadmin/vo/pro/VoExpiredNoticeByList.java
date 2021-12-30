package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Data
@ApiModel(value="商品过期提醒", description="商品过期提醒")
public class VoExpiredNoticeByList {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "商品过期提醒Id", name = "expiredNoticeId")
    private Integer expiredNoticeId;
    /**
     * 产品属性名称
     */
    @ApiModelProperty(value = "产品属性名称", name = "attributeName")
    private String attributeName;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", name = "classifyName")
    private String classifyName;

    /**
     * 产品属性表的code集合
     */
    @ApiModelProperty(value = "产品属性表的code集合", name = "fkProAttributeCodes")
    private String fkProAttributeCodes;

    /**
     * 商品分类名称; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = "商品分类名称; 和分类列表对应; 默认0:无分类;", name = "fkProClassifyCodes")
    private String fkProClassifyCodes;

    /**
     * 设置过期天数
     */
    @ApiModelProperty(value = "设置过期天数", name = "expiredDay")
    private Integer expiredDay;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    /**
     * 创建的用户
     */
    @ApiModelProperty(value = "创建的用户", name = "userId")
    private Integer userId;

    /**
     * 创建的用户的用户名
     */
    @ApiModelProperty(value = "创建的用户的用户名", name = "userName")
    private String userName;
}
