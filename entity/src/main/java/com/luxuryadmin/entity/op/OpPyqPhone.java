package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 推广号码资源
 *
 * @author monkey king
 * @date   2021/07/21 01:44:34
 */
@Data
public class OpPyqPhone {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 批次
     */
    private String batch;

    /**
     * 1:手机号;2:固定电话;3:其它
     */
    private String type;

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
     * 店铺名称
     */
    private String shopName;

    /**
     * 地图坐标
     */
    private String gps;

    /**
     * 行业
     */
    private String industry;

    /**
     * 是否奢当家注册用户;0:不是; 1:是
     */
    private String isUser;

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