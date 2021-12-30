package com.luxuryadmin.entity.op;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 帮助中心问题
 *
 * @author monkey king
 * @date   2020/07/09 11:33:17
 */
@Data
public class OpProblem {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "主键Id,逻辑id,软件内部关联")
    private Integer id;
    /**
     * 问题分类名称，先固定写死  仓库|订单|服务|店铺|员工|财务|其他
     */
    @ApiModelProperty(value = "问题分类名称，先固定写死  仓库|订单|服务|店铺|员工|财务|其他")
    private String problemCategoryName;
    /**
     * 问题标题
     */
    @ApiModelProperty(value = "问题标题")
    private String problemTitle;
    /**
     * 问题答案
     */
    @ApiModelProperty(value = "问题答案")
    private String problemAnswer;
    /**
     * 视频链接
     */
    @ApiModelProperty(value = "视频链接")
    private String videoUrl;
    /**
     * 播放次数
     */
    @ApiModelProperty(value = "播放次数")
    private Integer playNum;
    /**
     * 排序，数字越大排在越前面
     */
    @ApiModelProperty(value = "排序，数字越大排在越前面")
    private Integer sort;
    /**
     * 分类 0:图文教程 1:视频教程
     */
    @ApiModelProperty(value = "分类 0:图文教程 1:视频教程")
    private String type;
    /**
     * 状态;-1:已删除;0:未使用;1:使用中
     */
    @ApiModelProperty(value = "状态;-1:已删除;0:未使用;1:使用中")
    private Integer state;
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