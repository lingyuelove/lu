package com.luxuryadmin.service.shp;

import com.luxuryadmin.vo.pro.VoProQuality;

import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-27 18:02:36
 */
public interface ProQualityService {

    /**
     * 获取商品成色表
     *
     * @return
     */
    List<VoProQuality> listProQuality();
}
