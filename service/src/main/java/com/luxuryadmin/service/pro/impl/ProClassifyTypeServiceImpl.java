package com.luxuryadmin.service.pro.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassifyType;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProProductClassify;
import com.luxuryadmin.entity.pro.ProStandard;
import com.luxuryadmin.mapper.pro.ProClassifyTypeMapper;
import com.luxuryadmin.mapper.pro.ProProductClassifyMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProStandardMapper;
import com.luxuryadmin.mapper.sys.SysEnumMapper;
import com.luxuryadmin.param.pro.ParamClassifyTypeAdd;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamClassifyTypeSunAdd;
import com.luxuryadmin.param.pro.ParamClassifyTypeUpdate;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProClassifyTypeService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.pro.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * 商品补充信息分类表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-08-03 10:45:15
 */
@Service
@Transactional
public class ProClassifyTypeServiceImpl implements ProClassifyTypeService {


    /**
     * 注入dao
     */
    @Resource
    private ProClassifyTypeMapper proClassifyTypeMapper;
    @Autowired
    private ProClassifyService proClassifyService;
    @Resource
    private SysEnumMapper sysEnumMapper;
    @Resource
    private ProProductClassifyMapper proProductClassifyMapper;
    @Resource
    private ProProductMapper proProductMapper;
    @Autowired
    private ProProductService proProductService;
    @Resource
    private ProStandardMapper proStandardMapper;
    //方法废弃
//    @Override
//    public void addClassifyType(ParamClassifyTypeAdd classifyTypeAdd) {
//        String type = "1";
//        ProClassifyType classifyTypeOld = proClassifyTypeMapper.getClassifyTypeByNameAndType(type, classifyTypeAdd.getName(), classifyTypeAdd.getClassifyCode());
//        if (classifyTypeOld != null) {
//            throw new MyException("商品补充信息已存在");
//        }
//        ProClassifyType classifyType = new ProClassifyType();
//        classifyType.setFkProClassifyCode(classifyTypeAdd.getClassifyCode());
//        if (LocalUtils.isEmptyAndNull(classifyTypeAdd.getClassifyCode())) {
//            throw new MyException("商品分类不能为空");
//        }
//        classifyType.setType(type);
//        classifyType.setFkProClassifyCode(classifyTypeAdd.getClassifyCode());
//        classifyType.setName(classifyTypeAdd.getName());
//        if (LocalUtils.isEmptyAndNull(classifyTypeAdd.getShopId())) {
//            classifyType.setFkShpShopId(-9);
//        } else {
//            classifyType.setFkShpShopId(classifyTypeAdd.getShopId());
//        }
//        if (LocalUtils.isEmptyAndNull(classifyTypeAdd.getUserId())) {
//            classifyType.setInsertAdmin(-9);
//        } else {
//            classifyType.setInsertAdmin(classifyTypeAdd.getUserId());
//        }
//        classifyType.setSort(classifyTypeAdd.getSort());
//        this.addClassifyType(classifyType);
//    }

