package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 修改添加友商请求记录--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "修改添加友商请求记录--前端接收参数模型")
public class ParamBecomeLeaguer extends ParamToken {

    /**
     * 友商店铺id
     */
    @ApiModelProperty(value = "友商店铺id", name = "leaguerShopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[leaguerShopId]参数非法!")
    @NotBlank(message = "[leaguerShopId]不允许为空!")
    private String leaguerShopId;

    /**
     * 是否允许友商查看店铺商品; 0:不允许; 1:允许
     */
    @ApiModelProperty(value = "是否允许友商查看店铺商品; 0:不允许; 1:允许", required = true)
    @Pattern(regexp = "^[01]$", message = "[visible]参数非法!")
    @NotBlank(message = "[visible]不允许为空!")
    private String visible;


    /**
     * 好友状态: -10:已删除该条信息; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:已成为好友"
     */
    @ApiModelProperty(value = "备注", name = "note", required = false)
    @Length(max = 18, message = "备注长度不得超过18字符")
    private String note;


    public String getLeaguerShopId() {
        return leaguerShopId;
    }

    public void setLeaguerShopId(String leaguerShopId) {
        this.leaguerShopId = leaguerShopId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
}
