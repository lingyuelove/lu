package com.luxuryadmin.param.pro;


import com.luxuryadmin.common.aop.check.DateTime;
import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 商品页面查询参数实体--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商品页面查询参数实体")
@Data
public class ParamProductQuery extends ParamBasic {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;normal(默认排序);price(价格排序);time(入库时间排序);updateTime(更新时间排序);notDown(最久未一键下载);retrieveTime(取回时间排序)")
    //@Pattern(regexp = "^(normal)|(price)|(time)|(updateTime)|(notDown)|(finishTime)|(finishPrice)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true, value = "排序顺序;desc(降序) | asc(升序)",
            allowableValues = "desc,asc")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;

    /**
     * 状态编码;notRelease:未上架(10-19);onRelease:已上架(20-39包含锁单中);<br/>
     * onSale:在售商品(20-29不包含锁单中);onLock:锁单商品(30-39);saleOut:已售商品(40-49);<br/>
     * store:仓库(10-39不包含已售出);all:全部商品(不包含删除10-49);
     */
    @ApiModelProperty(name = "stateCode", required = false,
            value = "状态编码;notRelease:未上架(10-19);onRelease:已上架(20-39包含锁单中);" +
                    "onSale:在售商品(20-29不包含锁单中);onLock:锁单商品(30-39);saleOut:已售商品(40-49);" +
                    "store:仓库(10-39不包含已售出);all:全部商品(不包含删除10-49);")
    @Pattern(regexp = "^([0-9]{2})|(notRelease)|(onRelease)|(onSale)|(onLock)|(saleOut)|(store)|(all)|(notExpire)|(expire)|(redeem)$", message = "状态编码--参数错误")
    private String stateCode;

    /**
     * 适用人群
     */
    @ApiModelProperty(name = "targetUser", required = false, value = "适用人群;(通用;男;女)可多选,分号隔开")
    @Pattern(regexp = "^[通用男女;]{1,7}$", message = "适用人群--参数错误")
    private String targetUser;

    /**
     * 属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品)"
     */
    @ApiModelProperty(name = "attributeCode", required = false,
            value = "属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品)")
    @Pattern(regexp = "^[0-4;]{2,}$", message = "属性编码--参数错误")
    private String attributeCode;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);")
    @Length(min = 2, max = 20, message = "分类编码长度在2~20个字符")
    private String classifyCode;

    /**
     * 动态列表id
     */
    @ApiModelProperty(name = "dynamicId", required = false, value = "动态列表id")
    private String dynamicId;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;


    /**
     * 商品成色;
     */
    @ApiModelProperty(name = "sortValue", required = false,
            value = "商品成色;(N;S;A;B;C;D)可多选,分号隔开")
    @Pattern(regexp = "^[NSABCD;]{1,12}$", message = "适用人群--参数错误")
    private String quality;

    /**
     * 保卡选项;0:没有; 1:有;
     */
    @ApiModelProperty(name = "repairCard", required = false, value = "保卡选项;(0:没有; 1:有;)")
    @Pattern(regexp = "^[01]?$", message = "保卡选项--参数错误")
    private String repairCard;

    /**
     * 入库开始日期
     */
    @ApiModelProperty(name = "uploadStDateTime", required = false, value = "入库开始日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "入库开始日期-参数错误")
    private String uploadStDateTime;

    /**
     * 入库结束日期
     */
    @ApiModelProperty(name = "uploadEtDateTime", required = false, value = "入库结束日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "入库结束日期-参数错误")
    private String uploadEtDateTime;

    /**
     * 价格类型
     */
    @ApiModelProperty(name = "priceType", required = false,
            value = "价格类型;切换筛选价格时,需要把此类型传到服务器(initPrice:成本价格;salePrice:销售价格)")
    @Pattern(regexp = "^(initPrice)|(salePrice)$", message = "价格类型--参数错误")
    private String priceType;

    /**
     * 最低价格
     */
    @ApiModelProperty(name = "priceMin", required = false, value = "最低价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最低价格--参数错误")
    private String priceMin;

    /**
     * 最高价格
     */
    @ApiModelProperty(name = "priceMax", required = false, value = "最高价格;")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "最高价格--参数错误")
    private String priceMax;

    /**
     * 回收人员id
     */
    @ApiModelProperty(name = "recycleUserId", required = false, value = "回收人员id;多个用分号隔开")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "回收人员--参数错误")
    private String recycleUserId;

    /**
     * 入库人员id
     */
    @ApiModelProperty(name = "uploadUserId", required = false, value = "入库人员id;")
    @Pattern(regexp = "^[0-9]{5,}$", message = "入库人员--参数错误")
    private String uploadUserId;

    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    /**
     * userId;此值在控制层赋值(获取登录者的userId)
     */
    @ApiModelProperty(hidden = true)
    private Integer userId;

    /**
     * 最久未被一键下载;此值在控制层赋值
     */
    @ApiModelProperty(hidden = true)
    private String notDown;

    /**
     * 最久未被一键下载的用户id;此值在控制层赋值
     */
    @ApiModelProperty(hidden = true)
    private Integer notDownUserId;

    /**
     * 今天日期;格式 yyyy-MM-dd
     */
    @ApiModelProperty(hidden = true)
    private String todayDate;

    /**
     * 商品唯一编码,只有在查询商品列表时,此值才会赋值;<br/>
     * 结合pro_detail使用, 表的别名必须是det
     */
    @ApiModelProperty(hidden = true)
    private String uniqueCode;

    /**
     * 数据库的排序
     */
    @ApiModelProperty(hidden = true)
    private String sortKeyDb;

    @ApiModelProperty(value = "商品二级分类",name = "classifySub",required = false)
    private String classifySub;

    @ApiModelProperty(value = "更新人",name = "updateAdmin",required = false)
    private String updateAdmin;


    @ApiModelProperty(name = "retrieveStTime", required = false, value = "取回开始日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "取回开始日期-参数错误")
    private String retrieveStTime;

    /**
     * 取回结束日期
     */
    @ApiModelProperty(name = "retrieveEnTime", required = false, value = "取回结束日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "取回结束日期-参数错误")
    private String retrieveEnTime;

    @ApiModelProperty(name = "retrieveUserId", required = false, value = "取回人员id;")
//    @Pattern(regexp = "^[0-9]{5,}$", message = "取回人员--参数错误")
    private String retrieveUserId;

    @ApiModelProperty(name = "customerUser", hidden = true, value = "托管客户信息后端自行判断;")
    private String customerUser;

    /**
     * 任务类型：in：导入，out：导出
     */
    @ApiModelProperty(name = "exportType", hidden = true, value = "商家后台导出类型;")
    private String exportType;

    /**
     * 模块；订单模块；商品模块；账单模块，其它模块
     */
    @ApiModelProperty(name = "exportModule", hidden = true, value = "商家后台导出模块;")
    private String exportModule;

    @ApiModelProperty(value = "临时仓id--2.6.4", name = "tempId", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String tempId;


    @ApiModelProperty(value = "寄卖传送id--2.6.4", name = "conveyId", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "临时仓id--参数错误")
    private String conveyId;

    @ApiModelProperty(value = "寄卖传送类型 convey寄卖传送 warehouse仓库商品 conveySend发送方", name = "conveyState", required = false)
    private String conveyState;
}
