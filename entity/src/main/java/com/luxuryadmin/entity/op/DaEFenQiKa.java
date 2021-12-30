package com.luxuryadmin.entity.op;

import java.util.Date;

/**
 * 大额分期卡
 *
 * @author monkey king
 * @date   2019/12/16 11:24:53
 */
public class DaEFenQiKa {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;

    /**
     * 
     */
    private String 机构;

    /**
     * 
     */
    private String 机构名称;

    /**
     * 
     */
    private String 卡号;

    /**
     * 
     */
    private String 姓名;

    /**
     * 
     */
    private String 卡种类;

    /**
     * 
     */
    private String 证件号码;

    /**
     * 
     */
    private String 开卡日期;

    /**
     * 
     */
    private String 逾期类型;

    /**
     * 
     */
    private String 账单日;

    /**
     * 
     */
    private String 推广人编号;

    /**
     * 
     */
    private String 推广人姓名;

    /**
     * 
     */
    private String 联系电话;

    /**
     *
     */
    private String 省份;

    /**
     *
     */
    private String 城市;

    /**
     *
     */
    private String 运营商;


    /**
     * 
     */
    private Double 授信额度;

    /**
     * 
     */
    private Double 透支余额;

    /**
     * 
     */
    private Double 透支本金;

    /**
     * 
     */
    private Double 利息余额;

    /**
     * 
     */
    private Double 费用余额;

    /**
     * 
     */
    private Double 分期付款原始本金;

    /**
     * 
     */
    private Double 分期付款待摊本金;

    /**
     * 
     */
    private Double 分期付款原始费用;

    /**
     * 
     */
    private Double 分期付款待摊费用;

    /**
     * 
     */
    private Double 未还款额;

    /**
     * 
     */
    private String 专案代码;

    /**
     * 
     */
    private String 分期总额;

    /**
     * 
     */
    private String 年日均;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

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

    public String get机构() {
        return 机构;
    }

    public void set机构(String 机构) {
        this.机构 = 机构;
    }

    public String get机构名称() {
        return 机构名称;
    }

    public void set机构名称(String 机构名称) {
        this.机构名称 = 机构名称;
    }

    public String get卡号() {
        return 卡号;
    }

    public void set卡号(String 卡号) {
        this.卡号 = 卡号;
    }

    public String get姓名() {
        return 姓名;
    }

    public void set姓名(String 姓名) {
        this.姓名 = 姓名;
    }

    public String get卡种类() {
        return 卡种类;
    }

    public void set卡种类(String 卡种类) {
        this.卡种类 = 卡种类;
    }

    public String get证件号码() {
        return 证件号码;
    }

    public void set证件号码(String 证件号码) {
        this.证件号码 = 证件号码;
    }

    public String get开卡日期() {
        return 开卡日期;
    }

    public void set开卡日期(String 开卡日期) {
        this.开卡日期 = 开卡日期;
    }

    public String get逾期类型() {
        return 逾期类型;
    }

    public void set逾期类型(String 逾期类型) {
        this.逾期类型 = 逾期类型;
    }

    public String get账单日() {
        return 账单日;
    }

    public void set账单日(String 账单日) {
        this.账单日 = 账单日;
    }

    public String get推广人编号() {
        return 推广人编号;
    }

    public void set推广人编号(String 推广人编号) {
        this.推广人编号 = 推广人编号;
    }

    public String get推广人姓名() {
        return 推广人姓名;
    }

    public void set推广人姓名(String 推广人姓名) {
        this.推广人姓名 = 推广人姓名;
    }

    public String get联系电话() {
        return 联系电话;
    }

    public void set联系电话(String 联系电话) {
        this.联系电话 = 联系电话;
    }

    public String get城市() {
        return 城市;
    }

    public void set城市(String 城市) {
        this.城市 = 城市;
    }

    public String get省份() {
        return 省份;
    }

    public void set省份(String 省份) {
        this.省份 = 省份;
    }

    public String get运营商() {
        return 运营商;
    }

    public void set运营商(String 运营商) {
        this.运营商 = 运营商;
    }

    public Double get授信额度() {
        return 授信额度;
    }

    public void set授信额度(Double 授信额度) {
        this.授信额度 = 授信额度;
    }

    public Double get透支余额() {
        return 透支余额;
    }

    public void set透支余额(Double 透支余额) {
        this.透支余额 = 透支余额;
    }

    public Double get透支本金() {
        return 透支本金;
    }

    public void set透支本金(Double 透支本金) {
        this.透支本金 = 透支本金;
    }

    public Double get利息余额() {
        return 利息余额;
    }

    public void set利息余额(Double 利息余额) {
        this.利息余额 = 利息余额;
    }

    public Double get费用余额() {
        return 费用余额;
    }

    public void set费用余额(Double 费用余额) {
        this.费用余额 = 费用余额;
    }

    public Double get分期付款原始本金() {
        return 分期付款原始本金;
    }

    public void set分期付款原始本金(Double 分期付款原始本金) {
        this.分期付款原始本金 = 分期付款原始本金;
    }

    public Double get分期付款待摊本金() {
        return 分期付款待摊本金;
    }

    public void set分期付款待摊本金(Double 分期付款待摊本金) {
        this.分期付款待摊本金 = 分期付款待摊本金;
    }

    public Double get分期付款原始费用() {
        return 分期付款原始费用;
    }

    public void set分期付款原始费用(Double 分期付款原始费用) {
        this.分期付款原始费用 = 分期付款原始费用;
    }

    public Double get分期付款待摊费用() {
        return 分期付款待摊费用;
    }

    public void set分期付款待摊费用(Double 分期付款待摊费用) {
        this.分期付款待摊费用 = 分期付款待摊费用;
    }

    public Double get未还款额() {
        return 未还款额;
    }

    public void set未还款额(Double 未还款额) {
        this.未还款额 = 未还款额;
    }

    public String get专案代码() {
        return 专案代码;
    }

    public void set专案代码(String 专案代码) {
        this.专案代码 = 专案代码;
    }

    public String get分期总额() {
        return 分期总额;
    }

    public void set分期总额(String 分期总额) {
        this.分期总额 = 分期总额;
    }

    public String get年日均() {
        return 年日均;
    }

    public void set年日均(String 年日均) {
        this.年日均 = 年日均;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}