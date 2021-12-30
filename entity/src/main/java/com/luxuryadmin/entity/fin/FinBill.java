package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.util.Date;

/**
 * 帐单表
 *
 * @author zhangSai
 * @date   2021/04/23 14:19:52
 */
@Data
public class FinBill {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 账单名称
     */
    private String name;

    /**
     * 初始账单金额
     */
    private Long oldMoney;
    /**
     * 账单金额
     */
    private Long money;

    /**
     * 总金额
     */
    private Long totalMoney;

    /**
     * 对账类型逗号分隔 10:自有商品;30:质押商品;40:其他商品
     */
    private String types;

    /**
     * 对账状态 10：进行中 -99 已删除
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
     * 创建人
     */
    private Integer insertAdmin;

    /**
     * 更新人
     */
    private Integer updateAdmin;

//    /**
//     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
//     */
//    private String del;

    /**
     * 备注
     */
    private String remark;
}