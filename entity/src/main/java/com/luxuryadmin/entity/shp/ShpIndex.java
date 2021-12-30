package com.luxuryadmin.entity.shp;

import lombok.Data;

import java.util.Date;

/**
 * 首页功能列表
 *
 * @author monkey king
 * @date   2021/12/02 22:33:12
 */
@Data
public class ShpIndex {
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
     * 权限id
     */
    private Integer fkShpPermIndexId;

    /**
     * 权限id
     */
    private Integer type;

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
     * 备注
     */
    private String remark;

}