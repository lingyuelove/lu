package com.luxuryadmin.param.mem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author ZhangSai
 * @Classname 会员店铺查询类
 * @Description TODO
 * @Date 2021/3/26 15:57
 * @Created by ZhangSai
 */
@Data
public class ParamMemShop {
    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    /**
     * 开通时间 起始
     */
    @ApiModelProperty(value = "查询条件中-开通时间范围-开始", name = "insertTimeStart", required = false)
    private Date startTimeStart;

    /**
     * 开通时间 结束
     */
    @ApiModelProperty(value = "查询条件中-开通时间范围-结束", name = "insertTimeEnd", required = false)
    private Date startTimeEnd;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", name = "phone", required = false)
    private String phone;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
     */
    @ApiModelProperty(value = "店铺编号", name = "number", required = false)
    private String number;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称", name = "name", required = false)
    private String name;

    /**
     * 会员状态:  0:正常; 1:封禁;2:过期
     */
    @ApiModelProperty(value = "会员状态:  1:正常; 2:封禁;3:过期", name = "memberShopState", required = false)
    private Integer memberShopState;

    /**
     *  会员类型：0:年费会员;
     */
    @ApiModelProperty(value = "会员类型：0:年费会员;", name = "memberType", required = false)
    private Integer memberType;

    @ApiModelProperty(value = "邀请人昵称/用户编号", name = "memberType", required = false)
    private String fkInviteUser;
}
