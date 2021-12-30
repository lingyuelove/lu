package com.luxuryadmin.entity.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 分享商品
 *
 * @author monkey king
 * @date   2020/06/10 02:45:38
 */
@Data
public class ProShare {
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
     * 显示价格;如果为空,则不显示价格;如果不为空,则显示值里面对应的价格
     */
    private String showPrice;

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
     * 分享图片
     */
    private String shareImg;

    /**
     * 分享显示名字
     */
    private String  showName;

    /**
     * pro_product的id字段,多个用英文逗号隔开
     */
    private String proId;

    /**
     * 删除状态 0 未删除 1已删除
     */
    private String del;

    /**
     * 商品分类id; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifyCode;

    /**
     * 0:【LuxurySir】小程序访客;1:【奢当家】小程序访客
     */
    private String type;

    /**
     * 结束时间;如果为空,则长期有效
     */
    private Date endTime;

    /**
     * 商家联盟小程序关联的代理id(ShpUser.id)
     */
    private Integer unionUserId;
}