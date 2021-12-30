package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luxuryadmin.vo.mongo.UserBindShopCensus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @PackgeName: com.luxuryadmin.vo.op
 * @ClassName: VoOpEmployeeCensus
 * @Author: ZhangSai
 * Date: 2021/11/11 20:53
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpEmployeeCensus extends UserBindShopCensus {
    /**
     * 主键Id,逻辑id,软件内部关联
     */
    private Integer id;




}
