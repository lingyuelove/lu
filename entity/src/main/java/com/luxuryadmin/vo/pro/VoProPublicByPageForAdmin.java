package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProPublicByPageForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/18 15:37
 */
@Data
@ApiModel(value="公价商品库后台接口", description="公价商品库后台接口")
public class VoProPublicByPageForAdmin {
    @ApiModelProperty(value = "集合显示")
    private List<VoProPublicForAdmin> list;

    @ApiModelProperty(value = "pageNum", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "pageSize", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "hasNextPage", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "总数", name = "totalCount")
    private long total;
}
