package com.luxuryadmin.service.pro.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.mapper.org.OrgOrganizationTempMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProTempProductMapper;
import com.luxuryadmin.param.pro.ParamProTempProductQuery;
import com.luxuryadmin.param.pro.ParamProTempUpdate;
import com.luxuryadmin.param.pro.ParamTempProductOrgPageBySearch;
import com.luxuryadmin.service.pro.ProTempProductService;
import com.luxuryadmin.service.sys.SysEnumService;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.sys.VoSysEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2021-01-18 18:12:22
 */
@Slf4j
@Service
public class ProTempProductServiceImpl implements ProTempProductService {

    @Resource
    private ProTempProductMapper proTempProductMapper;

    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private ServicesUtil servicesUtil;
    @Autowired
    private SysEnumService sysEnumService;

    @Resource
    private OrgOrganizationTempMapper organizationTempMapper;

    @Override
    public ProTempProduct getProTempProductById(int shopId, int id) {
        return proTempProductMapper.getProTempProductById(shopId, id);
    }

    @Override
    public List<VoProductLoad> listVoProductLoad(ParamProTempProductQuery queryParam) {
        queryParam.setStateCode("all");
        return proProductMapper.listVoProductLoadByShopIdAndTempId(queryParam);
    }

    @Override
    public void addProTempProduct(int shopId, int userId, int tempId, String proIds) {
        //1.先查找这个临时仓的所有商品;
        List<Integer> tempProIdList = proTempProductMapper.listProTempIdByShopId(shopId, tempId);
        List<String> notAddProIdList = new ArrayList<>();
        String[] ids = LocalUtils.splitString(proIds, ",");
        //2.然后再排除已经添加过的proId;
        if (!LocalUtils.isEmptyAndNull(tempProIdList)) {
            for (String id : ids) {
                Integer notExistsProId = Integer.valueOf(id);
                if (!tempProIdList.contains(notExistsProId)) {
                    notAddProIdList.add(id);
                }
            }
        } else {
            notAddProIdList.addAll(Arrays.asList(ids));
        }
        //3.将剩下没有存在该临时仓的proId,添加进去该仓库;
        if (!LocalUtils.isEmptyAndNull(notAddProIdList)) {
            proTempProductMapper.saveBatchProTempProduct(shopId, userId, tempId, notAddProIdList);
        }

    }

    @Override
    public void deleteProTempProduct(int shopId, int proTempId, String proIds) {
        proIds = LocalUtils.packString(proIds.split(","));
        proTempProductMapper.deleteProTempProductByTempIdAndProId(shopId, proTempId, proIds);
    }

    @Override
    public void deleteAllProTempProductByTempId(int shopId, int proTempId) {
        proTempProductMapper.deleteAllProTempProductByTempId(shopId, proTempId);
    }

    @Override
    public void selectProductNum(Integer proId, Integer totalNum) {
        List<VoTempForPro> tempForPros = proTempProductMapper.selectProductNum(proId,totalNum);
        if (tempForPros != null && tempForPros.size() >0){
            List<String> tempNames = new ArrayList<>();
            tempForPros.forEach(tempForPro ->{
                tempNames.add(tempForPro.getTempName());
            });
            String tagNameList =  StringUtils.join(tempNames,"、");
            throw new MyException(tagNameList+"临时仓内该商品库存大于当前要修改库存");
        }
    }

    @Override
    public VoProductLoad getVoProductFromTempProduct(int shopId, int tempId, int proId) {
        return proTempProductMapper.getVoProductFromTempProduct(shopId, tempId, proId);
    }

    @Override
    public ProTempProduct getProTempProduct(int shopId, int tempId, int proId) {
        return proTempProductMapper.getProTempProduct(shopId, tempId, proId);
    }

    @Override
    public void updateProTempProduct(ParamProTempUpdate paramUpdate) {
        proTempProductMapper.updateProTempProductById(paramUpdate);
    }

    @Override
    public List<Integer> listProIdFromTempProduct(int shopId, int tempId) {
        return proTempProductMapper.listProIdFromTempProduct(shopId, tempId);
    }

