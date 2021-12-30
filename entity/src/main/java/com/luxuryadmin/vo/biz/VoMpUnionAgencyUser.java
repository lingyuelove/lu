package com.luxuryadmin.vo.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author monkey king
 * @date 2021-08-31 19:48:38
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
@Data
public class VoMpUnionAgencyUser {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 有效时间（天）
     */
    private Integer validDay;

}
