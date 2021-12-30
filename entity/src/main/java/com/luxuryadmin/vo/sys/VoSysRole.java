package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 系统管理-角色管理-角色
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoSysRole {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

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
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    /**
     * 备注
     */
    private String remark;
    /**
     * 是否为超级管理员 0是 1否
     */
    private Integer type;
    /**
     * 角色权限
     */
    private List<String> perms;


}
