package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺服务记录表
 *
 * @author monkey king
 * @date   2020/09/18 15:39:48
 */
@Data
public class ShpServiceRecord {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 商品图片链接URL列表，用英文分号分割
     */
    private String prodImgUrls;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 独立编码
     */
    private String uniqueCode;

    /**
     * 维修成本
     */
    private BigDecimal costAmount;

    /**
     * 实际收费
     */
    private BigDecimal realReceiveAmount;

    /**
     * 服务类型名称
     */
    private String typeName;

    /**
     * 维修人员ID
     */
    private Integer serviceShpUserId;

    /**
     * 接单人员ID
     */
    private Integer receiveShpUserId;

    /**
     * 备注
     */
    private String note;

    /**
     * 客户信息
     */
    private String customerInfo;

    /**
     * 服务状态 inService|服务中 finish|完成 cancel|取消
     */
    private String serviceStatus;

    /**
     * 服务编号
     */
    private String serviceNumber;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建者
     */
    private Integer insertAdmin;

    /**
     * 修改者
     */
    private Integer updateAdmin;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;
}