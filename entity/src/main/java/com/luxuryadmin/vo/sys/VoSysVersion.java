package com.luxuryadmin.vo.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 系统管理-角色管理-角色
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoSysVersion {
    /**
     *
     */
    private Long id;

    /**
     * app版本号 xx.xx.xx
     */
    private String appVersion;

    /**
     * 运营模块--平台类型的主键id
     */
    private Integer fkOpPlatformId;

    /**
     * 运营模块--平台类型名称
     */
    private String platformName;

    /**
     * 强制更新:0:非强制更新  1:强制更新
     */
    private String forcedUpdate;

    /**
     * 版本说明,支持h5格式
     */
    private String content;

    /**
     * 更新地址
     */
    private String updateUrl;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改用户_管理员id
     */
    private String updateAdmin;

}
