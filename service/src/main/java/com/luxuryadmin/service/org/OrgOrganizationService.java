package com.luxuryadmin.service.org;

import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.param.org.*;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;

import java.util.List;

/**
 * 机构仓
 *
 * @author zhangSai
 * @date 2021/04/21 13:56:16
 */
public interface OrgOrganizationService {
    /**
     * 新增
     *
     * @param organizationAdd
     * @return
     */
    Integer addOrganization(ParamOrganizationAdd organizationAdd);

    /**
     * 修改
     *
     * @param organizationUpdate
     * @return
     */
    Integer updateOrganization(ParamOrganizationUpdate organizationUpdate);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Integer id);

    /**
     * app端集合显示机构仓
     *
     * @return
     */
    VoOrganizationByApp getOrganizationPageByApp(ParamOrganizationSearch organizationSearch);

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    OrgOrganization getOrganization(Integer id);

    /**
     * 根据id更新实体
     *
     * @param organization
     * @return
     */
    Integer updateById(OrgOrganization organization);

}