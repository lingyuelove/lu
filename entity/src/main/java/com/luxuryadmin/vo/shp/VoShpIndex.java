package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 首页列表显示的vo实体
 *
 * @author monkey king
 * @date 2021-12-02 19:28:55
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoShpIndex {

    /**
     * ShpIndex.id
     * 该表的逻辑id
     */
    private Integer id;

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
     * 关联权限的名称
     */
    private String permRefName;


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
     * 类型   0：模块   1：页面   2：功能
     */
    private Integer type;

    /**
     * 序号排序
     */
    private Integer sort;

    /**
     * webView的url; 可直接访问
     */
    private String httpUrl;

    /**
     * webView里的业务链接
     */
    private String kturl;

    /**
     * 敏感权限; 0:否 | 1:是
     */
    private String isPrivate;

    /**
     * -9:权限配置仅对经营者显示
     */
    private String onlyShopId;

    private List<VoShpIndex> permIndexList;
}
