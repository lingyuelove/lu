package com.luxuryadmin.entity.biz;

import lombok.Data;

import java.util.Date;

/**
 * 商务模块--友商配置
 *
 * @author monkey king
 * @date   2019/12/01 04:54:26
 */
@Data
public class BizLeaguerConfig {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 邀请者(店主)shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 友商数量上限
     */
    private Integer limitLeaguerNum;

    /**
     * 已有友商数量
     */
    private Integer haveLeaguerNum;

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
     * 是否可以查看销售价
     */
    private Boolean isCanSeeSalePrice;

    /**
     * 是否可以查看友商价
     */
    private Boolean isCanSeeTradePrice;

    /**
     * 查看星标友商;0:查看全部友商商品 | 1:只查看星标友商商品
     */
    private String onlyShowTopLeaguer;

    /**
     * 优质友商推荐 0 不推荐 1 推荐
     */
    private String recommend;
}