package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @Classname VoShpShop
 * @Description TODO
 * @Date 2020/6/24 14:27
 * @Created by Administrator
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpShop {
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
    private Date tryStartTime;

    /**
     * 试用结束时间
     */
    private Date tryEndTime;

    /**
     * 付费使用开始时间
     */
    private Date payStartTime;

    /**
     * 付费使用结束时间
     */
    private Date payEndTime;

    /**
     * 管理员续时时长;(一般用于帐号到期,可以延长到期时间;最长一个星期)
     */
    private Date adminAddTime;

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
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户注册编号
     */
    private String userNumber;

    /**
     * 在售商品数量
     */
    private Integer onSaleProdNum;

    /**
     * 上传商品总数
     */
    private Integer uploadProdNum;

    /**
     * 开单总数
     */
    private Integer confirmOrderNum;

    /**
     * 在职员工数量
     */
    private Integer shopUserNum;

    /**
     * 在职员工数
     */
    private Integer employeeNum;

    /**
     * 是否为会员; yes | no
     */
    private String member;

    /**
     * 显示SysJobWx的nickname
     */
    private String jobWx;
}
