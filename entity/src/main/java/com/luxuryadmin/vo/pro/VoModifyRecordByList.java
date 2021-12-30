package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoModifyRecordByList
 * @Author: ZhangSai
 * Date: 2021/6/4 14:02
 */
@Data
@ApiModel(value="商品操作日志", description="商品操作日志")
public class VoModifyRecordByList {
    /**
     * 主键ID
     */
    @ApiModelProperty(name = "id", required = false, value = "主键ID")
    private Integer id;

    /**
     * 商品图片URL
     */
    @ApiModelProperty(name = "operateUserName", required = false, value = "操作人")
    private String operateUserName;

    /**
     * 商品名称
     */
    @ApiModelProperty(name = "operateTime", required = false, value = "操作时间")
    private String operateTime;

    /**
     * 备注
     */
    @ApiModelProperty(name = "operateModule", required = false, value = "操作模块")
    private String operateModule;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "operateContent", required = false, value = "操作内容")
    private String operateContent;

    @ApiModelProperty(value = "来源 默认app app；pc")
    private String source;
}
