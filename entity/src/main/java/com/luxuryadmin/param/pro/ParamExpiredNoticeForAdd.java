package com.luxuryadmin.param.pro;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 商品过期提醒新增类
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:59
 */
@Data
@ApiModel(value="商品过期提醒新增类", description="商品过期提醒新增类")
public class ParamExpiredNoticeForAdd extends ParamExpiredNoticeAdd{

    /**
     * shp_shop店铺id
     */
    private Integer shopId;

    /**
     * 创建的用户
     */
    private Integer userId;
}
