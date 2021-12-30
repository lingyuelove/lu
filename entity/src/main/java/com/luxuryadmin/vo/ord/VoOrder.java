package com.luxuryadmin.vo.ord;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator
 * @Classname VoOrder
 * @Description TODO
 * @Date 2020/6/30 10:11
 * @Created by Administrator
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOrder {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 订单编号(针对店铺的唯一订单编码)
     */
    private String number;

    /**
     * 状态  -90 已删除(不计入统计); -20:已退款; -10:已取消开单;  10：开单中  11: 预定中  20：已售出;
     */
    private String state;

    /**
     * 类型  YS:友商订单; DL:代理订单; KH:客户订单; QT:其它订单
     */
    private String type;

    /**
     * 销售途径
     */
    private String saleChannel;

    /**
     * 订单总数量;
     */
    private Integer totalNum;

    /**
     * 最终成交价(分)
     */
    private BigDecimal finishPrice;

    /**
     * 商品卖出时间
     */
    private Date finishTime;

    /**
     * 商品独立(唯一)编码,用户自填;
     */
    private String uniqueCode;

    /**
     * 发货详细地址
     */
    private String toAddress;

    /**
     * 订单收据状态: -10: 不开收据; 10:未开收据; 20:已开收据(机打); 21:已开收据(手写)
     */
    private String printState;

    /**
     * 销售员昵称
     */
    private String saleNickname;

    /**
     * 销售人员id
     */
    private Integer fkShpUserId;

    /**
     * pro_product的主键Id,逻辑id,软件内部关联
     */
    private Integer fkProProductId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * shp_shop的id字段,主键id
     */
    private Integer fkShpShopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 运营模块--渠道的主键id
     */
    private Integer fkOpChannelId;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 开单人员用户id
     */
    private Integer insertAdmin;

    /**
     * 开单人员昵称
     */
    private String insertNickname;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

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

    /**
     * 缩略图
     */
    private String smallImg;

    /**
     * 收件人姓名
     */
    private String addressName;

    /**
     * 收件人手机号
     */
    private String addressPhone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 地址
     */
    private String address;


    private String bizId;

    private String shopNumer;

    public String getShopNumer() {
        return shopNumer;
    }

    public void setShopNumer(String shopNumer) {
        this.shopNumer = shopNumer;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getSmallImg() {
        return smallImg;
    }

    public void setSmallImg(String smallImg) {
        this.smallImg = smallImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getFinishPrice() {
        return finishPrice;
    }

    public void setFinishPrice(BigDecimal finishPrice) {
        this.finishPrice = finishPrice;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getPrintState() {
        return printState;
    }

    public void setPrintState(String printState) {
        this.printState = printState;
    }

    public String getSaleNickname() {
        return saleNickname;
    }

    public void setSaleNickname(String saleNickname) {
        this.saleNickname = saleNickname;
    }

    public Integer getFkShpUserId() {
        return fkShpUserId;
    }

    public void setFkShpUserId(Integer fkShpUserId) {
        this.fkShpUserId = fkShpUserId;
    }

    public Integer getFkProProductId() {
        return fkProProductId;
    }

    public void setFkProProductId(Integer fkProProductId) {
        this.fkProProductId = fkProProductId;
    }

    public Integer getFkShpShopId() {
        return fkShpShopId;
    }

    public void setFkShpShopId(Integer fkShpShopId) {
        this.fkShpShopId = fkShpShopId;
    }

    public Integer getFkOpChannelId() {
        return fkOpChannelId;
    }

    public void setFkOpChannelId(Integer fkOpChannelId) {
        this.fkOpChannelId = fkOpChannelId;
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

    public String getInsertNickname() {
        return insertNickname;
    }

    public void setInsertNickname(String insertNickname) {
        this.insertNickname = insertNickname;
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

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getSaleChannel() {
        return saleChannel;
    }

    public void setSaleChannel(String saleChannel) {
        this.saleChannel = saleChannel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
