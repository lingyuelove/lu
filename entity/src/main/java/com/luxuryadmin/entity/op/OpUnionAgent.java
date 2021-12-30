package com.luxuryadmin.entity.op;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 代理人员表
 *
 * @author monkey king
 * @date   2021/09/16 18:00:49
 */
@Data
public class OpUnionAgent {
    /**
     * 
     */
    private Integer id;

    /**
     * 工作人员表id
     */
    private Integer fkOpEmployeeId;

    /**
     * 代理人员id
     */
    private Integer fkShpUserId;

    /**
     * 0:未绑定 | 1:已绑定
     */
    private String state;

    /**
     * 商家联盟分享开关: 0:关 | 1:开
     */
    private String agentSwitch;

    /**
     * 有效时间（天）
     */
    private Integer validDay;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;
     */
    private String del;

    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkOpEmployeeId() {
        return fkOpEmployeeId;
    }

    public void setFkOpEmployeeId(Integer fkOpEmployeeId) {
        this.fkOpEmployeeId = fkOpEmployeeId;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getValidDay() {
        return validDay;
    }

    public void setValidDay(Integer validDay) {
        this.validDay = validDay;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(Integer insertAdmin) {
        this.insertAdmin = insertAdmin;
    }

    public Integer getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(Integer updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}