package com.luxuryadmin.vo.ord;

import com.luxuryadmin.vo.org.VoAccessUserPageByApp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 临时仓订单page显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class VoOrderForTempPage {
    @ApiModelProperty(value = "临时仓订单集合显示", name = "list")
    private List<VoOrderForTemp> list;

    @ApiModelProperty(value = "页码", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "数量", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "是否有下一页", name = "hasNextPage")
    private Boolean  hasNextPage;

}
