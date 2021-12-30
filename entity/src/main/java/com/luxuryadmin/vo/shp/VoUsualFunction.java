package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author monkey king
 * @date 2020-06-15 17:25:30
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoUsualFunction {

    /**
     * 权限id
     */
    private Integer permId;

    /**
     * code值;用作于app跳转页面;
     */
    private String code;

    /**
     * 功能名称
     */
    private String name;


    /**
     * 二级页面菜单图标路径
     */
    private String iconUrl;

    /**
     * 父节点名称
     */
    private String parentName;

    /**
     * 父节点编码
     */
    private String parentCode;

    /**
     * 0:普通 | 1:新模块
     */
    private String newState;

    /**
     * 付费标识 0:普通 | 1:仅限vip使用
     */
    private String costState;

    /**
     * 父节点ID
     */
    private Integer parentId;

    /**
     * 序号排序
     */
    private Integer sort;

    /**
     * 版本号,99为特殊权限,只针对个别商家
     */
    private Integer version;

    /**
     * webView的url; 可直接访问
     */
    private String httpUrl;

    /**
     * webView里的业务链接
     */
    private String kturl;

}
