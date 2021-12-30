package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:     Mong
 * @Date:    2021/5/31 17:44
 * @Description:  分享添加会员时长类型视图类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpShareType {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 时长规则类型
     */
    private String code;
    /**
     * 时长
     */
    private BigDecimal hours;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;
    @ApiModelProperty(value = "可添加的次数")
    private Integer addNum;
}
