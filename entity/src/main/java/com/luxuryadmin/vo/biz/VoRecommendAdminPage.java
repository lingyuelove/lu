package com.luxuryadmin.vo.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoRecommendAdminPage
 * @Author: ZhangSai
 * Date: 2021/5/27 20:50
 */
@Data
@ApiModel(value="推荐友商Page admin端显示类", description="推荐友商Page admin端显示类")
public class VoRecommendAdminPage {

    private List<VoRecommendAdminList> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;
}
