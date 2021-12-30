package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoDynamicProductShow
 * @Author: ZhangSai
 * Date: 2021/11/11 11:57
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoDynamicProductShow {

    private List<VoDynamicProductList> dynamicProducts;
    @ApiModelProperty(value = "编辑商品权限", name = "uPermEdit")
    private String uPermEdit;
    @ApiModelProperty(value = "删除动态权限", name = "uPermDynamicDel")
    private String uPermDynamicDel;
}
