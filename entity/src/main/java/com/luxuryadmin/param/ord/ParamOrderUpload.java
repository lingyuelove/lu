package com.luxuryadmin.param.ord;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 确认开单--前端接收参数模型
 *
 * @author monkey king
 * @Date 2019-12-26 02:15:20
 */
@ApiModel(description = "确认开单--前端接收参数模型")
@Data
public class ParamOrderUpload extends ParamToken {

    /**
     * 防重复提交vid
     */
    @ApiModelProperty(value = "初始化订单列表时下发的vid;", name = "vid", required = true)
    @Length(min = 32, max = 32, message = "vid--参数错误")
    private String vid;

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "商品业务逻辑id;", name = "bizId", required = true)
    private String bizId;

    @ApiModelProperty(value = "临时仓id;", name = "tempId", required = false)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[tempId]参数非法!")
    private String tempId;

    /**
     * 独立编码
     */
    @ApiModelProperty(value = "独立编码;", name = "uniqueCode", required = false)
    private String uniqueCode;

    /**
     * 订单类型
     */
    @ApiModelProperty(value = "订单类型", name = "orderType", required = false)
    @NotBlank(message = "订单类型不允许为空")
    private String orderType;

    /**
     * 成交价格
     */
    @ApiModelProperty(value = "成交价格", name = "finalPrice", required = true)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "成交价格格式错误")
    //@NotBlank(message = "成交价格不允许为空")
    @Length(max = 15, message = "价格超出范围!")
    private String finalPrice;

    /**
     * 开单数量
     */
    @ApiModelProperty(value = "开单数量", name = "totalNum", required = true)
    @Min(value = 1, message = "数量不能小于1")
    @Max(value = 999, message = "数量最大为999")
    private String totalNum;

    /**
     * 开单人员id
     */
    @ApiModelProperty(value = "开单人员id", name = "userNumber", required = true)
    @Pattern(regexp = "^\\d{1,9}$", message = "开单人员id格式错误")
    @NotBlank(message = "开单人员id不允许为空")
    private String userId;

    /**
     * 销售途径
     */
    @ApiModelProperty(value = "销售途径", name = "saleChannel", required = false)
    private String saleChannel;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "note", required = false)
    private String note;

    /**
     * 收件人姓名
     */
    @ApiModelProperty(value = "收件人姓名", name = "customer", required = false)
    private String customer;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式", name = "contact", required = false)
    private String contact;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "address", required = false)
    private String address;

    /**
     * 售后保障
     */
    @ApiModelProperty(value = "售后保障", name = "afterSaleGuarantee", required = false)
    private String afterSaleGuarantee;

    /**
     * 合并后的收货地址
     */
    @ApiModelProperty(value = "合并后的收货地址", name = "receiveAddress", required = false)
    private String receiveAddress;

    /**
     * 销售时间
     */
    @ApiModelProperty(value = "销售时间", name = "saleTime", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleTime;

    /**
     * 销售时间
     */
    @ApiModelProperty(value = "销售时间", name = "saleTime", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String sale;

    /**
     * 扣款凭证图片URL
     */
    @ApiModelProperty(value = "扣款凭证图片URL,用逗号隔开", name = "deductVoucherImgUrl", required = false)
    private String deductVoucherImgUrl;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    @ApiModelProperty(value = "结款状态  -1:未填写| 0:未结款 | 1:已结款", name = "entrustState", required = false)
    private String entrustState;

    /**
     * 结款凭证
     */
    @ApiModelProperty(value = "结款凭证", name = "entrustImg", required = false)
    private String entrustImg;

    /**
     * 结款备注
     */
    @ApiModelProperty(value = "结款备注", name = "entrustRemark", required = false)
    @Length(max = 50, message = "结款备注必须≤50个字符")
    private String entrustRemark;

    @ApiModelProperty(value = "回收人员id", name = "recycleAdmin", required = false)
    private Integer recycleAdmin;

    @ApiModelProperty(value = "定金(分)", name = "preMoney")
    private String preMoney;

    @ApiModelProperty(value = "尾款(分)", name = "lastMoney")
    private String lastMoney;

    @ApiModelProperty(value = "锁单记录表id", name = "lockId")
    private String lockId;

    @ApiModelProperty(value = "地址id", name = "addressId")
    private String addressId;


    @ApiModelProperty(value = "店铺id", name = "shopId",hidden = true)
    private Integer shopId;
}
