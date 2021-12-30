package com.luxuryadmin.entity.pro;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 商品补充信息关联表 bean
 *
 * @author zhangsai
 * @Date 2021-08-03 10:45:15
 */
@Data
@ApiModel(description = "商品补充信息关联表")
public class ProProductClassify {


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * pro_product的主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "pro_product的主键Id,逻辑id,软件内部关联")
    private Integer fkProProductId;
    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(value = "shp_shop的id字段,主键id")
    private Integer fkShpShopId;
    /**
     * 商品补充信息分类id
     */
    @ApiModelProperty(value = "商品补充信息分类id")
    private Integer fkProClassifyTypeId;
    /**
     * 商品补充信息分类名称
     */
//    @ApiModelProperty(value = "商品补充信息分类名称 -- 一级名称")
//    private String classifyTypeName;
    /**
     * 补充信息类型名称
     */
    @ApiModelProperty(value = "补充信息类型内容名称 -- 二级名称")
    private String classifyTypeDetailName;
    /**
     * 补充信息类型名称内容
     */
    @ApiModelProperty(value = "补充信息类型名称内容 -- 三级名称")
    private String typeDetailSubName;
    /**
     * 插入时间
     */
    @ApiModelProperty(value = "插入时间")
    private Date insertTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 添加用户_管理员id
     */
    @ApiModelProperty(value = "添加用户_管理员id")
    private Integer insertAdmin;
    /**
     * 修改用户_管理员id
     */
    @ApiModelProperty(value = "修改用户_管理员id")
    private Integer updateAdmin;
    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    @ApiModelProperty(value = "是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件")
    private String del;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
