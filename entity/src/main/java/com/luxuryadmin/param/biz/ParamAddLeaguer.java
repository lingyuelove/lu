package com.luxuryadmin.param.biz;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 添加友商请求记录--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "添加友商请求记录--前端接收参数模型")
public class ParamAddLeaguer extends ParamToken {

    /**
     * 友商店铺id
     */
    @ApiModelProperty(value = "友商店铺id", name = "leaguerShopId", required = true)
    @Pattern(regexp = "^[0-9]{5,9}$", message = "[leaguerShopId]参数非法!")
    @NotBlank(message = "[leaguerShopId]不允许为空!")
    private String leaguerShopId;


    /**
     * 添加请求信息
     */
    @ApiModelProperty(value = "添加请求信息", name = "requestMsg", required = false)
    @Length(message = "请求信息不超过50字符!", max = 50)
    private String requestMsg;

    /**
     * 友商来源 默认友商搜索
     */
    @ApiModelProperty(value = "友商来源", name = "source", required = false)
    private String source;

    @ApiModelProperty(value = "我的店铺id", name = "myShopId", hidden = true)
    private Integer myShopId;

    @ApiModelProperty(value = "用户id", name = "userId",  hidden = true)
    private Integer userId;

    public String getLeaguerShopId() {
        return leaguerShopId;
    }

    public void setLeaguerShopId(String leaguerShopId) {
        this.leaguerShopId = leaguerShopId;
    }

    public String getRequestMsg() {
        return requestMsg;
    }

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getMyShopId() {
        return myShopId;
    }

    public void setMyShopId(Integer myShopId) {
        this.myShopId = myShopId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
