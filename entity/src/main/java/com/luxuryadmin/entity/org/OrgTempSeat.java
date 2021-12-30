package com.luxuryadmin.entity.org;

import lombok.Data;

import java.util.Date;

/**
 * 店铺机构仓排序位置分组表
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
public class OrgTempSeat {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 店铺id
     */
    private Integer fkShpShopId;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

}