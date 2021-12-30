package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 店铺操作日志表
 *
 * @author sanjin145
 * @date   2020/11/27 16:14:24
 */
@Data
public class ShpOperateLog {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 操作店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 操作店铺名称
     */
    private String shopName;

    /**
     * 操作人用户ID
     */
    private Integer operateUserId;

    /**
     * 操作人所在店铺用户名
     */
    private String operateUserShopName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作业务类型名称
     */
    private String operateTypeName;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作日期
     */
    private String operateDate;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 请求域名
     */
    private String requestDomain;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求方法 POST|GET
     */
    private String requestMethod;

    /**
     * 操作的商品ID
     */
    private Integer fkProProductId;

    /**
     * 操作订单ID
     */
    private Integer fkOrdOrderId;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}