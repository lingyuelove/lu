package com.luxuryadmin.param.shp;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author Administrator
 * @Classname ParamShpShop
 * @Description TODO
 * @Date 2020/6/24 13:57
 * @Created by Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamShpShop extends ParamToken {


    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    /**
     * 注册时间 起始
     */
    @ApiModelProperty(value = "查询条件中-注册时间范围-开始", name = "insertTimeStart", required = false)
    private Date insertTimeStart;

    /**
     * 注册时间 结束
     */
    @ApiModelProperty(value = "查询条件中-注册时间范围-结束", name = "insertTimeEnd", required = false)
    private Date insertTimeEnd;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
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
     * 认证状态:
     */
    @ApiModelProperty(value = "认证状态: 0:未认证; 1:已认证", name = "validateState", required = false)
    @Pattern(regexp = "^[01]$", message = "认证状态参数格式错误")
    private String validateState;

    /**
     * 是否逻辑删除;0:false:不删除;1:true:逻辑删除;对用户显示，所有查询sql都要带上del=0这个条件
     */
    @Pattern(regexp = "^[01]$", message = "是否逻辑删除参数格式错误")
    private String del = "0";

    /**
     * 是否会员
     */
    @ApiModelProperty(value = "yes:会员 | no:非会员", name = "validateState", required = false)
    private String member;

    /**
     * 运营人员id
     */
    @ApiModelProperty(value = "运营人员id", name = "sysJobWxId", required = false)
    private String sysJobWxId;

}
