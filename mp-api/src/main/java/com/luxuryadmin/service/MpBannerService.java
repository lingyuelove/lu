package com.luxuryadmin.service;


import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;

import java.util.List;

/**
 * banner表 service
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
public interface MpBannerService {


    /**
     * 获取banner信息
     *
     * @return
     */
    VoShopUnionByAppShow listBannerInfo();
}
