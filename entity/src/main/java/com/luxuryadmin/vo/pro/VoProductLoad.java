package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 商品VO模型-卡片形式显示-详情显示
 *
 * @author monkey king
 * @date 2019-12-23 18:40:32
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoProductLoad {

    private Integer proId;

    /**
     * 店铺id,属于哪个店铺的商品
     */
    private Integer shopId;

    /**
     * 商品位置id
     */
    private Integer dynamicProductId;
    /**
     * id
     */
    private Integer id;
    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    private String bizId;

    /**
     * 动态名称
     */
    private String dynamicName;

    /**
     * 动态id
     */
    private String dynamicId;

    /**
     * 是否存在 0不存在 1存在
     */
    private String exists;

    /**
     * 名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 标签;多个用分号隔开;
     */
    private String tag;

    /**
     * 是否推荐商品；0：不推荐；1：推荐; 目前预留字段
     */
    private String hot;

    /**
     * 是否分享产品；10：不分享；20：分享给友商; 21:分享给代理; 22:分享给所有人(任何一级分享都可以分享给用户看,除非不分享)
     */
    private String shareState;

    /**
     * 商品状态;英文;
     */
    private String stateUs;

    /**
     * 商品状态;中文;
     */
    private String stateCn;

    /**
     * 商品属性;英文;
     */
    private String attributeUs;

    /**
     * 商品属性;中文
     */
    private String attributeCn;

    /**
     * 商品属性;短名称
     */
    private String attributeShortCn;

    /**
     * 商品分类;英文;
     */
    private String classifyUs;

    /**
     * 商品分类;中文
     */
    private String classifyCn;

    @ApiModelProperty(value = "商品二级分类",name = "classifySub",required = false)
    private String classifySub;

    /**
     * 二级分类名称;用于显示
     */
    private String classifySubName;

    /**
     * 商品提示;
     */
    private String tips;

    /**
     * 总库存
     */
    private Integer totalNum;

    /**
     * 该商品剩余可用库存;
     */
    private Integer leftNum;

    /**
     * 锁单数量;
     */
    private Integer lockNum;

    /**
     * 已卖出商品数量
     */
    private Integer saleNum;

    /**
     * 已卖出临时仓商品数量
     */
    private Integer saleTempNum;

    /**
     * 成本价(分)
     */
    private String initPrice;

    /**
     * 友商价(分)
     */
    private String tradePrice;

    /**
     * 代理价(分)
     */
    private String agencyPrice;

    /**
     * 销售价(分)
     */
    private String salePrice;

    /**
     * 预付定金(分)
     */
    private String preMoney;

    /**
     * 预成交价(分)
     */
    private String preFinishMoney;

    /**
     * 公价
     */
    private String publicPrice;
    /**
     * 成交价格(分)
     */
    private String finishPrice;

    /**
     * 缩略图地址
     */
    private String smallImg;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 显示时间
     */
    private String showTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 商品上架时间(当为质押商品时,就是赎回时间)
     */
    private Date releaseTime;

    /**
     * 锁单记录表的id
     */
    private String lockId;

    /**
     * 锁单人员
     */
    private String lockedUser;

    /**
     * 锁定时间;(时间结束之前,该商品不能被其他人卖掉)
     */
    private Date lockTime;

    /**
     * 商品卖出时间
     */
    private Date finishTime;

    /**
     * 商品质押结束时间
     */
    private Date saveEndTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 保卡;0:没有; 1:有;
     */
    private String repairCard;
    /**
     * 保卡有效时间
     */
    private String repairCardTime;
    /**
     * 商品唯一编码
     */
    private String uniqueCode;
    /**
     * 保卡图片
     */
    private String cardCodeImg;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 保卡图片集合
     */
    private String[] cardCodeImgList;

    /**
     * 商品图片集合
     */
    private String[] productImgList;

    /**
     * 备注图片集合
     */
    private String[] remarkImgList;

    /**
     * 标签集合
     */
    private String[] tags;

    /**
     * 下载状态; 0:未被下载; 1:已被下载
     */
    private String downloadState;

    /**
     * 下载次数
     */
    private Integer downloadNum;


    /**
     * 适用人群
     */
    private String targetUser;

    /**
     * 适用人群
     */
    private String quality;

    /**
     * 商品来源
     */
    private String source;

    /**
     * 附件
     */
    private String accessory;

    /**
     * 入库人员
     */
    private String uploadUser;

    /**
     * 更新人员
     */
    private String updateUser;


    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户备注
     */
    private String customerRemark;

    /**
     * 0: 没有锁单; 1:有锁单记录
     */
    private String lockState;

    /**
     * 0:不是最新; 1:最新
     */
    private String newState;

    /**
     * 回收人员名称
     */
    private String recycleName;

    /**
     * 委托人信息
     */
    private String customerInfo;

    /**
     * 锁单原因
     */
    private String lockReason;

    /**
     * 赎回时间
     */
    private String redeemTime;

    /**
     * 最近更新时间
     */
    private Date recentDownloadTime;

    /**
     * 发货id
     */
    private Integer deliverId;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 备注里的图片
     */
    private String remarkImgUrl;

    /**
     * 所在临时仓
     */
    private String tempName;

    /**
     * 商品货号;商品分类前缀+商品id;eg:WD10050
     */
    private String autoNumber;

    /**
     * 所在临时仓集合
     */
    private List<VoTempForPro> tempForPros;
    /**
     * 临时仓商品列表显示结算价格
     */
    private String showPrice;
    @ApiModelProperty(name = "updateDeleteState", value = "更新删除备注状态 0：不可编辑 1：可编辑")
    private String updateDeleteState;

    @ApiModelProperty(name = "updateRetrieveState", value = "更新取回备注状态 0：不可编辑 1：可编辑")
    private String updateRetrieveState;


    /**
     * 临时仓商品列表显示成本价格
     */
    private String showInitPrice;
    @ApiModelProperty(value = "删除备注", name = "deleteRemark")
    private String deleteRemark;

    private Integer updateUserId;

    /**
     * 取回用户
     */
    @ApiModelProperty(value = "取回用户", name = "retrieveUserId")
    private Integer retrieveUserId;


    /**
     * 取回用户
     */
    @ApiModelProperty(value = "取回用户名", name = "retrieveUserName")
    private String retrieveUserName;

    @ApiModelProperty(value = "取回备注", name = "retrieveRemark")
    private String retrieveRemark;
    /**
     * 取回时间
     */
    @ApiModelProperty(value = "取回时间", name = "retrieveTime")
    private Date retrieveTime;

    private String shopName;

    private String shopNumber;

    /**
     * 商品分类系列;
     */
    @ApiModelProperty(value = "商品分类系列 --2.6.2",name = "subSeriesName",required = false)
    private String subSeriesName;

    /**
     * 商品分类型号
     */
    @ApiModelProperty(value = "商品分类型号 --2.6.2",name = "seriesModelName",required = false)
    private String seriesModelName;

    @ApiModelProperty(value = "补充信息分类集合显示")
    private List<VoClassifyTypeSonList> classifyTypeList;

    /**
     * 此状态不影响用户前端展示;商家联盟状态:0:不显示 | 1:显示
     */
    private String unionState;
    @ApiModelProperty(value = "临时仓状态 1本仓商品 0不是本仓 --2.6.4 ",name = "tempState")
    private String tempState;

    @ApiModelProperty(value = "临时仓商品状态 0:正常；1:已售出；2:已售罄", name = "tempProState")
    private String tempProState;

    @ApiModelProperty(value = "临时仓商品状态 0:正常；1:已售出；2:已售罄", name = "tempProStateName")
    private String tempProStateName;

    /**
     * 寄卖传送类型 convey寄卖传送接收方 warehouse仓库商品 conveySend发送方
     */
    @ApiModelProperty(value = "寄卖传送类型 convey寄卖传送接收方 warehouse仓库商品 conveySend发送方", name = "conveyState")
    private String conveyState;

    @ApiModelProperty(value = "寄卖传送类型状态 1:待提取 2:已提取 3:已确认 4:待确认", name = "conveyStateType")
    private String conveyStateType;

    @ApiModelProperty(value = "商品状态公用（寄卖传送：1:锁单；2:删除；3:已售罄） ", name = "proState")
    private String proState;

    @ApiModelProperty(value = "商品状态公用（寄卖传送：1:锁单；2:删除；3:已售罄）", name = "proStateName")
    private String proStateName;

    /**
     * 寄卖传送id
     */
    private Integer conveyId;

    private Integer conveyNum;
}
