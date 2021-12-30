package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户管理-帮助中心问题
 *
 * @author sanjin
 * @date 2020-07-09 16:53:43
 */

@ApiModel(description = "帮助中心问题")
@Data
public class ParamOpProblem {

    /**
     * 问题分类名称
     */
    @ApiModelProperty(name = "id", required = true, value = "问题ID")
    private Integer id;

    /**
     * 问题分类名称
     */
    @ApiModelProperty(name = "problemCategoryName", required = true, value = "问题分类名称")
    @NotBlank(message = "问题分类名称不能为空")
    private String problemCategoryName;

    /**
     * 问题标题
     */
    @ApiModelProperty(name = "problemTitle", required = true, value = "问题标题;长度不超过100")
    @Length(max = 100, message = "问题标题不能超过100个字符")
    private String problemTitle;


    /**
     * 问题答案
     */
    @ApiModelProperty(name = "problemAnswer", required = true, value = "问题答案；长度不超过1000")
    @Length(max = 1000, message = "问题答案不能超过1000个字符")
    private String problemAnswer;

    /**
     * 状态
     */
    @ApiModelProperty(name = "state", required = true, value = "状态;-1:已删除;0:未使用;1:使用中")
    @NotNull(message = "状态值不能为空")
    private Integer state;

    /**
     * 状态
     */
    @ApiModelProperty(name = "sort", required = true, value = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty(name = "videoUrl",value = "视频链接")
    private String videoUrl;

    @ApiModelProperty(name = "type",value = "分类 0:图文教程 1:视频教程")
    private String type;
}