    @Override
    public void addClassifyTypeSun(ParamClassifyTypeAdd classifyTypeAdd) {

        String type = "2";
        String typeSun = "3";
        ProClassifyType classifyType = new ProClassifyType();
        classifyType.setChoseType(classifyTypeAdd.getChoseType());
//        classifyType.setProClassifyTypeId(classifyTypeAdd.getClassifyTypeId());
        classifyType.setFkShpShopId(-9);
        classifyType.setInsertAdmin(-9);
        classifyType.setType(type);
        classifyType.setName(classifyTypeAdd.getName());
        classifyType.setSort(classifyTypeAdd.getSort());
        classifyType.setFkProClassifyCode(classifyTypeAdd.getClassifyCode());
        ProClassifyType proClassifyType = this.addClassifyType(classifyType);
        //String转Json转List实体类
        JSONArray jsonArray = JSONObject.parseArray(classifyTypeAdd.getClassifyTypeSunAdds());
        List<ParamClassifyTypeSunAdd> classifyTypeSunAdds = JSONObject.parseArray(jsonArray.toJSONString(), ParamClassifyTypeSunAdd.class);

//        List<ParamClassifyTypeSunAdd> classifyTypeSunAdds = classifyTypeAdd.getClassifyTypeSunAdds();

        if (!LocalUtils.isEmptyAndNull(classifyTypeSunAdds)) {
            classifyTypeSunAdds.forEach(classifyTypeSunAdd -> {
                ProClassifyType classifyTypeSun = new ProClassifyType();
                classifyTypeSun.setProClassifyTypeId(proClassifyType.getId());
                classifyTypeSun.setFkShpShopId(-9);
                classifyTypeSun.setInsertAdmin(-9);
                classifyTypeSun.setType(typeSun);
                classifyTypeSun.setName(classifyTypeSunAdd.getName());
                this.addClassifyType(classifyTypeSun);
            });
        }
    }

    @Override
    public void updateClassifyType(ParamClassifyTypeUpdate classifyTypeUpdate) {
        //判断是二级分类还是一级分类
        ProClassifyType classifyType = new ProClassifyType();
        classifyType.setId(classifyTypeUpdate.getId());
        classifyType.setSort(classifyTypeUpdate.getSort());
        classifyType.setName(classifyTypeUpdate.getName());
        classifyType.setFkProClassifyCode(classifyType.getFkProClassifyCode());
        classifyType.setChoseType(classifyTypeUpdate.getChoseType());
        //一级直接更新返回 一级废弃
//        if (!"2".equals(classifyTypeUpdate.getType())){
//            upClassifyType(classifyType);
//            return;
//        }
        //二级
        upClassifyType(classifyType);
        //String转Json转List实体类
        JSONArray jsonArray = JSONObject.parseArray(classifyTypeUpdate.getClassifyTypeSunAdds());
        List<ParamClassifyTypeSunAdd> classifyTypeSunAdds = JSONObject.parseArray(jsonArray.toJSONString(), ParamClassifyTypeSunAdd.class);
        //原有内容设置删除
        proClassifyTypeMapper.updateClassifyTypeForDel(classifyTypeUpdate.getId());
        //编辑内容判断是否为空
        if (!LocalUtils.isEmptyAndNull(classifyTypeSunAdds)) {
            classifyTypeSunAdds.forEach(classifyTypeSunAdd -> {
                classifyType.setName(classifyTypeSunAdd.getName());
                //判断是新增还是修改
                if (LocalUtils.isEmptyAndNull(classifyTypeSunAdd.getId())) {
                    ProClassifyType classifyTypeSun = new ProClassifyType();
                    classifyTypeSun.setProClassifyTypeId(classifyTypeUpdate.getId());
                    classifyTypeSun.setFkShpShopId(-9);
                    classifyTypeSun.setInsertAdmin(-9);
                    classifyTypeSun.setType("3");
                    classifyTypeSun.setName(classifyTypeSunAdd.getName());
                    this.addClassifyType(classifyTypeSun);
                } else {
                    classifyType.setId(classifyTypeSunAdd.getId());
                    classifyType.setDel("0");
                    upClassifyType(classifyType);
                }
            });

        }

    }

    public void upClassifyType(ProClassifyType classifyType) {
        classifyType.setUpdateTime(new Date());
        proClassifyTypeMapper.updateObject(classifyType);
    }

    //新增
    public ProClassifyType addClassifyType(ProClassifyType classifyType) {
        classifyType.setInsertTime(new Date());
        classifyType.setDel("0");
        classifyType.setState(1);
        proClassifyTypeMapper.saveObject(classifyType);
        return classifyType;
    }

    @Override
    public void deleteClassifyType(Integer id) {
        ProClassifyType classifyType = new ProClassifyType();
        classifyType.setId(id);
        classifyType.setDel("1");
        proClassifyTypeMapper.updateObject(classifyType);
    }

