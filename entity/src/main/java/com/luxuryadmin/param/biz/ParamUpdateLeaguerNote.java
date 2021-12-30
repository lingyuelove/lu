package com.luxuryadmin.param.biz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * 修改添加友商请求记录--前端接收参数模型
 *
 * @author monkey king
 * @date 2020-07-29 20:06:21
 */
@ApiModel(description = "修改添加友商请求记录--前端接收参数模型")
public class ParamUpdateLeaguerNote extends ParamUpdateLeaguer {

    /**
     * 好友状态: -10:已删除该条信息; 10:已发请求(待确认); 11:已发请求(已忽略); 12:已发请求(已过期); 20:已成为好友"
     */
    @ApiModelProperty(value = "备注", name = "note", required = false)
    @Length(max = 18, message = "备注长度不得超过18字符")
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
