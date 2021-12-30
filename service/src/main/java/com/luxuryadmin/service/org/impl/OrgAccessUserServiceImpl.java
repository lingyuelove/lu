package com.luxuryadmin.service.org.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.org.OrgAccessUser;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.mapper.org.OrgAccessUserMapper;
import com.luxuryadmin.mapper.org.OrgOrganizationMapper;
import com.luxuryadmin.param.org.ParamAccessUserAdd;
import com.luxuryadmin.param.org.ParamAccessUserSearch;
import com.luxuryadmin.param.org.ParamAccessUserUpdate;
import com.luxuryadmin.service.org.OrgAccessUserService;
import com.luxuryadmin.vo.org.VoAccessUserByApp;
import com.luxuryadmin.vo.org.VoAccessUserPageByApp;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 机构仓访问用户列表
 *
 * @author zhangSai
 * @date 2021/04/21 13:56:16
 */
@Slf4j
@Service
public class OrgAccessUserServiceImpl implements OrgAccessUserService {

    @Resource
    private OrgAccessUserMapper accessUserMapper;
    @Resource
    private OrgOrganizationMapper organizationMapper;

    @Override
    public Integer addAccessUser(ParamAccessUserAdd accessUserAdd) {
        OrgAccessUser orgAccessUser =  getAccessUser(accessUserAdd.getPhone(), accessUserAdd.getOrganizationId(),null);
        if (orgAccessUser != null){
            OrgAccessUser accessUser = new OrgAccessUser();
            BeanUtils.copyProperties(accessUserAdd, accessUser);
            accessUser.setFkOrgOrganizationId(accessUserAdd.getOrganizationId());
            accessUser.setUpdateAdmin(accessUserAdd.getUserId());
            accessUser.setUpdateTime(new Date());
            accessUser.setAccessType(accessUserAdd.getAccessType());
            accessUser.setFkShpShopId(accessUserAdd.getShopId());
            accessUser.setId(accessUser.getId());
            Integer result = accessUserMapper.updateObject(accessUser);
            return orgAccessUser.getId();
        }else {
            OrgAccessUser accessUser = new OrgAccessUser();
            BeanUtils.copyProperties(accessUserAdd, accessUser);
            accessUser.setFkOrgOrganizationId(accessUserAdd.getOrganizationId());
            accessUser.setInsertAdmin(accessUserAdd.getUserId());
            accessUser.setInsertTime(new Date());
            accessUser.setFkShpShopId(accessUserAdd.getShopId());
            Integer result = accessUserMapper.saveObject(accessUser);
            return accessUser.getId();
        }


    }

    @Override
    public Integer updateAccessUser(OrgAccessUser orgAccessUser) {
        return accessUserMapper.updateObject(orgAccessUser);
    }

    @Override
    public Integer deleteAccessUser(Integer id) {
        OrgAccessUser accessUser = new OrgAccessUser();
        accessUser.setId(id);
        accessUser.setAccessType("-99");
        Integer result = accessUserMapper.updateObject(accessUser);
        return result;
    }

    @Override
    public VoAccessUserByApp getAccessUserByApp(ParamAccessUserSearch accessUserSearch) {
        PageHelper.startPage(accessUserSearch.getPageNum(), accessUserSearch.getPageSize());
        List<VoAccessUserPageByApp> accessUserPageByApps = accessUserMapper.getAccessUserByApp(accessUserSearch.getOrganizationId(), accessUserSearch.getAccessType());
        OrgOrganization organization = (OrgOrganization) organizationMapper.getObjectById(accessUserSearch.getOrganizationId());
        PageInfo<VoAccessUserPageByApp> pageInfo = new PageInfo(accessUserPageByApps);
        VoAccessUserByApp accessUserByApp = new VoAccessUserByApp();
        accessUserByApp.setPageNum(pageInfo.getPageNum());
        accessUserByApp.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            accessUserByApp.setHasNextPage(true);
        } else {
            accessUserByApp.setHasNextPage(false);
        }
        accessUserByApp.setList(accessUserPageByApps);
        accessUserByApp.setOrganizationState(organization.getState());
        accessUserByApp.setOrganizationId(accessUserSearch.getOrganizationId());
        return accessUserByApp;
    }

    @Override
    public OrgAccessUser getAccessUser(String phone, Integer organizationId,String accessType) {
        return accessUserMapper.getAccessUser(phone,organizationId,accessType);
    }

    @Override
    public List<VoAccessUserPageByApp> getAccessUserList(String phone, Integer organizationId) {
        return accessUserMapper.getAccessUserList(phone, organizationId);
    }

    @Override
    public String getAccessUserPhoneById(Integer id) {
        OrgAccessUser accessUser = (OrgAccessUser) accessUserMapper.getObjectById(id);
        if (!LocalUtils.isEmptyAndNull(accessUser)) {
           return accessUser.getPhone();
        }
        return "";
    }
}