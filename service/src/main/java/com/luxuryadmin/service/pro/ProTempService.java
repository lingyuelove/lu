package com.luxuryadmin.service.pro;

import com.luxuryadmin.entity.pro.ProTemp;
import com.luxuryadmin.vo.pro.VoProTemp;
import com.luxuryadmin.vo.pro.VoTempForOrg;
import com.luxuryadmin.vo.pro.VoTempForPro;

import java.util.List;

/**
 * 临时仓
 *
 * @author monkey king
 * @date 2021-01-17 14:04:11
 */
public interface ProTempService {

    /**
     * 获取店铺临时仓
     *
     * @param shopId
     * @return
     */
    List<VoProTemp> listVoProTemp(int shopId);

    /**
     * 创建临时仓库
     *
     * @param proTemp
     * @return
     */
    void createProTemp(ProTemp proTemp);

    /**
     * 修改临时仓
     *
     * @param proTemp
     */
    void updateProTemp(ProTemp proTemp);

    /**
     * 删除临时仓<br/>
     * 删除临时仓时,要把关联该临时仓的所有商品都删除掉
     *
     * @param shopId
     * @param proTempId
     */
    void deleteProTemp(int shopId, int proTempId);


    /**
     * 获取临时仓的商品总数
     *
     * @param shopId
     * @param tempIdArray
     * @return
     */
    List<VoProTemp> countTempProductTotalNum(int shopId, Object[] tempIdArray);

    /**
     * 获取临时仓的售罄商品总数
     *
     * @param shopId
     * @param tempIdArray
     * @return
     */
    List<VoProTemp> countTempProductSaleOutNum(int shopId, Object[] tempIdArray);

    /**
     * 获取店铺临时仓详情
     *
     * @param id
     * @return
     */
    VoTempForOrg getVoTempForOrg(Integer id, Integer shopId);


    /**
     * 获取商品所在临时仓的名称
     *
     * @param shopId
     * @param proId
     * @return
     */
    String getTempName(int shopId, int proId);

    /**
     * 获取商品所在的所有临时仓
     * @param shopId
     * @param proId
     * @return
     */
    List<VoTempForPro> getTempForPro(Integer shopId, Integer proId);

    ProTemp getTempById(Integer id);
}
