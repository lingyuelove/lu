package com.luxuryadmin.vo.shp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.shp
 * @ClassName: VoShopMember
 * @Author: ZhangSai
 * Date: 2021/6/8 20:52
 */
@Data
@ApiModel(value="店铺会员", description="店铺会员")
public class VoShopMemberAddHour {

    @ApiModelProperty(value = "今日分享次数", name = "shareCount")
    private Integer shareCount;

    @ApiModelProperty(value = "累计获得时长", name = "totalHours")
    private String totalHours;

    @ApiModelProperty(value = "原到期日", name = "payEndTime")
    private String payEndTime;

    @ApiModelProperty(value = "原到期日", name = "payEndTimeOld")
    private String payEndTimeOld;

    @ApiModelProperty(value = "已获得日数", name = "dayCount")
    private Integer dayCount;

    @ApiModelProperty(value = "今日可分享总次数", name = "shareTotalCount")
    private Integer shareTotalCount;

    @ApiModelProperty(value = "会员标题", name = "memberTitle")
    private String memberTitle;

    @ApiModelProperty(value = "会员描述", name = "description")
    private String description;

    @ApiModelProperty(value = "活动规则链接", name = "showUrl")
    private String showUrl;

    @ApiModelProperty(value = "非会员说明", name = "tipTitle")
    private String tipTitle;
    @ApiModelProperty(value = "会员到期", name = "vipExpireTip")
    private String vipExpireTip;
}
