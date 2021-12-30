package com.luxuryadmin.vo.mem;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZhangSai
 * @Classname 会员店铺集合显示类
 * @Description TODO
 * @Date 2021/3/26 15:57
 * @Created by ZhangSai
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoExportMemShop {

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    /**
     * 店铺编号
     */
    @ExcelProperty(value = "店铺编号")
    @ApiModelProperty(value = "店铺编号")
    private String shopNumber;

    /**
     * 店铺名称
     */
    @ExcelProperty(value = "店铺名称")
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ExcelProperty(value = "会员类型")
    @ApiModelProperty(value = "会员类型")
    private String memberType;

    @ExcelProperty(value = "手机号码")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 邀请人昵称/用户编号
     */
    @ExcelProperty(value = "邀请人昵称/用户编号")
    @ApiModelProperty(value = "邀请人昵称/用户编号")
    private String fkInviteUser;

    /**
     * 付费使用开始时间
     */
    @ExcelProperty(value = "付费使用开始时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "付费使用开始时间")
    private Date payStartTime;

    @ExcelProperty(value = "会员状态")
    @ApiModelProperty(value = "会员状态:1正常 2封禁 3过期", name = "memberShopState")
    private String memberShopState;

    /**
     * 店铺状态的code;禁用:-99,启用:10
     */
    @ExcelProperty(value = "店铺状态")
    private String fkShpStateCode;

}
