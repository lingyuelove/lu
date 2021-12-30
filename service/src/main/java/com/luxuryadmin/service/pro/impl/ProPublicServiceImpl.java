package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.mapper.pro.ProPublicMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.service.pro.ProClassifyTypeService;
import com.luxuryadmin.service.pro.ProPublicService;
import com.luxuryadmin.vo.pro.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公价商品表 业务逻辑层;
 *
 * @author monkey king
 * @date 2021-06-22 23:24:46
 */
@Slf4j
@Service
public class ProPublicServiceImpl implements ProPublicService {

    @Resource
    private ProPublicMapper proPublicMapper;
    @Autowired
    private ServicesUtil servicesUtil;
    @Autowired
    private ProClassifySubService proClassifySubService;
    @Autowired
    private ProClassifyTypeService proClassifyTypeService;
    @Override
    public void saveBatch(List<ProPublic> list) {
        long st = System.currentTimeMillis();
        proPublicMapper.saveBatch(list);
        long et = System.currentTimeMillis();
        log.info("=======公价商品批量插入,条数为: {},耗时为: {}ms", list.size(), (et - st));
    }

    @Override
    public void updateBatchSmallImg(List<ProPublic> list) {
        proPublicMapper.updateBatchSmallImg(list);
    }


    @Override
    public List<VoProPublic> listProPublic(ParamProPublic queryParam) {
        List<VoProPublic> voProPublicList = proPublicMapper.listProPublic(queryParam);
        if (LocalUtils.isEmptyAndNull(voProPublicList)){
            return voProPublicList;
        }
        voProPublicList.forEach(voProPublic -> {
            List<String>  supplementInfo =getSupplementInfo(voProPublic);
            voProPublic.setSupplementInfo(supplementInfo);
        });
        return voProPublicList;
    }

    /**
     *
     * @param voProPublic
     * @return
     */
    public List<String> getSupplementInfo(VoProPublic voProPublic){
        ParamClassifyTypeSearch classifyTypeSearch =new ParamClassifyTypeSearch();
        classifyTypeSearch.setClassifyCode(voProPublic.getClassifyCode());
        List<VoClassifyTypeSonList> classifyTypeLists = proClassifyTypeService.getClassifyTypeList(classifyTypeSearch);
        if (LocalUtils.isEmptyAndNull(classifyTypeLists)){
            return null;
        }
        List<String> supplementInfoList = new ArrayList<>();
        classifyTypeLists.forEach(classifyTypeSonList -> {
            if (!LocalUtils.isEmptyAndNull(classifyTypeSonList.getName())){
                if ("机芯类型".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getWatchCoreType());
                    supplementInfoList.add(typeName);
                }
                if ("表壳材质".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getWatchcase());
                    supplementInfoList.add(typeName);
                }
                if ("表盘直径".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getWatchcaseSize());
                    supplementInfoList.add(typeName);
                }
                if ("材质".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getMaterial());
                    supplementInfoList.add(typeName);
                }
                if ("尺寸".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getObjectSize());
                    supplementInfoList.add(typeName);
                }
                if ("尺码".equals(classifyTypeSonList.getName())){
                    String typeName = getName(voProPublic.getClothesSize());
                    supplementInfoList.add(typeName);
                }
                if ("官方指导价".equals(classifyTypeSonList.getName())){
                    supplementInfoList.add(voProPublic.getPublicPrice());
                }
            }
        });
//        String supplementInfo =supplementInfoList;
        return supplementInfoList;
    }

    /**
     * 判断字段是否有空格 去空格
     * @param typeName
     * @return
     */
    public String getName(String typeName){
        if (!LocalUtils.isEmptyAndNull(typeName)){
             String typeNameNew =typeName.replace(" ", "");
            if (LocalUtils.isEmptyAndNull(typeNameNew)){
                return null;
            }
        }
        return typeName;
    }
    @Override
    public List<ProPublic> listAllProPublic() {
        return proPublicMapper.listAllProPublic();
    }

    @Override
    public List<VoProPublic> querySerialNo(String name) {
        return proPublicMapper.querySerialNo(name);
    }

    @Override
    public List<VoProPublic> queryTypeNo(String name, String serialNo) {
        return proPublicMapper.queryTypeNo(name, serialNo);
    }

    @Override
    public VoProPublicByPageForAdmin getPublicByPageForAdmin(ParamPublicForAdmin publicForAdmin) {
        PageHelper.startPage(publicForAdmin.getPageNum(), publicForAdmin.getPageSize());
        List<VoProPublicForAdmin> list = proPublicMapper.getPublicByListForAdmin(publicForAdmin);
        VoProPublicByPageForAdmin proPublicByPageForAdmin = new VoProPublicByPageForAdmin();
        if (LocalUtils.isEmptyAndNull(list)) {
            return proPublicByPageForAdmin;
        }
        list.forEach(voProPublicForAdmin -> {


//            voProPublicForAdmin.setClassifyCode(proClassifySubService.getClassifyCodeName(voProPublicForAdmin.getClassifyCode()));
        });
        PageInfo<VoClassifyTypeSonList> pageInfo = new PageInfo(list);

        proPublicByPageForAdmin.setPageNum(pageInfo.getPageNum());
        proPublicByPageForAdmin.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            proPublicByPageForAdmin.setHasNextPage(true);
        } else {
            proPublicByPageForAdmin.setHasNextPage(false);
        }
        proPublicByPageForAdmin.setList(list);
        proPublicByPageForAdmin.setTotal(pageInfo.getTotal());
        return proPublicByPageForAdmin;
    }

    @Override
    public void addPublicForAdmin(ParamPublicAddForAdmin publicAddForAdmin) {

        ProPublic proPublic = new ProPublic();
        BeanUtils.copyProperties(publicAddForAdmin, proPublic);
        ProPublic proPublicOld = proPublicMapper.getByPublic(proPublic);
        if (proPublicOld != null) {
            throw new MyException("此公价商品已新增");
        }

        proPublic.setInsertTime(new Date());
        proPublic.setInsertAdmin(-9);
        proPublic.setDescription(proPublic.getName());
        proPublicMapper.saveObject(proPublic);


    }

    @Override
    public void updatePublicForAdmin(ParamPublicUpdateForAdmin publicUpdateForAdmin) {
        ProPublic proPublic = new ProPublic();
        BeanUtils.copyProperties(publicUpdateForAdmin, proPublic);
        if (!LocalUtils.isEmptyAndNull(proPublic.getName())) {
            proPublic.setDescription(proPublic.getName());
        }

        proPublic.setUpdateTime(new Date());
        proPublic.setUpdateAdmin(-9);
        proPublicMapper.updateObject(proPublic);
    }

    @Override
    public void deletePublicForAdmin(Integer id) {
        ProPublic proPublic = new ProPublic();
        proPublic.setState(-1);
        proPublic.setId(id);
        proPublicMapper.updateObject(proPublic);
    }
}
