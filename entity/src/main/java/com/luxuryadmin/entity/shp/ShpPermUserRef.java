package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 用户与权限对应关系(新版权限)
 *
 * @author monkey king
 * @date   2021/12/02 02:01:02
 */
@Data
public class ShpPermUserRef {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 店铺--用户表ID
     */
    private Integer fkShpUserId;

    /**
     * 员工权限索引表ID
     */
    private Integer fkShpPermIndexId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

}