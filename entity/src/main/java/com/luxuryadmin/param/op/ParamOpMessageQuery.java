package com.luxuryadmin.param.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @Classname ParamOpProblemQuery
 * @Description TODO
 * @Date 2020/7/9 16:23:25
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "广告管理-消息推送")
@Data
public class ParamOpMessageQuery {

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 10;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "记录编号", name = "id", required = false)
    private Long id;

    /**
     * 问题标题
     */
    @ApiModelProperty(value = "消息标题", name = "title", required = false)
    private String title;

    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型</br> system|系统消息</br> other|其它消息", name = "type", required = false)
    private String type;

    /**
     * 消息类型
     */
    @ApiModelProperty(value = "推送状态 </br> no_push|未推送</br> have_push|已推送", name = "pushState", required = false)
    private String pushState;

    /**
     * 插入时间 起始
     */
    @ApiModelProperty(value = "查询条件中-反馈时间范围-开始", name = "insertTimeStart", required = false)
    private Date pushTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-反馈时间范围-结束", name = "insertTimeEnd", required = false)
    private Date pushTimeEnd;
}
