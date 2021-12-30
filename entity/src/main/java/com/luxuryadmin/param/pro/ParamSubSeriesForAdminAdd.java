package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifySubForAdminAdd
 * @Author: ZhangSai
 * Date: 2021/8/17 15:34
 */
@ApiModel(description = "品牌系列添加类")
@Data
public class ParamSubSeriesForAdminAdd {
    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;
//    @ApiModelProperty(name = "classifyCode", required = false,value = " 商品分类code; 和分类列表对应; 默认0:无分类;逗号分隔")
//    private String classifyCode;
    /**
     * 商品品牌分类pro_classify_sub表品牌名称
     */
    @ApiModelProperty(name = "classifySubName", required = false,value = "商品品牌分类pro_classify_sub表品牌名称")
    private String classifySubName;

    /**
     * 分类名称;限长20个汉字
     */
    @ApiModelProperty(name = "name", required = false,value = "系列名称")
    private String name;


    @ApiModelProperty(name = "serviceModelName", required = false,value = "型号名称;逗号分隔")
    private String serviceModelName;
}
