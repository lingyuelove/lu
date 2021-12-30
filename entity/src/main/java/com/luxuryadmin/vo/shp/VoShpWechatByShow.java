package com.luxuryadmin.vo.shp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.shp
 * @ClassName: VoShpWechatByShow
 * @Author: ZhangSai
 * Date: 2021/8/30 17:06
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="友商店铺联系方式的显示", description="友商店铺联系方式的显示")
public class VoShpWechatByShow {

    @ApiModelProperty(name = "list", value = "友商店铺联系方式list;")
    private List<VoShpWechat> list;

    @ApiModelProperty(name = "strings", value = "负责内容集合显示;")
    private List<String> strings;

    @ApiModelProperty(name = "voShpWechat", value = "友商店铺手机联系方式;")
    private VoShpWechat voShpWechat;

    @ApiModelProperty(name = "unionVerifyUrl", value = "认证链接;")
    private String unionVerifyUrl;

    @ApiModelProperty(name = "unionVerifyState", value = "认证状态;状态 0 未审核 1已通过 2未通过3去认证")
    private String unionVerifyState;
}