    @Override
    public VoTempProductOrgByApp getTempProductOrgByApp(ParamTempProductOrgPageBySearch tempProductOrgPageBySearch) {
        PageHelper.startPage(Integer.parseInt(tempProductOrgPageBySearch.getPageNum()), tempProductOrgPageBySearch.getPageSize());
        List<VoTempProductOrgPageByApp> tempProductOrgPageByApps = proTempProductMapper.getTempProductOrgByApp(tempProductOrgPageBySearch);
        if (tempProductOrgPageByApps != null && tempProductOrgPageByApps.size() > 0) {
            tempProductOrgPageByApps.forEach(voOrganizationTempPageByApp -> {
//缩略图
                String smallImg = voOrganizationTempPageByApp.getSmallImg();
                voOrganizationTempPageByApp.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

                voOrganizationTempPageByApp.setShowSeat(voOrganizationTempPageByApp.getShowSeat()+"号展位");
            });
        }
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(tempProductOrgPageByApps);
        VoTempProductOrgByApp voTempProductOrgByApp = new VoTempProductOrgByApp();
        voTempProductOrgByApp.setPageNum(pageInfo.getPageNum());
        voTempProductOrgByApp.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voTempProductOrgByApp.setHasNextPage(true);
        } else {
            voTempProductOrgByApp.setHasNextPage(false);
        }
        voTempProductOrgByApp.setList(tempProductOrgPageByApps);

