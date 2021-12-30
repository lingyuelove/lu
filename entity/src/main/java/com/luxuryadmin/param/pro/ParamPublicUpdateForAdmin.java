package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamPublicUpdateForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/18 16:42
 */
@Data
@ApiModel(value="公价商品编辑后台", description="公价商品编辑后台")
public class ParamPublicUpdateForAdmin extends ParamPublicAddForAdmin{
    @ApiModelProperty(name = "id",required = true, value = "主键ID")
    private Integer id;
    @ApiModelProperty(name = "state",required = true, value = "状态")
    private Integer state;
}
