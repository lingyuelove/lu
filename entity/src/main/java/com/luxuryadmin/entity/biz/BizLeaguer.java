package com.luxuryadmin.entity.biz;

import lombok.Data;

import java.util.Date;

/**
 * 商务模块--友商好友管理(联系人)--一个好友只有一条记录;可重复使用(包含友商黑名单);
 *
 * @author monkey king
 * @date 2019/12/01 04:54:26
 */
@Data
public class BizLeaguer {
    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 邀请者(店主)shp_shop的id字段,主键id
     */
    private Integer fkInviterShopId;

    /**
     * 被邀请者(友商)shp_shop的id字段,主键id
     */
    private Integer fkBeInviterShopId;

    /**
     * 给友商备注
     */
    private String note;

    /**
     * 是否允许友商查看店铺商品; 0:不允许; 1:允许
     */
    private String visible;

    /**
     * 置顶; 0:不置顶; 1:置顶
     */
    private String top;

    /**
     * 标签
     */
    private String label;

    /**
     * 好友状态: -90: 拉黑; -10:已删除; 10:已发请求(待确认); 20:已成为好友
     */
    private String state;

    /**
     * 标记:1:主动加好友(inviter为主动); 2:被动同意为好友(be_inviter为主动);
     */
    private String mark;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 确认成为好友时间
     */
    private Date becomeFriendTime;

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
     * 是否可以查看销售价
     */
    private Integer isCanSeeSalePrice;

    /**
     * 是否可以查看友商价
     */
    private Integer isCanSeeTradePrice;

    /**
     * 是否可以查看友商价
     */
    private Integer isWantSeeLeaguerProd;



    public BizLeaguer() {

    }

    /**
     * 添加成为友商, 默认state=20
     *
     * @param fkInviterShopId   主动添加
     * @param fkBeInviterShopId 被动添加
     * @param mark              mark 为 1时, 上述成立, mark 为2时, 反过来;
     */
    public BizLeaguer(Integer fkInviterShopId, Integer fkBeInviterShopId, String mark) {
        this.fkInviterShopId = fkInviterShopId;
        this.fkBeInviterShopId = fkBeInviterShopId;
        this.state = "20";
        this.mark = mark;
        this.insertTime = new Date();
        this.becomeFriendTime = this.insertTime;
    }

    public BizLeaguer(Integer fkInviterShopId, Integer fkBeInviterShopId, String note,
                      String visible, String label, String state, String mark) {
        this.fkInviterShopId = fkInviterShopId;
        this.fkBeInviterShopId = fkBeInviterShopId;
        this.note = note;
        this.visible = visible;
        this.label = label;
        this.state = state;
        this.mark = mark;
    }
}