package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 运营模块--帮助中心问题VO
 *
 * @author sanjin
 * @date   2020/07/09 20:09:30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpProblem {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 问题分类名称，先固定写死  仓库|订单|服务|店铺|员工|财务|其他
     */
    private String problemCategoryName;

    /**
     * 问题标题
     */
    private String problemTitle;

    /**
     * 问题答案
     */
    private String problemAnswer;

    /**
     * 排序，数字越大排在越前面
     */
    private Integer sort;

    /**
     * 状态;-1:已删除;0:未使用;1:使用中
     */
    private Integer state;

    @ApiModelProperty(value = "分类 0:图文教程 1:视频教程")
    private String type;
    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createPerson;

    @ApiModelProperty(name = "videoUrl",value = "视频链接")
    private String videoUrl;

    @ApiModelProperty(value = "播放次数")
    private Integer playNum;
}