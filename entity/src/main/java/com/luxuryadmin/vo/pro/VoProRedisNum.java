package com.luxuryadmin.vo.pro;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.models.auth.In;

/**
 * 商品数量详细;
 * 总数量(可卖数量,锁单数量)
 *
 * @author monkey king
 * @date 2020-06-03 21:14:13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoProRedisNum {

    /**
     * 总库存
     */
    private Integer totalNum;

    /**
     * 可用库存
     */
    private Integer leftNum;

    /**
     * 锁单数量
     */
    private Integer lockNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(Integer leftNum) {
        this.leftNum = leftNum;
    }

    public Integer getLockNum() {
        return lockNum;
    }

    public void setLockNum(Integer lockNum) {
        this.lockNum = lockNum;
    }
}
