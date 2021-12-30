package com.luxuryadmin.vo.mongo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.service.util.mongo
 * @ClassName: UserBindShopCensus
 * @Author: ZhangSai
 * Date: 2021/11/5 14:24
 */
@Data
@Document("user_bind_shop_census")
public class UserBindShopCensus {
    @ApiModelProperty(value = "用户id", name = "userId")
    private Integer userId;
    @ApiModelProperty(value = "推广人id", name = "opEmployeeId")
    private Integer opEmployeeId;
    @ApiModelProperty(value = "当日邀请注册用户数", name = "registerUserCount")
    private Integer registerUserCount;
    @ApiModelProperty(value = "当日新开店数", name = "shopCount")
    private Integer shopCount;

    @ApiModelProperty(value = "当日小程序访问人数", name = "visitShopAppletCount")
    private Integer visitShopAppletCount;

    @ApiModelProperty(value = "当日新用户访问人数", name = "newVisitAppletCount")
    private Integer newVisitAppletCount;
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private String insertTime;
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;

    /**
     * 邀请人编码
     */
    private String inviteUserNumber;

    @ApiModelProperty(value = "是否删除 0未删除 1已删除", name = "del")
    private String del;
}
