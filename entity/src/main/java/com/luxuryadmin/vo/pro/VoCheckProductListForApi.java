package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="盘点作品集合显示", description="盘点作品集合显示")
public class VoCheckProductListForApi {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id", name = "id;")
    private Integer id;
    /**
     * 商品唯一标识符,业务id,此id对外开放
     */
    @ApiModelProperty(value = "shp_shop店铺业务id,此id对外开放", name = "bizId")
    private String bizId;
    /**
     * pro_product商品id
     */
    @ApiModelProperty(value = "pro_product商品id", name = "fkProProductId")
    private Integer fkProProductId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    /**
     * 缩略图地址
     */
    @ApiModelProperty(value = "缩略图地址", name = "smallImg")
    private String smallImg;

    /**
     * 该商品总库存;
     */
    @ApiModelProperty(value = "该商品总库存", name = "totalNum")
    private Integer totalNum;

    /**
     * 盘点类型 缺失:0  存在:1
     */
    @ApiModelProperty(value = "盘点类型 缺失:0  存在:1", name = "checkState")
    private String checkType;

    /**
     * 状态设置 已盘点yes 未盘点no
     */
    @ApiModelProperty(value = "已盘点yes 未盘点no", name = "checkState")
    private String checkState;

    /**
     * 商品属性;短名称
     */
    @ApiModelProperty(value = "商品属性;短名称", name = "attributeShortCn")
    private String attributeShortCn;

    /**
     * 适用人群
     */
    @ApiModelProperty(value = "适用人群", name = "targetUser")
    private String targetUser;

    @ApiModelProperty(value = "临时仓id", name = "tempId")
    private Integer tempId;

    /**
     * 所在临时仓
     */
    @ApiModelProperty(value = "所在临时仓", name = "tempName")
    private String tempName;

    @ApiModelProperty(value = "临时仓商品状态 0:正常；1:已售出；2:已售罄", name = "tempProState")
    private String tempProState;

    @ApiModelProperty(value = "临时仓商品状态 0:正常；1:已售出；2:已售罄", name = "tempProStateName")
    private String tempProStateName;
    @ApiModelProperty(value = "盘点类型 temp:临时仓； warehouse:仓库", name = "type")
    private String type;

}
