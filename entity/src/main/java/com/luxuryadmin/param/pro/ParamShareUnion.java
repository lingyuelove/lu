package com.luxuryadmin.param.pro;

import com.luxuryadmin.common.aop.check.DateTime;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 保存分享产品--查询参数实体
 *
 * @author monkey king
 * @date 2020-06-13 17:14:05
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "保存分享产品--查询参数实体")
@Data
public class ParamShareUnion extends ParamToken {

    /**
     * 商品id,多个用英文逗号隔开;例如: 10001,10002
     */
    @ApiModelProperty(name = "proIds", required = true,
            value = "商品id,多个用英文逗号隔开;例如: 10001,10002 全点分享则填写'all' ")
    @DateTime
    @NotBlank(message = "有效期不允许为空")
    private String endTime;


    /**
     * shp_user的id字段,主键id
     */
    @ApiModelProperty(name = "userId", value = "关联人的用户id")
    @NotNull(message = "关联人员不允许为空")
    @Pattern(regexp = "^[0-9]{5,}$", message = "关联人员参数错误")
    private String userId;

    /**
     * 小程序封面
     */
    @ApiModelProperty(name = "mpImg", value = "小程序封面")
    private String mpImg;

}
