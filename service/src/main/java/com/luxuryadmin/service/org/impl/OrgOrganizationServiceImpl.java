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
import com.luxuryadmin.mapper.org.OrgOrganizationMapper;
import com.luxuryadmin.mapper.org.OrgOrganizationTempMapper;
import com.luxuryadmin.param.org.ParamOrganizationAdd;
import com.luxuryadmin.param.org.ParamOrganizationSearch;
import com.luxuryadmin.param.org.ParamOrganizationUpdate;
import com.luxuryadmin.service.org.OrgOrganizationService;
import com.luxuryadmin.vo.org.VoOrganizationByApp;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 机构仓
 *
 * @author zhangSai
 * @date   2021/04/21 13:56:16
 */
@Slf4j
@Service
public class OrgOrganizationServiceImpl implements OrgOrganizationService {

    @Resource
    private OrgOrganizationMapper organizationMapper;

    @Resource
    private OrgOrganizationTempMapper organizationTempMapper;

    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected ServicesUtil servicesUtil;

    @Override
    public Integer addOrganization(ParamOrganizationAdd organizationAdd) {
        String proTempKey = ConstantRedisKey.getCreateProTempKey(organizationAdd.getShopId(), organizationAdd.getInsertAdmin());
        servicesUtil.validVid(proTempKey, organizationAdd.getVid());
        //查询是否有相同的机构临时仓
        OrgOrganization oldOrganization =organizationMapper.getByName(organizationAdd.getShopId(),organizationAdd.getName());
        if (oldOrganization != null){
            throw new MyException("此机构临时仓已添加");
        }
        OrgOrganization organization = new OrgOrganization();
        BeanUtils.copyProperties(organizationAdd, organization);
        organization.setFkShpShopId(organizationAdd.getShopId());
        organization.setInsertTime(new Date());
        try {
            if (organizationAdd.getStartTime() != null){
                organization.setStartTime(DateUtil.parseDefaultSt(organizationAdd.getStartTime()));
            }
            if (organizationAdd.getEndTime() != null){
                organization.setEndTime(DateUtil.parseDefaultEt(organizationAdd.getEndTime()));
            }

        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        Integer result = organizationMapper.saveObject(organization);
        redisUtil.delete(proTempKey);
        return result;
    }

    @Override
    public Integer updateOrganization(ParamOrganizationUpdate organizationUpdate) {
        //查询是否有相同的机构临时仓
        OrgOrganization oldOrganization =organizationMapper.getByName(organizationUpdate.getShopId(),organizationUpdate.getName());
        if (oldOrganization != null && !oldOrganization.getId().equals(organizationUpdate.getId())){
            throw new MyException("此机构临时仓名称已存在");
        }

        OrgOrganization organization = new OrgOrganization();
        BeanUtils.copyProperties(organizationUpdate, organization);
        organization.setUpdateTime(new Date());
        try {
            if (organizationUpdate.getStartTime() != null){
                organization.setStartTime(DateUtil.parseDefaultSt(organizationUpdate.getStartTime()));
            }
            if (organizationUpdate.getEndTime() != null){
                organization.setEndTime(DateUtil.parseDefaultEt(organizationUpdate.getEndTime()));
            }

        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return organizationMapper.updateObject(organization);
    }

    @Override
    public Integer delete(Integer id) {

        organizationTempMapper.deleteByOrganizationId(id);
        return organizationMapper.deleteObject(id);
    }

    @Override
    public VoOrganizationByApp getOrganizationPageByApp(ParamOrganizationSearch organizationSearch) {

        PageHelper.startPage(organizationSearch.getPageNum(), organizationSearch.getPageSize());
        List<VoOrganizationPageByApp> organizationPageByApps = organizationMapper.getOrganizationPageByApp(organizationSearch.getShopId());
        if (organizationPageByApps != null && organizationPageByApps.size()>0){
            organizationPageByApps.forEach(organizationPageByApp ->{
                Integer productCount =organizationTempMapper.getProductCount(organizationPageByApp.getId(),null);
                if (productCount == null){
                    organizationPageByApp.setProductCount(0);
                }else {
                    organizationPageByApp.setProductCount(productCount);
                }
                Integer shopCount = organizationTempMapper.getShopCount(organizationPageByApp.getId(),null);
                if (shopCount == null){
                    organizationPageByApp.setShopCount(0);
                }else {
                    organizationPageByApp.setShopCount(shopCount);
                }
                BigDecimal totalPrice = organizationTempMapper.getProductPrice(organizationPageByApp.getId(),null);
                if (totalPrice == null){
                    organizationPageByApp.setTotalPrice(new BigDecimal(0));
                }else {
                    organizationPageByApp.setTotalPrice(totalPrice);
                }
            });
        }
        PageInfo<VoOrganizationPageByApp> pageInfo = new PageInfo(organizationPageByApps);
        VoOrganizationByApp voOrganizationByApp = new VoOrganizationByApp();
        voOrganizationByApp.setPageNum(pageInfo.getPageNum());
        voOrganizationByApp.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voOrganizationByApp.setHasNextPage(true);
        } else {
            voOrganizationByApp.setHasNextPage(false);
        }
        voOrganizationByApp.setList(organizationPageByApps);
        String vid = LocalUtils.getUUID();
        String proTempKey = ConstantRedisKey.getCreateProTempKey(organizationSearch.getShopId(), organizationSearch.getUserId());
        redisUtil.setExMINUTES(proTempKey, vid, 120);
        voOrganizationByApp.setVid(vid);
        return voOrganizationByApp;
    }

    @Override
    public OrgOrganization getOrganization(Integer id) {
        return (OrgOrganization) organizationMapper.getObjectById(id);
    }

    @Override
    public Integer updateById(OrgOrganization organization) {
        return organizationMapper.updateObject(organization);
    }
}