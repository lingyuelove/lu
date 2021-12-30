package com.luxuryadmin.vo.org;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class VoAccessUserByApp {

    @ApiModelProperty(value = "机构仓访问用户列表", name = "hasNextPage")
    private List<VoAccessUserPageByApp> list;

    @ApiModelProperty(value = "页码", name = "hasNextPage")
    private Integer pageNum;

    @ApiModelProperty(value = "数量", name = "hasNextPage")
    private Integer pageSize;

    @ApiModelProperty(value = "是否有下一页", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "机构仓id", name = "organizationId")
    private Integer organizationId;

    /**
     * 展会状态 10 不开启限制 | 20 开启限制
     */
    @ApiModelProperty(value = "展会状态 10 不开启限制 | 20 开启限制", name = "organizationState")
    private String organizationState;

}
