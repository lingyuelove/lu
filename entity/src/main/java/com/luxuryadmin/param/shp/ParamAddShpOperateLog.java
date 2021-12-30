package com.luxuryadmin.param.shp;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * 新增店铺操作日志--前端参数模型
 *
 * @author sanjin145
 * @Date 2020-11-27 16:55
 */
@Data
public class ParamAddShpOperateLog {

    /**
     * 店铺ID
     */
    private Integer shopId;

    /**
     * 操作用户ID
     */
    private Integer operateUserId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作业务类型名称
     */
    private String operateName;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 商品ID
     */
    private Integer prodId;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 请求request
     */
    private HttpServletRequest request;
}
