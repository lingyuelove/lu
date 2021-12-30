package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 运营模块--帮助中心问题VO
 *
 * @author sanjin
 * @date   2020/07/09 20:09:30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpEmployeeList {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String username;

    /**
     * 代理数
     */
    private Integer agentCount;


    /**
     * 注册时间
     */
    private Date insertTime;

    /**
     * 添加工作人员时间
     */
    private Date employeeInsertTime;

    /**
     * 个人邀请码
     */
    private String userNumber;

    /**
     * 商家联盟分享开关: 0:关 | 1:开
     */
    private String unionSwitch;
}