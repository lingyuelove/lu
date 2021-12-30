package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoConveyForApp
 * @Author: ZhangSai
 * Date: 2021/11/23 11:23
 */
@Data
@ApiModel(value="商品传送表实体参数", description="商品传送表实体参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoConveyForApp {

    @ApiModelProperty(value = "商品传送集合显示", name = "conveys")
    private  List<VoConvey> conveys;
    @ApiModelProperty(value = "新增设置vid", name = "vid")
    private String vid;
}
