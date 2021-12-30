package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
@Data
@ApiModel(value="盘点作品详情", description="盘点作品详情")
public class VoCheckProductDetailByApi {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id", name = "id")
    private Integer id;

    @ApiModelProperty(name = "bizId", value = "商品业务逻辑id;多个用英文分号隔开")
    private String bizId;
    /**
     * pro_check的主键Id
     */
    @ApiModelProperty(value = "盘点Id", name = "checkId")
    private Integer checkId;
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
     * 盘点类型 缺失:0  存在:1
     */
    @ApiModelProperty(value = "盘点类型 缺失:0  存在:1", name = "checkType")
    private String checkType;

    /**
     * 状态设置 已盘点yes 未盘点no
     */
    @ApiModelProperty(value = "已盘点yes 未盘点no", name = "checkState")
    private String checkState;
    /**
     * 盘点时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "盘点时间", name = "updateTime")
    private String updateTime;

    /**
     * 盘点人
     */
    @ApiModelProperty(value = "盘点人", name = "insertAdmin")
    private Integer updateAdmin;

    /**
     * 盘点人名称
     */
    @ApiModelProperty(value = "盘点人名称", name = "insertAdminName")
    private String updateAdminName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品图片集合
     */
    private String[] productImgList;

    /**
     * 商品状态;英文;
     */
    private String stateUs;

    /**
     * 商品状态;中文;
     */
    private String stateCn;
    /**
     * 商品上架
     */
    private String uPermRelease;

    /**
     * 商品下架
     */
    private String  uPermBackOff;
    @ApiModelProperty(value = "临时仓类型 临时仓:temp  仓库:warehouse", name = "type")
    private String  type;
}
