package com.luxuryadmin.param.op;

import com.luxuryadmin.param.common.ParamToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 任务下载中心
 *
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParamUploadDownload extends ParamToken {

    /**
     * id
     */
    @ApiModelProperty(value = "点击下载时需要传的参数id", name = "id")
    @Pattern(regexp = "^[0-9]+$", message = "[id]格式错误")
    private String id;

    /**
     * 开始时间
     */
    private String stTime;

    /**
     * 结束时间
     */
    private Date etTime;


}