    @Override
    public List<VoClassifyTypeSonList> getClassifyTypeList(ParamClassifyTypeSearch classifyTypeSearch) {
//        List<VoClassifyTypeList> classifyTypeLists = proClassifyTypeMapper.getClassifyTypeList(classifyTypeSearch);

        List<VoClassifyTypeSonList> classifyTypeSonLists = this.getClassifyTypeSonLists(classifyTypeSearch);
        if (LocalUtils.isEmptyAndNull(classifyTypeSonLists)) {
            return classifyTypeSonLists;
        }

        return classifyTypeSonLists;
    }

    @Override
    public List<VoClassify> getClassifyTypeForClassify(ParamClassifyTypeSearch classifyTypeSearch) {
        List<VoProClassify> classifyList = proClassifyService.listProClassifyByState(classifyTypeSearch.getShopId(), "1");
        if (LocalUtils.isEmptyAndNull(classifyList)) {
            return null;
        }
        List<VoClassify> classifies = new ArrayList<>();
        //所有分类下边的扩展信息 无论是否有值
        String type =null;
        String bizId = classifyTypeSearch.getBizId();
        Integer productId = null;
        if (!LocalUtils.isEmptyAndNull(bizId)){
            ProProduct product = proProductService.getProProductForDeleteByShopIdBizId(classifyTypeSearch.getShopId(),bizId);
            if (product != null){
                type =product.getFkProClassifyCode();
                productId = product.getId();
            }
        }
        String typeNew =type;

        ProStandard proStandard = proStandardMapper.getByProductId(productId);
        classifyList.forEach(voProClassify -> {
            //获取商品二级分类
            classifyTypeSearch.setClassifyCode(voProClassify.getCode());
            List<VoClassifyTypeSonList> classifyTypeLists = getClassifyTypeSonLists(classifyTypeSearch);

            if (!LocalUtils.isEmptyAndNull(classifyTypeLists) && !LocalUtils.isEmptyAndNull(proStandard)) {
                listClassifyTypeContext(classifyTypeLists,proStandard);
//                classifyTypeLists.forEach(classifyTypeSonList -> {
//                    //判断商品类型和补充信息类型是否相同 查看商品是否存在商品补充信息 如果存在 则添加
//                    Boolean flag =!LocalUtils.isEmptyAndNull(proStandard) && classifyTypeSonList.getState() != 0 && !LocalUtils.isEmptyAndNull(typeNew) && typeNew.equals(voProClassify.getCode());
//                    if (flag) {
//                        ProProductClassify productClassify = proProductClassifyMapper.getProductClassifyBySearch(productIdNew, classifyTypeSonList.getPId(), classifyTypeSonList.getName());
//                        if (productClassify != null) {
//                            classifyTypeSonList.setContent(productClassify.getTypeDetailSubName());
//                        }
//                    }
//                });
//                objectMap.put("classifyTypeListFor"+voProClassify.getCode(), classifyTypeLists);

            }
            VoClassify classify =new VoClassify();
            BeanUtils.copyProperties(voProClassify,classify);
            classify.setSon(classifyTypeLists);
            classifies.add(classify);
        });


        return classifies;

    }

