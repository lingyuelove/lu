package com.luxuryadmin.entity.sys;

import lombok.Data;

import java.util.Date;

/**
 * 系统版本表
 *
 * @author monkey king
 * @date   2019/12/01 05:24:38
 */
@Data
public class SysVersion {
    /**
     * 
     */
    private Long id;

    /**
     * 更新地址
     */
    private String updateUrl;

    /**
     * 版本说明,支持h5格式
     */
    private String content;

    /**
     * 运营模块--平台类型的主键id
     */
    private Integer fkOpPlatformId;

    /**
     * 强制更新:0:非强制更新  1:强制更新
     */
    private String forcedUpdate;

    /**
     * 更新文件大小
     */
    private String fileSize;

    /**
     * app版本号 xx.xx.xx
     */
    private String appVersion;

    /**
     * 是否显示弹框 0：不显示；1：显示
     */
    private String showDialog;

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
}