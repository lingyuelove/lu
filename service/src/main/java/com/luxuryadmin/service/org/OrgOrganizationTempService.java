package com.luxuryadmin.service.org;

import com.luxuryadmin.entity.org.OrgOrganizationTemp;
import com.luxuryadmin.param.org.ParamOrganizationTempAdd;
import com.luxuryadmin.param.org.ParamOrganizationTempSearch;
import com.luxuryadmin.param.org.ParamOrganizationTempUpdate;
import com.luxuryadmin.vo.org.VoOrganizationTempByApp;
import com.luxuryadmin.vo.org.VoOrganizationTempByApplets;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApplets;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构临时仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:27
 */
public interface OrgOrganizationTempService {

    /**
     * 新增
     * @param organizationTempAdd
     * @return
     */
    Integer addOrganizationTemp(ParamOrganizationTempAdd organizationTempAdd);

    /**
     * 修改
     * @param organizationTempUpdate
     * @return
     */
    Integer updateOrganizationTemp(ParamOrganizationTempUpdate organizationTempUpdate);

    /**
     * 删除
     * @param id
     * @return
     */
    Integer delete(Integer id);

    /**
     * 机构临时仓app端集合显示
     * @param organizationTempSearch
     * @return
     */
    VoOrganizationTempByApp getOrganizationTempPageByApp(ParamOrganizationTempSearch organizationTempSearch);

    /**
     * 机构临时仓小程序显示
     * @param organizationTempSearch
     * @return
     */
    VoOrganizationTempByApplets getOrganizationTempPageByApplets(ParamOrganizationTempSearch organizationTempSearch);

    OrgOrganizationTemp getByTempId(Integer tempId,Integer organizationId);
}