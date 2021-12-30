package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息子类型列表VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoMessageSubTypeList {

    /**
     * 切换栏展示子类型列表
     */
    @ApiModelProperty(value = "切换栏展示子类型列表", name = "switchTabSubTypeList", required = false)
    private List<VoMessageSubType> switchTabSubTypeList;

    /**
     * 所有子类型列表
     */
    @ApiModelProperty(value = "所有子类型列表", name = "allSubTypeList", required = false)
    private List<VoMessageSubType> allSubTypeList;

}
