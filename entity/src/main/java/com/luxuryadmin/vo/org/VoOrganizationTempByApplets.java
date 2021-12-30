package com.luxuryadmin.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 机构临时仓小程序端显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓小程序端显示", description="机构临时仓小程序端显示")
public class VoOrganizationTempByApplets {
    private List<VoOrganizationTempPageByApplets> list;

    private Integer pageNum;

    private Integer pageSize;

    private Long total;
    private Boolean  hasNextPage;
    @ApiModelProperty(value = "机构临时仓名称", name = "orgName")
    private String orgName;

}
