package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 小程序访客记录表---代理专用;
一人一条记录
 *
 * @author monkey king
 * @date   2021/08/28 17:22:54
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProShareSeeUserAgency {
    /**
     * 主键id
     */
    private Integer id;

    /**
     *  店铺名称
     */
    private String shopName;

    /**
     * 佣金发放: -1:不发放 | 0:未发放; | 1:已发放
     */
    private String state;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别  0：未知、1：男、2：女
     */
    private String gender;

    /**
     * 已发放佣金(分);
     */
    private String alreadySendMoney;

    /**
     * 分享人
     */
    private String shareUser;

    /**
     * 首次访问小程序时间
     */
    private String insertTime;
    /**
     * 店铺付费时间
     */
    private String payTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 后台修改人员;
     */
    private String updateAdmin;

}