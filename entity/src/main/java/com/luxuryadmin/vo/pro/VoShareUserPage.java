package com.luxuryadmin.vo.pro;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoShareUserPage
 * @Author: ZhangSai
 * Date: 2021/6/30 16:10
 */
@Data
@ApiModel(value="小程序访分享page", description="小程序访分享page")
public class VoShareUserPage {
    private List<VoShareUserList> list;

    private Integer pageNum;

    private Integer pageSize;

    private Long total;


    private Boolean  hasNextPage;
}
