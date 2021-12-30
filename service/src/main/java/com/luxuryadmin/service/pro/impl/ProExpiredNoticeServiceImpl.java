package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProExpiredNotice;
import com.luxuryadmin.mapper.pro.ProCheckMapper;
import com.luxuryadmin.mapper.pro.ProCheckProductMapper;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.mapper.pro.ProExpiredNoticeMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProExpiredNoticeService;
import com.luxuryadmin.vo.pro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date 2021/05/11 10:45:48
 */
@Slf4j
@Service
public class ProExpiredNoticeServiceImpl implements ProExpiredNoticeService {
    @Resource
    private ProExpiredNoticeMapper expiredNoticeMapper;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private OpPushService opPushService;

    @Resource
    private ProClassifyMapper classifyMapper;

    @Autowired
    protected ServicesUtil servicesUtil;

    @Override
    public Integer addExpiredNotice(ParamExpiredNoticeForAdd expiredNoticeForAdd) {
        List<VoExpiredNoticeByList> expiredNoticeByLists = expiredNoticeMapper.getExpiredListByShopId(expiredNoticeForAdd.getShopId());
        if (expiredNoticeByLists != null && expiredNoticeByLists.size() > 11) {
            throw new MyException("最多可添加10条提醒记录");
        }

        ProExpiredNotice expiredNotice = new ProExpiredNotice();
        expiredNotice.setFkShpShopId(expiredNoticeForAdd.getShopId());
        expiredNotice.setInsertAdmin(expiredNoticeForAdd.getUserId());
        expiredNotice.setInsertTime(new Date());
        expiredNotice.setFkProAttributeCodes(expiredNoticeForAdd.getAttributeCodes());
        expiredNotice.setFkProClassifyCodes(expiredNoticeForAdd.getClassifyCodes());
        expiredNotice.setExpiredDay(expiredNoticeForAdd.getExpiredDay());
        ProExpiredNotice expiredNoticeOld = expiredNoticeMapper.getByExpiredNotice(expiredNotice);
        if (expiredNoticeOld != null) {
            throw new MyException("此商品通知已存在");
        }
        if (expiredNoticeForAdd.getClassifyCodes() == null) {
            expiredNotice.setClassifyName("全部");
        } else {
            List<String> classifyCodes = Arrays.asList(expiredNoticeForAdd.getClassifyCodes().split(","));
            List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(expiredNoticeForAdd.getShopId(), "1");
            if (classifyCodes.size() == voProClassifyList.size()) {
                expiredNotice.setClassifyName("全部");
            } else {
                List<String> classifyCodeList = new ArrayList<>();
                classifyCodes.forEach(classify -> {
                    VoProClassify voProClassify = classifyMapper.getProClassifyByType(expiredNoticeForAdd.getShopId(), "1", classify);
                    if (!LocalUtils.isEmptyAndNull(voProClassify)){
                        classifyCodeList.add(voProClassify.getName());
                    }

                });
                String classifyName = StringUtils.join(classifyCodeList, "/");
                expiredNotice.setClassifyName(classifyName);
            }
        }

        List<String> idList = Arrays.asList(expiredNoticeForAdd.getAttributeCodes().split(","));
        List<String> fkProAttributeCodes = new ArrayList<>();
        idList.forEach(id -> {
            if ("10".equals(id)) {
                fkProAttributeCodes.add("自有商品");
            }
            if ("20".equals(id)) {
                fkProAttributeCodes.add("寄卖商品");
            }
            if ("40".equals(id)) {
                fkProAttributeCodes.add("其他商品");
            }

        });
        String fkProAttributeCodeName = StringUtils.join(fkProAttributeCodes, " ");
        expiredNotice.setAttributeName(fkProAttributeCodeName);
        expiredNoticeMapper.saveObject(expiredNotice);
        return expiredNotice.getId();
    }

    @Override
    public Integer deleteExpiredNotice(Integer expiredNoticeId) {
        return expiredNoticeMapper.deleteObject(expiredNoticeId);
    }


    @Override
    public ProExpiredNotice getExpiredNoticeById(Integer expiredNoticeId) {
        ProExpiredNotice expiredNotice = (ProExpiredNotice) expiredNoticeMapper.getObjectById(expiredNoticeId);
        return expiredNotice;
    }

    @Override
    public VoExpiredNoticeByPage getExpiredNoticeByPage(Integer shopId) {

        List<VoExpiredNoticeByList> expiredNoticeByLists = expiredNoticeMapper.getExpiredListByShopId(shopId);
        VoExpiredNoticeByPage voExpiredNoticeByPage = new VoExpiredNoticeByPage();
        if (expiredNoticeByLists == null && expiredNoticeByLists.size() <= 0) {
            return voExpiredNoticeByPage;
        }
        expiredNoticeByLists.forEach(expiredNotice -> {

        });
        voExpiredNoticeByPage.setExpiredNoticeByLists(expiredNoticeByLists);
        voExpiredNoticeByPage.setTotalCount(expiredNoticeByLists.size());
        return voExpiredNoticeByPage;
    }