        Integer productCount = organizationTempMapper.getProductCount(tempProductOrgPageBySearch.getOrganizationId(), null);
        if (productCount == null) {
            voTempProductOrgByApp.setProductNum(0);
        } else {
            voTempProductOrgByApp.setProductNum(productCount);
        }
        Integer shopCount = organizationTempMapper.getShopCount(tempProductOrgPageBySearch.getOrganizationId(), null);
        if (shopCount == null) {
            voTempProductOrgByApp.setShopNum(0);
        } else {
            voTempProductOrgByApp.setShopNum(shopCount);
        }
        BigDecimal totalPrice = organizationTempMapper.getProductPrice(tempProductOrgPageBySearch.getOrganizationId(), null);
        if (totalPrice == null) {
            voTempProductOrgByApp.setProductPrice(new BigDecimal(0));
        } else {
            voTempProductOrgByApp.setProductPrice(totalPrice);
        }
        return voTempProductOrgByApp;
    }

    @Override
    public VoTempProductOrgByApp getTempProductOrgForShopByApp(ParamTempProductOrgPageBySearch tempProductOrgPageBySearch) {
        PageHelper.startPage(Integer.parseInt(tempProductOrgPageBySearch.getPageNum()), tempProductOrgPageBySearch.getPageSize());
        List<VoTempProductOrgPageByApp> tempProductOrgPageByApps = proTempProductMapper.getTempProductOrgByApp(tempProductOrgPageBySearch);
        VoTempProductOrgByApp voTempProductOrgForShopByApp = new VoTempProductOrgByApp();
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(tempProductOrgPageByApps);

        voTempProductOrgForShopByApp.setPageNum(pageInfo.getPageNum());
        voTempProductOrgForShopByApp.setPageSize(pageInfo.getPageSize());
        if (tempProductOrgPageByApps == null || tempProductOrgPageByApps.size() <= 0) {
            voTempProductOrgForShopByApp.setHasNextPage(false);
            return voTempProductOrgForShopByApp;
        }

        tempProductOrgPageByApps.forEach(voOrganizationTempPageByApp -> {
            voOrganizationTempPageByApp.setShowSeat(voOrganizationTempPageByApp.getShowSeat()+"号展位");
            //缩略图
            String smallImg = voOrganizationTempPageByApp.getSmallImg();
            if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                voOrganizationTempPageByApp.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
            }
        });

        if (pageInfo.getNextPage() > 0) {
            voTempProductOrgForShopByApp.setHasNextPage(true);
        } else {
            voTempProductOrgForShopByApp.setHasNextPage(false);
        }
        voTempProductOrgForShopByApp.setShopName(tempProductOrgPageByApps.get(0).getShopName());
        voTempProductOrgForShopByApp.setList(tempProductOrgPageByApps);
        return voTempProductOrgForShopByApp;
    }

    @Override
    public VoTempProductOrgDetailByApp getTempProductOrgDetail(Integer tempProductId) {
        VoTempProductOrgDetailByApp voTempProductOrgDetailByApp = proTempProductMapper.getTempProductOrgDetail(tempProductId);
        if (voTempProductOrgDetailByApp == null) {
            return null;
        }
        //商品图片
        String productImg = voTempProductOrgDetailByApp.getProductImg();
        if (!LocalUtils.isEmptyAndNull(productImg)) {
            String[] productImgArray = productImg.split(";");
            for (int i = 0; i < productImgArray.length; i++) {
                if (!productImgArray[i].contains("http")) {
                    productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                }
            }
            voTempProductOrgDetailByApp.setProductImgList(productImgArray);
        }
        return voTempProductOrgDetailByApp;
    }

    @Override
    public VoTempProductOrgByApplets getTempProductOrgByApplets(ParamTempProductOrgPageBySearch paramTempProductOrgPageBySearch) {
        PageHelper.startPage(Integer.parseInt(paramTempProductOrgPageBySearch.getPageNum()), paramTempProductOrgPageBySearch.getPageSize());

        List<VoTempProductOrgPageByApplets> tempProductOrgPageByApps = proTempProductMapper.getTempProductOrgByApplets(paramTempProductOrgPageBySearch);
        if (tempProductOrgPageByApps != null && tempProductOrgPageByApps.size() > 0) {
            tempProductOrgPageByApps.forEach(voOrganizationTempPageByApp -> {
                voOrganizationTempPageByApp.setShowSeat(voOrganizationTempPageByApp.getShowSeat()+"号展位");
                //缩略图
                String smallImg = voOrganizationTempPageByApp.getSmallImg();

                voOrganizationTempPageByApp.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

            });

        }
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(tempProductOrgPageByApps);
        VoTempProductOrgByApplets voTempProductOrgByApplets = proTempProductMapper.getByTempId(paramTempProductOrgPageBySearch.getTempId());
        if (voTempProductOrgByApplets == null) {
            voTempProductOrgByApplets = new VoTempProductOrgByApplets();
        }
        voTempProductOrgByApplets.setPageNum(pageInfo.getPageNum());
        voTempProductOrgByApplets.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            voTempProductOrgByApplets.setHasNextPage(true);
        } else {
            voTempProductOrgByApplets.setHasNextPage(false);
        }
        if (!LocalUtils.isEmptyAndNull(voTempProductOrgByApplets.getHeadImgUrl()) && !voTempProductOrgByApplets.getHeadImgUrl().contains("http")) {
            voTempProductOrgByApplets.setHeadImgUrl(servicesUtil.formatImgUrl(voTempProductOrgByApplets.getHeadImgUrl(), true));
        }
        voTempProductOrgByApplets.setList(tempProductOrgPageByApps);
        List<VoSysEnum> sysEnums = sysEnumService.listVoSysEnum("pro_classify");
        voTempProductOrgByApplets.setSysEnums(sysEnums);
        voTempProductOrgByApplets.setCoverImgUrl(servicesUtil.formatImgUrl(voTempProductOrgByApplets.getCoverImgUrl(), false));
        return voTempProductOrgByApplets;
    }

    /**
     * 查询临时仓商品列表2.5.2--mong
     * @param queryParam
     * @return
     */
    @Override
    public List<VoProductLoad> listVoProductLoadByPrice(ParamProTempProductQuery queryParam) {
        queryParam.setStateCode("all");
        return proProductMapper.listVoProductLoadByPriceByPrice(queryParam);
    }

    @Override
    public Map<String, Object> getProductLoadByPriceByPrice(ParamProTempProductQuery queryParam) {
        return proProductMapper.getProductLoadByPriceByPrice(queryParam);
    }

    /**
     * 根据临时仓id，修改临时仓商品价格为null
     * @param proTempId
     */
    @Override
    public void updateSalePriceByTempId(String proTempId) {
        proProductMapper.updateSalePriceByTempId(proTempId);
    }

    /**
     * 根据商品id查询商品数量
     * @param bizId
     * @return
     */
    @Override
    public Integer getShopNumById(String bizId) {
        return proProductMapper.getShopNumById(bizId);
    }

    @Override
    public VoProTempProduct getNewProTempProduct(int shopId, int tempId, int proId) {
        return proTempProductMapper.getNewProTempProduct(shopId,tempId,proId);
    }

    /**
     * 修改临时仓默认价格类型
     * @param priceType
     * @param proTempId
     */
    @Override
    public void updateTempPriceTypeById(String priceType, String proTempId) {
        proTempProductMapper.updateTempPriceTypeById(priceType,proTempId);
    }

    @Override
    public String getTempProState(int shopId, int tempId, int proId) {
        ProProduct product =(ProProduct)proProductMapper.getObjectById(proId);
        if (product == null){
            throw new MyException("该商品不存在！");
        }
        if (product.getTotalNum()<=0){
            return "2";
        }
        ProTempProduct tempProduct= proTempProductMapper.getProTempProduct(shopId,tempId,proId);
        if (tempProduct == null){
            throw new MyException("该临时仓商品不存在！");
        }
        if (tempProduct.getNum() != null && tempProduct.getNum()==0){
            return "1";
        }
        return "0";
    }

    @Override
    public void updateTempProductCount(ProTempProduct tempProduct) {
        proTempProductMapper.updateObject(tempProduct);
    }

}
