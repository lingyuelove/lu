package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 运营模块--渠道
 *
 * @author monkey king
 * @date   2019/12/01 05:22:30
 */
@Data
public class OpChannel {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 运营模块--平台类型的主键id
     */
    private Integer fkOpPlatformId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道编码
     */
    private String channelCode;


    /**
     * 对外查询编码,建议uuid,唯一性
     */
    private String outsideCode;

    /**
     * 状态:0: 禁用; 1:启用;(如果禁用, 则该通道的所有访问将被禁止)
     */
    private String state;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

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

}