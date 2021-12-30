package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户身份权限关联模板
 *
 * @author monkey king
 * @date 2020-09-04 15:35:40
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoUserPermission {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 父权限ID，根目录为0
     */
    private String tplName;

    /**
     * 权限ID
     */
    private String permId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }
}
