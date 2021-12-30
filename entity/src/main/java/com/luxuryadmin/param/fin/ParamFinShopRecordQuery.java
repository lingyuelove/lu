package com.luxuryadmin.param.fin;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.servlet.tags.Param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 薪资提成明细--前端接收参数模型
 *
 * @author sanjin145
 * @date 2020-10-21 22:31:50
 */
@ApiModel(description = "薪资提成明细")
@Data
public class ParamFinShopRecordQuery extends ParamToken {

    /**
     * 店铺ID
     */
    @ApiModelProperty(name = "shopId", required = false, value = "店铺ID")
    private Integer shopId;

    /**
     * 开始流水发生时间
     */
    @ApiModelProperty(name = "happenTimeStart", required = false, value = "开始流水发生时间")
    private String happenTimeStart;

    /**
     * 结束流水发生时间
     */
    @ApiModelProperty(name = "happenTimeStart", required = false, value = "结束流水发生时间")
    private String happenTimeEnd;

    @ApiModelProperty(name = "finShopRecordTypeName", required = false, value = "流水类别名称")
    private String finShopRecordTypeName;

    /**
     * 会员id
     */
    @ApiModelProperty(name = "userId", required = false, value = "userId,如果查询全部, 则不需要传此参数")
    @Pattern(regexp = "^[0-9]+$", message = "userId--参数错误")
    private String userId;

}
