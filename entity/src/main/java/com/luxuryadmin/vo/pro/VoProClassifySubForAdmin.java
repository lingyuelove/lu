package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProClassifySubForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/17 10:49
 */
@Data
@ApiModel(value="后台集合显示品牌分类", description="后台集合显示品牌分类")
public class VoProClassifySubForAdmin {

    /**
     * 二级分类id
     */
    @ApiModelProperty(value = "二级分类id", name = "id")
    private Integer id;

    /**
     * 图标地址
     */
    @ApiModelProperty(value = "图标地址", name = "iconUrl")
    private String iconUrl;


    /**
     * 二级分类名称
     */
    @ApiModelProperty(value = "二级分类名称", name = "name")
    private String name;

    /**
     * 状态;-1:已删除;0:未使用;1:使用中
     */
    @ApiModelProperty(value = "状态;0:隐藏;1:显示")
    private Integer state;

    /**
     * 商品分类code; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = " 商品分类code; 和分类列表对应; 默认0:无分类;")
    private String classifyCode;

    /**
     * 商品分类中文名称; 和分类列表对应; 默认0:无分类;
     */
    @ApiModelProperty(value = " 商品分类中文名称; 和分类列表对应; 默认0:无分类;")
    private String classifyCodeName;
}
