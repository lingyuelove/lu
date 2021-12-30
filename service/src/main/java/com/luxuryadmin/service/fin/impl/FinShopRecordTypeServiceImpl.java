package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.fin.FinShopRecordType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.mapper.fin.FinShopRecordTypeMapper;
import com.luxuryadmin.service.fin.FinShopRecordTypeService;
import com.luxuryadmin.vo.fin.VoFinShopRecordType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinShopRecordTypeServiceImpl implements FinShopRecordTypeService {

    @Resource
    private FinShopRecordTypeMapper finShopRecordTypeMapper;

    @Override
    public int initFinShopRecordTypeByShopIdAndUserId(int shopId, int userId) {
        //添加默认店铺财务流水类型
        int count = 0;

        String[] defaultRecordInTypeArr = {"其它"};
        for (String defaultRecordInType : defaultRecordInTypeArr) {
            count++;
            addFinShopRecordType(shopId, userId, defaultRecordInType, EnumFinShopRecordInoutType.IN.getCode());
        }

        String[] defaultRecordOutTypeArr = {"快递费", "维修费", "参展费", "其它"};
        for (String defaultRecordOutType : defaultRecordOutTypeArr) {
            count++;
            addFinShopRecordType(shopId, userId, defaultRecordOutType, EnumFinShopRecordInoutType.OUT.getCode());
        }

        return count;
    }

    @Override
    public List<VoFinShopRecordType> listFinShopRecordTypeByShopId(int shopId, String inoutType) {
        List<FinShopRecordType> list = finShopRecordTypeMapper.listFinShopRecordTypeByShopId(shopId, inoutType);
        List<VoFinShopRecordType> voList = formatFinShopRecordTypeList(list);
        return voList;
    }

    @Override
    public List<String> listAllRecordTypeWithoutInoutType(Integer shopId) {
        List<String> list = finShopRecordTypeMapper.listAllRecordTypeWithoutInoutType(shopId);
        return list;
    }

    @Override
    public void deleteFinShopRecordTypeByShopId(int shopId) {
        finShopRecordTypeMapper.deleteFinShopRecordTypeByShopId(shopId);
    }

    @Override
    public FinShopRecordType addFinShopRecordType(Integer shopId, Integer userId, String finShopRecordTypeName, String inoutType) {
        FinShopRecordType finShopRecordType = new FinShopRecordType();
        finShopRecordType.setFkShpShopId(shopId);
        finShopRecordType.setFinRecordTypeName(finShopRecordTypeName);
        finShopRecordType.setInoutType(inoutType);
        finShopRecordType.setInsertAdmin(userId);
        finShopRecordType.setUpdateAdmin(userId);
        finShopRecordType.setInsertTime(new Date());
        finShopRecordType.setUpdateTime(new Date());
        finShopRecordType.setDel(ConstantCommon.DEL_OFF);

        FinShopRecordType finShopRecordTypeExists = finShopRecordTypeMapper.selectFinShopRecordTypeByName(shopId, finShopRecordTypeName, inoutType);
        if (null != finShopRecordTypeExists) {
            throw new MyException("流水类型已存在，不能重复添加");
        }
        finShopRecordTypeMapper.insertSelective(finShopRecordType);
        return finShopRecordType;
    }

    @Override
    public Integer deleteFinShopRecordType(Integer shopId, Integer userId, Integer finShopRecordTypeId) {
        //如果只剩最后一个流水类型，不允许删除
        Integer typeCount = finShopRecordTypeMapper.selectCountByShopId(shopId);
        if (typeCount.equals(1)) {
            throw new MyException("流水类型至少保留1个");
        }

        //物理删除流水类型
        //finShopRecordTypeMapper.updateByPrimaryKeySelective(finShopRecordTypeUpdate);
        return finShopRecordTypeMapper.deleteByPrimaryKey(finShopRecordTypeId);
    }

    @Override
    public Integer initAllShpFinShopRecordType() {
        List<Integer> shopIdList = finShopRecordTypeMapper.selectAllNeedInitShopId();
        Integer totalResultCount = 0;
        for (Integer shopId : shopIdList) {
            totalResultCount++;
            initFinShopRecordTypeByShopIdAndUserId(shopId, -1);
        }
        return totalResultCount;
    }

    /**
     * 将财务流水类型DO转换为VO
     *
     * @param typeList
     * @return
     */
    private List<VoFinShopRecordType> formatFinShopRecordTypeList(List<FinShopRecordType> typeList) {
        List<VoFinShopRecordType> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(typeList)) {
            for (FinShopRecordType type : typeList) {
                VoFinShopRecordType voType = new VoFinShopRecordType();
                BeanUtils.copyProperties(type, voType);
                voList.add(voType);
            }
        }
        return voList;
    }
}
