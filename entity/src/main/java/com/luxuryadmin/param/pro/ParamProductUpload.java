package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 上传商品--前端接收参数模型
 *
 * @author monkey king
 * @date 2019-12-23 13:56:43
 */
@ApiModel(description = "上传商品参数实体")
@Data
public class ParamProductUpload extends ParamProductQuery {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 商品属性
     */
    @ApiModelProperty(value = "商品属性;10:自有商品; 20:寄卖商品; 30:质押(典当)商品; 40:其他商品", name = "attribute", required = true)
    @NotBlank(message = "商品属性不允许为空")
    private String attribute;

    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存;不填默认为1", name = "totalNum", required = false)
    @Min(value =1,message = "库存不能小于1")
    @Max(value =999,message = "库存数量最大为999")
    @NotBlank(message = "商品库存不允许为空")
    @Pattern(regexp = "^[0-9]+$", message = "商品库存-格式错误")
    private String totalNum;

    /**
     * 商品状态
     */
    @ApiModelProperty(value = "商品状态;10:保存到库存;11:保存并下架;20:立即上架", name = "state", required = true)
    @Pattern(regexp = "^\\d{2}$", message = "商品状态校验错误")
    @NotBlank(message = "商品状态不允许为空")
    private String state;

    /**
     * 防重复提交vid
     */
    @ApiModelProperty(value = "初始化上传商品时下发的vid;", name = "vid", required = true)
    @Length(min = 32, max = 32, message = "vid--参数错误")
    private String vid;

    /**
     * 商品图片;
     */
    @ApiModelProperty(value = "商品图片地址,多个用分号隔开;eg:/product/abc.png;/product/efg.png", name = "proImgUrl", required = true)
    @Length(max = 1000, message = "商品图片长度必须≤1000个字符")
    private String proImgUrl;

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(value = "商品业务逻辑id;更新商品时,请赋值;", name = "bizId", required = false)
    private String bizId;

    /**
     * 缩略图
     */
    @ApiModelProperty(value = "商品缩略图片;eg:/product/efg.png", name = "smallImgUrl", required = false)
    @Length(max = 250, message = "缩略图长度必须≤250个字符")
    private String smallImgUrl;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称(传中文到服务器)", name = "name", required = false)
    @Length(max = 50, message = "商品名称长度必须≤50个字符")
    private String name;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述", name = "description", required = false)
    @Length(max = 250, message = "商品描述长度必须≤250个字符")
    private String description;

    /**
     * 商品来源
     */
    @ApiModelProperty(value = "商品来源", name = "source", required = false)
    @Length(max = 20, message = "商品来源长度必须≤20个字符")
    private String source;

    /**
     * 商品成色
     */
    @ApiModelProperty(value = "商品成色(传code值到到服务器);", name = "quality", required = false)
    @Length(max = 4, message = "商品成色必须≤4个字符")
    private String quality;

    /**
     * 适用人群
     */
    @ApiModelProperty(value = "适用人群(传中文到服务器)", name = "targetUser", required = false)
    @Length(max = 4, message = "适用人群必须≤4个字符")
    private String targetUser;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类;例如: WB,ZB,XB,XX,PS,QT", name = "classify", required = false)
    private String classify;

    /**
     *
     */
    @ApiModelProperty(value = "商品二级分类id",name = "classifySub",required = false)
    private String classifySub;

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

    /**
     * 产品质押结束时间
     */
    @ApiModelProperty(value = "产品质押结束时间", name = "saveEndTime", required = false)
    private String saveEndTime;

    /**
     * 有无保卡
     */
    @ApiModelProperty(value = "保卡;0:没有; 1:有; ", name = "repairCard", required = false)
    private String repairCard;

    /**
     * 保卡有效时间
     */
    @ApiModelProperty(value = "保卡有效时间", name = "repairCardTime", required = false)
    private String repairCardTime;

    /**
     * 独立编码
     */
    @ApiModelProperty(value = "独立编码,多个用分号隔开;", name = "uniqueCode", required = false)
    private String uniqueCode;

    /**
     * 保卡图片地址
     */
    @ApiModelProperty(value = "保卡图片地址,多个用分号隔开;", name = "cardCodeImgUrl", required = false)
    @Length(max = 1000, message = "保卡图片长度必须≤1000个字符")
    private String cardCodeImgUrl;

