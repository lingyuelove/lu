package com.luxuryadmin.param.ord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Classname ParamProduct
 * @Description TODO
 * @Date 2020/6/30 09:59
 * @Created by Administrator
 */
@ApiModel(description = "订单管理-订单列表参数实体")
@Data
public class ParamOrdOrder {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 10;

    /**
     * 插入时间 起始
     */
    @ApiModelProperty(value = "查询条件中-创建时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-创建时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    @ApiModelProperty(value = "订单编号", name = "number", required = false)
    private String number;

    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "店铺编号--参数错误")
    private String shopNumber;

    /**
     * 状态  -90 已删除(不计入统计); -20:已退款; -10:已取消开单;  10：开单中  11: 预定中  20：已售出;
     */
    @ApiModelProperty(value = "订单状态：", name = "state", required = false)
    private String state;

    /**
     * pro_product的主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "商品编号", name = "fkProProductId", required = false)
    private Integer fkProProductId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", name = "productName", required = false)
    private String productName;

    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(value = "店铺编号", name = "fkShpShopId", required = false)
    private Integer fkShpShopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称", name = "shopName", required = false)
    private String shopName;

    /**
     * 商品卖出时间
     */
    @ApiModelProperty(value = "查询条件中-销售时间范围-开始", name = "finishTimeStart", required = false)
    private Date finishTimeStart;

    /**
     * 商品卖出时间
     */
    @ApiModelProperty(value = "查询条件中-销售时间范围-截止", name = "finishTimeStart", required = false)
    private Date finishTimeEnd;

}
