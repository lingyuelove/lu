package com.luxuryadmin.param.sys;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * 系统管理-账号管理
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "系统管理-账号管理")
public class ParamSysUser {

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
    @ApiModelProperty(value = "查询条件中-创建时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-创建时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(value = "账户id", name = "id", required = false)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username", required = false)
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname", required = false)
    private String nickname;

    /**
     * 状态  0：禁用   1：正常
     */
    @ApiModelProperty(value = "状态  0：禁用   1：正常", name = "state", required = false)
    private String state;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    private String del;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Date getInsertTimeStart() {
        return insertTimeStart;
    }

    public void setInsertTimeStart(Date insertTimeStart) {
        this.insertTimeStart = insertTimeStart;
    }

    public Date getInsertTimeEnd() {
        return insertTimeEnd;
    }

    public void setInsertTimeEnd(Date insertTimeEnd) {
        this.insertTimeEnd = insertTimeEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }
}
