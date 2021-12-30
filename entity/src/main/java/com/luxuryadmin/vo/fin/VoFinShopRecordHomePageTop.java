package com.luxuryadmin.vo.fin;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoFinShopRecordHomePageTop {

    @ApiModelProperty(value = "总收入", name = "totalInAmount", required = false)
    private String totalInAmount;

    @ApiModelProperty(value = "总支出", name = "totalOutAmount", required = false)
    private String totalOutAmount;

}
