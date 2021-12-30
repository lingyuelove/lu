package com.luxuryadmin.entity.org;

import lombok.Data;

import java.util.Date;

/**
 * 机构临时仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:27
 */
@Data
public class OrgOrganizationTemp {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 机构id
     */
    private Integer fkOrgOrganizationId;

    /**
     * 临时仓id
     */
    private Integer fkProTempId;

    /**
     * 店铺id
     */
    private Integer fkShpShopId;

    /**
     * 店铺机构仓排序位置分组名称
     */
    private String tempSeatName;

    /**
     * 展会位置
     */
    private String showSeat;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

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