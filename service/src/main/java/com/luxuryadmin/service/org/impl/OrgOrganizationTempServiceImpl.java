package com.luxuryadmin.service.org.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.org.OrgOrganization;
import com.luxuryadmin.entity.org.OrgOrganizationTemp;
import com.luxuryadmin.mapper.org.OrgOrganizationMapper;
import com.luxuryadmin.mapper.org.OrgOrganizationTempMapper;
import com.luxuryadmin.param.org.ParamOrganizationTempAdd;
import com.luxuryadmin.param.org.ParamOrganizationTempSearch;
import com.luxuryadmin.param.org.ParamOrganizationTempUpdate;
import com.luxuryadmin.service.org.OrgOrganizationTempService;
import com.luxuryadmin.vo.org.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 机构临时仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:57:27
 */
@Slf4j
@Service
public class OrgOrganizationTempServiceImpl implements OrgOrganizationTempService {
    @Resource
    private OrgOrganizationMapper organizationMapper;
    @Resource
    private OrgOrganizationTempMapper organizationTempMapper;
    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected ServicesUtil servicesUtil;
    @Override
    public Integer addOrganizationTemp(ParamOrganizationTempAdd organizationTempAdd) {
        String organizationTempKey = ConstantRedisKey.getCreateProTempKey(organizationTempAdd.getShopId(), organizationTempAdd.getOrganizationId());
        servicesUtil.validVid(organizationTempKey, organizationTempAdd.getVid());
        OrgOrganizationTemp organizationTemp = new OrgOrganizationTemp();
        BeanUtils.copyProperties(organizationTempAdd, organizationTemp);
        organizationTemp.setInsertTime(new Date());
        organizationTemp.setFkProTempId(organizationTempAdd.getTempId());
        organizationTemp.setFkShpShopId(organizationTempAdd.getShopId());
        organizationTemp.setFkOrgOrganizationId(organizationTempAdd.getOrganizationId());
        Integer result = organizationTempMapper.saveObject(organizationTemp);
        redisUtil.delete(organizationTempKey);
        return organizationTemp.getId();
    }

    @Override
    public Integer updateOrganizationTemp(ParamOrganizationTempUpdate organizationTempUpdate) {
        OrgOrganizationTemp organizationTemp = new OrgOrganizationTemp();
        BeanUtils.copyProperties(organizationTempUpdate, organizationTemp);
        return organizationTempMapper.updateObject(organizationTemp);
    }

    @Override
    public Integer delete(Integer id) {

        return organizationTempMapper.deleteObject(id);
    }

    @Override
    public VoOrganizationTempByApp getOrganizationTempPageByApp(ParamOrganizationTempSearch organizationTempSearch) {
        OrgOrganization organization = (OrgOrganization)organizationMapper.getObjectById(organizationTempSearch.getOrganizationId());
        if (organization == null){
            throw new MyException("此云仓不存在");
        }
        PageHelper.startPage(Integer.parseInt(organizationTempSearch.getPageNum()), organizationTempSearch.getPageSize());
        List<VoOrganizationTempPageByApp> organizationTempPageByApps = organizationTempMapper.getOrganizationTempPageByApp(organizationTempSearch.getOrganizationId());
        if (organizationTempPageByApps != null && organizationTempPageByApps.size() >0){
            organizationTempPageByApps.forEach(voOrganizationTempPageByApp -> {
                Integer productCount = organizationTempMapper.getProductCount(voOrganizationTempPageByApp.getOrganizationId(),voOrganizationTempPageByApp.getTempId());
                if (productCount == null){
                    voOrganizationTempPageByApp.setProductCount(0);
                }else {
                    voOrganizationTempPageByApp.setProductCount(productCount);
                }
            });
        }
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(organizationTempPageByApps);
        VoOrganizationTempByApp voOrganizationTempByApp = new VoOrganizationTempByApp();
        voOrganizationTempByApp.setPageNum(pageInfo.getPageNum());
        voOrganizationTempByApp.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voOrganizationTempByApp.setHasNextPage(true);
        } else {
            voOrganizationTempByApp.setHasNextPage(false);
        }
        voOrganizationTempByApp.setList(organizationTempPageByApps);
        if (organization.getStartTime() != null){
            voOrganizationTempByApp.setStartTime(DateUtil.formatShort(organization.getStartTime()));
        }
        if (organization.getEndTime() != null){
            voOrganizationTempByApp.setEndTime(DateUtil.formatShort(organization.getEndTime()));
        }

        String vid = LocalUtils.getUUID();
        String organizationTempKey = ConstantRedisKey.getCreateProTempKey(organizationTempSearch.getShopId(), organizationTempSearch.getOrganizationId());
        redisUtil.setExMINUTES(organizationTempKey, vid, 120);
        voOrganizationTempByApp.setVid(vid);
        return voOrganizationTempByApp;
    }

    @Override
    public VoOrganizationTempByApplets getOrganizationTempPageByApplets(ParamOrganizationTempSearch organizationTempSearch) {
        OrgOrganization organization = (OrgOrganization)organizationMapper.getObjectById(organizationTempSearch.getOrganizationId());
        if (organization == null){
            throw new MyException("此云仓不存在");
        }
        if (organization.getStartTime() != null && organization.getStartTime().after(new Date())){
            throw new MyException("此云仓未开始");
        }
        if (organization.getEndTime() != null && organization.getEndTime().before(new Date())){
            throw new MyException("此云仓已过期");
        }
        PageHelper.startPage(Integer.parseInt(organizationTempSearch.getPageNum()), organizationTempSearch.getPageSize());
        List<VoOrganizationTempPageByApplets> organizationTempPageByApplets = organizationTempMapper.getOrganizationTempPageByApplets(organizationTempSearch.getOrganizationId(), organizationTempSearch.getSearchName());
        if (organizationTempPageByApplets != null && organizationTempPageByApplets.size() >0){
            organizationTempPageByApplets.forEach(organizationTempPageByApplet -> {
                Integer productCount = organizationTempMapper.getProductCount(organizationTempPageByApplet.getOrganizationId(),organizationTempPageByApplet.getTempId());
                if (productCount == null){
                    organizationTempPageByApplet.setProductCount(0);
                }else {
                    organizationTempPageByApplet.setProductCount(productCount);
                }
            });
        }
        PageInfo<VoOrganizationTempPageByApplets> pageInfo = new PageInfo(organizationTempPageByApplets);
        VoOrganizationTempByApplets voOrganizationTempByApplets = new VoOrganizationTempByApplets();
        voOrganizationTempByApplets.setList(organizationTempPageByApplets);
        voOrganizationTempByApplets.setPageNum(pageInfo.getPageNum());
        voOrganizationTempByApplets.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voOrganizationTempByApplets.setHasNextPage(true);
        } else {
            voOrganizationTempByApplets.setHasNextPage(false);
        }
        voOrganizationTempByApplets.setOrgName(organization.getName());
        voOrganizationTempByApplets.setTotal(pageInfo.getTotal());
        return voOrganizationTempByApplets;
    }

    @Override
    public OrgOrganizationTemp getByTempId(Integer tempId,Integer organizationId) {
        return organizationTempMapper.getByTempId(tempId,organizationId);
    }
}