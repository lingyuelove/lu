package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.param.pro.ParamCheckProductAddList;
import com.luxuryadmin.param.pro.ParamCheckListForApiBySearch;
import com.luxuryadmin.param.pro.ParamCheckUpdateForApi;
import com.luxuryadmin.vo.pro.VoCheckListForApi;
import com.luxuryadmin.vo.pro.VoProClassify;

import java.util.List;

/**
 *
 * @author zhangsai
 */
public interface ProCheckService {

    /**
     * 新增盘点
     * @param checkProductAddList
     */
    void addCheck( ParamCheckProductAddList checkProductAddList);

    /**
     * 新增临时仓盘点
     * @param checkProductAddList
     */
    void addCheckForTemp( ParamCheckProductAddList checkProductAddList);

    /**
     * 商户所有盘点集合显示
     * @param checkListForApiBySearch
     * @return
     */
    List<VoCheckListForApi> getCheckListForApi(ParamCheckListForApiBySearch checkListForApiBySearch);

    /**
     * 修改盘点
     * @param checkUpdateForApi
     */
    void updateCheckState(ParamCheckUpdateForApi checkUpdateForApi);

    ProCheck getById(Integer id);

    void deleteCheck(Integer id);


    /**
     * 删除盘点数据(用于注销店铺)
     * @param shopId
     * @return
     */
    void deleteProCheckByShopId(int shopId);

    /**
     * 判断盘点状态
     * @param shopId
     * @return
     */
    void getStateByShopId(Integer shopId);

    List<VoProClassify> getClassifyList(Integer shopId,Integer checkId);
}