    public List<VoClassifyTypeSonList> listClassifyTypeContext( List<VoClassifyTypeSonList> classifyTypeLists,ProStandard proStandard){
        classifyTypeLists.forEach(classifyTypeSonList -> {
            if ("机芯类型".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getWatchCoreType());
            }
            if ("表壳材质".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getWatchcase());
            }
            if ("表盘直径".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getWatchcaseSize());
            }
            if ("材质".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getMaterial());
            }
            if ("尺寸".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getObjectSize());
            }
            if ("尺码".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getClothesSize());
            }
            if ("官方指导价".equals(classifyTypeSonList.getName())){
                classifyTypeSonList.setContent(proStandard.getPublicPrice());
            }
        });
        return classifyTypeLists;
    }
    //获取二级分类三级分类
    public List<VoClassifyTypeSonList> getClassifyTypeSonLists(ParamClassifyTypeSearch classifyTypeSearch) {
        List<VoClassifyTypeSonList> classifyTypeSonLists = proClassifyTypeMapper.getClassifyTypeSonLists(classifyTypeSearch);
        if (LocalUtils.isEmptyAndNull(classifyTypeSonLists)) {
            return classifyTypeSonLists;
        }

        Integer shopId = classifyTypeSearch.getShopId();
        Integer productId = classifyTypeSearch.getProductId();

        classifyTypeSonLists.forEach(classifyTypeSonList -> {
            List<VoClassifyTypeGrandSonList> classifyTypeGrandSonLists = proClassifyTypeMapper.getClassifyTypeGrandSonList(classifyTypeSonList.getId(), shopId);
            classifyTypeSonList.setGrandson(classifyTypeGrandSonLists);
            if (productId != null && classifyTypeSonList.getState() != 0) {
                ProProductClassify productClassify = proProductClassifyMapper.getProductClassifyBySearch(productId, classifyTypeSonList.getPId(), classifyTypeSonList.getName());
                if (productClassify != null) {
                    classifyTypeSonList.setContent(productClassify.getTypeDetailSubName());
                }
            }

        });

        return classifyTypeSonLists;
    }

    @Override
    public List<VoClassify> getClassifyListForApp(ParamClassifyTypeSearch classifyTypeSearch) {
        List<VoClassify> classifies = new ArrayList<>();
        List<VoProClassify> classifyList = proClassifyService.listProClassifyByState(classifyTypeSearch.getShopId(), "1");
        if (LocalUtils.isEmptyAndNull(classifyList)) {
            return null;
        }
        //所有分类下边的扩展信息 无论是否有值
        classifyList.forEach(voProClassify -> {
//			if ("WB".equals(voProClassify.getCode()) || "XB".equals(voProClassify.getCode())){
            VoClassify classify = new VoClassify();
            BeanUtils.copyProperties(voProClassify, classify);
            classifyTypeSearch.setClassifyCode(voProClassify.getCode());

            List<VoClassifyTypeSonList> classifyTypeLists = getClassifyTypeSonLists(classifyTypeSearch);

            if (!LocalUtils.isEmptyAndNull(classifyTypeLists)) {
                classify.setSon(classifyTypeLists);
                classifies.add(classify);
            }
//            }

        });
        if (LocalUtils.isEmptyAndNull(classifies)) {
            return null;
        }

        return classifies;
    }

    @Override
    public VoClassifyTypeSonPage getClassifyListForAdmin(ParamClassifyTypeSearch classifyTypeSearch) {
        if (LocalUtils.isEmptyAndNull(classifyTypeSearch.getPageNum())) {
            classifyTypeSearch.setPageNum("1");
        }
        PageHelper.startPage(Integer.parseInt(classifyTypeSearch.getPageNum()), classifyTypeSearch.getPageSize());
        List<VoClassifyTypeSonList> classifyTypeSonLists = this.getClassifyTypeSonLists(classifyTypeSearch);
        if (LocalUtils.isEmptyAndNull(classifyTypeSonLists)) {
            return null;
        }
        PageInfo<VoClassifyTypeSonList> pageInfo = new PageInfo(classifyTypeSonLists);
        VoClassifyTypeSonPage classifyTypeSonPage = new VoClassifyTypeSonPage();
        classifyTypeSonPage.setPageNum(pageInfo.getPageNum());
        classifyTypeSonPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            classifyTypeSonPage.setHasNextPage(true);
        } else {
            classifyTypeSonPage.setHasNextPage(false);
        }
        classifyTypeSonPage.setList(classifyTypeSonLists);
        classifyTypeSonPage.setTotal(pageInfo.getTotal());
        return classifyTypeSonPage;
    }


}
