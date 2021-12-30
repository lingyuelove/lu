package com.luxuryadmin.vo.biz;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoShopUnionForAdminList
 * @Author: ZhangSai
 * Date: 2021/7/19 10:10
 */
@Data
@ApiModel(value="商家联盟List显示类", description="商家联盟List显示类")
public class VoShopUnionForAdminList {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    @ExcelIgnore
    @ApiModelProperty(value = "主键Id", name = "id")
    private Integer id;

    /**
     * 店铺表主键Id,逻辑id,软件内部关联
     */
    @ExcelIgnore
    @ApiModelProperty(value = "店铺表主键Id", name = "shopId")
    private Integer shopId;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "店铺编号", index = 0)
    @ApiModelProperty(value = "店铺编号", name = "number")
    private String number;

    /**
     * 店铺名称
     */
    @ExcelProperty(value = "店铺名称", index = 1)
    @ColumnWidth(20)
    @ApiModelProperty(value = "店铺名称", name = "name")
    private String name;

    /**
     * 店铺地址
     */
    @ExcelProperty(value = "店铺地址", index = 3)
    @ColumnWidth(20)
    @ApiModelProperty(value = "店铺地址", name = "address")
    private String address;

    /**
     * 上传状态
     */
    @ApiModelProperty(value = "上传状态", name = "uploadState")
    @ExcelProperty(value = "上传状态", index = 7)
    @ColumnWidth(20)
    private String uploadState;

    /**
     * 店铺固话
     */
    @ExcelProperty(value = "手机号", index = 2)
    @ColumnWidth(20)
    @ApiModelProperty(value = "店铺固话", name = "contact")
    private String contact;

    /**
     * 商品*库存数量
     */
    @ExcelProperty(value = "店铺商品总数", index = 4)
    @ColumnWidth(20)
    @ApiModelProperty(value = "总数量（商品*库存）", name = "totalNum")
    private Integer totalNum;

    /**
     * 商品*库存数量
     */
    @ExcelProperty(value = "有效同行价商品数量", index = 5)
    @ColumnWidth(30)
    @ApiModelProperty(value = "同行价大于x的总数量（商品*库存）", name = "totalNum")
    private Integer totalTradePriceNum;

    /**
     * 联盟开始时间
     */
    @ExcelProperty(value = "加入时间", index = 6)
    @ColumnWidth(20)
    @ApiModelProperty(value = "联盟时间-开始 yyyy-MM-dd HH:mm:ss", name = "insertTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String insertTime;
    /**
     * 0:非会员; 1:体验会员;2:正式会员;3:靓号会员
     */
    @ExcelIgnore
    @ApiModelProperty(value = "会员状态0:非会员; 1:会员", name = "memberState")
    private String memberState;

    @ExcelIgnore
    @ApiModelProperty(value = "经营者手机号", name = "phoneNumber")
    private String phoneNumber;
    @ExcelIgnore
    @ApiModelProperty(value = "会员付费时间-开始 yyyy-MM-dd HH:mm:ss", name = "payStartTime")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String payStartTime;

    @ExcelIgnore
    @ApiModelProperty(value = "店铺注册时间 yyyy-MM-dd HH:mm:ss", name = "shopInsertTime")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String shopInsertTime;
}
