package com.luxuryadmin.service.pro;

import com.luxuryadmin.param.pro.ParamClassifyTypeShopAdd;

/**
 *单个店铺补充信息不适用关联表 service
 *@author zhangsai
 *@Date 2021-08-05 14:29:05
 */
public interface ProClassifyTypeShopService {
    /**
     * 新增单个店铺补充信息不适用关联表
     * @param classifyTypeShopAdd
     */
    void addRemoveClassifyType(ParamClassifyTypeShopAdd classifyTypeShopAdd);
}
