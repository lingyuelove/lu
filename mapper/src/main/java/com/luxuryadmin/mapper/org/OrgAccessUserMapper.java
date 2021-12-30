package com.luxuryadmin.mapper.org;


import com.luxuryadmin.common.base.BaseMapper;
import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.param.org.ParamAccessUserSearch;
import com.luxuryadmin.vo.org.VoAccessUserPageByApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Mapper
public interface OrgAccessUserMapper extends BaseMapper {

    /**
     * 集合显示
     * @param organizationId
     * @return
     */
    List<VoAccessUserPageByApp>  getAccessUserByApp(@Param("organizationId")Integer organizationId,@Param("accessType")String accessType);

    /**
     * 机构仓访问用户详情
     * @param phone
     * @param organizationId
     * @param accessType 获取类型 -90 已删除 | 10白名单 | 20黑名单 不传则查询所有 已删除或未删除 传10则查询白名单和黑名单
     * @return
     */
    OrgAccessUser getAccessUser(@Param("phone")String phone, @Param("organizationId")Integer organizationId, @Param("accessType")String accessType);

    /**
     * 机构仓访问用户列表
     * @param phone
     * @return
     */
    List<VoAccessUserPageByApp> getAccessUserList(@Param("phone")String phone,@Param("organizationId")Integer organizationId);
}
