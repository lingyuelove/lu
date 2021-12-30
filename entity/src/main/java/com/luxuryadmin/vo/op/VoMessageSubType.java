package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 消息子类型VO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoMessageSubType {

    /**
     * 子类型Code
     */
    private String code;

    /**
     * 子类型中文名称
     */
    private String cnName;

}
