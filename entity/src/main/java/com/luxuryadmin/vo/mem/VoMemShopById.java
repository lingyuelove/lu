package com.luxuryadmin.vo.mem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZhangSai
 * @Classname 会员店铺单个显示类
 * @Description TODO
 * @Date 2021/3/26 15:57
 * @Created by ZhangSai
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoMemShopById {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 付费使用结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付费使用结束时间")
    private Date payEndTime;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号")
    private String shopNumber;


    /**
     * 备注
     */
    @ApiModelProperty(value = "封禁理由")
    private String remark;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "友商数量")
    private String leaguerCount;

    /**
     * 店铺状态的code;禁用:-99,启用:10
     */
    @ApiModelProperty(value = "店铺状态的code;禁用:-99,启用:10")
    private Integer fkShpStateCode;
}
