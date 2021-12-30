package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProCheckProduct;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.mapper.pro.ProCheckMapper;
import com.luxuryadmin.mapper.pro.ProCheckProductMapper;
import com.luxuryadmin.param.pro.ParamCheckProductListForApiBySearch;
import com.luxuryadmin.param.pro.ParamCheckProductUpdateStateForApi;
import com.luxuryadmin.service.pro.ProCheckProductService;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.pro.ProTempService;
import com.luxuryadmin.vo.pro.VoCheckProductDetailByApi;
import com.luxuryadmin.vo.pro.VoCheckProductListForApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProCheckProductServiceImpl implements ProCheckProductService {

    @Resource
    private ProCheckProductMapper checkProductMapper;
    @Autowired
    protected ServicesUtil servicesUtil;
    @Autowired
    private ProTempService proTempService;
    @Autowired
    private ProTempProductService proTempProductService;
    @Resource
    private ProCheckMapper checkMapper;
    @Override
    public Map<String, Object> getCheckProductListForApi(ParamCheckProductListForApiBySearch checkProductListForApiBySearch) {
        ProCheck check =  checkMapper.getById(Integer.parseInt(checkProductListForApiBySearch.getFkProCheckId()));
        if (check != null && check.getFkProTempId() != null){
            checkProductListForApiBySearch.setTempId(check.getFkProTempId());
        }
        checkProductListForApiBySearch.setUniqueCode(checkProductListForApiBySearch.getProductName());
        PageHelper.startPage(checkProductListForApiBySearch.getPageNum(), checkProductListForApiBySearch.getPageSize());
        List<VoCheckProductListForApi> voCheckProductListForApis = checkProductMapper.getCheckProductListForApi(checkProductListForApiBySearch);
        if (voCheckProductListForApis != null && voCheckProductListForApis.size() > 0) {
            voCheckProductListForApis.forEach(voCheckProductListForApi -> {
                //缩略图
                String smallImg = voCheckProductListForApi.getSmallImg();
                voCheckProductListForApi.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
                //查看商品属性(长属性和短属性)
                voCheckProductListForApi.setAttributeShortCn(servicesUtil.getAttributeCn(voCheckProductListForApi.getAttributeShortCn(), false));

                //临时仓商品状态 是否已售罄 去掉
                if (!LocalUtils.isEmptyAndNull(voCheckProductListForApi.getTempId())){
//                    String tempProState =proTempProductService.getTempProState(checkProductListForApiBySearch.getShopId(),voCheckProductListForApi.getTempId(), voCheckProductListForApi.getFkProProductId());
//                    voCheckProductListForApi.setTempProState(tempProState);
//                    if ("1".equals(tempProState)){
//                        voCheckProductListForApi.setTempProStateName("本仓已售罄");
//                    }
//                    if ("2".equals(tempProState)){
//                        voCheckProductListForApi.setTempProStateName("仓库无库存");
//                    }
                }else {
                    String tempName = proTempService.getTempName(checkProductListForApiBySearch.getShopId(), voCheckProductListForApi.getFkProProductId());
                    voCheckProductListForApi.setTempName(tempName);
                }

            });
        }
        PageInfo pageInfo = new PageInfo(voCheckProductListForApis);
        Map<String, Object> objectMap = new HashMap<>(16);

        //已经盘点存在数量
        Integer existenceCount = checkProductMapper.getCountBySearch(Integer.parseInt(checkProductListForApiBySearch.getFkProCheckId()), "1", "yes", checkProductListForApiBySearch.getFkProClassifyCode());
        if (existenceCount == null) {
            objectMap.put("existenceCount", 0);

        } else {
            objectMap.put("existenceCount", existenceCount);
        }
        //已经盘点缺失数量
        Integer defectCount = checkProductMapper.getCountBySearch(Integer.parseInt(checkProductListForApiBySearch.getFkProCheckId()), "0", "yes", checkProductListForApiBySearch.getFkProClassifyCode());
        if (defectCount == null) {
            objectMap.put("defectCount", 0);

        } else {
            objectMap.put("defectCount", defectCount);
        }
        objectMap.put("list", voCheckProductListForApis);
        objectMap.put("pageNum", pageInfo.getPageNum());
        objectMap.put("pageSize", pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            objectMap.put("hasNextPage", true);
        } else {
            objectMap.put("hasNextPage", false);
        }
        return objectMap;
    }

    @Override
    public VoCheckProductDetailByApi getCheckProductDetailByApi(Integer id) {
        VoCheckProductDetailByApi voCheckProductDetailByApi = checkProductMapper.getCheckProductDetailByApi(id);
        //商品图片
        String productImg = voCheckProductDetailByApi.getProductImg();
        if (!LocalUtils.isEmptyAndNull(productImg)) {
            String[] productImgArray = productImg.split(";");
            for (int i = 0; i < productImgArray.length; i++) {
                if (!productImgArray[i].contains("http")) {
                    productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                }
            }
            voCheckProductDetailByApi.setProductImgList(productImgArray);
        }
        //缩略图
        String smallImg = voCheckProductDetailByApi.getSmallImg();
        voCheckProductDetailByApi.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
        return voCheckProductDetailByApi;
    }

    @Override
    public void updateCheckProduct(ParamCheckProductUpdateStateForApi checkProductUpdateStateForApi) {
        checkProductMapper.updateCheckProduct(checkProductUpdateStateForApi.getId(), "yes", checkProductUpdateStateForApi.getCheckType(), checkProductUpdateStateForApi.getRemark(), checkProductUpdateStateForApi.getUserId());
    }

    @Override
    public ProCheckProduct getById(Integer id) {
        if (id == null || id == 0) {
            return null;
        }
        ProCheckProduct proCheckProduct = checkProductMapper.getCheckProductById(id);
        return proCheckProduct;
    }

    @Override
    public VoCheckProductListForApi getCheckProductForApi(String bizId, Integer fkProCheckId) {
        VoCheckProductListForApi checkProductForApi = checkProductMapper.getCheckProductForApi(bizId, fkProCheckId);
        if (checkProductForApi == null) {
            return null;
        }
        //缩略图
        String smallImg = checkProductForApi.getSmallImg();
        checkProductForApi.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
        //查看商品属性(长属性和短属性)
        checkProductForApi.setAttributeShortCn(servicesUtil.getAttributeCn(checkProductForApi.getAttributeShortCn(), false));
        return checkProductForApi;
    }

    @Override
    public Map<String, Integer> getCheckProductCount(Integer fkProCheckId) {
        Map<String, Integer> integerMap = new HashMap<>();
        //已经盘点数量
        Integer haveCount = checkProductMapper.getCountBySearch(fkProCheckId, null, "yes", null);
        if (haveCount == null) {
            integerMap.put("haveCount", 0);

        } else {
            integerMap.put("haveCount", haveCount);
        }
        //未盘点数量
        Integer notCount = checkProductMapper.getCountBySearch(fkProCheckId, null, "no", null);
        if (notCount == null) {
            integerMap.put("notCount", 0);

        } else {
            integerMap.put("notCount", notCount);
        }

        return integerMap;
    }
}
