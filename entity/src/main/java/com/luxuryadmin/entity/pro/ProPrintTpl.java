package com.luxuryadmin.entity.pro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 商品打印模板表
 *
 * @author sanjin145
 * @date   2020/12/23 15:33:56
 */
@Data
public class ProPrintTpl {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 店铺ID
     */
    private Integer fkShpShopId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建人
     */
    private Integer insertAdmin;

    /**
     * 修改人
     */
    private Integer updateAdmin;

    /**
     * 创建时间
     */
    private Date insertTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @ApiModelProperty(name = "state", required = false, value = "0普通模板 1快速模板")
    private String state;

}