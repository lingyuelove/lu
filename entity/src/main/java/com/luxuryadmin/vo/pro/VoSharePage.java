package com.luxuryadmin.vo.pro;

import com.luxuryadmin.vo.org.VoOrganizationTempPageByApplets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.luxuryadmin.vo.pro
 * @ClassName: VoSharePage
 * @Author: ZhangSai
 * Date: 2021/6/30 15:18
 */
@Data
@ApiModel(value="小程序分享page", description="小程序分享page")
public class VoSharePage {
    private List<VoShareList> list;

    private Integer pageNum;

    private Integer pageSize;

    private Long total;
    private Boolean  hasNextPage;
    @ApiModelProperty(value = "是否开启访客功能 0 未开启 1已开启", name = "openShareUser")
    private String openShareUser;

    @ApiModelProperty(value = "访客跳转说明链接", name = "openShareUserUrl")
    private String openShareUserUrl;
}
