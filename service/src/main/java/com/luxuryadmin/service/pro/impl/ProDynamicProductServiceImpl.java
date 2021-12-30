package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.entity.pro.ProDynamicProduct;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.enums.pro.EnumProDynamic;
import com.luxuryadmin.mapper.pro.ProDynamicMapper;
import com.luxuryadmin.mapper.pro.ProDynamicProductMapper;
import com.luxuryadmin.mapper.pro.ProLockRecordMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.pro.ParamDynamicProductAdd;
import com.luxuryadmin.param.pro.ParamDynamicProductDelete;
import com.luxuryadmin.param.pro.ParamDynamicProductQuery;
import com.luxuryadmin.param.pro.ParamProductLock;
import com.luxuryadmin.service.pro.ProDynamicProductService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.vo.pro.VoDynamicProductList;
import com.luxuryadmin.vo.pro.VoLockNumByProId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 动态列表商品信息 serverImpl
 *
 * @author zhangsai
 * @Date 2021-11-05 11:45:33
 */
@Service
@Transactional
public class ProDynamicProductServiceImpl implements ProDynamicProductService {


    /**
     * 注入dao
     */
    @Resource
    private ProDynamicProductMapper proDynamicProductMapper;

    @Resource
    private ProDynamicMapper proDynamicMapper;

    @Resource
    private ProLockRecordMapper proLockRecordMapper;

    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private ProLockRecordService proLockRecordService;
    /**
     * 获取商品位置列表
     *
     * @param param
     * @return
     */
    @Override
    public List<VoDynamicProductList> listDynamicProduct(ParamDynamicProductQuery param) {
        List<VoDynamicProductList> voDynamicProducts = proDynamicProductMapper.listDynamicProduct(param);
        return voDynamicProducts;
    }

