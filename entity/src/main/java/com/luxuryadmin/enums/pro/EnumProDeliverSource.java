package com.luxuryadmin.enums.pro;

import java.util.Arrays;

/**
 * 发货表中发货订单来源
 *
 * @author taoqimin
 * @date 2020-09-25
 */
public enum EnumProDeliverSource {
    /**
     * 订单
     */
    ORDER("订单"),

    /**
     * 锁单
     */
    LOCK_RECORD("锁单");

    private String description;

    EnumProDeliverSource(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static EnumProDeliverSource getByName(String name) {
        return Arrays.asList(EnumProDeliverSource.values())
                .stream()
                .filter(rechargeOrderTypeEnum -> rechargeOrderTypeEnum.name().equals(name))
                .findFirst()
                .orElse(null);
    }
}
