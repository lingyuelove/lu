package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoProClassifySubPageForAdmin
 * @Author: ZhangSai
 * Date: 2021/8/17 11:06
 */
@Data
@ApiModel(value="后台集合显示品牌分类", description="后台集合显示品牌分类")
public class VoProClassifySubPageForAdmin {

    private List<VoProClassifySubForAdmin> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private long total;
}
