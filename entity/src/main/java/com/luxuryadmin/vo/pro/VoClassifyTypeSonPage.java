package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoClassifyTypeSonPage
 * @Author: ZhangSai
 * Date: 2021/8/13 14:34
 */
@Data
@ApiModel(value="补充信息分类二级page显示实体参数", description="补充信息分类二级page显示实体参数")
public class VoClassifyTypeSonPage {
    @ApiModelProperty(value = "补充信息分类二级级集合显示")
    private List<VoClassifyTypeSonList> list;

    @ApiModelProperty(value = "pageNum", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "pageSize", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "hasNextPage", name = "hasNextPage")
    private Boolean  hasNextPage;

    @ApiModelProperty(value = "总数", name = "totalCount")
    private long total;
}
