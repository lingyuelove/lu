package com.luxuryadmin.service.ord;


import com.luxuryadmin.entity.ord.OrdPrintTpl;
import com.luxuryadmin.param.ord.ParamPrintTplUpload;

import java.util.List;

/**
 *订单打印模板表 service
 *@author zhangsai
 *@Date 2021-09-26 16:00:53
 */
public interface OrdPrintTplService{

    /**
     * 订单打印模板--新增/编辑
     * @param paramPrintTplUpload
     */
    void saveOrUpPrintTpl(ParamPrintTplUpload paramPrintTplUpload);

    /**
     * 订单打印模板
     * @param shopId
     * @return
     */
    OrdPrintTpl getTplByShopId(Integer shopId);

    /**
     * 订单打印模板 系统加本店
     * @param shopId
     * @return
     */
    List<OrdPrintTpl> listTplByShopId(Integer shopId);

}
