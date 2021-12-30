package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 消息中心店铺用户关联表
 *
 * @author monkey king
 * @date   2020/07/16 17:46:20
 */
@Data
public class OpMessageShopUserRef {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 店铺用户ID
     */
    private Integer fkShpUserId;

    /**
     * 点击状态 0|未点击未读 1|已点击已读
     */
    private String clickState;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否逻辑删除;0:不删除;1:逻辑删除;所有查询sql都要带上del=0这个条件;
     */
    private String del;

    /**
     * 消息推送参数
     */
    private String extraParam;

}