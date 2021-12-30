package com.luxuryadmin.vo.usr;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户邀请列表
 */
@Data
@ApiModel(description = "邀请人列表VO对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoInviteList {

    /**
     * 邀请人数
     */
    @ApiModelProperty(value = "邀请人数", name = "invitePersonNum", required = false)
    private String invitePersonNum;

    /**
     * 邀请人详情列表
     */
    @ApiModelProperty(value = "邀请人详情列表", name = "inviteDetailList", required = false)
    private List<VoInviteDetail> inviteDetailList;

}
