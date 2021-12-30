package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 店铺--权限管理
 *
 * @author monkey king
 * @date 2019/12/01 04:55:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpPermission {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 父权限ID，根目录为0
     */
    private Integer parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 权限相对路径URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String permission;

    /**
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer type;

    /**
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer display;

    /**
     * 权限图片地址
     */
    private String iconUrl;

    /**
     * h5链接
     */
    private String httpUrl;

    /**
     * 新模块状态 0:普通 | 1:新模块
     */
    private String newState;

    /**
     * 付费模块状态 0:普通 | 1:仅限vip使用
     */
    private String costState;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 颜色编码; 16进制; FFFFFF
     */
    private String color;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private Integer versions;

    private List<VoShpPermission> permissionList;


}