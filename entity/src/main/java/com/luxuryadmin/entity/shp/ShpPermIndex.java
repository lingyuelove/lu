package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 员工权限索引表(新版权限)
 *
 * @author monkey king
 * @date 2021/12/02 02:01:02
 */
@Data
public class ShpPermIndex {
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
     * 0:普通 | 1:新上线
     */
    private String newState;

    /**
     * 0:所有可以访问 | 1:仅限会员访问
     */
    private String costState;

    /**
     * 类型   0：根目录   1：子菜单   2：按钮
     */
    private Integer type;

    /**
     * 是否在app首页的全部功能里显示:  0:不显示; 1:显示
     */
    private Integer display;

    /**
     * 敏感权限 0:否 | 1:是
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
     * 16进制的颜色编码; FFFFFF
     */
    private String color;

    /**
     * ios线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    private Integer iosVersion;

    /**
     * android线上版本号;权限版本号小于等于端上版本号都显示,向下兼容
     */
    private Integer androidVersion;

    /**
     * 指定店铺id可见 -1:对所有人开放;不需要配置权限;-9:权限列表里面,只对经营者展示;
     */
    private String onlyShopId;

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
     * 备注
     */
    private String remark;

}