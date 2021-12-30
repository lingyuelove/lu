package com.luxuryadmin.service.pro;


import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.pro.ParamTempMovePro;
import com.luxuryadmin.param.pro.ParamTempProductMoveQuery;
import com.luxuryadmin.vo.pro.VoMoveProductLoad;
import com.luxuryadmin.vo.pro.VoProduct;
import com.luxuryadmin.vo.pro.VoProductLoad;

import java.util.List;

/**
 *临时仓商品移动历史表 service
 *@author zhangsai
 *@Date 2021-09-24 17:46:58
 */
public interface ProTempProductMoveService{

    /**
     * 新增移仓
     * @param paramTempMovePro
     * @return
     */
    String moveProductToTemp(ParamTempMovePro paramTempMovePro);

    /**
     * 临时仓转入转出记录
     * @param paramTempProductMoveQuery
     * @return
     */
    List<VoMoveProductLoad> listMoveProductToTemp(ParamTempProductMoveQuery paramTempProductMoveQuery);
}
