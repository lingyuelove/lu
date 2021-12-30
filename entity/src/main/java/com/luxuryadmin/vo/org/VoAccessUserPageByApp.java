package com.luxuryadmin.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Data
public class VoAccessUserPageByApp {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", name = "id")
    private Integer id;

    /**
     * 机构id
     */
    @ApiModelProperty(value = "机构id", name = "id")
    private Integer organizationId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone")
    private String phone;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", name = "insertTime")
    private String insertTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "insertAdmin")
    private Integer insertAdmin;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", name = "insertAdminName")
    private String insertAdminName;

    /**
     * 获取类型 -90 已删除 | 10白名单 | 20黑名单
     */
    private String accessType;
}
