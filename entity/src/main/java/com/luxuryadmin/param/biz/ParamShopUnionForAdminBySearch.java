package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.param.biz
 * @ClassName: ParamShopUnionForAdminBySearch
 * @Author: ZhangSai
 * Date: 2021/7/16 19:57
 */
@Data
@ApiModel(description = "商家联盟后台管理搜索")
public class ParamShopUnionForAdminBySearch {
    /**
     * 联盟时间 起始
     */
    @ApiModelProperty(value = "查询条件中-联盟时间-开始 yyyy-MM-dd HH:mm:ss", name = "insertTimeStart")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTimeStart;
    /**
     * 联盟时间 起始
     */
    @ApiModelProperty(value = "查询条件中-联盟时间-结束 yyyy-MM-dd HH:mm:ss", name = "insertTimeEnd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insertTimeEnd;

    @ApiModelProperty(value = "登录标识符", name = "token")
    private String token;

    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
    @Min(value = 1, message = "当前页不能小于1")
    @Max(value = 999, message = "当前页最大为999")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    @ApiModelProperty(value = "店铺编号", name = "shopNumber", required = false)
    @Pattern(regexp = "^[0-9]+$", message = "店铺编号--参数错误")
    private String shopNumber;

    @ApiModelProperty(value = "店铺名称", name = "shopName", required = false)
    private String shopName;

    @ApiModelProperty(value = "店铺id", name = "shopId", required = false)
    @Pattern(regexp = "^[0-9,]+$", message = "店铺id--参数错误")
    private String shopId;

    @ApiModelProperty(value = "搜索号码手机号", name = "phone", required = false)
    private String phone;

    /**
     * 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     */
    @ApiModelProperty(value = "会员状态0:非会员; 1:会员", name = "memberState", required = false)
    private String memberState;

    @ApiModelProperty(value = "添加类型 10 白名单 20 商家店铺", name = "type", required = true)
    private String type;

    @ApiModelProperty(value = "上传状态: 未上传 | 部分上传 | 全部上传", name = "uploadState", required = false)
    private String uploadState;
    @ApiModelProperty(value = "同行价大于x x的值", name = "tradePrice")
    private String tradePrice;
}
