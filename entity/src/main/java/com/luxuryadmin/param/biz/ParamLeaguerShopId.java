package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 删除友商请求记录--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "修改添加友商请求记录--前端接收参数模型")
public class ParamLeaguerShopId extends ParamToken {

    /**
     * 友商店铺id
     */
    @ApiModelProperty(value = "友商店铺id", name = "leaguerShopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[leaguerShopId]参数非法!")
    @NotBlank(message = "[leaguerShopId]不允许为空!")
    private String leaguerShopId;


    public String getLeaguerShopId() {
        return leaguerShopId;
    }

    public void setLeaguerShopId(String leaguerShopId) {
        this.leaguerShopId = leaguerShopId;
    }

}
