package com.luxuryadmin.enums.sys;

/**
 * SysSalt表的类型枚举
 *
 * @author monkey king
 * @date 2019-12-26 17:53:56
 */
public enum EnumSysConfigNew {

    //枚举
    INVITE_PAGE("invite", "invitePageH5Url", "邀请页面H5url"),
    //枚举
    MSG_POLL_INTERVAL_SECONDS("msg", "pollIntervalSecond", "消息轮询间隔秒数");

    /**
     * 代码(存入数据库)
     */
    private String masterConfigKey;
    /**
     * 代码名称(显示作用)
     */
    private String subConfigKey;

    /**
     * 代码说明(对代码进行详细的补充说明)
     */
    private String configDesc;

    EnumSysConfigNew(String masterConfigKey, String subConfigKey, String configDesc) {
        this.masterConfigKey = masterConfigKey;
        this.subConfigKey = subConfigKey;
        this.configDesc = configDesc;
    }

    public String getMasterConfigKey() {
        return masterConfigKey;
    }

    public void setMasterConfigKey(String masterConfigKey) {
        this.masterConfigKey = masterConfigKey;
    }

    public String getSubConfigKey() {
        return subConfigKey;
    }

    public void setSubConfigKey(String subConfigKey) {
        this.subConfigKey = subConfigKey;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    @Override
    public String toString() {
        return "EnumSysConfigNew{" +
                "masterConfigKey='" + masterConfigKey + '\'' +
                ", subConfigKey='" + subConfigKey + '\'' +
                ", configDesc='" + configDesc + '\'' +
                '}';
    }
}
