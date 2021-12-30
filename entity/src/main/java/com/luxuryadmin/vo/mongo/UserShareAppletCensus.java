package com.luxuryadmin.vo.mongo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @PackgeName: 运营绑定店铺数据统计
 * @ClassName: UserShareAppletCensus
 * @Author: ZhangSai
 * Date: 2021/11/5 14:24
 */
@Data
@Document("user_share_applet_census")
public class UserShareAppletCensus {
    @ApiModelProperty(value = "用户id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "关联工作微信id", name = "jobWxId")
    private Integer jobWxId;
    @ApiModelProperty(value = "当日注册店铺数", name = "registerShopCount")
    private Integer registerShopCount;
    @ApiModelProperty(value = "当日购买会员店铺数", name = "buyMemShopCount")
    private Integer buyMemShopCount;

    @ApiModelProperty(value = "当天月内转化会员量 会员付费时间 - 店铺注册时间小于等于30天", name = "unUseShopCount")
    private Integer buyMemForMonthCount;

    @ApiModelProperty(value = "当天百日内转化会员量 会员付费时间 - 店铺注册时间小于等于100天", name = "unUseShopCount")
    private Integer buyMemForHundredCount;

    @ApiModelProperty(value = "当天召回会员 会员付费时间 - 店铺注册时间大于100天", name = "unUseShopCount")
    private Integer buyMemForOldCount;

    @ApiModelProperty(value = "在使用所有店铺的数量", name = "memShopCount")
    private Integer memShopCount;
    @ApiModelProperty(value = "在使用非会员店铺数", name = "comMemShopCount")
    private Integer comMemShopCount;

    @ApiModelProperty(value = "十日内使用店铺数量", name = "useShopCount")
    private Integer useShopCount;

    @ApiModelProperty(value = "十日内不使用店铺数量", name = "unUseShopCount")
    private Integer unUseShopCount;

    @ApiModelProperty(value = "客户总量 共绑定了多少店铺", name = "totalShopCount")
    private Integer totalShopCount;

    @ApiModelProperty(value = "未逾期店铺 未付费购买会员", name = "unExpiredCount")
    private Integer unExpiredCount;

    @ApiModelProperty(value = "逾期店铺 未付费已过期", name = "expiredCount")
    private Integer expiredCount;
    /**
     * 微信昵称
     */
    @ApiModelProperty(name = "wxNickname", value = "微信昵称")
    private String wxNickname;
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private String updateTime;

    @ApiModelProperty(value = "是否删除 0未删除 1已删除", name = "del")
    private String del;
}
