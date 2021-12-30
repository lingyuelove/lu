package com.luxuryadmin.mapper.pro;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.vo.pro.VoProTemp;
import com.luxuryadmin.vo.pro.VoTempForOrg;
import com.luxuryadmin.vo.pro.VoTempForPro;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
@Mapper
public interface ProTempMapper extends BaseMapper {

    /**
     * 获取店铺临时仓
     *
     * @param shopId
     * @return
     */
    List<VoProTemp> listVoProTemp(int shopId);

    /**
     * 删除临时仓(不做物理删除,只标记状态)<br/>
     * 状态等于-99
     *
     * @param shopId
     * @param proTempId proTemp.id
     * @return
     */
    int deleteProTempByShopId(@Param("shopId") int shopId, @Param("proTempId") int proTempId);

    /**
     * 获取临时仓的商品总数
     *
     * @param shopId
     * @param tempIds
     * @return
     */
    List<VoProTemp> countTempProductTotalNum(
            @Param("shopId") int shopId, @Param("tempIds") String tempIds);

    /**
     * 获取临时仓的售罄商品总数
     *
     * @param shopId
     * @param tempIds
     * @return
     */
    List<VoProTemp> countTempProductSaleOutNum(
            @Param("shopId") int shopId, @Param("tempIds") String tempIds);

    /**
     * 获取店铺临时仓详情
     *
     * @param id
     * @return
     */
    VoTempForOrg getVoTempForOrg(@Param("id") Integer id);

    /**
     * 获取商品所在临时仓的名称
     *
     * @param shopId
     * @param proId
     * @return
     */
    String getTempName(@Param("shopId") int shopId, @Param("proId") int proId);

    /**
     * 获取商品所在的所有临时仓
     * @param shopId
     * @param proId
     * @return
     */
    List<VoTempForPro> getTempForPro(@Param("shopId")Integer shopId, @Param("proId")Integer proId);
}