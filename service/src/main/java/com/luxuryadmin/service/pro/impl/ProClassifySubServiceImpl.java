package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.PinYinHead;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProClassifySub;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.mapper.pro.ProClassifySubMapper;
import com.luxuryadmin.mapper.pro.ProSeriesModelMapper;
import com.luxuryadmin.mapper.pro.ProSubSeriesMapper;
import com.luxuryadmin.mapper.sys.SysEnumMapper;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.vo.pro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vdurmont.emoji.EmojiParser;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: Mong
 * @Date: 2021/5/27 15:23
 * @Description: 商品二级分类逻辑层
 */
@Slf4j
@Service
public class ProClassifySubServiceImpl implements ProClassifySubService {

    @Resource
    private ProClassifySubMapper proClassifySubMapper;
    @Resource
    private ProClassifyMapper classifyMapper;
    @Resource
    private SysEnumMapper sysEnumMapper;
    @Autowired
    private ServicesUtil servicesUtil;
    @Resource
    private ProSubSeriesMapper proSubSeriesMapper;
    @Resource
    private ProSeriesModelMapper proSeriesModelMapper;
    @Override
    public List<VoProClassifySub> listAllProClassifySub(ParamProClassifySubQuery paramProClassifySubQuery) {
        Map<String, Object> map = new HashMap<>(16);
        //查询一级分类和店铺下的所有二级分类
        List<VoProClassifySub> voProClassifySubs = proClassifySubMapper.listProClassifySub(paramProClassifySubQuery.getClassifyCode(), paramProClassifySubQuery.getShopId());
        voProClassifySubs.forEach(voProClassifySub -> {
            //对emoji表情的处理
            voProClassifySub.setName(EmojiParser.parseToUnicode(voProClassifySub.getName()));
        });
//        if (voProClassifySubs != null) {
//            //遍历a-z字母
//            for (char c = 'A'; c <= 'Z'; c++) {
//                String letter = c + "";
//                List<VoProClassifySub> voProClassifySubArrayList = new ArrayList<>();
//                //遍历所有二级分类
//                for (int i = 0; i < voProClassifySubs.size(); i++) {
//                    VoProClassifySub voProClassifySub = voProClassifySubs.get(i);
//                    //判断分类首字母是否匹配遍历字母
//                    if (letter.equalsIgnoreCase(voProClassifySub.getLetter())) {
//                        voProClassifySubArrayList.add(voProClassifySub);
//                    }
//                }
//                //添加所匹配的二级分类到map，key为首字母，value为二级分类集合
//                if (voProClassifySubArrayList != null && voProClassifySubArrayList.size()>0 ){
//                    map.put(letter, voProClassifySubArrayList);
//                }
//            }
//
//        }
//        log.info("结果是"+map);
        formatProClassifySub(voProClassifySubs);

        return voProClassifySubs;
    }


    /**
     * 根据一级分类id查询二级分类
     *
     * @param paramQuery
     * @return
     */
    @Override
    public List<VoProClassifySub> listProClassifySubPage(ParamProClassifySubQuery paramQuery) {
        List<VoProClassifySub> classifySubList =proClassifySubMapper.listProClassifySubPage(paramQuery);
        if (LocalUtils.isEmptyAndNull(classifySubList)){
            return classifySubList;
        }
        classifySubList.forEach(voProClassifySub ->{
            //对emoji表情的处理
            voProClassifySub.setName(EmojiParser.parseToUnicode(voProClassifySub.getName()));
        });
        formatProClassifySub(classifySubList);
        return classifySubList;
    }

    /**
     * 添加二级分类
     *
     * @param paramAdd
     */
    @Override
    public void addProClassifySub(ParamProClassifySubAdd paramAdd) {
        ProClassifySub classifySub = new ProClassifySub();
        //set code为分类名称首字母大写拼接
        classifySub.setCode(PinYinHead.getPinYinHeadChar(paramAdd.getName()));
        classifySub.setName(paramAdd.getName());
        classifySub.setDescription(paramAdd.getName());
        //set type为0，0：系统自带，1：用户自建
        classifySub.setType("0");
        //set shopId为99，商铺通用id
        classifySub.setFkShpShopId(-9);
        classifySub.setState(1);
        classifySub.setIconUrl(paramAdd.getIconUrl());
        classifySub.setFkProClassifyCode(paramAdd.getClassifyCode());
//        classifySub.setFkProClassifyId(Integer.parseInt(paramAdd.getClassifyId()));
        classifySub.setInsertTime(new Date());
        classifySub.setInsertAdmin(-9);
        classifySub.setSort(paramAdd.getSort());
        proClassifySubMapper.saveObject(classifySub);
    }

