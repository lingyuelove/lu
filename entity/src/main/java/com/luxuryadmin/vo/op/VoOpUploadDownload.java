package com.luxuryadmin.vo.op;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * @author monkey king
 * @date 2021-09-08 20:13:08
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoOpUploadDownload {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * -90:删除 | 10:进行中 | 20:已取消； | 21:导出失败 | 30：已完成；
     */
    private String state;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务总消耗毫秒数
     */
    private Integer totalMs;

    /**
     * 下载次数
     */
    private Integer downloadTimes;

    /**
     * 任务类型：in：导入，out：导出
     */
    private String type;

    /**
     * 模块；订单模块；商品模块；账单模块，其它模块
     */
    private String module;

    /**
     * 店铺id；shp_shop的id字段,主键id;
     */
    private String shopId;

    /**
     * 下载用户；shp_user的id字段,主键id;
     */
    private String userId;

    /**
     * 下载内容选择得时间范围：开始时间
     */
    private String startTime;

    /**
     * 下载内容选择得时间范围：结束时间
     */
    private String endTime;

    /**
     * 下载地址全路径;http开头
     */
    private String url;

    /**
     * 插入时间
     */
    private String insertTime;

    /**
     * 任务结束时间
     */
    private String finishTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String nickname;

    /**
     * 更新人员
     */
    private String updateNickname;

}
