package com.luxuryadmin.entity.org;

import lombok.Data;

import java.util.Date;

/**
 * 机构仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class OrgOrganization {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 机构临时仓名称
     */
    private String name;

    /**
     * 展会状态 10 不开启限制 | 20 开启限制
     */
    private String state;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    private Integer insertAdmin;

    /**
     * 更新人
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String remark;

}