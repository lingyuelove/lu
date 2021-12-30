package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamProductLockRemark
 * @Author: ZhangSai
 * Date: 2021/7/1 21:39
 */
@Data
@ApiModel(description = "锁单详情--编辑锁单内容")
public class ParamProductLockRemark extends ParamToken {

    /**
     * 锁单原因
     */
    @ApiModelProperty(name="lockReason",required = false,value = "锁单原因")
    private String lockReason;


    /**
     * 锁单记录id
     */
    @ApiModelProperty(name = "lockId", required = false, value = "锁单记录id")
    @Pattern(regexp = "^\\d{5,9}$", message = "lockId--参数错误")
    private String lockId;

    /**
     * 商品业务逻辑id
     */
    @ApiModelProperty(name = "bizId", required = true, value = "商品业务逻辑id;")
    @NotBlank(message = "bizId--参数错误")
    private String bizId;


}
