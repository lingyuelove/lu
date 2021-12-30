package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 临时仓VO层
 *
 * @author monkey king
 * @date 2021-01-17 14:10:42
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoProTemp {

    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private String tempId;

    /**
     * 名称
     */
    private String name;

    /**
     * 插入时间
     */
    private String insertTime;

    /**
     * 添加用户_管理员id
     */
    private String insertUser;

    /**
     * 商品总数
     */
    private String proTotalNum;

    /**
     * 售罄商品(数量)
     */
    private String saleNum;


    /**
     * 默认价格类型
     */
    private String defaultRequest;

}
