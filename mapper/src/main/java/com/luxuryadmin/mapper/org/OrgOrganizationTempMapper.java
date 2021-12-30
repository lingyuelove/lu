package com.luxuryadmin.mapper.org;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.org.OrgOrganizationTemp;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApplets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 机构临时仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:27
 */
@Mapper
public interface OrgOrganizationTempMapper  extends BaseMapper {

    void deleteByOrganizationId(@Param("organizationId")Integer organizationId);


    /**
     * 机构临时仓app端集合显示
     * @param organizationId
     * @return
     */
    List<VoOrganizationTempPageByApp> getOrganizationTempPageByApp(@Param("organizationId")Integer organizationId);

    /**
     * 机构临时仓小程序显示
     * @param organizationId
     * @return
     */
    List<VoOrganizationTempPageByApplets> getOrganizationTempPageByApplets(@Param("organizationId")Integer organizationId,@Param("searchName")String searchName);

    Integer getProductCount(@Param("organizationId")Integer organizationId,@Param("tempId")Integer tempId);


    Integer getShopCount(@Param("organizationId")Integer organizationId,@Param("shopId")Integer shopId);


    BigDecimal getProductPrice(@Param("organizationId")Integer organizationId,@Param("shopId")Integer shopId);

    OrgOrganizationTemp getByTempId(@Param("tempId")Integer tempId,@Param("organizationId")Integer organizationId);
}