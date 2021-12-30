package com.luxuryadmin.vo.fin;

import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
/**
 * 帐单列表集合显示
 *
 * @author zhangSai
 * @date 2021/04/23 14:19:52
 */
@Data
@ApiModel(value="帐单列表集合显示", description="帐单列表集合显示")
public class VoBillByApp {

    @ApiModelProperty(value = "帐单列表集合显示", name = "list")
    private List<VoBillPageByApp> list;

    @ApiModelProperty(value = "分页参数", name = "pageNum")
    private Integer pageNum;

    @ApiModelProperty(value = "分页参数", name = "pageSize")
    private Integer pageSize;

    @ApiModelProperty(value = "是否存在下一页", name = "hasNextPage")
    private Boolean  hasNextPage;

}
