package com.luxuryadmin.service.biz;

import com.luxuryadmin.entity.biz.BizShopSee;
import com.luxuryadmin.param.biz.ParamShopSeeAdd;
import com.luxuryadmin.param.biz.ParamShopSeeSearch;

/**
 *店铺查看次数表 service
 *@author zhangsai
 *@Date 2021-05-27 11:42:44
 */
public interface BizShopSeeService{

    /**
     * 新增
     * @param shopSeeAdd
     */
    void addShopSee(ParamShopSeeAdd shopSeeAdd);

    /**
     * 根据信息信号寻是否存在此纪录
     * @param shopSeeSearch
     * @return
     */
    BizShopSee getBySearch(ParamShopSeeSearch shopSeeSearch);

    /**
     * 前端新增
     * @param shopSeeAdd
     */
    void saveShopSee(ParamShopSeeAdd shopSeeAdd);
}
