package com.luxuryadmin.vo.org;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
/**
 * 机构仓app端显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构仓app端显示大类", description="机构仓app端显示大类")
public class VoOrganizationByApp {

    private List<VoOrganizationPageByApp> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private  String vid;

}