    @Override
    public void addProClassifySubForApi(ParamClassifySubAddForApi classifySubAddForApi) {
        ProClassifySub classifySub = new ProClassifySub();
        VoProClassify classify =null;
        if (!LocalUtils.isEmptyAndNull(classifySubAddForApi.getClassifyCode())){
             classify = classifyMapper.getProClassifyByType(classifySubAddForApi.getShopId(), null, classifySubAddForApi.getClassifyCode());
            if (classify == null) {
                throw new MyException("暂无此一级分类");
            }
        }
        List<ProClassifySub>  classifySubList=proClassifySubMapper.getClassifySubListByName(classifySubAddForApi.getName());
        if (!LocalUtils.isEmptyAndNull(classifySubList)){
            classifySubList.forEach(proClassifySub -> {
                proClassifySub.setDel("1");
                proClassifySubMapper.updateObject(proClassifySub);
            });
        }
        //set code为分类名称首字母大写拼接
        classifySub.setCode(PinYinHead.getPinYinHeadChar(classifySubAddForApi.getName()));
        classifySub.setDescription(classifySubAddForApi.getName());
        classifySub.setName(classifySubAddForApi.getName());
        //set type为0，0：系统自带，1：用户自建
        classifySub.setType("1");
        //set shopId为99，商铺通用id
        classifySub.setFkShpShopId(classifySubAddForApi.getShopId());
        classifySub.setState(1);
        classifySub.setFkProClassifyCode("QT");
        if (classify != null) {
            classifySub.setFkProClassifyId(classify.getId());
        }else {
            classifySub.setFkProClassifyId(-9);
        }

        classifySub.setInsertTime(new Date());
        classifySub.setInsertAdmin(classifySubAddForApi.getUserId());
        proClassifySubMapper.saveObject(classifySub);
    }

    /**
     * 修改二级分类
     *
     * @param paramUpdate
     */
    @Override
    public void updateProClassifySub(ParamProClassifySubUpdate paramUpdate) {
        ProClassifySub classifySub = new ProClassifySub();
        classifySub.setId(Integer.parseInt(paramUpdate.getId()));
        classifySub.setFkProClassifyCode(paramUpdate.getClassifyCode());
        classifySub.setFkShpShopId(-9);
        //set code为分类名称首字母大写拼接
        classifySub.setIconUrl(paramUpdate.getIconUrl());
        classifySub.setCode(PinYinHead.getPinYinHeadChar(paramUpdate.getName()));
        classifySub.setDescription(paramUpdate.getName());
        classifySub.setName(paramUpdate.getName());
        classifySub.setState(paramUpdate.getState());
        classifySub.setSort(paramUpdate.getSort());
        classifySub.setRemark(paramUpdate.getRemark());
        proClassifySubMapper.updateObject(classifySub);
    }

    @Override
    public void updateProClassifySubForState(ParamProClassifySubUpdate paramUpdate) {
        ProClassifySub classifySubOld = (ProClassifySub)proClassifySubMapper.getObjectById(paramUpdate.getId());
        if (classifySubOld ==null){
            return;
        }
        if ("1".equals(paramUpdate.getState())){
            List<ProClassifySub>  classifySubList=proClassifySubMapper.getClassifySubListByName(classifySubOld.getName());
            if (!LocalUtils.isEmptyAndNull(classifySubList) ){
                classifySubList.forEach(proClassifySub -> {
                    proClassifySub.setDel("1");
                    proClassifySubMapper.updateObject(proClassifySub);
                });
            }
        }

        ProClassifySub classifySub = new ProClassifySub();
        classifySub.setId(Integer.parseInt(paramUpdate.getId()));
        classifySub.setState(paramUpdate.getState());
        proClassifySubMapper.updateObject(classifySub);
    }

    @Override
    public void updateProClassifySubForApi(ParamClassifySubUpdateForApi classifySubUpdateForApi) {
        ProClassifySub classifySub = new ProClassifySub();
        //set code为分类名称首字母大写拼接
        classifySub.setCode(PinYinHead.getPinYinHeadChar(classifySubUpdateForApi.getName()));
        classifySub.setDescription(classifySubUpdateForApi.getName());
        classifySub.setId(Integer.parseInt(classifySubUpdateForApi.getId()));
        classifySub.setName(classifySubUpdateForApi.getName());
        classifySub.setUpdateAdmin(classifySubUpdateForApi.getUserId());
        classifySub.setUpdateTime(new Date());
        proClassifySubMapper.updateObject(classifySub);
    }

