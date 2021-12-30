package com.luxuryadmin.param.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @Classname ParamBizCommentQuery
 * @Description TODO
 * @Date 2020/6/24 10:05
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "用户管理-意见反馈")
public class ParamBizCommentQuery {

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
    @ApiModelProperty(value = "查询条件中-反馈时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 插入时间 结束
     */
    @ApiModelProperty(value = "查询条件中-反馈时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;


    /**
     * 主键Id
     */
    @ApiModelProperty(value = "反馈编号", name = "id", required = false)
    private Integer id;

    /**
     * 反馈人联系方式
     */
    @ApiModelProperty(value = "反馈人联系方式", name = "contact", required = false)
    private String contact;


    /**
     * 反馈人手机号
     */
    @ApiModelProperty(value = "反馈人手机号", name = "phone", required = false)
    private String phone;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
