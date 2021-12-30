package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺财务流水表
 *
 * @author sanjin145
 * @date   2020/10/21 17:08:44
 */
@Data
public class FinShopRecord {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 出入类型 in|收入 out|支出
     */
    private String inoutType;

    /**
     * 流水子类型 中文
     */
    private String inoutSubType;

    /**
     * 流水类型 system|系统生成 manual|人工记录
     */
    private String recordType;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 流水详情图片地址
     */
    private String imgUrlDetail;

    /**
     * 流水号
     */
    private String streamNo;

    /**
     * 变动金额 收入为正，支出为负(单位为分)
     */
    private BigDecimal changeAmount;

    /**
     * 流水关联的订单ID
     */
    private String fkOrderId;

    /**
     * 流水发生时间
     */
    private Date happenTime;

    /**
     * 流水发生日期
     */
    private String happenDate;

    /**
     * 流水发生月份
     */
    private String happenMonth;

    /**
     * 流水发生年份
     */
    private String happenYear;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建者用户ID
     */
    private Integer insertAdmin;

    /**
     * 修改者用户ID
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;
}