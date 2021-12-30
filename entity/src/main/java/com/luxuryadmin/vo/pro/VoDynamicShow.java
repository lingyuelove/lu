package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoDynamicShow
 * @Author: ZhangSai
 * Date: 2021/11/11 11:39
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="动态列表", description="动态列表")
public class VoDynamicShow {

    private List<VoDynamicList> voDynamics;
    @ApiModelProperty(value = "删除动态权限", name = "uPermDynamicDel")
    private String uPermDynamicDel;
}
