package com.luxuryadmin.param.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Classname ParamOpProblemQuery
 * @Description TODO
 * @Date 2020/7/9 16:23:25
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "用户管理-帮助中心")
@Data
public class ParamOpProblemQuery {

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 10;

//    /**
//     * 插入时间 起始
//     */
//    @ApiModelProperty(value = "查询条件中-反馈时间范围-开始", name = "insertTimeStart", required = false)
//    private Date insertTimeStart;
//
//    /**
//     * 插入时间 结束
//     */
//    @ApiModelProperty(value = "查询条件中-反馈时间范围-结束", name = "insertTimeEnd", required = false)
//    private Date insertTimeEnd;


//    /**
//     * 主键Id
//     */
//    @ApiModelProperty(value = "反馈编号", name = "id", required = false)
//    private Integer id;

    /**
     * 问题类别名称
     */
    @ApiModelProperty(value = "问题类别名称", name = "problemCategoryName", required = false)
    private String problemCategoryName;


    /**
     * 问题标题
     */
    @ApiModelProperty(value = "问题标题", name = "phone", required = false)
    private String problemTitle;

    /**
     * 问题状态;-1:已删除;0:未使用;1:使用中
     */
    @ApiModelProperty(value = "问题状态", name = "state", required = false)
    private Integer state;

    @ApiModelProperty(value = "分类 0:图文教程 1:视频教程")
    private String type;
}
