package com.luxuryadmin.service.pro.impl;

import java.util.Date;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.enums.pro.EnumDynamicInitialize;
import com.luxuryadmin.enums.pro.EnumProDynamic;
import com.luxuryadmin.mapper.pro.ProDynamicMapper;
import com.luxuryadmin.mapper.pro.ProDynamicProductMapper;
import com.luxuryadmin.param.pro.ParamDynamicDelete;
import com.luxuryadmin.param.pro.ParamDynamicQuery;
import com.luxuryadmin.param.pro.ParamDynamicSave;
import com.luxuryadmin.service.pro.ProDynamicService;
import com.luxuryadmin.vo.pro.VoCountDynamic;
import com.luxuryadmin.vo.pro.VoDynamicAndProductInfoList;
import com.luxuryadmin.vo.pro.VoDynamicList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 商品位置列表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Service
@Transactional
public class ProDynamicServiceImpl implements ProDynamicService {


    /**
     * 注入dao
     */
    @Resource
    private ProDynamicMapper proDynamicMapper;

    @Resource
    private ProDynamicProductMapper proDynamicProductMapper;

    @Resource
    private ServicesUtil servicesUtil;

    /**
     * 自定义
     */
    private final static String CUSTOM_URL = "/default/pro/dynamic/custom.png";


    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    @Override
    public List<VoDynamicList> listDynamic(ParamDynamicQuery param) {
        List<ProDynamic> proDynamics = proDynamicMapper.listDynamic(param);
        List<Integer> dynamicIds = proDynamics.stream().map(ProDynamic::getId).collect(Collectors.toList());
        List<VoDynamicList> voDynamics = new ArrayList<>();
        if (dynamicIds != null && dynamicIds.size() > 0) {
            Map<Integer, VoCountDynamic> dynamicProductMap = new HashMap<>();
            List<VoCountDynamic> voCountDynamicProducts = proDynamicProductMapper.listCountByDynamicIds(dynamicIds);
            if (voCountDynamicProducts != null && voCountDynamicProducts.size() > 0) {
                dynamicProductMap = voCountDynamicProducts.stream().collect(Collectors.toMap(VoCountDynamic::getFkProDynamicId, Function.identity()));
            }
            for (int i = 0; i <proDynamics.size() ; i++) {
                ProDynamic proDynamic = proDynamics.get(i);
                VoDynamicList voDynamicList = new VoDynamicList();
                voDynamicList.setId(proDynamic.getId());
                voDynamicList.setName(proDynamic.getName());
                if (dynamicProductMap.get(proDynamic.getId()) != null) {
                    voDynamicList.setTotalNum(dynamicProductMap.get(proDynamic.getId()).getTotalNum().toString());
                } else {
                    voDynamicList.setTotalNum("0");
                }
                String url = servicesUtil.formatImgUrl(proDynamic.getUrl(), false);
                voDynamicList.setUrl(url);
                voDynamics.add(voDynamicList);
            }
        }
        return voDynamics;
    }

    /**
     * 新增商品位置
     *
     * @param param
     */
    @Override
    public void saveDynamic(ParamDynamicSave param) {
        if (param.getName().length() > 10) {
            throw new MyException("最多十个字符");
        }
        List<ProDynamic> proDynamicInfos = proDynamicMapper.getDynamicInfoByName(param);
        if (proDynamicInfos != null && proDynamicInfos.size() > 0) {
            throw new MyException("不可重复名称");
        }
        ProDynamic proDynamicCount = new ProDynamic();
        proDynamicCount.setFkShpShopId(param.getShopId());
        proDynamicCount.setDel("0");
        Integer count = proDynamicMapper.getCount(param.getShopId());
        if (count != null && count >= 50) {
            throw new MyException("动态最多添加50条");
        }
        ProDynamic proDynamic = new ProDynamic();
        proDynamic.setName(param.getName());
        proDynamic.setIsInitialize(1);
        proDynamic.setFkShpShopId(param.getShopId());
        proDynamic.setInsertTime(new Date());
        proDynamic.setUpdateTime(new Date());
        proDynamic.setInsertAdmin(param.getUserId());
        proDynamic.setUrl(CUSTOM_URL);
        proDynamicMapper.saveObject(proDynamic);
    }

    /**
     * 删除商品位置
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDynamic(ParamDynamicDelete param) {
        String[] idArr = param.getIds().split(",");
        List<String> idsStr = Arrays.asList(idArr);
        List<Integer> ids = idsStr.stream().map(Integer::parseInt).collect(Collectors.toList());
        proDynamicMapper.deleteDynamicByIds(ids);
        proDynamicProductMapper.deleteDynamicProductByDynamicIds(ids);
    }

    /**
     * 根据商品id获取商品位置信息
     *
     * @param proId
     * @param shopId
     * @return
     */
    @Override
    public ProDynamic getDynamicProductInfoByProductId(Integer proId, Integer shopId) {
        return proDynamicMapper.getDynamicInfoByProductId(proId, shopId);
    }

    /**
     * 根据商品id或者动态名称和商品id
     *
     * @param proIds
     * @param shopId
     * @return
     */
    @Override
    public List<VoDynamicAndProductInfoList> listInfoByProId(List<Integer> proIds, Integer shopId) {
        return proDynamicMapper.listInfoByProId(proIds, shopId);
    }

    /**
     * 初始化店铺信息
     *
     * @param shopId
     */
    @Override
    public void saveInitializeDynamic(Integer shopId) {
        List<ProDynamic> proDynamics = new ArrayList<>();
        for (EnumProDynamic eunm : EnumProDynamic.values()) {
            ProDynamic proDynamic = new ProDynamic();
            proDynamic.setName(eunm.getMsg());
            proDynamic.setUrl(eunm.getUrl());
            proDynamic.setIsInitialize(0);
            proDynamic.setSort(eunm.getCode());
            proDynamic.setFkShpShopId(shopId);
            proDynamic.setInsertTime(new Date());
            proDynamic.setUpdateTime(new Date());
            proDynamic.setInsertAdmin(-9);
            proDynamics.add(proDynamic);
        }
        proDynamicMapper.saveBatch(proDynamics);
    }
}
