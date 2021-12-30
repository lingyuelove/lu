package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author:     Mong
 * @Date:    2021/5/31 17:44
 * @Description:  分享添加会员时长操作记录视图类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpShareTotal {

    /**
     * 主键id
     */
    private Integer id;
    /**
     * 商铺用户表shp_user主键id
     */
    private Integer shpUserId;

    /**
     * 商铺用户表shp_shop主键id
     */
    private Integer shopId;
    /**
     * 操作记录次数
     */
    private Integer totalCount;
    /**
     * 当日操作累计时长
     */
    private BigDecimal totalHours;
    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;
    /**
     * 备注
     */
    private String remark;


}
