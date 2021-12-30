package com.luxuryadmin.vo.pro;

import com.luxuryadmin.entity.ord.OrdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @Author: taoqimin
 * Date: 2021-09-27
 */
@Data
@ApiModel(value="筛选信息", description="筛选信息")
public class VoFiltrateInfo {

    @ApiModelProperty(value = "发货人员")
    private List<VoOrderUserInfo> listDeliverUser;

    @ApiModelProperty(value = "锁单人员")
    private List<VoOrderUserInfo> listLockUser;

    @ApiModelProperty(value = "开单人员")
    private List<VoOrderUserInfo> listOrderrUser;

    @ApiModelProperty(value = "订单类型")
    private List<OrdType>  ordTypeList;
}
