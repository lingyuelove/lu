package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 统计邀请人数
 *
 * @author monkey king
 * @date 2021-04-02 1:01:47
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "统计邀请人数", description = "统计邀请人数")
public class VoCountInviteShop {

    /**
     * 邀请总人数
     */
    @ApiModelProperty(value = "邀请总人数", name = "totalInviteUser")
    private Integer totalInviteUser;

    /**
     * vip店铺数量
     */
    @ApiModelProperty(value = "vip店铺数量", name = "vipShop")
    private Integer vipShop;

    /**
     * 体验店铺数量
     */
    @ApiModelProperty(value = "体验店铺数量", name = "tryShop")
    private Integer tryShop;

    /**
     * 过期店铺数量
     */
    @ApiModelProperty(value = "过期店铺数量", name = "expiredShop")
    private Integer expiredShop;

    /**
     * 邀请的店铺
     */
    @ApiModelProperty(value = "邀请的店铺", name = "listInviteShop")
    private List<VoInviteShop> listInviteShop;


}
