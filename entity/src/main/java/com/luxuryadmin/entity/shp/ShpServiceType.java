package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 店铺服务类型表
 *
 * @author monkey king
 * @date   2020/09/18 15:39:48
 */
@Data
public class ShpServiceType {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 创建者
     */
    private Integer insertAdmin;

    /**
     * 修改者
     */
    private Integer updateAdmin;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;
}