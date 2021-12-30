package com.luxuryadmin.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
/**
 * 机构仓app端显示
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="机构临时仓app端显示大类", description="机构临时仓app端显示大类")
public class VoOrganizationTempByApp {
    private List<VoOrganizationTempPageByApp> list;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    private  String vid;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty(value = "开始时间", name = "startTime", required = false)
    private String startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @ApiModelProperty(value = "结束时间", name = "endTime", required = false)
    private String endTime;
}
