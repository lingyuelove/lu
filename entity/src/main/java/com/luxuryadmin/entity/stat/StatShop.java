package com.luxuryadmin.entity.stat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运营统计-店铺表
 *
 * @author sanjin145
 * @date   2020/12/15 16:18:43
 */
@Data
public class StatShop {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 上传商品数量
     */
    private Integer prodNumUpload;

    /**
     * 在售商品数量
     */
    private Integer prodNumOnsale;

    /**
     * 总销售量
     */
    private Integer totalSaleNum;

    /**
     * 总销售额
     */
    private BigDecimal totalSaleAmount;

    /**
     * 店铺员工数量
     */
    private Integer shopStaffNum;

    /**
     * 店铺友商数量
     */
    private Integer shopLeaguerNum;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}