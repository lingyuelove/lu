package com.luxuryadmin.entity.org;

import lombok.Data;

import java.util.Date;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class OrgAccessUser {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 机构id
     */
    private Integer fkOrgOrganizationId;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 获取类型 -90 已删除 | 10白名单 | 20黑名单
     */
    private String accessType;

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
