package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: VoRecycleUserList
 * @Author: ZhangSai
 * Date: 2021/6/11 10:52
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="回收分析 --回收排行榜", description="回收分析 --回收排行榜")
public class VoRecycleUserList {

    @ApiModelProperty(value = "用户ID", name = "userId")
    private Integer userId;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", name = "nickName")
    private String nickName;

    /**
     * 用户角色
     */
    @ApiModelProperty(value = "用户角色", name = "roleName")
    private String roleName;

    /**
     * 头像URL
     */
    @ApiModelProperty(value = "头像URL", name = "headImgUrl")
    private String headImgUrl;

    @ApiModelProperty(value = "回收总成本价(分)", name = "initAllPrice")
    private BigDecimal initAllPrice;

    @ApiModelProperty(value = "回收数量", name = "recycleCount")
    private Integer recycleCount;

    @ApiModelProperty(value = "排名", name = "ranking")
    private Integer ranking;
}
