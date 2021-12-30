package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamPublicByDeleteForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/19 14:38
 */
@Data
@ApiModel(value="公价商品删除后台", description="公价商品删除后台")
public class ParamPublicByDeleteForAdmin {
    @ApiModelProperty(name = "id",required = true, value = "主键ID")
    private Integer id;


}
