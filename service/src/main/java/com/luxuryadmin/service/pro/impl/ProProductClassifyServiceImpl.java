package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.entity.pro.ProClassifyTypeShop;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProProductClassify;
import com.luxuryadmin.mapper.pro.*;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProProductClassifyService;
import com.luxuryadmin.vo.pro.VoClassifyTypeList;
import com.luxuryadmin.vo.pro.VoClassifyTypeSonList;
import com.luxuryadmin.vo.pro.VoProClassify;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 商品补充信息关联表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-08-03 10:45:15
 */
@Service
@Transactional
public class ProProductClassifyServiceImpl implements ProProductClassifyService {

    /**
     * 注入dao
     */
    @Resource
    private ProProductClassifyMapper proProductClassifyMapper;
    @Resource
    private ProClassifyTypeMapper classifyTypeMapper;
    @Resource
    private ProProductMapper proProductMapper;
    @Resource
    private ProClassifyMapper proClassifyMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProductClassifyList(ParamProductClassifyAdd productClassifyAdd) {
        List<ParamProductClassifySunAddList> productClassifySunAddLists = productClassifyAdd.getProductClassifySunAddLists();
        //参数为空 跳出分类
        if (LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
            return;
        }

        this.addProductClassifySunAddLists(productClassifySunAddLists, productClassifyAdd);
    }

