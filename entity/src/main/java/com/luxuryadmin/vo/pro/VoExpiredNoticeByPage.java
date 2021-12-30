package com.luxuryadmin.vo.pro;

import com.luxuryadmin.vo.fin.VoBillDayPageByApp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Data
@ApiModel(value="商品过期提醒", description="商品过期提醒")
public class VoExpiredNoticeByPage {

    @ApiModelProperty(value = "商品过期提醒列表", name = "expiredNoticeByLists")
    private List<VoExpiredNoticeByList> expiredNoticeByLists;

    @ApiModelProperty(value = "总数", name = "totalCount")
    private Integer totalCount;

}
