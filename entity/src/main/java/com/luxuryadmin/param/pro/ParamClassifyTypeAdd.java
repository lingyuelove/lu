package com.luxuryadmin.param.pro;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.param.pro
 * @ClassName: ParamClassifyTypeAdd
 * @Author: ZhangSai
 * Date: 2021/8/3 11:15
 */
@Data
@ApiModel(value="新增补充信息分类实体参数", description="新增补充信息分类实体参数")
public class ParamClassifyTypeAdd extends ParamToken {

    @ApiModelProperty(name = "shopId", hidden = true, value = "商铺主键id 后端自己获取")
    private Integer shopId;

    @ApiModelProperty(name = "userId", hidden = true, value = "用户主键id 后端自己获取")
    private Integer userId;

//    @ApiModelProperty(name = "classifyTypeId", required = false,value = "商品补充信息分类表主键id,供二级三级使用")
//    private Integer classifyTypeId;
    @ApiModelProperty(name = "name", required = true,value = "分类名称;限长50个汉字")
    @NotBlank(message = "name--参数为空错误")
    private String name;

    @ApiModelProperty(name = "sort", required = false,value = "序号排序")
    private Integer sort;
    @ApiModelProperty(name = "type", required = false,value = "类型;1:一级分类;2:二级分类;3:三级分类")
    private String type;

    @ApiModelProperty(name = "choseType", required = true,value = "二级分类必填；参数类型;1:下拉框;2:单行输入框;3:单选框;4:复选框;5:单选标签;6:多行输入框;")
    @NotBlank(message = "choseType--参数为空错误")
    private String choseType;

    @ApiModelProperty(name = "classifyCode", required = true,value = "商品系列表英文拼写，现只有箱包和腕表")
    @NotBlank(message = "classifyCode--参数为空错误")
    private String classifyCode;

    @ApiModelProperty(name = "classifyTypeSunAdds", required = false,value = "二级分类内容")
    private String classifyTypeSunAdds;
}
