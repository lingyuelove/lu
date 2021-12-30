package com.luxuryadmin.entity.op;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 工作人员表
 *
 * @author monkey king
 * @date   2021/09/16 18:00:49
 */
@Data
public class OpEmployee {
    /**
     * 
     */
    private Integer id;

    /**
     * 工作人员id
     */
    private Integer fkShpUserId;

    /**
     * 预留字段;0:技术部门;1:运营部;默认为 1
     */
    private String type;

    /**
     * 商家联盟分享开关: 0:关 | 1:开
     */
    private String unionSwitch;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

}