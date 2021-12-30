package com.luxuryadmin.vo.mem;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZhangSai
 * @Classname 会员店铺订单集合显示类
 * @Description TODO
 * @Date 2021/3/26 15:57
 * @Created by ZhangSai
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoMemShopOrder {

    /**
     * 第三方支付订单号
     */
    @ApiModelProperty(value = "交易流水号", name = "payOrderId")
    @ExcelProperty(value = "交易流水号")
    @ColumnWidth(12)
    private String transactionId;

    @ApiModelProperty(value = "手机号码")
    @ExcelProperty(value = "手机号码")
    @ColumnWidth(12)
    private String phone;

    @ApiModelProperty(value = "店铺编号")
    @ExcelProperty(value = "店铺编号")
    @ColumnWidth(12)
    private String shopNumber;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @ExcelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "会员类型：年费会员")
    @ExcelProperty(value = "会员类型：年费会员")
    private String memberType;

    @ApiModelProperty(value = "支付通道; weixin、alipay、other", name = "payChannel", required = false)
    @ExcelProperty(value = "支付通道; weixin、alipay、other")
    private String payChannel;

    /**
     * 实际支付金额(分)
     */
    @ApiModelProperty(value = "支付金额")
    @ExcelProperty(value = "支付金额")
    private Long realMoney;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date insertTime;

    /**
     * -99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功
     */
    @ExcelIgnore
    private Integer state;
    @ExcelProperty(value = "订单状态")
    private String stateCn;
}
