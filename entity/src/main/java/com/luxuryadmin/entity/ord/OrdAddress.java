package com.luxuryadmin.entity.ord;

import lombok.Data;

import java.util.Date;

/**
 * 订单地址管理
 *
 * @author monkey king
 * @date 2019/12/01 04:54:52
 */
@Data
public class OrdAddress {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 收件人姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * ord_order的id字段,主键id
     */
    private Integer fkOrdOrderId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合并后的收货信息
     */
    private String receiveAddress;


}