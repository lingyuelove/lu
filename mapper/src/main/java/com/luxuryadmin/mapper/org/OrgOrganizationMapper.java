package com.luxuryadmin.mapper.org;

import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Mapper
public interface OrgOrganizationMapper  extends BaseMapper {
    /**
     * app端集合显示机构仓
     * @param shopId
     * @return
     */
    List<VoOrganizationPageByApp> getOrganizationPageByApp(@Param("shopId")Integer shopId);

    /**
     * 机构仓
     * @param shopId
     * @param name
     * @return
     */
    OrgOrganization getByName(@Param("shopId")Integer shopId,@Param("name")String name);

    OrgOrganization getById(@Param("id")Integer id);
}