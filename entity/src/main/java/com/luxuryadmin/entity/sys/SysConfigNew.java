package com.luxuryadmin.entity.sys;

import lombok.Data;

import java.util.Date;

/**
 * 
 *
 * @author sanjin145
 * @date   2020/09/03 16:57:57
 */
@Data
public class SysConfigNew {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 主key
     */
    private String masterConfigKey;

    /**
     * 子key
     */
    private String subConfigKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置描述
     */
    private String configDesc;

    /**
     * key类型;如:redis
     */
    private String type;

    /**
     * 0:不自增; 1:值自增;
     */
    private String autoAdd;

    /**
     * 创建用户
     */
    private Integer insertAdmin;

    /**
     * 更新用户
     */
    private Integer updateAdmin;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

}