package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoMoveProductLoad
 * @Author: ZhangSai
 * Date: 2021/9/27 15:11
 */
@Data
public class VoMoveProductLoad extends VoProductLoad{

    @ApiModelProperty(value = "操作人 --2.6.4 ",name = "moveUserName")
    private String moveUserName;
    @ApiModelProperty(value = "转出仓名称 --2.6.4 ",name = "moveTempName;")
    private String removeTempName;
    @ApiModelProperty(value = "转入仓名称 --2.6.4 ",name = "enterTempName;")
    private String enterTempName;
    @ApiModelProperty(value = "转仓时间 --2.6.4 ",name = "moveTime")
    private String moveTime;

    @ApiModelProperty(value = "临时仓转出数量 --2.6.4 ",name = "moveNum")
    private String moveNum;
}
