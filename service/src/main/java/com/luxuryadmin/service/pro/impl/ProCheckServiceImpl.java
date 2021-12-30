package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProTemp;
import com.luxuryadmin.mapper.pro.ProCheckMapper;
import com.luxuryadmin.mapper.pro.ProCheckProductMapper;
import com.luxuryadmin.mapper.pro.ProClassifyMapper;
import com.luxuryadmin.param.pro.ParamCheckListForApiBySearch;
import com.luxuryadmin.param.pro.ParamCheckProductAddList;
import com.luxuryadmin.param.pro.ParamCheckUpdateForApi;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.vo.pro.VoCheckListForApi;
import com.luxuryadmin.vo.pro.VoProClassify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProCheckServiceImpl implements ProCheckService {
    @Resource
    private ProCheckMapper checkMapper;
    @Resource
    private ProClassifyMapper classifyMapper;
    @Resource
    private ProCheckProductMapper checkProductMapper;
    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProTempService proTempService;
    @Override
    public void addCheck(ParamCheckProductAddList checkProductAddList) {
        ProCheck check = new ProCheck();
        BeanUtils.copyProperties(checkProductAddList, check);
        check.setInsertAdmin(checkProductAddList.getFkShpUserId());
        check.setCheckState("10");
        check.setInsertTime(new Date());

        try {
            if(checkProductAddList.getStartTime() != null){
                check.setStartTime(DateUtil.parseHHMM(checkProductAddList.getStartTime()));
            }
            if(checkProductAddList.getEndTime() != null){
                check.setEndTime(DateUtil.parseHHMM(checkProductAddList.getEndTime()));
            }
        } catch (ParseException e) {

            throw new MyException("新增盘点时间错误为"+e);
        }
        List<String> idList = Arrays.asList(checkProductAddList.getFkProAttributeCodes().split(","));
        String type = "全部";
        if (!LocalUtils.isEmptyAndNull(checkProductAddList.getFkProClassifyCodes())){
            List<String> classifyCodes = Arrays.asList(checkProductAddList.getFkProClassifyCodes().split(","));
            List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(checkProductAddList.getFkShpShopId(), "1");
            if (classifyCodes.size() != voProClassifyList.size() ) {
                type="分类";
            }
        }
        if (LocalUtils.isEmptyAndNull(checkProductAddList.getCheckName()) ) {
            if (idList.size() >= 3) {
                check.setCheckName("盘点"+type+"商品");
            } else {
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
                String fkProAttributeCodeName = StringUtils.join(fkProAttributeCodes, "、");

                check.setCheckName("盘点" + fkProAttributeCodeName + "的"+type+"商品");
            }
        }
        checkMapper.saveObject(check);

        checkProductAddList.setFkProAttributeCodeList(idList);
        checkProductAddList.setFkProCheckId(check.getId());
        if (!LocalUtils.isEmptyAndNull(checkProductAddList.getFkProClassifyCodes())){
            List<String> classifyCodes = Arrays.asList(checkProductAddList.getFkProClassifyCodes().split(","));
            checkProductAddList.setFkProClassifyCodeList(classifyCodes);
        }else {
            checkProductAddList.setFkProClassifyCodeList(null);
        }
        checkProductMapper.addCheckProductList(checkProductAddList);
    }

    @Override
    public void addCheckForTemp(ParamCheckProductAddList checkProductAddList) {
        ProCheck check = new ProCheck();
        BeanUtils.copyProperties(checkProductAddList, check);
        check.setInsertAdmin(checkProductAddList.getFkShpUserId());
        check.setCheckState("10");
        check.setInsertTime(new Date());

        ProTemp temp = proTempService.getTempById(Integer.parseInt(checkProductAddList.getTempId()));
        if (temp == null) {
            throw new MyException("临时仓不存在！");
        }
        check.setCheckName("盘点临时仓:" + temp.getName());
        check.setFkProTempId(Integer.parseInt(checkProductAddList.getTempId()));
        checkMapper.saveObject(check);

        //新增临时仓盘点商品
        checkProductAddList.setFkProCheckId(check.getId());
        checkProductMapper.addCheckTempProductList(checkProductAddList);
    }

    @Override
    public List<VoCheckListForApi> getCheckListForApi(ParamCheckListForApiBySearch checkListForApiBySearch) {
        checkMapper.updateCheckState(checkListForApiBySearch.getFkShpShopId());
        List<VoCheckListForApi> voCheckListForApis = checkMapper.getCheckListForApi(checkListForApiBySearch);
        voCheckListForApis.forEach(voCheckListForApi -> {
          if ("temp".equals(voCheckListForApi.getType())){
              getTempCheckCount(voCheckListForApi);
              voCheckListForApi.setTypeName("临时仓盘点");
          }else {
              getCheckCount(voCheckListForApi);
              voCheckListForApi.setTypeName("仓库盘点");
          }
        });
        return voCheckListForApis;
    }

    public void getCheckCount(VoCheckListForApi voCheckListForApi){
        //总数
        Integer totalCount = checkProductMapper.getCountBySearch(voCheckListForApi.getId(), null, null, null);
        if (totalCount == null) {
            voCheckListForApi.setTotalCount(0);
        } else {
            voCheckListForApi.setTotalCount(totalCount);
        }
        //已经盘点数量
        Integer overCount = checkProductMapper.getCountBySearch(voCheckListForApi.getId(), null, "yes", null);
        if (overCount == null) {
            voCheckListForApi.setOverCount(0);
        } else {
            voCheckListForApi.setOverCount(overCount);
        }
        //已经盘点的商品存在的数量
        Integer haveCount = checkProductMapper.getCountBySearch(voCheckListForApi.getId(), "1", "yes", null);
        if (haveCount == null) {
            voCheckListForApi.setHaveCount(0);
        } else {
            voCheckListForApi.setHaveCount(haveCount);
        }
        //已经盘点的商品缺失的数量
        Integer notCount = checkProductMapper.getCountBySearch(voCheckListForApi.getId(), "0", "yes", null);
        if (notCount == null) {
            voCheckListForApi.setNotCount(0);
        } else {
            voCheckListForApi.setNotCount(notCount);
        }
    }
    public void getTempCheckCount(VoCheckListForApi voCheckListForApi){
        //总数
        Integer totalCount = checkProductMapper.getTempCountBySearch(voCheckListForApi.getId(), null, null, voCheckListForApi.getTempId());
        if (totalCount == null) {
            voCheckListForApi.setTotalCount(0);
        } else {
            voCheckListForApi.setTotalCount(totalCount);
        }
        //已经盘点数量
        Integer overCount = checkProductMapper.getTempCountBySearch(voCheckListForApi.getId(), null, "yes", voCheckListForApi.getTempId());
        if (overCount == null) {
            voCheckListForApi.setOverCount(0);
        } else {
            voCheckListForApi.setOverCount(overCount);
        }
        //已经盘点的商品存在的数量
        Integer haveCount = checkProductMapper.getTempCountBySearch(voCheckListForApi.getId(), "1", "yes", voCheckListForApi.getTempId());
        if (haveCount == null) {
            voCheckListForApi.setHaveCount(0);
        } else {
            voCheckListForApi.setHaveCount(haveCount);
        }
        //已经盘点的商品缺失的数量
        Integer notCount = checkProductMapper.getTempCountBySearch(voCheckListForApi.getId(), "0", "yes", voCheckListForApi.getTempId());
        if (notCount == null) {
            voCheckListForApi.setNotCount(0);
        } else {
            voCheckListForApi.setNotCount(notCount);
        }
    }
    @Override
    public void updateCheckState(ParamCheckUpdateForApi checkUpdateForApi) {

        checkMapper.updateCheck(checkUpdateForApi.getId(), checkUpdateForApi.getCheckState());
    }

    @Override
    public ProCheck getById(Integer id) {
        if (id == null || id == 0) {
            return null;
        }
        return checkMapper.getById(id);
    }

    @Override
    public void deleteCheck(Integer id) {
        checkMapper.deleteCheck(id);
        checkProductMapper.deleteByCheckId(id);
    }

    @Override
    public void deleteProCheckByShopId(int shopId) {
        checkProductMapper.deleteProCheckProductByShopId(shopId);
        checkMapper.deleteProCheckByShopId(shopId);
    }

    @Override
    public void getStateByShopId(Integer shopId) {
        ProCheck check =  checkMapper.getByShopId(shopId);
        if (check != null){
            throw new MyException("盘点中，只允许查看操作");
        }

    }

    @Override
    public List<VoProClassify> getClassifyList(Integer shopId, Integer checkId) {
        ProCheck check = checkMapper.getById(checkId);
        if (check == null) {
            throw new MyException("此盘点不存在");
        }

        if (check.getFkProClassifyCodes() != null) {
            List<VoProClassify> classifyCodeList = new ArrayList<>();
            //商品种类集合显示
            List<String> classifyCodes = Arrays.asList(check.getFkProClassifyCodes().split(","));

            classifyCodes.forEach(classify -> {
                VoProClassify voProClassify = classifyMapper.getProClassifyByType(shopId, "1", classify);
                if (!LocalUtils.isEmptyAndNull(voProClassify)){
                    classifyCodeList.add(voProClassify);
                }

            });
            return classifyCodeList;
        }else {
            List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
            return voProClassifyList;
        }
    }

}
