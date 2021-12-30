package com.luxuryadmin.param.mem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author ZhangSai
 * @Classname 会员店铺查询类
 * @Description TODO
 * @Date 2021/3/26 15:57
 * @Created by ZhangSai
 */
@Data
public class ParamMemShopOrder {
    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)

    @Max(value = 999, message = "当前页最大为999")
    private int pageNum;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)

    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize;

    /**
     * 开通时间 起始
     */
    @ApiModelProperty(value = "查询条件中-支付时间范围-开始", name = "payTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 开通时间 结束
     */
    @ApiModelProperty(value = "查询条件中-支付时间范围-结束", name = "payTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 会员购买订单id
     */
    @ApiModelProperty(value = "会员购买订单id", name = "transactionId", required = false)
    private String transactionId;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", name = "phone", required = false)
    private String phone;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
     */
    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = false)
    private String shopNumber;

    /**
     * -99:删除支付订单;10:待支付,11:主动取消支付,12:超时自动取消; 20:支付失败; 30:退款中,31:退款成功,32:退款失败; 33:已支付;40:支付成功
     */
    @ApiModelProperty(value = "会员订单状态：;10:待支付,11取消支付;20:支付失败; 30:退款; 40:支付成功", name = "state", required = false)
    private Integer state;


    /**
     * 支付通道; weixin、alipay、other
     */
    @ApiModelProperty(value = "支付通道;微信： weixin、支付宝：alipay、后台添加：other", name = "payChannel", required = false)
    private String payChannel;

    /**
     *  会员类型：0:年费会员;
     */
    @ApiModelProperty(value = "会员类型：0:年费会员;", name = "memberType", required = false)
    private Integer memberType;
}
