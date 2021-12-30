package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 店铺--权限管理
 *
 * @author monkey king
 * @date   2019/12/01 04:55:22
 */
@Data
public class ShpPermission {
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
     * 权限编码(页面跳转时的判断值)
     */
    private String code;

    /**
     * webView的url
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
     * 0:普通 | 1:新上线; 默认为0
     */
    private String newState;

    /**
     * 0:所有可以访问 | 1:仅限会员访问; 默认为0
     */
    private String costState;

    /**
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer type;

    /**
     * 是否在app首页的全部功能里显示:  0:不显示; 1:显示
     */
    private Integer display;

    /**
     * 权限图片地址
     */
    private String iconUrl;

    /**
     * 排序
     */
    private Integer sort;

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