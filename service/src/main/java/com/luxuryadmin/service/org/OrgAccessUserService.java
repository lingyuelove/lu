package com.luxuryadmin.service.org;

import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.param.org.*;
import com.luxuryadmin.vo.org.VoAccessUserByApp;
import com.luxuryadmin.vo.org.VoAccessUserPageByApp;
import com.luxuryadmin.vo.org.VoOrganizationByApp;

import java.util.List;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
public interface OrgAccessUserService {
    /**
     * 新增
     * @param accessUserAdd
     * @return
     */
    Integer addAccessUser(ParamAccessUserAdd accessUserAdd);

    /**
     * 修改
     * @param orgAccessUser
     * @return
     */
    Integer updateAccessUser(OrgAccessUser orgAccessUser);

    /**
     * 删除
     * @param id
     * @return
     */
    Integer deleteAccessUser(Integer id);

    /**
     * 集合显示
     * @param accessUserSearch
     * @return
     */
    VoAccessUserByApp getAccessUserByApp(ParamAccessUserSearch accessUserSearch);

    /**
     * 根据手机号获取详情
     * @param phone
     * @param organizationId
     * @param accessType 获取类型 -90 已删除 | 10白名单 | 20黑名单 不传则查询所有 已删除或未删除 传10则查询白名单和黑名单
     * @return
     */
    OrgAccessUser getAccessUser(String phone, Integer organizationId,String accessType);

    /**
     *
     * @param phone
     * @return
     */
    List<VoAccessUserPageByApp> getAccessUserList(String phone, Integer organizationId);


    /**
     * 根据手机号获取详情
     * @param id
     * @return
     */
    String getAccessUserPhoneById(Integer id);
}