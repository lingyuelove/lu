package com.luxuryadmin.entity.ord;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单记录表
 *
 * @author monkey king
 * @date 2019/12/01 04:54:52
 */
@Data
public class OrdOrder {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    private String number;

    /**
     * 状态  -90 已删除(不计入统计); -20:已退款; -10:已取消开单;  10：开单中  11: 预定中  20：已售出;
     */
    private String state;

    /**
     * 类型  YS:友商订单; DL:代理订单; KH:客户订单; QT:其它订单
     */
    private String type;

    /**
     * 开单类型  normal:普通开单; quick:快速开单（不在库开单） temp|临时仓开单
     */
    private String openType;

    /**
     * 销售途径
     */
    private String saleChannel;

    /**
     * 订单总数量;
     */
    private Integer totalNum;

    /**
     * 最终成交价(分)
     */
    private BigDecimal finishPrice;

    /**
     * 定金(分)
     */
    private BigDecimal preMoney;

    /**
     * 尾款(分)
     */
    private BigDecimal lastMoney;

    /**
     * 商品卖出时间
     */
    private Date finishTime;

    /**
     * 商品独立(唯一)编码,用户自填;
     */
    private String uniqueCode;

    /**
     * 发货详细地址
     */
    private String toAddress;

    /**
     * 订单收据状态: -10: 不开收据; 10:未开收据; 20:已开收据(机打); 21:已开收据(手写)
     */
    private String printState;

    /**
     * 销售员昵称
     */
    private String saleNickname;

    /**
     * 销售人员id
     */
    private Integer fkShpUserId;

    /**
     * pro_product的主键Id,逻辑id,软件内部关联
     */
    private Integer fkProProductId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 临时仓id,主键id
     */
    private Integer fkProTempId;

    /**
     * 运营模块--渠道的主键id
     */
    private Integer fkOpChannelId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 开单人员用户id
     */
    private Integer insertAdmin;

    /**
     * 开单人员昵称
     */
    private String insertNickname;

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
     * 删除备注
     */
    private String deleteRemark;
    /**
     * 备注
     */
    private String remark;

    /**
     * 售后保障字段，多个用英文逗号隔开
     */
    private String afterSaleGuarantee;

    /**
     * 退货原因
     */
    private String cancelReason;

    /**
     * 退货人
     */
    private Integer cancelPerson;

    /**
     * 退货时间
     */
    private Date cancelTime;

    /**
     * 扣款凭证图片URL
     */
    private String deductVoucherImgUrl;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    private String entrustState;

    /**
     * 结款凭证
     */
    private String entrustImg;

    /**
     * 结款备注
     */
    private String entrustRemark;

    /**
     * 年化收益率
     */
    private String yearRate;

    /**
     * 结款人
     */
    private Integer entrustAdmin;

    /**
     * 结款时间
     */
    private Date entrustTime;

    /**
     * 锁单id
     */
    private Integer fkProLockRecordId;
}