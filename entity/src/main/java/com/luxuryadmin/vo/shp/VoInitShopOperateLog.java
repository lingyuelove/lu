package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Classname VoInitShopService
 * @Description 初始化店铺操作日志VO
 * @Date 2020/11/21 20:57
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoInitShopOperateLog {

    /**
     * 模块列表
     */
    @ApiModelProperty(name = "shpOperateModuleList", required = false, value = "模块列表")
    private List<String> shpOperateModuleList;

    /**
     * 店铺人员列表
     */
    @ApiModelProperty(name = "shpUserList", required = false, value = "店铺人员列表")
    private List<VoEmployee> shpUserList;

}
