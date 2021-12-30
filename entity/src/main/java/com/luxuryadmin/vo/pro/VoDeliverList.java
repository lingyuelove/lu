package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoDynamicProductShow
 * @Author: ZhangSai
 * Date: 2021/11/11 11:57
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoDeliverList {

    private List<VoProDeliverByPage> objList;
    @ApiModelProperty(value = "数量", name = "dropSum")
    private String dropSum;
}
