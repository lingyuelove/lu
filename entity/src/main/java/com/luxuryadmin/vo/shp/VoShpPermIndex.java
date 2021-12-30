package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺--权限管理
 *
 * @author monkey king
 * @date 2019/12/01 04:55:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpPermIndex implements Serializable {
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
     * h5链接
     */
    private String httpUrl;

    /**
     * 权限相对路径URL
     */
    private String url;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String permission;

    /**
     * 新模块状态 0:普通 | 1:新模块
     */
    private String newState;

    /**
     * 付费模块状态 0:普通 | 1:仅限vip使用
     */
    private String costState;

    /**
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer type;

    /**
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer display;

    /**
     * 敏感权限: 0:否 | 1:是
     */
    private String isPrivate;

    /**
     * 权限图片地址
     */
    private String iconUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 颜色编码; 16进制; FFFFFF
     */
    private String color;

    /**
     * ios线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    private String iosVersion;

    /**
     * android线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    private String androidVersion;

    /**
     *  指定店铺id可见 -1:对所有人开放;不需要配置权限;-9:权限列表里面,只对经营者展示;
     */
    private String onlyShopId;

    /**
     * 备注
     */
    private String remark;

    private List<VoShpPermIndex> permIndexList;


}