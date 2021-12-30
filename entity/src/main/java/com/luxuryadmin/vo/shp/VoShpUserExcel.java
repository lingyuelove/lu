package com.luxuryadmin.vo.shp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @Classname VoShpUser
 * @Description TODO
 * @Date 2020/6/22 18:06
 * @Created by Administrator
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoShpUserExcel {

    /**
     * 会员号
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "用户编号", index = 0)
    private Integer number;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称", index = 1)
    @ColumnWidth(20)
    private String nickname;

    /**
     * 手机号;对称加密存储
     */
    @ExcelProperty(value = "手机号", index = 2)
    @ColumnWidth(12)
    private String phone;

    /**
     * 插入时间
     */
    @ExcelProperty(value = "注册时间", index = 3)
    @ColumnWidth(20)
    private Date insertTime;


    /**
     * 邀请人编码
     */
    @ExcelProperty(value = "邀请人编码", index = 4)
    @ColumnWidth(10)
    private String inviteUserNumber;

}
