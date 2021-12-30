package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoModifyRecordByPage
 * @Author: ZhangSai
 * Date: 2021/6/4 14:02
 */
@Data
@ApiModel(value="商品操作日志", description="商品操作日志")
public class VoModifyRecordByPage {
    @ApiModelProperty(value = "商品操作日志", name = "modifyRecordByLists")
    private List<VoModifyRecordByList> list;

    @ApiModelProperty(value = "pageNum", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "pageSize", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "hasNextPage", name = "hasNextPage")
    private Boolean  hasNextPage;
}
