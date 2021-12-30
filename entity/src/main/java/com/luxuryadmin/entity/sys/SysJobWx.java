package com.luxuryadmin.entity.sys;

import lombok.Data;

import java.util.Date;

/**
 * 微信客服号(线上运营部门的工作号);
 *
 * @author monkey king
 * @date   2021/08/03 20:32:53
 */
@Data
public class SysJobWx {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户id,关联的后台用户id
     */
    private Integer fkSysUserId;

    /**
     * 绑定状态 -1:已解绑 | 0:未绑定 |  1:已绑定
     */
    private Integer state;

    /**
     * 类型;预留字段
     */
    private String type;

    /**
     * 微信帐号
     */
    private String wxAccount;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 微信绑定手机号
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Integer insertAdmin;

    /**
     * 更新人
     */
    private Integer updateAdmin;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;
}