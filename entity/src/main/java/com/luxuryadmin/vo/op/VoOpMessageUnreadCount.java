package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-07-13 18:47
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpMessageUnreadCount {

    /**
     * 店铺未读消息数量
     */
    private Integer shopMessageUnreadCount;

    /**
     * 友商申请未读消息数量
     */
    private Integer friendBusinessMessageUnreadCount;

    /**
     * 系统未读消息数量
     */
    private Integer systemMessageUnreadCount;

    /**
     * 其它消息数量
     */
    private Integer otherMessageUnreadCount;

    /**
     * 总未读消息数量
     */
    private Integer totalUnReadCount;

    /**
     * 轮训时间间隔
     */
    private Integer pollInterval;

}
