package com.luxuryadmin.param.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "记一笔更新实体类")
public class ParamFinShopRecordUpdate {


    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = true, value = "主键ID")
//    @NotBlank(message="主键ID不能为空")
    private Integer id;


    @ApiModelProperty(name = "shopId", required = false, value = "店铺id")
    private Integer shopId;

    @ApiModelProperty(name = "userId", required = false, value = "用户id")
    private Integer userId;

    /**
     * 流水变动金额
     */
    @ApiModelProperty(name = "changeAmount", required = false, value = "流水变动金额 字符串类型")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?$", message = "流水变动金额-格式错误")
    @Length(max = 11,message = "价格超出范围!")
    private String changeAmount;

    /**
     * 流水类别
     */
    @ApiModelProperty(name = "type", required = false, value = "流水类别，中文，如【商品销售】，不包含括号")
//    @NotNull(message="流水类别不能为空")
    private String type;

    /**
     * 流水发生日期
     */
    @ApiModelProperty(name = "happenTime", required = false, value = "流水发生日期")
//    @NotNull(message="流水发生日期不能为空")
    private String happenTime;

    /**
     * 流水发生日期
     */
    @ApiModelProperty(name = "note", required = false, value = "备注")
    private String note;

    /**
     * 流水详情图片地址
     */
    @ApiModelProperty(value = "流水详情图片地址", name = "imgUrlDetail", required = false)
    private String imgUrlDetail;
}
