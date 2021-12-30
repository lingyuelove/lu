package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * 意见反馈--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-06-05 23:03:43
 */
@ApiModel(description = "意见反馈")
public class ParamBizComment {

    /**
     * token;登录标识符
     */
    @ApiModelProperty(name = "token", required = true, value = "token;登录标识符")
    private String token;

    /**
     * 内容
     */
    @ApiModelProperty(name = "content", required = true, value = "内容;长度不超过500")
    @Length(max = 500, message = "内容长度必须≤500个字符")
    private String content;


    /**
     * 平台类型：pc，android，ios，wap
     */
    @ApiModelProperty(name = "platform", required = true, value = "平台类型：pc，android，ios，wap")
    @Pattern(regexp = "^(pc)|(android)|(ios)|(wap)$", message = "平台类型--参数错误")
    private String platform;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
