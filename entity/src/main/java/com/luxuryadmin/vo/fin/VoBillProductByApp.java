package com.luxuryadmin.vo.fin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * 各类商品价格类
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value="各类商品价格类", description="各类商品价格类")
public class VoBillProductByApp {
    @ApiModelProperty(name = "billProductMoneyByApps", value = "各类商品价格类")
    private List<VoBillProductMoneyByApp> list;

    @ApiModelProperty(name = "vid", value = "vid")
    private String vid;
}