    @Override
    public List<VoClassifyTypeSonList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch) {
        String bizId = classifyTypeSearch.getBizId();
        Integer proId = classifyTypeSearch.getProductId();
        ProProduct product =null;
        if (!LocalUtils.isEmptyAndNull(bizId) && LocalUtils.isEmptyAndNull(proId)) {
             product = proProductMapper.getProProductByShopIdBizId(classifyTypeSearch.getShopId(), bizId);
            if (product != null) {
                classifyTypeSearch.setProductId(product.getId());
            }
        }else if (!LocalUtils.isEmptyAndNull(proId)){
            product = (ProProduct)proProductMapper.getObjectById(proId);
        }
//        List<VoClassifyTypeSonList> classifyTypeSonList = new ArrayList<>();

        List<VoClassifyTypeSonList> classifyTypeSonLists = proProductClassifyMapper.getClassifyTypeSonList(classifyTypeSearch);
        if ( product != null){
            List<VoClassifyTypeSonList> classifyTypeSons =getClassifyTypeSonListForProduct(product);
            classifyTypeSons.addAll(classifyTypeSonLists);
            return classifyTypeSons;
        }
        return classifyTypeSonLists;
    }

    public  List<VoClassifyTypeSonList> getClassifyTypeSonListForProduct(ProProduct product){
        List<VoClassifyTypeSonList> classifyTypeSonLists = new ArrayList<>();
        VoClassifyTypeSonList classifyTypeSonList1 =new VoClassifyTypeSonList();
        classifyTypeSonList1.setName("分类");
//        classifyTypeSonList1.setContent(product.getFkProClassifyCode());
        VoProClassify classify=proClassifyMapper.getProClassifyByType(product.getFkShpShopId(), null,product.getFkProClassifyCode());
        if (classify != null){
            classifyTypeSonList1.setContent(classify.getName());
        }
        classifyTypeSonLists.add(classifyTypeSonList1);
        if (!LocalUtils.isEmptyAndNull(product.getFkProClassifySubName())){
            VoClassifyTypeSonList classifyTypeSonList2 =new VoClassifyTypeSonList();
            classifyTypeSonList2.setName("品牌");
            classifyTypeSonList2.setContent(product.getFkProClassifySubName());
            classifyTypeSonLists.add(classifyTypeSonList2);
        }
        if (!LocalUtils.isEmptyAndNull(product.getFkProSubSeriesName())){
            VoClassifyTypeSonList classifyTypeSonList3 =new VoClassifyTypeSonList();
            classifyTypeSonList3.setName("系列");
            classifyTypeSonList3.setContent(product.getFkProSubSeriesName());
            classifyTypeSonLists.add(classifyTypeSonList3);
        }
        if (!LocalUtils.isEmptyAndNull(product.getFkProSeriesModelName())){
            VoClassifyTypeSonList classifyTypeSonList4 =new VoClassifyTypeSonList();
            classifyTypeSonList4.setName("型号");
            classifyTypeSonList4.setContent(product.getFkProSeriesModelName());
            classifyTypeSonLists.add(classifyTypeSonList4);
        }
        return classifyTypeSonLists;
    }
    @Transactional(rollbackFor = Exception.class)
    public void addProductClassifySunAddLists(List<ParamProductClassifySunAddList> productClassifySunAddLists, ParamProductClassifyAdd productClassifyAdd) {
//		List<ParamProductClassifySunAddList> productClassifySunAddLists =productClassifySunAdd.getProductClassifySunAddLists();
//		Integer classifyTypeId = productClassifySunAdd.getClassifyTypeId();
        Integer productId = productClassifyAdd.getProductId();
        Integer userId = productClassifyAdd.getUserId();
        Integer shopId = productClassifyAdd.getShopId();
        if (LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
            return;
        }
//		ProClassifyType classifyTypeShop = classifyTypeMapper.getObjectById(classifyTypeId);
//		if (classifyTypeShop == null){
//			throw new MyException("暂无此扩展信息分类");
//		}
        //以下是新增 要进行判断 是新增还是编辑
        //逻辑 查询此商品是否有补充信息 如果有 del设置为已删除 然后商品更新或新增商品拓展信息 最后物理删除del为1的商品补充信息
        //查询此商品是否有补充信息 如果有 del设置为已删除
        proProductClassifyMapper.updateDelByProductId(productId);
        productClassifySunAddLists.forEach(paramProductClassifySunAddList -> {
            Integer classifyTypeDetailId = paramProductClassifySunAddList.getId();
            ProClassifyType classifyTypeDetail = classifyTypeMapper.getObjectById(classifyTypeDetailId);
            if (classifyTypeDetail == null) {
                throw new MyException("暂无此扩展信息分类");
            }
            String classifyTypeDetailName = classifyTypeDetail.getName();
            Integer classifyTypeId = classifyTypeDetail.getId();
            ProClassifyType classifyTypeShop = classifyTypeMapper.getObjectById(classifyTypeId);
            if (classifyTypeShop == null) {
                throw new MyException("暂无此扩展信息分类");
            }
            ProProductClassify productClassifyOld = proProductClassifyMapper.getProductClassifyBySearch(productId, classifyTypeId, classifyTypeDetailName);
            //商品更新或新增商品拓展信息
            if (productClassifyOld == null) {
                ProProductClassify productClassify = new ProProductClassify();
                productClassify.setFkProProductId(productId);
                productClassify.setFkProClassifyTypeId(classifyTypeId);
//                productClassify.setClassifyTypeName(classifyTypeShop.getName());
                productClassify.setClassifyTypeDetailName(classifyTypeDetailName);
                productClassify.setTypeDetailSubName(paramProductClassifySunAddList.getContent());
                productClassify.setInsertAdmin(userId);
                productClassify.setFkShpShopId(shopId);
                this.addProductClassify(productClassify);
            } else {
                productClassifyOld.setTypeDetailSubName(paramProductClassifySunAddList.getContent());
                productClassifyOld.setUpdateAdmin(userId);
                updateProductClassify(productClassifyOld);
            }
        });
        //物理删除del为1的商品补充信息
        proProductClassifyMapper.delByProductId(productId);
    }

    public void addProductClassify(ProProductClassify productClassify) {
        productClassify.setDel("0");
        productClassify.setInsertTime(new Date());
        proProductClassifyMapper.saveObject(productClassify);

    }

    public void updateProductClassify(ProProductClassify productClassify) {
        productClassify.setDel("0");
        productClassify.setUpdateTime(new Date());
        proProductClassifyMapper.updateObject(productClassify);

    }
}
