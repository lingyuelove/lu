package com.luxuryadmin.vo.pro;

import com.luxuryadmin.vo.org.VoTempSeatList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 根据临时仓id获取详情 ps:机构临时仓所需接口
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="根据临时仓id获取详情", description="根据临时仓id获取详情")
public class VoTempForOrg {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    @ApiModelProperty(value = "主键Id临时仓编号", name = "id")
    private String id;


    @ApiModelProperty(value = "主键Id临时仓编号", name = "id")
    private Integer shopId;

    /**
     * 临时仓名称
     */
    @ApiModelProperty(value = "临时仓名称", name = "tempName")
    private String tempName;

    /**
     * 店铺编号: 店铺id拼接毫秒级别的时间戳
     */
    @ApiModelProperty(value = "店铺编号: 店铺id拼接毫秒级别的时间戳", name = "number")
    private String shopNumber;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    /**
     * 店铺固话
     */
    @ApiModelProperty(value = "店铺固话", name = "shopContact")
    private String shopContact;

    @ApiModelProperty(value = "商品数量", name = "productCount")
    private Integer productCount;

    @ApiModelProperty(value = "店铺机构仓排序位置分组集合显示", name = "tempSeatLists")
    private List<VoTempSeatList> tempSeatLists;

}
