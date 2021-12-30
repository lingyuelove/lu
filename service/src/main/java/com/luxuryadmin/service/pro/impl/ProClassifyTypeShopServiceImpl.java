package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.entity.pro.ProClassifyTypeShop;
import com.luxuryadmin.mapper.pro.ProClassifyTypeShopMapper;
import com.luxuryadmin.param.pro.ParamClassifyTypeShopAdd;
import com.luxuryadmin.service.pro.ProClassifyTypeShopService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 单个店铺补充信息不适用关联表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-08-05 14:29:05
 */
@Service
@Transactional
public class ProClassifyTypeShopServiceImpl implements ProClassifyTypeShopService {

    /**
     * 注入dao
     */
    @Resource
    private ProClassifyTypeShopMapper proClassifyTypeShopMapper;


    @Override
    public void addRemoveClassifyType(ParamClassifyTypeShopAdd classifyTypeShopAdd) {

        Boolean addClassifyTypeId =false;
        if (!LocalUtils.isEmptyAndNull(classifyTypeShopAdd.getClassifyTypeId())){
            addClassifyTypeId =true;
        }
        List<String> classifyTypeIdList = Arrays.asList(classifyTypeShopAdd.getClassifyTypeId().split(";"));
        if (addClassifyTypeId && !LocalUtils.isEmptyAndNull(classifyTypeIdList)) {
            addClassifyTypeId =true;
        }
        if (addClassifyTypeId){
            //启用
            if (addClassifyTypeId){
                classifyTypeIdList.forEach(id -> {
                    Integer classifyTypeId = Integer.parseInt(id);
                    ProClassifyTypeShop classifyTypeShopOld = proClassifyTypeShopMapper.getTypeShopByShopIdAndTypeId(classifyTypeShopAdd.getShopId(), classifyTypeId);
                    if (classifyTypeShopOld != null){
                        proClassifyTypeShopMapper.deleteObject(classifyTypeShopOld);
                    }
                });
            }
        }


        //禁用
        String classifyTypeIdForNot =classifyTypeShopAdd.getClassifyTypeIdForNot();
        Boolean addClassifyTypeIdForNot =false;
        if (!LocalUtils.isEmptyAndNull(classifyTypeIdForNot)){
            addClassifyTypeIdForNot =true;
        }
        List<String> classifyTypeIdForNotList = Arrays.asList(classifyTypeShopAdd.getClassifyTypeIdForNot().split(";"));
        if (addClassifyTypeIdForNot && !LocalUtils.isEmptyAndNull(classifyTypeIdForNotList)) {
            addClassifyTypeIdForNot =true;
        }
        if (addClassifyTypeIdForNot){
            classifyTypeIdForNotList.forEach(id -> {
                Integer classifyTypeId = Integer.parseInt(id);
                ProClassifyTypeShop classifyTypeShopOld = proClassifyTypeShopMapper.getTypeShopByShopIdAndTypeId(classifyTypeShopAdd.getShopId(), classifyTypeId);
                if (classifyTypeShopOld == null){
                    ProClassifyTypeShop classifyTypeShop = new ProClassifyTypeShop();
                    classifyTypeShop.setFkShpShopId(classifyTypeShopAdd.getShopId());
                    classifyTypeShop.setFkProClassifyTypeId(classifyTypeId);
                    classifyTypeShop.setInsertAdmin(classifyTypeShopAdd.getUserId());
                    classifyTypeShop.setInsertTime(new Date());
                    classifyTypeShop.setState(0);
                    proClassifyTypeShopMapper.saveObject(classifyTypeShop);
                }
            });
        }

    }
}
