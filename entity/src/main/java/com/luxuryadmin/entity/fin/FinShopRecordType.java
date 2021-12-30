package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.util.Date;

/**
 * 店铺财务流水类型表
 *
 * @author monkey king
 * @date   2020/10/19 16:24:33
 */
@Data
public class FinShopRecordType {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 店铺流水类型名称
     */
    private String finRecordTypeName;

    /**
     * 出入类型 in|收入 out|支出
     */
    private String inoutType;

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