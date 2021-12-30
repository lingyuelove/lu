package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProSubSeriesByPageForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/17 16:46
 */
@Data
@ApiModel(value="后台集合显示型号分类", description="后台集合显示型号分类")
public class VoProSubSeriesByPageForAdmin {

    private List<VoProSubSeriesForAdmin> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private long total;

}
