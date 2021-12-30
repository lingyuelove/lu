package com.luxuryadmin.service;


import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.vo.award.VOAwardDay;

/**
 * 追加时长表 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:00
 */
public interface MpAddVipTimeService {


    /**
     * 获取奖励记录o
     *
     * @param param
     * @return
     */
    VOAwardDay getAwardRecord(ParamBasic param);
}