    /**
     * 删除二级分类
     *
     * @param id
     */
    @Override
    public void deleteProClassifySub(String id) {
        ProClassifySub classifySubOld =(ProClassifySub)proClassifySubMapper.getObjectById(id);
        ProClassifySub classifySub = new ProClassifySub();
        classifySub.setId(Integer.parseInt(id));
        classifySub.setDel("1");
        proClassifySubMapper.updateObject(classifySub);
        if (classifySubOld != null){
            proSubSeriesMapper.deleteByClassifySub(classifySubOld.getName());
            proSeriesModelMapper.deleteSeriesModelClassifySub(classifySubOld.getName(),null);
        }

    }

    /**
     * 根据名称查询二级分类
     *
     * @param name
     * @return
     */
    @Override
    public VoProClassifySub getProClassifySubByName(String name, Integer shopId) {
        return proClassifySubMapper.getProClassifySubByName(name, shopId);
    }

    @Override
    public VoProClassifySub getProClassifySubByNameForAdmin(String name) {
        return proClassifySubMapper.getProClassifySubByName(name, null);
    }

    /**
     * 根据id查询二级跟雷
     *
     * @param id
     */
    @Override
    public VoProClassifySub getProClassifySubById(String id) {
        return proClassifySubMapper.getProClassifySubById(id);
    }

    @Override
    public List<VoProClassifySub> listAllProClassifySub(String classifyName) {
        List<VoProClassifySub> list = proClassifySubMapper.listAllProClassifySub(classifyName);
        formatProClassifySub(list);
        return list;
    }

    @Override
    public VoProClassifySubPageForAdmin getClassifySubPageForAdmin(ParamProClassifySubQuery paramQuery) {
        //添加分页
        PageHelper.startPage(Integer.parseInt(paramQuery.getPageNum()), paramQuery.getPageSize());
        List<VoProClassifySubForAdmin> list =proClassifySubMapper.getClassifySubPageForAdmin(paramQuery);
        VoProClassifySubPageForAdmin classifySubPageForAdmin = new VoProClassifySubPageForAdmin();
        //判断是否为空
        if (LocalUtils.isEmptyAndNull(list)){
            return classifySubPageForAdmin;
        }
        //遍历赋值品牌类型
        list.forEach(classifySubForAdmin -> {
            classifySubForAdmin.setClassifyCodeName(getClassifyCodeName(classifySubForAdmin.getClassifyCode()));
        });
        PageInfo pageInfo = new PageInfo(list);
        classifySubPageForAdmin.setPageNum(pageInfo.getPageNum());
        classifySubPageForAdmin.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            classifySubPageForAdmin.setHasNextPage(true);
        } else {
            classifySubPageForAdmin.setHasNextPage(false);
        }
        classifySubPageForAdmin.setList(list);
        classifySubPageForAdmin.setTotal(pageInfo.getTotal());
        return classifySubPageForAdmin;
    }

    @Override
    public String getClassifyCodeName(String classifyCode) {
        if (LocalUtils.isEmptyAndNull(classifyCode)){
            return null;
        }
        //根据逗号分隔转化为list
        List<String> codeList = Arrays.asList(classifyCode.split(","));
        List<String> nameList = new ArrayList<>();
        codeList.forEach(code ->{
            VoProClassify classify =   sysEnumMapper.getProClassifyByCode(code);
            if (classify != null){
                nameList.add(classify.getName());
            }
        });
        if (LocalUtils.isEmptyAndNull(nameList)){
            return null;
        }
        String names =  StringUtils.join(nameList,",");
        return names;
    }

    /**
     * 格式化查询
     *
     * @param list
     */
    private void formatProClassifySub(List<VoProClassifySub> list) {
        if (!LocalUtils.isEmptyAndNull(list)) {
            for (VoProClassifySub claSub : list) {
                String iconUrl = claSub.getIconUrl();
                if (!LocalUtils.isEmptyAndNull(iconUrl)) {
                    claSub.setIconUrl(servicesUtil.formatImgUrl(iconUrl));
                }
                if (!claSub.getName().equals(claSub.getDescription()) && !LocalUtils.isEmptyAndNull(claSub.getDescription())) {
                    claSub.setShowName(EmojiParser.parseToUnicode(claSub.getDescription()) + "/" + claSub.getName());
                } else {
                    claSub.setShowName(claSub.getName());
                }
            }
        }
    }
}
