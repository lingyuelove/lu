package com.luxuryadmin.param.op;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 回收归集接收类
 * Mong
 */
@Data
public class ParamOpRecycleQuery {


    private int id;
    @ApiModelProperty(value = "品牌", name = "brandName", required = true)
    @Length(max = 100,message = "品牌名称长度超限!")
    private String brandName;
    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    @Pattern(regexp = "^[1][3456789][0-9]{9}$", message = "手机号格式错误!")
    private String phone;
    @ApiModelProperty(value = "购买价格", name = "buyPrice", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "请输入正确价格!")
    private String buyPrice;


    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 添加用户_管理员id
     */
    private Integer insertAdmin;

    /**
     * 修改用户_管理员id
     */
    private Integer updateAdmin;

    /**
     * 备注
     */
    private String remark;


}
