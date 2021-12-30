package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Classname VoShpServiceUser
 * @Description 店铺服务人员VO
 * @Date 2020/9/18 16:03
 * @Created by sanjin145
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpServiceUser {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userMame;

}
