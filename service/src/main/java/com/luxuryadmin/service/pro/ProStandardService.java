package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProStandard;
import com.luxuryadmin.mapper.pro.ProStandardMapper;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductClassifyAdd;
import com.luxuryadmin.vo.pro.VoClassifyTypeSon;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;

import java.util.List;


/**
 *商品规格表 service
 *@author zhangsai
 *@Date 2021-09-16 17:48:52
 */
public interface ProStandardService {

    /**
     * 新增
     * @param productClassifyAdd
     */
    void addProductClassifyTypeList(ParamProductClassifyAdd productClassifyAdd);

    /**
     * 商品补充信息集合显示
     * @param classifyTypeSearch
     * @return
     */
    VoClassifyTypeSon getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch);

    /**
     * 删除
     * @param productId
     */
    void deleteStandard( Integer productId);
}
