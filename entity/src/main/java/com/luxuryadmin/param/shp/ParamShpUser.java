package com.luxuryadmin.param.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Administrator
 * @Classname ParamShpUser
 * @Description TODO
 * @Date 2020/6/22 17:25
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "用户管理-用户列表")
@Data
public class ParamShpUser {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    private int pageSize = 10;

    /**
     * 插入时间 起始
     */
    @ApiModelProperty(value = "查询条件中-注册时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-注册时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "用户主键Id", name = "id", required = false)
    private Integer id;

    @ApiModelProperty(value = "用户编号", name = "number", required = false)
    private String number;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名|账号", name = "username", required = false)
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname", required = false)
    private String nickname;

    /**
     * 手机号;对称加密存储
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = false)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "委托方手机号码格式错误")
    private String phone;

    /**
     * 邀请人编码
     */
    @ApiModelProperty(value = "邀请人编码", name = "inviteUserId", required = false)
    private String inviteUserId;

    /**
     * 注册渠道
     */
    @ApiModelProperty(value = "注册渠道", name = "channelId", required = false)
    private Integer channelId;

    /**
     * 状态  0：禁用   1：正常
     */
    @ApiModelProperty(value = "状态 0：禁用 1：正常", name = "state", required = false)
    @Pattern(regexp = "^[01]$", message = "状态参数格式错误")
    private String state;

    /**
     * 身份信息是否上传 0：未上传 1：上传
     */
    @ApiModelProperty(value = "身份信息是否上传 0：禁用 1：正常", name = "detailFlag", required = false)
    @Pattern(regexp = "^[01]$", message = "身份信息是否上传参数格式错误")
    private String detailFlag;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = false)
    private String shopNumber;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺名称", name = "shopName", required = false)
    private String shopName;

}
