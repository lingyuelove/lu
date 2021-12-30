package com.luxuryadmin.service.biz;

/**
 * @author monkey king
 * @date 2020-01-12 18:12:01
 */
public interface BizLogService {

    /**
     * 添加商务模块操作日志
     *
     * @param shopId 店铺id
     * @param userId 操作人id
     * @param type   模块,leaguer,agency
     * @param remake 记录详情
     * @return
     */
    int saveBizLog(int shopId, int userId, String type, String remake);

    /**
     * 删除已删除店铺的友商日志
     * @param shopId
     */
    void deleteBizLogByShop(int shopId);
}
