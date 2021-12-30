package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProPrintTpl;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.enums.pro.EnumProPrintTpl;
import com.luxuryadmin.mapper.pro.ProPrintTplMapper;
import com.luxuryadmin.param.pro.ParamProPrintTpl;
import com.luxuryadmin.service.pro.ProPrintTplService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.op.VoMessageSubType;
import com.luxuryadmin.vo.pro.ParamProPrintTplAdd;
import com.luxuryadmin.vo.pro.VoProPrintTpl;
import com.luxuryadmin.vo.pro.VoProductLoad;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 商品打印Service实现类
 */
@Service
public class ProPrintTplServiceImpl implements ProPrintTplService {

    @Resource
    private ProPrintTplMapper proPrintTplMapper;
    @Autowired
    protected ProProductService proProductService;
    @Autowired
    private ShpShopService shpShopService;
    @Override
    public ProPrintTpl addProPrintTpl(Integer shopId, Integer userId, ParamProPrintTplAdd paramProPrintTplAdd) {
        ProPrintTpl proPrintTpl = new ProPrintTpl();
        BeanUtils.copyProperties(paramProPrintTplAdd,proPrintTpl);
        proPrintTpl.setFkShpShopId(shopId);
        proPrintTpl.setInsertAdmin(userId);
        proPrintTpl.setInsertTime(new Date());
        proPrintTpl.setUpdateTime(new Date());
        //不传模板类型默认普通模板
        if (LocalUtils.isEmptyAndNull(paramProPrintTplAdd.getState())){
            proPrintTpl.setState("0");
        }else {
            proPrintTpl.setState(paramProPrintTplAdd.getState());
        }

        proPrintTplMapper.saveObject(proPrintTpl);
        return proPrintTpl;
    }

    @Override
    public Integer updateProPrintTpl(Integer shopId, Integer userId, ParamProPrintTplAdd paramProPrintTplUpdate) {
        ProPrintTpl proPrintTpl = new ProPrintTpl();
        BeanUtils.copyProperties(paramProPrintTplUpdate,proPrintTpl);
        proPrintTpl.setId(paramProPrintTplUpdate.getId());
        proPrintTpl.setUpdateAdmin(userId);
        proPrintTpl.setUpdateTime(new Date());
        return proPrintTplMapper.updateObject(proPrintTpl);
    }

    @Override
    public VoProPrintTpl loadProPrintTplById(Integer shopId, Integer proPrintTplId) {
        VoProPrintTpl proPrintTpl = proPrintTplMapper.selectProPrintTplById(shopId,proPrintTplId);
        return proPrintTpl;
    }

    @Override
    public Integer deleteProPrintTplById(Integer shopId, Integer proPrintTplId) {
        return proPrintTplMapper.deleteProPrintTplById(shopId,proPrintTplId);
    }

    @Override
    public List<VoProPrintTpl> loadProPrintTplList(ParamProPrintTpl proPrintTpl) {
        List<VoProPrintTpl> proPrintTplList = proPrintTplMapper.selectProPrintTplList(proPrintTpl.getShopId());
        Boolean flag =LocalUtils.isEmptyAndNull(proPrintTpl.getBizId());
        proPrintTplList.forEach(voProPrintTpl -> {
            if ("1".equals(voProPrintTpl.getState())){
                if (!flag){
                    voProPrintTpl.setTitle(voProPrintTpl.getTitle()+("(快速模板)"));
                }
                voProPrintTpl.setContent(getContent(voProPrintTpl.getContent(),proPrintTpl.getBizId(),proPrintTpl.getShopId()));
            }
        });
        return proPrintTplList;
    }

    public String getContent(String content,String bizId,Integer shopId){
        if (LocalUtils.isEmptyAndNull(content)){
            return null;
        }
        VoProductLoad vo = null;
        if (!LocalUtils.isEmptyAndNull(bizId)){
             vo = proProductService.getProductDetailByShopIdBizId(shopId, bizId);

        }
        if (vo != null){
            ShpShop shop =shpShopService.getShpShopById(shopId.toString());
            if (shop != null){
                vo.setShopName(shop.getName());
            }else {
                vo.setShopName("");
            }
            String tagNameList = EnumProPrintTpl.getStateName(content,vo);
            return tagNameList;
        }else {
//            List<String> contentList = Arrays.asList(content.split(";"));
//            List<String> contents =new ArrayList<>();
//            contentList.forEach(s -> {
//                s =s+"\n";
//                contents.add(s);
//            });
//            String tagNameList =  StringUtils.join(contents,"");
            return content;
        }

    }






}
