package com.luxuryadmin.entity.ord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 分享电子凭证
 *
 * @author monkey king
 * @date   2020-09-04 19:49:55
 */
@Data
public class OrdShareReceipt {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * shp_user的id字段,主键id
     */
    private Integer fkShpUserId;

    /**
     * 店铺编号;对外显示
     */
    private String shopNumber;

    /**
     * 员工编号;对外显示
     */
    private Integer userNumber;

    /**
     * 是否显示店铺商品;0:不显示; 1:显示
     */
    private String showProduct;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 分享批号
     */
    private String shareBatch;

    /**
     * 分享名称
     */
    private String shareName;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 收据类型
     */
    private String receiptType;

    /**
     * 展示类型 all 全部显示 no 全部不显示 content：只展示内容
     */
    private String showType;

    @ApiModelProperty(name = "showProduct", required = false,
            value = "是否显示分享物流信息;0:不显示; 1:显示")
    private String showDeliver;
}