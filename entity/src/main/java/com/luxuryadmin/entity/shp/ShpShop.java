package com.luxuryadmin.entity.shp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺表;
 *
 * @author monkey king
 * @date 2019/12/01 04:55:22
 */
@Data
public class ShpShop {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
     */
    private String number;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 店铺描述
     */
    private String desc;

    /**
     * 店铺固话
     */
    private String contact;

    /**
     * 封面图片地址
     */
    private String coverImgUrl;

    /**
     * 店铺头像地址
     */
    private String headImgUrl;

    /**
     * 店铺管理员id.店长id
     */
    private Integer fkShpUserId;

    /**
     * 店铺状态的code;详情查看shp_state表
     */
    private Integer fkShpStateCode;

    /**
     * 店铺属性的code;详情查看shp_attribute表
     */
    private Integer fkShpAttributeCode;

    /**
     * 认证状态:
     */
    private String validateState;

    /**
     * 付费月数:  1:月付; 3:季付; 6:半年付; 12:年付; 36:三年付;
     */
    private Integer payMonth;

    /**
     * 试用开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date tryStartTime;

    /**
     * 试用结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date tryEndTime;

    /**
     * 付费使用开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payStartTime;

    /**
     * 付费使用结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date payEndTime;

    private Date payEndTimeOld;

    /**
     * 管理员续时时长;(一般用于帐号到期,可以延长到期时间;最长一个星期)
     */
    private Date adminAddTime;

    /**
     * 店铺添加时长
     */
    private BigDecimal totalHours;
    /**
     * 累计付费月数
     */
    private Integer totalMonth;

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

    /**
     * 是否是会员 yes|是会员(付费会员) no|不是会员(或者体验会员)
     */
    private String isMember;

    /**
     * 微信小程序封面图片URL
     */
    private String miniProgramCoverImgUrl;

    /**
     * 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     */
    private Integer memberState;

    /**
     * 工作微信id
     */
    private Integer fkSysJobWxId;

}