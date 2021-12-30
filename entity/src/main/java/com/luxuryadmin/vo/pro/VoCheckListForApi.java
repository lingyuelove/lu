package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="盘点集合显示参数", description="盘点集合显示参数")
public class VoCheckListForApi {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id", name = "id")
    private Integer id;


    /**
     * shp_shop店铺id
     */
    @ApiModelProperty(value = "shp_shop店铺id", name = "fkShpShopId")
    private Integer fkShpShopId;
    /**
     * pro_temp的id字段,主键id
     */
    @ApiModelProperty(value = "临时仓id", name = "tempId")
    private Integer tempId;
    /**
     *盘点类型 临时仓盘点 仓库盘点
     */
    @ApiModelProperty(value = "盘点类型 temp:临时仓； warehouse:仓库", name = "type")
    private String type;
    @ApiModelProperty(value = "盘点类型 temp:临时仓； warehouse:仓库 中文名称", name = "typeName")
    private String typeName;
    /**
     * 盘点名称
     */
    @ApiModelProperty(value = "盘点名称", name = "checkName")
    private String checkName;

    /**
     * 盘点状态 10:进行中 | 20:取消 | 30:完成
     */
    @ApiModelProperty(value = "盘点状态 10:进行中 | 20:取消 | 30:完成", name = "checkState")
    private String checkState;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;


    @ApiModelProperty(value = "盘点人名称", name = "checkName")
    private String insertUserName;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数", name = "totalCount")
    private Integer totalCount;

    /**
     * 已经盘点数量
     */
    @ApiModelProperty(value = "已经盘点数量", name = "overCount")
    private Integer overCount;

    /**
     * 已经盘点的商品存在的数量
     */
    @ApiModelProperty(value = "已经盘点的商品存在的数量", name = "haveCount")
    private Integer haveCount;

    /**
     * 已经盘点的商品缺失的数量
     */
    @ApiModelProperty(value = "已经盘点的商品缺失的数量", name = "notCount")
    private Integer notCount;

    /**
     * 盘点开始时间
     */
    @ApiModelProperty(value = "盘点开始时间", name = "startTime")
    private Date startTime;

    /**
     * 盘点结束时间
     */
    @ApiModelProperty(value = "盘点结束时间", name = "endTime")
    private String endTime;
}
