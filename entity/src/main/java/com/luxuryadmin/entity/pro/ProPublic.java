package com.luxuryadmin.entity.pro;

import lombok.Data;

import java.util.Date;

/**
 * 公价商品表
 *
 * @author monkey king
 * @date   2021/06/22 22:51:04
 */
@Data
public class ProPublic {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * -1:删除 | 0:停用 | 1:启用; 默认为 1
     */
    private Integer state;

    /**
     * 产品分类表的code;默认':无分类;
     */
    private String classifyCode;


    /**
     * 第三方分类;此分类是其它平台对其进行分类;
     */
    private String thirdClassify;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 对name的中文描述
     */
    private String description;

    /**
     * 型号
     */
    private String typeNo;

    /**
     * 系列
     */
    private String serialNo;

    /**
     * 机芯类型
     */
    private String watchCoreType;

    /**
     * 表壳材质
     */
    private String watchcase;

    /**
     * 表盘直径
     */
    private String watchcaseSize;

    /**
     * 材质
     */
    private String material;

    /**
     * 尺寸;一般有长宽高
     */
    private String objectSize;

    /**
     * 尺码;一般指衣服和鞋的大小
     */
    private String clothesSize;

    /**
     * 国内公价(元)
     */
    private Integer publicPrice;

    /**
     * 公价图地址
     */
    private String smallImg;

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