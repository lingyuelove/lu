package com.luxuryadmin.entity.ord;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收据管理
 *
 * @author monkey king
 * @date   2019/12/01 04:54:52
 */
public class OrdReceipt {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 收据单价(分)
     */
    private BigDecimal unitPrice;

    /**
     * 收据总价(分)
     */
    private BigDecimal totalPrice;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 店铺联系电话
     */
    private String phone;

    /**
     * 票据状态: -10: 取消打印; 10:未打印; 20:已打印(线上); 21:(线下)  22:补打印;
     */
    private String state;

    /**
     * 甲方(店名/公司名)
     */
    private String partyA;

    /**
     * 乙方(客户姓名/对方公司名)
     */
    private String partyB;

    /**
     * 商品独立编码
     */
    private String proUniqueCode;

    /**
     * 销售员id
     */
    private Integer fkShpUserId;

    /**
     * ord_order的主键Id,逻辑id,软件内部关联
     */
    private Integer fkOrdOrderId;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    private String fkOrdOrderNumber;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getProUniqueCode() {
        return proUniqueCode;
    }

    public void setProUniqueCode(String proUniqueCode) {
        this.proUniqueCode = proUniqueCode;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkOrdOrderId() {
        return fkOrdOrderId;
    }

    public void setFkOrdOrderId(Integer fkOrdOrderId) {
        this.fkOrdOrderId = fkOrdOrderId;
    }

    public String getFkOrdOrderNumber() {
        return fkOrdOrderNumber;
    }

    public void setFkOrdOrderNumber(String fkOrdOrderNumber) {
        this.fkOrdOrderNumber = fkOrdOrderNumber;
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

    public Integer getInsertAdmin() {
        return insertAdmin;
    }

    public void setInsertAdmin(Integer insertAdmin) {
        this.insertAdmin = insertAdmin;
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