package com.luxuryadmin.vo.biz;



import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoLeaguerRecommendPage
 * @Author: ZhangSai
 * Date: 2021/5/27 18:27
 */
@Data
@ApiModel(value="推荐友商Page显示类", description="推荐友商Page显示类")
public class VoLeaguerRecommendPage {

    private List<VoLeaguerRecommend> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;
}
