package com.luxuryadmin.mapper;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.MpBanner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * banner表 dao
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:01
 */
@Mapper
public interface MpBannerMapper extends BaseMapper<MpBanner> {


    /**
     * 获取banner信息
     *
     * @return
     */
    List<MpBanner> listBannerInfo();
}
