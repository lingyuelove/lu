package com.luxuryadmin.vo.pro;

import com.luxuryadmin.vo.sys.VoSysEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="临时仓商品在机构集合显示小程序端", description="临时仓商品在机构集合显示小程序端")
public class VoTempProductOrgByApplets {

    @ApiModelProperty(value = "临时仓商品在机构集合显示小程序端", name = "list")
    private List<VoTempProductOrgPageByApplets> list;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "店铺头像", name = "headImgUrl")
    private String headImgUrl;

    /**
     * 店铺固话
     */
    @ApiModelProperty(value = "店铺固话", name = "shopContact")
    private String shopContact;

    @ApiModelProperty(value = "展会位置", name = "showSeat")
    private String showSeat;

    @ApiModelProperty(value = "店铺机构仓排序位置分组名称", name = "tempSeatName")
    private String tempSeatName;

    private Integer pageNum;

    private Integer pageSize;

    private Boolean  hasNextPage;

    @ApiModelProperty(value = "搜索查询集合类型", name = "搜索查询集合类型")
    private List<VoSysEnum>  sysEnums;

    /**
     * 封面照片地址
     */
    @ApiModelProperty(value = "封面照片地址", name = "coverImgUrl")
    private String coverImgUrl;

}
