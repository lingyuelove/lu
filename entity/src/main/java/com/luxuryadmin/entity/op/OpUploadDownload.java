package com.luxuryadmin.entity.op;

import lombok.Data;

import java.util.Date;

/**
 * 上传下载任务列表
 *
 * @author monkey king
 * @date 2021/09/06 14:50:31
 */
@Data
public class OpUploadDownload {
    /**
     * 主键Id,逻辑id,软件内部关联;不要把此id暴露在前端;
     */
    private Integer id;

    /**
     * -90:删除 | 10:进行中 | 20:已取消； | 21:导出失败 | 30：已完成；
     */
    private Integer state;

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
    private Integer fkShpShopId;

    /**
     * 下载用户；shp_user的id字段,主键id;
     */
    private Integer fkShpUserId;

    /**
     * 下载内容选择得时间范围：开始时间
     */
    private Date startTime;

    /**
     * 下载内容选择得时间范围：结束时间
     */
    private Date endTime;

    /**
     * 下载地址全路径;http开头
     */
    private String url;

    /**
     * 插入时间
     */
    private Date insertTime;

    /**
     * 任务结束时间
     */
    private Date finishTime;

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
     * 版本号;用于更新时对比操作;
     */
    private Integer versions;

    /**
     * 备注
     */
    private String remark;

    /**
     * 导出失败,异常原因
     */
    private String exceptionRemark;

}