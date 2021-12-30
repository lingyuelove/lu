package com.luxuryadmin.entity.fin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金流水记录表
 *
 * @author monkey king
 * @date   2019/12/27 20:32:30
 */
@Data
public class FinFundRecord {
    /**
     * 主键Id
     */
    private Integer id;

    private Integer fkFinBillId;
    /**
     * 交易金额(分)
     */
    private BigDecimal money;

    /**
     * 成本价(分)
     */
    private BigDecimal initPrice;

    /**
     * 状态 0:收入; 1:支出
     */
    private String state;

    /**
     * 流水发生途径 10:app内部交易; 20:外部导入(用户通过app自建账单);21:外部导入(通过web后台导入excel);30:其它途径
     */
    private String way;

    /**
     * 分类名称+shopId和fin_classify表对应;
     */
    private String finClassifyName;

    /**
     * 分类名称 10 入库商品；20:商品销售; 30:维修服务;40薪资;50:其他收支;60:质押商品
     */
    private String fundType;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * ord_order的主键Id,逻辑id,软件内部关联;0代表没有该订单记录;
     */
    private Integer fkOrdOrderId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 账单发生时间
     */
    private Date happenTime;

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


}