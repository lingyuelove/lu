package com.luxuryadmin.param.ord;


import com.luxuryadmin.common.aop.check.DateTime;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 订单管理页面查询参数实体--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "订单管理页面查询参数实体")
public class ParamOrderQuery extends ParamToken {


    /**
     * 排序字段
     * normal(默认排序);price(价格排序);time(入库时间排序);notDown(最久未一键下载)
     */
    @ApiModelProperty(name = "sortKey", required = true,
            value = "排序字段;price(价格排序);time(销售时间排序);")
    @Pattern(regexp = "^(price)|(time)|(yearRate)$", message = "排序字段--参数错误")
    private String sortKey;

    /**
     * 排序顺序;desc(降序) | asc(升序)
     */
    @ApiModelProperty(name = "sortValue", required = true,
            value = "排序顺序;desc(降序) | asc(升序)")
    @Pattern(regexp = "^(desc)|(asc)$", message = "排序顺序--参数错误")
    private String sortValue;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "proName", required = false, value = "商品名称;多个关键字请用空格隔开")
    @Length(max = 50, message = "商品名称必须≤50个字符")
    private String proName;

    @ApiModelProperty(name = "state", required = false, value = "状态:获取全部时,不需要传值; 20:已完成  -20:已退款")
    @Pattern(regexp = "^(20)|(-20)$", message = "state--参数错误")
    private String state;

    /**
     * 结款状态  -1:未填写| 0:未结款 | 1:已结款
     */
    @ApiModelProperty(name = "entrustState", required = false, value = "结款状态  -1:未填写| 0:未结款 | 1:已结款")
    private String entrustState;

    /**
     * 回收人员id
     */
    @ApiModelProperty(name = "recycleUserId", required = false, value = "回收人员id;多个用分号隔开")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "销售人员--参数错误")
    private String recycleUserId;

    /**
     * 销售人员id
     */
    @ApiModelProperty(name = "saleUserId", required = false, value = "销售人员id;多个用分号隔开")
    @Pattern(regexp = "^[0-9;]{5,}$", message = "销售人员--参数错误")
    private String saleUserId;

    /**
     * 销售途径
     */
    @ApiModelProperty(name = "saleChannel", required = false, value = "销售途径")
    private String saleChannel;

    /**
     * 订单类型
     */
    @ApiModelProperty(name = "orderType", required = false, value = "YS;DL;KH;QT订单类型;多个用英文分号分割拼接")
    private String orderType;

    /**
     * 属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品)"
     */
    @ApiModelProperty(name = "attributeCode", required = false,
            value = "属性编码(传code值到服务器;10:自有商品;20:寄卖商品;30:质押商品;40:其他商品);如有多个请用分号隔开拼接")
    @Pattern(regexp = "^[12340;]{2,}$", message = "属性编码--参数错误")
    private String attributeCode;

    /**
     * 分类编码;all:全部
     */
    @ApiModelProperty(name = "classifyCode", required = false, value = "分类编码(传code值到服务器);多个请用分号隔开")
    @Length(min = 2, max = 100, message = "分类编码长度在2~100个字符")
    private String classifyCode;

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
     * 销售开始日期
     */
    @ApiModelProperty(name = "saleStDateTime", required = false, value = "销售开始日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "入库开始日期-参数错误")
    private String saleStDateTime;

    /**
     * 销售结束日期
     */
    @ApiModelProperty(name = "saleEtDateTime", required = false, value = "销售结束日期;格式yyyy-MM-dd HH:mm:ss)")
    @DateTime(message = "销售结束日期-参数错误")
    private String saleEtDateTime;

    /**
     * shopId;此值在控制层赋值(获取登录者的shopId)
     */
    @ApiModelProperty(hidden = true)
    private Integer shopId;

    /**
     * 商品唯一编码,只有在查询商品列表时,此值才会赋值;<br/>
     * 结合pro_detail使用, 表的别名必须是det
     */
    @ApiModelProperty(hidden = true)
    private String uniqueCode;

    @ApiModelProperty(name = "customerUser", hidden = true, value = "托管客户信息后端自行判断;")
    private String customerUser;

}
