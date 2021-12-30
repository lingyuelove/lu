package com.luxuryadmin.entity.shp;

import java.util.Date;

/**
 * 店铺编号池
 *
 * @author monkey king
 * @date   2019/12/19 16:24:30
 */
public class ShpNumberRecord {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * shopUser:店铺用户；shop:店铺
     */
    private String type;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 管理员id
     */
    private Integer insertAdmin;

    /**
     * 开始编号
     */
    private Integer startNumber;

    /**
     * 结束编号
     */
    private Integer endNumber;

    /**
     * 编码总数
     */
    private Integer totalNum;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(Integer insertAdmin) {
        this.insertAdmin = insertAdmin;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    public Integer getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(Integer endNumber) {
        this.endNumber = endNumber;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
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