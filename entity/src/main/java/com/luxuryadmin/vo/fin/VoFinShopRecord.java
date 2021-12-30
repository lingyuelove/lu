package com.luxuryadmin.vo.fin;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.net.URL;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinShopRecord {

    /**
     * 主键ID
     */
    @ExcelIgnore
    @ApiModelProperty(value = "主键ID", name = "id", required = false)
    private Integer id;

    /**
     * 图片地址
     */
    @ExcelIgnore
    @ApiModelProperty(value = "图片地址", name = "imgUrl", required = false)
    private String imgUrl;

    @ColumnWidth(20)
    @ExcelProperty(value = "图片", index = 6)
    @ApiModelProperty(value = "图片地址", name = "imgUrl", required = false)
    private URL  imgUrlShow;
    /**
     * 流水详情图片地址
     */
    @ExcelIgnore
    @ApiModelProperty(value = "流水详情图片地址", name = "imgUrlDetail", required = false)
    private String imgUrlDetail;

    /**
     * 流水详情图片地址列表
     */
    @ExcelIgnore
    @ApiModelProperty(value = "流水详情图片地址列表", name = "imgUrlDetailList", required = false)
    private List<String> imgUrlDetailList;

    /**
     * 流水类型名称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "分类", index = 1)
    @ApiModelProperty(value = "流水类型名称", name = "finRecordTypeName", required = false)
    private String finRecordTypeName;

    /**
     * 流水类型名称
     */
    @ExcelIgnore
    @ApiModelProperty(value = "收入支出类型", name = "收入支出类型 in|收入 out|支出", required = false)
    private String inoutType;

    /**
     * 备注
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "备注", index = 5)
    @ApiModelProperty(value = "备注", name = "note", required = false)
    private String note;

    /**
     * 创建时间
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间", index = 3)
    @ApiModelProperty(value = "创建时间", name = "insertTime", required = false)
    private String insertTime;

    /**
     * 流水时间
     */
    @ExcelIgnore
    @ApiModelProperty(value = "发送时间", name = "happenTime", required = false)
    private String happenTime;

    /**
     * 流水日期
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "发生日期", index = 2)
    @ApiModelProperty(value = "流水日期", name = "happenDate", required = false)
    private String happenDate;

    /**
     * 变动金额
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "费用", index = 0)
    @ApiModelProperty(value = "变动金额", name = "changeAmount", required = false)
    private String changeAmount;

    /**
     * 报销人在店铺的昵称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建人", index = 4)
    @ApiModelProperty(value = "报销人在店铺的昵称", name = "userShopNickname", required = false)
    private String userShopNickname;

}
