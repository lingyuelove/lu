package com.luxuryadmin.param.pro;

import com.luxuryadmin.param.common.ParamBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="盘点集合显示搜索参数", description="盘点集合显示搜索参数")
public class ParamCheckListForApiBySearch extends ParamBasic {

    /**
     * 登录标识符
     */
    @ApiModelProperty(value = "登录标识符", name = "token", required = true)
    private String token;

//    @ApiModelProperty(value = "分页查询，页码：不传默认为1;", name = "pageNum", required = false)
//    @Min(value = 1, message = "当前页不能小于1")
//    @Max(value = 999, message = "当前页最大为999")
//    private int pageNum = 1;

    @ApiModelProperty(value = "分页查询，每页多少条数据：不传默认为10;", name = "pageSize", required = false)
    @Min(value = 10, message = "分页不能小于10")
    @Max(value = 100, message = "条数显示最大为100条")
    private int pageSize = 10;

    /**
     * shp_shop店铺id
     */
    private Integer fkShpShopId;

    /**
     * 盘点状态 10:进行中 | 20:取消 | 30:完成
     */
    @ApiModelProperty(value = "盘点状态 10:进行中 | 20:取消 | 30:完成", name = "checkState", required = true)
    @NotBlank(message = "盘点状态不为空")
    private String checkState;
}
