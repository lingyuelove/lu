package com.luxuryadmin.vo.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.biz
 * @ClassName: VoShopUnionForAdminPage
 * @Author: ZhangSai
 * Date: 2021/7/19 10:09
 */
@Data
@ApiModel(value="商家联盟Page显示类", description="商家联盟Page显示类")
public class VoShopUnionForAdminPage {
    private List<VoShopUnionForAdminList> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private long total;
}