    @Override
    public List<VoProClassify> getProClassifyList(Integer expiredNoticeId, Integer shopId) {
        ProExpiredNotice expiredNotice = (ProExpiredNotice) expiredNoticeMapper.getObjectById(expiredNoticeId);
        if (expiredNotice == null) {
            throw new MyException("此提醒不存在");
        }

        if (expiredNotice.getFkProClassifyCodes() != null) {
            List<VoProClassify> classifyCodeList = new ArrayList<>();
            //商品种类集合显示
            List<String> classifyCodes = Arrays.asList(expiredNotice.getFkProClassifyCodes().split(","));

            classifyCodes.forEach(classify -> {
                VoProClassify voProClassify = classifyMapper.getProClassifyByType(shopId, "1", classify);
                if (voProClassify != null){
                    classifyCodeList.add(voProClassify);
                }

            });
            return classifyCodeList;
        } else {
            List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
            return voProClassifyList;
        }

    }

    @Override
    public VoExpiredProductByPage getExpiredProductByPage(ParamExpiredProductSearch expiredProductSearch) {
        VoExpiredProductByPage expiredProductByPage = new VoExpiredProductByPage();
        ProExpiredNotice expiredNotice = (ProExpiredNotice) expiredNoticeMapper.getObjectById(expiredProductSearch.getExpiredNoticeId());
        if (expiredNotice == null) {
            return expiredProductByPage;
        }

        //过期商品集合显示
        ParamExpiredProductForMapperSearch expiredProductForMapperSearch = new ParamExpiredProductForMapperSearch();
        BeanUtils.copyProperties(expiredProductSearch, expiredProductForMapperSearch);
        //商品分类集合
        if (expiredNotice.getFkProClassifyCodes() != null) {
            List<String> classifyCodes = Arrays.asList(expiredNotice.getFkProClassifyCodes().split(","));
            expiredProductForMapperSearch.setClassifyCodeList(classifyCodes);
        }
        //商品属性集合
        if (expiredNotice.getFkProAttributeCodes() != null) {
            List<String> attributeCodes = Arrays.asList(expiredNotice.getFkProAttributeCodes().split(","));
            expiredProductForMapperSearch.setAttributeCodeList(attributeCodes);
        }
        expiredProductForMapperSearch.setExpiredDay(expiredNotice.getExpiredDay());
        expiredProductForMapperSearch.setUniqueCode(expiredProductSearch.getProName());
        PageHelper.startPage(Integer.parseInt(expiredProductForMapperSearch.getPageNum()), expiredProductForMapperSearch.getPageSize());
        expiredProductForMapperSearch.setShopId(expiredProductSearch.getShopId());
        //商品过期提醒列表
        List<VoExpiredProductByList> expiredProductByLists = expiredNoticeMapper.getExpiredProductLists(expiredProductForMapperSearch);
        //获取总价格和总数量
        Map<String, Object> objectMap = expiredNoticeMapper.getExpiredProductTotalMoneyAndCount( expiredProductForMapperSearch);
        BigDecimal totalNum = (BigDecimal) objectMap.get("totalNum");
        expiredProductByPage.setProductTotalNum(totalNum.intValue());
        expiredProductByPage.setProductTotalNumName("已提醒商品总数");
        String totalPrice =  (String)objectMap.get("totalPrice");
//        totalPrice = totalPrice.divide(new BigDecimal(100)).setScale(2);

        totalPrice = StringUtil.removeEnd(totalPrice, ".00");
        expiredProductByPage.setProductTotalPrice(totalPrice);
        expiredProductByPage.setProductTotalPriceName("已提醒商品总价值");
        PageInfo pageInfo = new PageInfo(expiredProductByLists);
        expiredProductByPage.setExpiredProductLists(expiredProductByLists);
        expiredProductByPage.setPageNum(pageInfo.getPageNum());
        expiredProductByPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            expiredProductByPage.setHasNextPage(true);
        } else {
            expiredProductByPage.setHasNextPage(false);
        }
        return expiredProductByPage;
    }

    @Override
    public void sendMessageForExpiredProduct() {

        List<ProExpiredNotice> expiredNotices = expiredNoticeMapper.listObject(null);
        if (expiredNotices == null && expiredNotices.size() <= 0) {
            return;
        }
        expiredNotices.forEach(expiredNotice -> {
            ParamExpiredProductForMapperSearch expiredProductForMapperSearch = new ParamExpiredProductForMapperSearch();
            if (expiredNotice.getFkProClassifyCodes() != null) {
                List<String> classifyCodes = Arrays.asList(expiredNotice.getFkProClassifyCodes().split(","));
                expiredProductForMapperSearch.setClassifyCodeList(classifyCodes);
            }
            if (expiredNotice.getFkProAttributeCodes() != null) {
                List<String> attributeCodes = Arrays.asList(expiredNotice.getFkProAttributeCodes().split(","));
                expiredProductForMapperSearch.setAttributeCodeList(attributeCodes);
            }
            expiredProductForMapperSearch.setExpiredDay(expiredNotice.getExpiredDay());
            expiredProductForMapperSearch.setShopId(expiredNotice.getFkShpShopId());
            List<VoExpiredProductByList> expiredProductByLists = expiredNoticeMapper.getExpiredProductLists(expiredProductForMapperSearch);
            if (expiredProductByLists != null && expiredProductByLists.size() > 0) {
                opPushService.pushMessageForExpiredProduct(expiredNotice.getId(), expiredNotice.getFkShpShopId(), expiredNotice.getExpiredDay());
            }

        });
    }

    @Override
    public Map<String, Object> getExpiredAddNeed(Integer shopId) {
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("classifyList", voProClassifyList);
        List<Integer> dayTypeList = new ArrayList<>();
        dayTypeList.add(30);
        dayTypeList.add(90);
        dayTypeList.add(180);
        objectMap.put("dayTypeList", dayTypeList);
        return objectMap;
    }
}