    /**
     * 2.6.6添加商品位置
     *
     * @param dynamicProduct
     */
    @Override
    public void saveDynamicProduct(ParamDynamicProductAdd dynamicProduct) {
        List<Integer> proIds = Arrays.asList(dynamicProduct.getProId().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (StringUtil.isBlank(dynamicProduct.getDynamicId())){
            proDynamicProductMapper.deleteDynamicProductByProIds(proIds,dynamicProduct.getShopId());
            return;
        }
        int dynamicId = Integer.parseInt(dynamicProduct.getDynamicId());
        //获取商品信息
        List<ProProduct> proProducts = proProductMapper.getProductByIds(proIds);
        ProDynamic proDynamic = proDynamicMapper.getObjectById(dynamicId);

        if (proDynamic != null && "0".equals(proDynamic.getDel()) && proDynamic.getFkShpShopId().equals(dynamicProduct.getShopId())) {
            //获取锁单数量
            List<VoLockNumByProId> voLockNumByProIds = proLockRecordMapper.getLockByProId(proIds, dynamicProduct.getShopId());
            Map<Integer, VoLockNumByProId> lockNumByProIdMap = new HashMap<>();
            if (voLockNumByProIds != null && voLockNumByProIds.size() > 0) {
                lockNumByProIdMap = voLockNumByProIds.stream().collect(Collectors.toMap(VoLockNumByProId::getProId, Function.identity()));
            }
            //获取已存在商品信息
            List<ProDynamicProduct> proDynamics = proDynamicProductMapper.getDynamicProductInfoByProductId(proIds, dynamicProduct.getShopId());
            Map<Integer, ProDynamicProduct> proDynamicProductMap = new HashMap<>();
            if (proDynamics != null && proDynamics.size() > 0) {
                proDynamicProductMap = proDynamics.stream().collect(Collectors.toMap(ProDynamicProduct::getFkProProductId, Function.identity()));
            }
            List<ProDynamicProduct> proDynamicProductsAdd = new ArrayList<>();
            List<Integer> dynamicProductIds = new ArrayList<>();
            for (ProProduct pp : proProducts) {
                if (proDynamicProductMap.get(pp.getId()) != null) {
                    ProDynamicProduct proDynamicProduct = proDynamicProductMap.get(pp.getId());
                    if (!proDynamicProduct.getState().equals(40)) {
                        dynamicProductIds.add(proDynamicProduct.getId());
                    }
                } else {
                    ProDynamicProduct pdp = new ProDynamicProduct();
                    pdp.setFkProDynamicId(dynamicId);
                    pdp.setFkProProductId(pp.getId());
                    VoLockNumByProId voLockNumByProId = lockNumByProIdMap.get(pp.getId());
                    if (voLockNumByProId != null && voLockNumByProId.getLockNum().equals(pp.getTotalNum())) {
                        pdp.setState(EnumDynamicProductState.ALREADY_LOCK.getCode());
                    } else {
                        pdp.setState(EnumDynamicProductState.NORMAL.getCode());
                    }
                    pdp.setFkShpShopId(dynamicProduct.getShopId());
                    pdp.setInsertTime(new Date());
                    pdp.setUpdateTime(new Date());
                    pdp.setInsertAdmin(dynamicProduct.getUserId());
                    proDynamicProductsAdd.add(pdp);
                }

            }
            if (proDynamicProductsAdd.size() > 0) {
                proDynamicProductMapper.saveBatch(proDynamicProductsAdd);
            }
            if (dynamicProductIds.size() > 0) {
                proDynamicProductMapper.updateByIds(dynamicProductIds, dynamicProduct.getDynamicId());
            }
        }
    }

    /**
     * 2.6.6删除商品位置
     *
     * @param param
     */
    @Override
    public void deleteDynamicProduct(ParamDynamicProductDelete param) {
        List<Integer> ids = Arrays.asList(param.getDynamicProductIds().split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        proDynamicProductMapper.deleteDynamicProduct(ids);
    }

    /**
     * 根据商品id删除动态信息
     *
     * @param proId
     * @param shopId
     */
    @Override
    public void deleteDynamicProductByProId(Integer proId, Integer shopId) {
        proDynamicProductMapper.deleteDynamicProductByProId(proId, shopId);
    }

    /**
     * 根据proid修改动态状态
     *
     * @param proId
     * @param state
     */
    @Override
    public void updateStateByProId(Integer proId, Integer state, Integer shopId) {
        proDynamicProductMapper.updateStateByProId(proId, state, shopId);
    }

    @Override
    public void updateListStateByProId(ParamProductLock lockParam, ProProduct proProduct) {
        List<Integer> ids = new ArrayList<>();
        ids.add(proProduct.getId());
        List<VoLockNumByProId> voLockNumByProIds = proLockRecordService.getLockByProId(ids, lockParam.getShopId());
        if (voLockNumByProIds != null && voLockNumByProIds.size() > 0) {
            VoLockNumByProId voLockNumByProId = voLockNumByProIds.get(0);
            if (proProduct.getTotalNum() - voLockNumByProId.getLockNum() <= 0) {
                this.updateStateByProId(proProduct.getId(), EnumDynamicProductState.ALREADY_LOCK.getCode(), lockParam.getShopId());
            }
        }
    }

    /**
     * 根据商品id查询动态信息
     *
     * @param proId
     * @param shopId
     * @return
     */
    @Override
    public ProDynamicProduct getDynamicProductInfoByProId(Integer proId, Integer shopId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(proId);
        List<ProDynamicProduct> dynamicProductInfoByProductId = proDynamicProductMapper.getDynamicProductInfoByProductId(ids, shopId);
        return dynamicProductInfoByProductId != null && dynamicProductInfoByProductId.size() > 0 ? dynamicProductInfoByProductId.get(0) : null;
    }

    /**
     * 正常修改
     *
     * @param proDynamicProduct
     */
    @Override
    public void update(ProDynamicProduct proDynamicProduct) {
        proDynamicProductMapper.updateObject(proDynamicProduct);
    }
}
