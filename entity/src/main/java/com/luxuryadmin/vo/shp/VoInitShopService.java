package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Classname VoInitShopService
 * @Description 初始化店铺服务VO
 * @Date 2020/9/18 15:57
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoInitShopService {

    /**
     * 店铺类型列表
     */
    @ApiModelProperty(name = "shpServiceTypeList", required = false, value = "店铺类型列表")
    private List<VoShpServiceType> shpServiceTypeList;

    /**
     * 服务人员列表
     */
    @ApiModelProperty(name = "shpServiceUserList", required = false, value = "服务人员列表")
    private List<VoEmployee> shpServiceUserList;
    /**
     * 服务人员列表
     */
    @ApiModelProperty(name = "receiveUserList", required = false, value = "服务人员列表")
    private List<VoEmployee> receiveUserList;

    @ApiModelProperty(name = "totalUserList", required = false, value = "店铺全部人员列表")
    private List<VoEmployee> totalUserList;
}