    /**
     * 成本价
     */
    @ApiModelProperty(value = "成本价;不填默认为0", name = "initPrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "成本价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String initPrice;

    /**
     * 友商价
     */
    @ApiModelProperty(value = "友商价;不填默认为0", name = "tradePrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "友商价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String tradePrice;

    /**
     * 代理价
     */
    @ApiModelProperty(value = "代理价;不填默认为0", name = "agencyPrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "代理价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String agencyPrice;

    /**
     * 销售价(卖客价)
     */
    @ApiModelProperty(value = "卖客售价(卖客价);不填默认为0", name = "salePrice", required = false)
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "销售价-格式错误")
    @Length(max = 15,message = "价格超出范围!")
    private String salePrice;

    /**
     * 商品标签
     */
    @ApiModelProperty(value = "商品标签;多个用分号隔开", name = "tag", required = false)
    @Length(max = 250, message = "商品标签长度必须≤250个字符")
    private String tag;

    /**
     * 备注来源
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    @Length(max = 250, message = "备注来源长度必须≤250个字符")
    private String remark;


    /**
     * 备注图片地址
     */
    @ApiModelProperty(value = "备注图片地址,多个用分号隔开;", name = "remarkImgUrl", required = false)
    @Length(max = 1000, message = "备注图片地址长度必须≤1000个字符")
    private String remarkImgUrl;



    /**
     * 商品附件
     */
    @ApiModelProperty(value = "商品附件", name = "accessory", required = false)
    @Length(max = 100, message = "商品附件必须≤100个字符")
    private String accessory;


    /**
     * 商品委托方--姓名
     */
    @ApiModelProperty(value = "商品委托方--姓名(限制10个字符以内)", name = "cName", required = false)
    @Length(max = 10, message = "委托方姓名必须≤10个字符")
    private String cName;

    /**
     * 商品委托方--手机号码
     */
    @ApiModelProperty(value = "商品委托方--手机号码", name = "cPhone", required = false)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "委托方手机号码格式错误")
    private String cPhone;

    /**
     * 商品委托方--备注
     */
    @ApiModelProperty(value = "商品委托方--备注", name = "cRemark", required = false)
    @Length(max = 250, message = "委托方备注必须≤250个字符")
    private String cRemark;

    /**
     * 商品委托方--信息
     */
    @ApiModelProperty(value = "商品委托方--信息", name = "customerInfo", required = false)
    @Length(max = 250, message = "委托方信息必须≤250个字符")
    private String customerInfo;

    /**
     * 成本价备注
     */
    @ApiModelProperty(value = "成本价备注", name = "changeInitPriceRemark", required = false)
    @Length(max = 250, message = "成本价备注必须≤250个字符")
    private String changeInitPriceRemark;

    /**
     * 回收人员
     */
    @ApiModelProperty(value = "回收人员", name = "recycleAdmin", required = false)
    private Integer recycleAdmin;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id", name = "dynamicId", required = false)
    private String dynamicId;

    /**
     * 是否分享产品；10：不分享；20：分享给友商; 21:分享给代理; 22:分享给所有人(任何一级分享都可以分享给用户看,除非不分享)
     */
    @ApiModelProperty(value = "是否同步到友商相册;10:不同步;22:同步分享", name = "recycleAdmin", required = true)
    @Pattern(regexp = "^(10)|(22)$", message = "[shareState]--参数错误")
    private String shareState;

    /**
     * 视频地址
     */
    @ApiModelProperty(value = "视频地址", name = "videoUrl", required = false)
    @Length(max = 250, message = "缩略图长度必须≤250个字符")
    private String videoUrl;

    @ApiModelProperty(value = "新增商品补充信息集合 json转Stringid和content参数 “[{\"id\":10111,\"content\":\"是\"},{\"id\":10112,\"content\":\"否\"}]”", name = "productClassifySunAddLists", required = false)
    private String productClassifyAddLists;
    @Override
    public String toString() {
        return "name:" + name + ";Classify:" + classify + ";proImgUrl:" + proImgUrl;
    }
}
