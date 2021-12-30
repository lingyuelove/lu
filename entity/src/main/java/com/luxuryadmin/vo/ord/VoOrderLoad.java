package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单卡片显示
 *
 * @author monkey king
 * @date 2019-12-25 21:13:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoOrderLoad {

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 商品ID
     */
    private Integer fkProProductId;

    /**
     * 商品系统编码
     */
    private String bizId;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 属性名称：10:本店产品; 20:寄卖产品; 30:质押(典当)产品 40:其它
     */
    private String attributeUs;

    /**
     * 商品属性;code字段 中文名称
     */
    private String attributeCn;

    /**
     * 商品分类;英文;
     */
    private String classifyUs;

    /**
     * 商品分类;中文
     */
    private String classifyCn;

    /**
     * 商品适用人群
     */
    private String targetUser;

    /**
     * 商品成色
     */
    private String quality;

    /**
     * 销售总数量
     */
    private Integer totalNum;

    /**
     * 成交价(分)
     */
    private Long salePrice;

    /**
     * 定金(分)
     */
    private Long preMoney;

    /**
     * 尾款(分)
     */
    private Long lastMoney;

    /**
     * 成本价（分）
     */
    private Long initPrice;

    /**
     * 订单流水号
     */
    private String orderBizId;

    /**
     * 订单状态;中文;
     */
    private String state;

    /**
     * 订单状态;中文;
     */
    private String stateCn;

    /**
     * 订单类型
     */
    private String orderType;

    /**
     * 开单类型
     */
    private String openType;

    /**
     * 销售途径
     */
    private String saleChannel;

    /**
     * 销售员用户ID
     */
    private Integer saleUserId;

    /**
     * 销售员昵称
     */
    private String saleNickname;

    /**
     * 销售时间
     */
    private String saleTime;

    /**
     * 开单人员昵称
     */
    private Integer insertAdmin;
    /**
     * 开单人员昵称
     */
    private Integer updateAdmin;
    /**
     * 开单人员昵称
     */
    private String insertNickname;

    /**
     * 订单生成时间
     */
    private Date insertTime;

    /**
     * 打印状态
     */
    private String printStateCn;


    /**
     * 打印时间
     */
    private Date printTime;


    /**
     * 独立编码
     */
    private String uniqueCode;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 收件人姓名
     */
    private String customer;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 地址
     */
    private String address;

    /**
     * 售后保障，多个用英文逗号分隔
     */
    private String afterSaleGuarantee;

    /**
     * 退货原因
     */
    private String cancelReason;

    /**
     * 退货人
     */
    private String cancelPerson;

    /**
     * 退货时间
     */
    private Date cancelTime;

    /**
     * 合并后的订单详情
     */
    private String receiveAddress;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    private String entrustState;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    private String entrustStateName;

    /**
     * 结款凭证
     */
    @ApiModelProperty(value = "结款凭证", name = "entrustImg")
    private String entrustImg;

    @ApiModelProperty(value = "结款凭证集合显示", name = "entrustImgList")
    private List<String> entrustImgList;

    /**
     * 回收人员
     */
    @ApiModelProperty(value = "回收人员id", name = "recycleAdmin")
    private Integer recycleAdmin;

    /**
     * 回收人员
     */
    @ApiModelProperty(value = "回收人员名字", name = "recycleAdminName")
    private String recycleAdminName;
    /**
     * 结款备注
     */
    @ApiModelProperty(value = "结款备注", name = "entrustRemark")
    private String entrustRemark;

    /**
     * 商品属性;短名称 例：寄
     */
    @ApiModelProperty(value = "商品属性短名称 例：寄", name = "attributeShortCn")
    private String attributeShortCn;

    /**
     * 扣款凭证图片URL
     */
    @ApiModelProperty(value = "扣款凭证图片URL,用逗号隔开", name = "deductVoucherImgUrl", required = false)
    private String deductVoucherImgUrl;


    /**
     * 扣款凭证图片URL列表
     */
    @ApiModelProperty(value = "扣款凭证图片URL列表", name = "deductVoucherImgUrlList", required = false)
    private List<String> deductVoucherImgUrlList;


    /**
     * 等同于attributeUs;兼容线上2.4.2;
     */
    private String attributeCodeCn;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String deleteState;

    @ApiModelProperty(value = "删除备注", name = "deleteRemark")
    private String deleteRemark;

    @ApiModelProperty(name = "updateDeleteState", value = "更新删除备注状态 0：不可编辑 1：可编辑")
    private String updateDeleteState;
    @ApiModelProperty(name = "productDeleteState", value = "商品删除状态 0：未删除 1：已删除")
    private String productDeleteState;

    @ApiModelProperty(name = "uPermHistoryDelete", value = "已删除订单删除权限")
    private String uPermHistoryDelete;

    /**
     * 年化收益率(%)
     */
    private String yearRate;

    /**
     * 初次入库时间
     */
    private String uploadStoreTime;
    /**
     * 结款人员
     */
    private String entrustNickname;
    /**
     * 结款时间
     */
    private String entrustTime;

    /**
     * 锁单id
     */
    private Integer lockId;

    /**
     * 锁单备注
     */
    private String lockReason;

    /**
     * 锁单时间
     */
    private String lockTime;

    /**
     * 锁单用户
     */
    private String lockUser;

    /**
     * 预成交价(分)
     */
    private String preFinishMoney;
}
