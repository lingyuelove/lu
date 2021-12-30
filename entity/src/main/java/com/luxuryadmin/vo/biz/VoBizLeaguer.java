package com.luxuryadmin.vo.biz;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.vo.shp.VoShpWechat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-01-11 22:24:02
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
@Data
public class VoBizLeaguer {

    /**
     * id
     */
    @ApiModelProperty(value="leaguerId")
    private Integer leaguerId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value="店铺名称")
    private String shopName;

    /**
     * 被邀请者(友商)shp_shop的id字段,主键id
     */
    @ApiModelProperty(value="被邀请者(友商)shp_shop的id字段,主键id")
    private Integer shopId;

    /**
     * 店铺头像
     */
    @ApiModelProperty(value="店铺头像")
    private String headImgUrl;

    /**
     * 店铺头像
     */
    @ApiModelProperty(value="店铺头像")
    private String coverImgUrl;

    /**
     * 店铺地址
     */
    @ApiModelProperty(value="店铺地址")
    private String address;

    /**
     * 给友商备注
     */
    @ApiModelProperty(value="给友商备注")
    private String note;

    /**
     * 添加时间
     */
    @ApiModelProperty(value="添加时间")
    private Date insertTime;

    /**
     * 是否允许友商查看店铺商品; 0:不允许; 1:允许
     */
    @ApiModelProperty(value="是否允许友商查看店铺商品; 0:不允许; 1:允许")
    private String visible;

    /**
     * 置顶; 0:不置顶; 1:置顶
     */
    @ApiModelProperty(value="置顶; 0:不置顶; 1:置顶")
    private String top;

    /**
     * 店长手机号
     */
    @ApiModelProperty(value="店长手机号;(搜索添加友商时,如果显示手机号,则代表已成为友商)")
    private String phone;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value="店铺编号")
    private String shopNumber;

    /**
     * 经营者昵称
     */
    @ApiModelProperty(value="经营者昵称")
    private String shopkeeperNickname;

    /**
     * 是否可以查看销售价
     */
    @ApiModelProperty(value="是否可以查看销售价 1|可以 0|不可以")
    private Integer isCanSeeSalePrice;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否可以查看友商价 1|可以 0|不可以")
    private Integer isCanSeeTradePrice;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否看友商店铺商品 1|可以 0|不可以")
    private Integer isWantSeeLeaguerProd;

    /**
     * 是否可以查看友商价
     */
    @ApiModelProperty(value="是否看友商店铺商品 1|可以 0|不可以")
    private Integer notSeeGoods;

    /**
     * 店铺微信列表
     */
    @ApiModelProperty(value="店铺微信列表")
    private List<VoShpWechat> voShpWechatList;

    /**
     * 店铺商品数量
     */
    @ApiModelProperty(value="店铺在售商品数量")
    private Integer onSaleProductNum;

    @ApiModelProperty(value="友商好友状态 0:不是好友; 1:是好友 2是本人")
    private String leaguerFriendState;

    @ApiModelProperty(value = "友商店铺编号", name = "leaguerShopNumber")
    private String leaguerShopNumber;
}
