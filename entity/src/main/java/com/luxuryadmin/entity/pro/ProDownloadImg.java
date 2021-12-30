package com.luxuryadmin.entity.pro;

import java.util.Date;

/**
 * 产品图片下载记录
 *
 * @author monkey king
 * @date   2020/03/03 17:29:09
 */
public class ProDownloadImg {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * pro_product的业务逻辑id
     */
    private String fkProProductBizId;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 店铺管理员id.店长id
     */
    private Integer fkShpUserId;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

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

    public String getFkProProductBizId() {
        return fkProProductBizId;
    }

    public void setFkProProductBizId(String fkProProductBizId) {
        this.fkProProductBizId = fkProProductBizId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}