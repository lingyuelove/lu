package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.entity.shp.ShpAfterSaleGuarantee;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.shp.ShpAfterSaleGuaranteeMapper;
import com.luxuryadmin.param.shp.ParamAddShpAfterSaleGuarantee;
import com.luxuryadmin.service.shp.ShpAfterSaleGuaranteeService;
import com.luxuryadmin.vo.shp.VoShpAfterSaleGuarantee;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: TODO
 * @author: walkingPotato
 * @date: 2020-08-20 15:02
 * @email: dingxin_hz@163.com
 * @version: V1.0
 */
@Service
public class ShpAfterSaleGuaranteeServiceImpl implements ShpAfterSaleGuaranteeService {

    @Resource
    private ShpAfterSaleGuaranteeMapper shpAfterSaleGuaranteeMapper;

    @Resource
    private OrdOrderMapper ordOrderMapper;

    /**
     * 添加店铺售后保障
     * @param shopId
     * @param userId
     * @param guaranteeInfo
     */
    @Override
    public ShpAfterSaleGuarantee addAfterSaleGuarantee(Integer shopId, Integer userId, ParamAddShpAfterSaleGuarantee guaranteeInfo) {
        ShpAfterSaleGuarantee shpAfterSaleGuarantee = new ShpAfterSaleGuarantee();
        shpAfterSaleGuarantee.setFkShpShopId(shopId);
        shpAfterSaleGuarantee.setGuaranteeName(guaranteeInfo.getGuaranteeName());
        shpAfterSaleGuarantee.setInsertAdmin(userId);
        shpAfterSaleGuarantee.setUpdateAdmin(userId);
        shpAfterSaleGuarantee.setInsertTime(new Date());
        shpAfterSaleGuarantee.setUpdateTime(new Date());
        shpAfterSaleGuarantee.setDel(ConstantCommon.DEL_OFF);

        shpAfterSaleGuaranteeMapper.insertSelective(shpAfterSaleGuarantee);
        return shpAfterSaleGuarantee;
    }

    @Override
    public Integer deleteAfterSaleGuarantee(Integer shopId, Integer userId, Integer guaranteeId) throws Exception{

        Map<String,Object> guaranteeParams = new HashMap<>();
        guaranteeParams.put("fkShpShopId",shopId);
        guaranteeParams.put("id",guaranteeId);
        ShpAfterSaleGuarantee shpAfterSaleGuarantee = shpAfterSaleGuaranteeMapper.selectByShopIdAndGuaranteeId(guaranteeParams);
        if(null == shpAfterSaleGuarantee){
            throw new Exception("删除失败，未查询到对应的售后保障字段");
        }

        Map<String,Object> orderParams = new HashMap<>();
        orderParams.put("fkShpShopId",shopId);
        orderParams.put("queryGuaranteeName",shpAfterSaleGuarantee.getGuaranteeName());
        //查询该【售后保障】是否已在开单中使用，已使用的话，不允许删除
        Boolean isUsed = ordOrderMapper.selectAfterSaleGuaranteeCountByName(orderParams) > 0;
        if(isUsed){
            throw new Exception("该标签已被使用，不可删除~");
        }

        //逻辑删除
        shpAfterSaleGuarantee.setDel(ConstantCommon.DEL_ON);
        shpAfterSaleGuarantee.setUpdateAdmin(userId);
        shpAfterSaleGuarantee.setUpdateTime(new Date());
        return shpAfterSaleGuaranteeMapper.updateByPrimaryKeySelective(shpAfterSaleGuarantee);
    }

    @Override
    public List<VoShpAfterSaleGuarantee> listAfterSaleGuarantee(Integer shopId) {
        List<VoShpAfterSaleGuarantee> list = new ArrayList<>();

        List<VoShpAfterSaleGuarantee> dbList = shpAfterSaleGuaranteeMapper.selectGuaranteeListByShopId(shopId);
        return dbList;
    }

    @Override
    public void initShpAfterSaleGuarantee(int shopId, int userId) {
        //添加默认售后保障
        String[] defaultGuaranteeNameArr = {"假一赔三","一年质保","一年内八折回收"};
        for(int i=0;i<3;i++){
            ParamAddShpAfterSaleGuarantee paramAddShpAfterSaleGuarantee = new ParamAddShpAfterSaleGuarantee();
            paramAddShpAfterSaleGuarantee.setGuaranteeName(defaultGuaranteeNameArr[i]);
            addAfterSaleGuarantee(shopId,userId,paramAddShpAfterSaleGuarantee);
        }
    }

    @Override
    public Integer initAllShpAfterSaleGuarantee() {
        List<Integer> shopIdList = shpAfterSaleGuaranteeMapper.selectAllNeedInitShopId();
        Integer totalResultCount = 0;
        for (Integer shopId : shopIdList) {
            totalResultCount++;
            initShpAfterSaleGuarantee(shopId,-1);
        }
        return totalResultCount;
    }

    @Override
    public void deleteShpAfterSaleGuaranteeByShopId(int shopId) {
        shpAfterSaleGuaranteeMapper.deleteShpAfterSaleGuaranteeByShopId(shopId);
    }
}
