package com.luxuryadmin.entity.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺管理员详情表
 *
 * @author monkey king
 * @date   2019/12/01 04:55:22
 */
@ApiModel(description = "店铺管理员详情表--查询参数实体")
public class ShpUserDetail implements Serializable {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ApiModelProperty(name = "id",value = "主键Id")
    private Integer id;

    /**
     * 商家版用户ID
     */
    @ApiModelProperty(name = "fkShpUserId",value = "商家版用户ID")
    private Integer fkShpUserId;

    /**
     * 性别; 男,女,未知
     */
    @ApiModelProperty(name = "sex",value = "性别; 男,女,未知")
    private String sex;

    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email",value = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "qq",value = "手机号")
    private String qq;

    /**
     * 身份证人脸面图片地址
     */
    @ApiModelProperty(name = "idcardUrlFace",value = "身份证人脸面图片地址")
    private String idcardUrlFace;

    /**
     * 身份证国徽面图片地址
     */
    @ApiModelProperty(name = "idcardUrlNation",value = "身份证国徽面图片地址")
    private String idcardUrlNation;

    /**
     * shp_shop的id字段,主键id
     */
    @ApiModelProperty(name = "fkShpShopId",value = "shp_shop的id字段")
    private Integer fkShpShopId;

    /**
     * 插入时间
     */
    @ApiModelProperty(name = "insertTime",value = "插入时间")
    private Date insertTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "updateTime",value = "修改时间")
    private Date updateTime;

    /**
     * 修改用户_管理员id
     */
    @ApiModelProperty(name = "updateAdmin",value = "修改用户_管理员id")
    private Integer updateAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    @ApiModelProperty(name = "versions",value = "版本号")
    private Integer versions;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    @ApiModelProperty(name = "del",value = "是否逻辑删除")
    private String del;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark",value = "备注")
    private String remark;

    public Integer getId() {
        return id;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getIdcardUrlFace() {
        return idcardUrlFace;
    }

    public void setIdcardUrlFace(String idcardUrlFace) {
        this.idcardUrlFace = idcardUrlFace;
    }

    public String getIdcardUrlNation() {
        return idcardUrlNation;
    }

    public void setIdcardUrlNation(String idcardUrlNation) {
        this.idcardUrlNation = idcardUrlNation;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
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

    public Integer getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(Integer updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Integer getVersions() {
        return versions;
    }

    public void setVersions(Integer versions) {
        this.versions = versions;
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