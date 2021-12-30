package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductClassifyAdd;
import com.luxuryadmin.param.pro.ParamProductClassifyAddList;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;

import java.util.List;

/**
 *商品补充信息关联表 service
 *@author zhangsai
 *@Date 2021-08-03 10:45:15
 */
public interface ProProductClassifyService{

    /**
     * 新增
     * @param productClassifyAdd
     */
    void addProductClassifyList(ParamProductClassifyAdd productClassifyAdd);

    /**
     * 商品补充信息集合显示
     * @param classifyTypeSearch
     * @return
     */
    List<VoClassifyTypeSonList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch);
}
