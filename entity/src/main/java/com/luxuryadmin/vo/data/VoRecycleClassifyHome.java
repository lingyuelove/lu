package com.luxuryadmin.vo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.data
 * @ClassName: VoRecycleClassifyHome
 * @Author: ZhangSai
 * Date: 2021/6/11 10:53
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="回收分析 --产品属性分析", description="回收分析 --产品属性分析")
public class VoRecycleClassifyHome {

    @ApiModelProperty(value = "产品属性", name = "classifyCode")
    private String classifyCode;

    @ApiModelProperty(value = "产品属性中文名", name = "classifyCodeName")
    private String classifyCodeName;

    @ApiModelProperty(value = "产品属性数量", name = "classifyCount")
    private Integer classifyCount;

    @ApiModelProperty(value = "产品属性图片", name = "classifyCodeImg")
    private String classifyCodeImg;
}
