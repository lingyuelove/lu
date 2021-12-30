package com.luxuryadmin.entity.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表;该表和(pro_detail)一对一关系
 *
 * @author monkey king
 * @date   2019/12/01 04:55:08
 */
@Data
public class ProProduct {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 商品状态表的code ：-90：已删除(不记入账本)；已取回（仅寄卖商品有此状态）
     * 10:未上架(库存)；11:未上架(上架后人工下架);
     * 12:未上架(商品存于保管期间); 20:已上架；
     * 21:再次上架; 30:正在交易(锁单)
     * 40:已售出(零售)；41:已售出(代理);
     * 42:已售出(友商);
     */
    private Integer fkProStateCode;

    /**
     * 商品属性表的code
     */
    private String fkProAttributeCode;

    /**
     * 商品分类id; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifyCode;


    /**
     * 商品二级分类id; 和分类列表对应; 默认0:无分类;
     */
    private String fkProClassifySubName;

    /**
     * 商品分类系列;
     */
    private String fkProSubSeriesName;

    /**
     * 商品分类型号
     */
    private String fkProSeriesModelName;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品成色
     */
    private String quality;

    /**
     * 适用人群
     */
    private String targetUser;

    /**
     * 标签;多个用分号隔开;
     */
    private String tag;

    /**
     * 该商品总库存;
     */
    private Integer totalNum;


    /**
     * 卖出库存
     */
    private Integer saleNum;

    /**
     * 是否推荐商品；0：不推荐；1：推荐; 目前预留字段
     */
    private String hot;

    /**
     * 是否推荐商品；10：不分享；20：分享给友商; 21:分享给代理; 22:分享给所有人(任何一级分享都可以分享给用户看,除非不分享)
     */
    private String share;

    /**
     * 成本价(分)
     */
    private BigDecimal initPrice;

    /**
     * 友商价(分)
     */
    private BigDecimal tradePrice;

    /**
     * 代理价(分)
     */
    private BigDecimal agencyPrice;

    /**
     * 销售价(分)
     */
    private BigDecimal salePrice;

    /**
     * 公价
     */
    private Integer publicPrice;
    /**
     * 最终成交价(分)
     */
    private BigDecimal finishPrice;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 商品上架时间
     */
    private Date releaseTime;

    /**
     * 锁定时间;(时间结束之前,该商品不能被其他人卖掉)
     */
    private Date lockTime;

    /**
     * 锁单用户id
     */
    private Integer lockUserId;

    /**
     * 商品卖出时间
     */
    private Date finishTime;

    /**
     * 商品质押结束时间
     */
    private Date saveEndTime;

    /**
     * 添加用户_管理员id; -9:微商相册导入; -8:段小狸导入;-7:excel导入;-6大象小程序
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
     * 此状态不影响用户前端展示;商家联盟状态:0:不显示 | 1:显示
     */
    private String unionState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 回收人员
     */
    private Integer recycleAdmin;

    @ApiModelProperty(value = "取回备注", name = "retrieveRemark")
    private String retrieveRemark;

    /**
     * 取回用户
     */
    @ApiModelProperty(value = "取回用户", name = "fkShpRetrieveUserId")
    private Integer fkShpRetrieveUserId;


    /**
     * 取回时间
     */
    @ApiModelProperty(value = "取回时间", name = "retrieveTime")
    private Date retrieveTime;
    /**
     * 删除备注
     */
    private String deleteRemark;

    /**
     * 寄卖传送类型 convey寄卖传送接收方 warehouse仓库商品 conveySend发送方
     */
    private String conveyState;

    /**
     * 寄卖传送id
     */
    private Integer fkProConveyId;
}