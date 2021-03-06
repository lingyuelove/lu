package com.luxuryadmin.service.biz.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.biz.BizShopUnion;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.mapper.biz.BizShopUnionMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamShopUnionAdd;
import com.luxuryadmin.param.biz.ParamShopUnionDelete;
import com.luxuryadmin.param.biz.ParamShopUnionForAdminBySearch;
import com.luxuryadmin.param.op.ParamAppBannerQuery;
import com.luxuryadmin.param.pro.ParamProProduct;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.op.OpBannerService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminList;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminPage;
import com.luxuryadmin.vo.op.VoOpBanner;
import com.luxuryadmin.vo.op.VoOpBannerForUnion;
import com.luxuryadmin.vo.org.VoOrganizationPageByApp;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.pro.VoProductLoad;
import com.luxuryadmin.vo.pro.VoProductLoadForUnionPage;
import com.luxuryadmin.vo.shp.VoShpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * biz_shop_union serverImpl
 *
 * @author zhangsai
 * @Date 2021-07-16 17:58:54
 */
@Service
public class BizShopUnionServiceImpl implements BizShopUnionService {


    /**
     * ??????dao
     */
    @Resource
    private BizShopUnionMapper bizShopUnionMapper;
    @Resource
    private ProProductMapper proProductMapper;
    @Autowired
    protected ServicesUtil servicesUtil;
    @Autowired
    private ProClassifyService proClassifyService;
    @Autowired
    private ShpShopService shopService;
    @Autowired
    private BizLeaguerService bizLeaguerService;
    @Autowired
    private OpBannerService opBannerService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addShopUnion(ParamShopUnionAdd shopUnionAdd) {
        Integer shopId = shopService.getShopIdByShopNumber(shopUnionAdd.getShopNumber());
        if (shopId == null) {
            throw new MyException("?????????????????????");
        }
        ShpShop shpShop = shopService.getShpShopById(shopId.toString());
        if (shpShop != null && shpShop.getMemberState() < 2) {
            throw new MyException("????????????????????????");
        }
        BizShopUnion shopUnionOld = bizShopUnionMapper.getByShopId(shopId);
        Boolean addFlag = true;
        if (shopUnionOld != null) {

            if (shopUnionOld.getState() > 0) {
                String oldType = shopUnionOld.getType();
                String type = shopUnionAdd.getType();
                String seller = "20";
                String buy = "10";
                if (buy.equals(oldType) && seller.equals(type)) {
                    addFlag = false;
                }
                if (seller.equals(oldType) && buy.equals(type)) {
                    throw new MyException("??????????????????????????????");
                }
                if (seller.equals(oldType) && seller.equals(type)) {
                    throw new MyException("??????????????????????????????");
                }
                if (buy.equals(oldType) && buy.equals(type)) {
                    throw new MyException("???????????????????????????");
                }
            } else {
                shopUnionOld.setState(10);
                addFlag = false;
            }

        }
        if (addFlag) {
            BizShopUnion shopUnion = new BizShopUnion();
            shopUnion.setState(10);
            shopUnion.setFkShpShopId(shopId);
            shopUnion.setInsertAdmin(-9);
            shopUnion.setInsertTime(new Date());
            shopUnion.setType(shopUnionAdd.getType());
            bizShopUnionMapper.saveObject(shopUnion);
        } else {
            shopUnionOld.setType(shopUnionAdd.getType());
            shopUnionOld.setUpdateTime(new Date());
            shopUnionOld.setUpdateAdmin(shopUnionAdd.getUserId());
            bizShopUnionMapper.updateObject(shopUnionOld);
        }
        //??????redis?????????????????????
        refreshBizShopUnionForRedis();
    }

    @Override
    public void removeUnionShop(Integer id) {
        BizShopUnion shopUnion = new BizShopUnion();
        shopUnion.setId(id);
        shopUnion.setState(-10);
        bizShopUnionMapper.updateObject(shopUnion);

        //??????redis?????????????????????
        refreshBizShopUnionForRedis();
    }

    @Override
    public void removeUnionShopByApp(ParamShopUnionDelete shopUnionDelete) {
        BizShopUnion shopUnion = bizShopUnionMapper.getByShopId(shopUnionDelete.getShopId());
        if (shopUnion == null) {
            throw new MyException("????????????????????????");
        }
        shopUnion.setState(-10);
        shopUnion.setUpdateAdmin(shopUnionDelete.getUserId());
        shopUnion.setUpdateTime(new Date());
        bizShopUnionMapper.updateObject(shopUnion);

        //??????redis?????????????????????
        refreshBizShopUnionForRedis();
    }

    @Override
    public VoShopUnionForAdminPage getShopUnionForAdminPage(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch) {

        getTradePrice(shopUnionForAdminBySearch);
        PageHelper.startPage(shopUnionForAdminBySearch.getPageNum(), shopUnionForAdminBySearch.getPageSize());

        List<VoShopUnionForAdminList> shopUnionForAdminLists = bizShopUnionMapper.getUnionForAdminList(shopUnionForAdminBySearch);
        if (shopUnionForAdminLists != null && shopUnionForAdminLists.size() > 0) {
            shopUnionForAdminLists.forEach(shopUnionForAdminList -> {
                if ("10".equals(shopUnionForAdminBySearch.getType())) {
                    Boolean flag = shopUnionForAdminList.getMemberState() != null && ("0".equals(shopUnionForAdminList.getMemberState()) || "1".equals(shopUnionForAdminList.getMemberState()));
                    Boolean flag2 = shopUnionForAdminList.getMemberState() != null && ("2".equals(shopUnionForAdminList.getMemberState()) || "3".equals(shopUnionForAdminList.getMemberState()));
                    if (flag) {
                        shopUnionForAdminList.setMemberState("0");
                    }
                    if (flag2) {
                        shopUnionForAdminList.setMemberState("1");
                    }
                }

            });
        }
        PageInfo pageInfo = new PageInfo(shopUnionForAdminLists);
        VoShopUnionForAdminPage shopUnionForAdminPage = new VoShopUnionForAdminPage();
        shopUnionForAdminPage.setPageNum(pageInfo.getPageNum());
        shopUnionForAdminPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            shopUnionForAdminPage.setHasNextPage(true);
        } else {
            shopUnionForAdminPage.setHasNextPage(false);
        }
        shopUnionForAdminPage.setList(shopUnionForAdminLists);
        shopUnionForAdminPage.setTotal(pageInfo.getTotal());
        return shopUnionForAdminPage;
    }

    @Override
    public List<VoShopUnionForAdminList> listShopUnionForAdmin(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch) {
        getTradePrice(shopUnionForAdminBySearch);
        return bizShopUnionMapper.getUnionForAdminList(shopUnionForAdminBySearch);
    }

    /**
     * ???????????????/?????? ?????????????????????x???redis??????????????????
     * @param shopUnionForAdminBySearch
     * @return
     */
    public ParamShopUnionForAdminBySearch getTradePrice(ParamShopUnionForAdminBySearch shopUnionForAdminBySearch){
        String tradePriceKey = ConstantRedisKey.MPSHOWMINPRICE;
        String tradePrice =redisUtil.get(tradePriceKey);
        //???????????? ???????????????0
        if (LocalUtils.isEmptyAndNull(tradePrice)){
            tradePrice = "0";
            redisUtil.set(tradePriceKey,tradePrice);
        }
        shopUnionForAdminBySearch.setTradePrice(tradePrice);
        return shopUnionForAdminBySearch;
    }
    @Override
    public VoShopUnionByAppShow getShopUnionByAppShow(Integer shopId) {
        VoShopUnionByAppShow shopUnionByAppShow = new VoShopUnionByAppShow();
        List<VoProClassify> voProClassifyList = proClassifyService.listSysProClassifyByState(null);
        shopUnionByAppShow.setClassifyList(voProClassifyList);
        //????????????????????????????????????????????????
        //String minPrice = redisUtil.get("_mp_show_min_price");
        //String maxPrice = redisUtil.get("_mp_show_max_price");
        //Map<String, Object> objectMap = proProductMapper.getUnionForAppByStatistics(shopId, minPrice, maxPrice);
        //BigDecimal totalNum = (BigDecimal) objectMap.get("totalNum");
        //shopUnionByAppShow.setProductTotalNum(totalNum.intValue());
        //BigDecimal totalPrice = (BigDecimal) objectMap.get("totalPrice");
        //totalPrice = totalPrice.divide(new BigDecimal(100)).setScale(2);
        //shopUnionByAppShow.setProductTotalPrice(totalPrice);
        ParamAppBannerQuery paramAppBannerQuery = new ParamAppBannerQuery();
        paramAppBannerQuery.setPos("shopUnion");
        List<VoOpBanner> voOpBannerList = opBannerService.listOpBannerByPath(paramAppBannerQuery);
        if (voOpBannerList != null && voOpBannerList.size() > 0) {
            List<VoOpBannerForUnion> opBannerForUnions = new ArrayList<>();
            voOpBannerList.forEach(voOpBanner -> {
                VoOpBannerForUnion opBannerForUnion = new VoOpBannerForUnion();
                BeanUtils.copyProperties(voOpBanner, opBannerForUnion);
                opBannerForUnions.add(opBannerForUnion);
            });
            shopUnionByAppShow.setBannerList(opBannerForUnions);
        }
        //?????????????????????????????????
//        shopUnionByAppShow.setProductTotalName("????????????(???)");
//        shopUnionByAppShow.setProductTotalPriceName("???????????????(???)");
        return shopUnionByAppShow;
    }

    @Override
    public List<VoLeaguerProduct> getUnionProductByAppShow(ParamLeaguerProductQuery productQuery) {
        String tradePriceKey = ConstantRedisKey.MPSHOWMINPRICE;
        productQuery.setShowMinPrice(redisUtil.get(tradePriceKey));
        productQuery.setShowMaxPrice(redisUtil.get(tradePriceKey));
        PageHelper.startPage(Integer.parseInt(productQuery.getPageNum()), 10);
        List<VoLeaguerProduct> voUnionProducts = proProductMapper.getUnionForAppList(productQuery);
        if (voUnionProducts != null && voUnionProducts.size() > 0) {
            voUnionProducts.forEach(voLeaguerProduct -> {
                voLeaguerProduct.setSmallImg(servicesUtil.formatImgUrl(voLeaguerProduct.getSmallImg(), true));
                //??????????????????????????????2021-07-31 00:32:36
                //VoCanSeeLeaguerPriceInfo canSeeLeaguerPriceInfo = bizLeaguerService.isCanSeePriceForUnion(voLeaguerProduct.getShopId(), -9);
                ////???????????????????????????
                //Boolean isCanSeeTradePrice = canSeeLeaguerPriceInfo.getIsCanSeeTradePrice();
                ////???????????????????????????????????????????????????????????????
                //if (!isCanSeeTradePrice) {
                //    voLeaguerProduct.setTradePrice(null);
                //}
                ////???????????????????????????
                ////Boolean isCanSeeSalePrice = canSeeLeaguerPriceInfo.getIsCanSeeSalePrice();
                //////???????????????????????????????????????????????????????????????
                ////if (!isCanSeeSalePrice) {
                ////    voLeaguerProduct.setSalePrice(null);
                ////}
                //????????????????????????????????????????????????2021-07-29 02:46:02
                voLeaguerProduct.setSalePrice(null);
                if (!LocalUtils.isEmptyAndNull(voLeaguerProduct.getReleaseTime())) {
                    voLeaguerProduct.setShowTime("????????????:" + voLeaguerProduct.getReleaseTime());
                }
            });
        }
        return voUnionProducts;
    }

    @Override
    public VoProductLoadForUnionPage getProductLoadForUnionPage(ParamProProduct proProduct) {
        proProduct.setShowMinPrice(redisUtil.get("_mp_show_min_price"));
        proProduct.setShowMaxPrice(redisUtil.get("_mp_show_max_price"));
        PageHelper.startPage(proProduct.getPageNum(), proProduct.getPageSize());
        String showTimeSt = DateUtil.formatShort(DateUtil.addDaysFromOldDate(new Date(), -90).getTime());
        proProduct.setShowTimeSt(showTimeSt);
        List<VoProductLoad> productLoads = proProductMapper.getProductLoadForUnionList(proProduct);
        PageInfo<VoOrganizationPageByApp> pageInfo = new PageInfo(productLoads);
        VoProductLoadForUnionPage productLoadForUnionPage = new VoProductLoadForUnionPage();
        productLoadForUnionPage.setPageNum(proProduct.getPageNum());
        productLoadForUnionPage.setPageSize(proProduct.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            productLoadForUnionPage.setHasNextPage(true);
        } else {
            productLoadForUnionPage.setHasNextPage(false);
        }
        productLoadForUnionPage.setList(productLoads);
        productLoadForUnionPage.setTotal(pageInfo.getTotal());
        DecimalFormat df = new DecimalFormat(",##0.##");
        String minPrice = redisUtil.get("_mp_show_min_price");
        String maxPrice = redisUtil.get("_mp_show_max_price");
        Map<String, Object> objectMap = proProductMapper.getUnionForAppByStatistics(proProduct);
        BigDecimal totalNum = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);
        if (!LocalUtils.isEmptyAndNull(objectMap)) {
            totalNum = (BigDecimal) objectMap.get("totalNum");
            try {
                totalPrice = LocalUtils.calcNumber(objectMap.get("totalPrice"), "/", 100);
            } catch (Exception ignored) {
            }
        }
        productLoadForUnionPage.setTotalNum(df.format(totalNum));
        productLoadForUnionPage.setTotalPrice(df.format(totalPrice));
        return productLoadForUnionPage;
    }

    @Override
    public List<BizShopUnion> listBizShopUnionByState(String state) {
        return bizShopUnionMapper.listBizShopUnionByState(state);
    }

    @Override
    public void refreshBizShopUnionForRedis() {
        List<BizShopUnion> bizShopUnions = listBizShopUnionByState("10");
        StringBuilder shopId = new StringBuilder();
        if (!LocalUtils.isEmptyAndNull(bizShopUnions)) {
            for (BizShopUnion union : bizShopUnions) {
                shopId.append(union.getFkShpShopId()).append(",");
            }
        }
        redisUtil.set(ConstantRedisKey.SHOP_UNION, shopId.toString());
    }


    @Override
    public List<VoLeaguerProduct> listUnionProductNoPage(ParamLeaguerProductQuery productQuery) {
        productQuery.setShowMinPrice(redisUtil.get("_mp_show_min_price"));
        productQuery.setShowMaxPrice(redisUtil.get("_mp_show_max_price"));
        int currentPage = Integer.parseInt(productQuery.getPageNum());
        int pageSize = 30;
        productQuery.setPageNum((currentPage * 500) + "");
        String showTimeSt = DateUtil.formatShort(DateUtil.addDaysFromOldDate(new Date(), -90).getTime());
        productQuery.setShowTimeSt(showTimeSt);
        //????????????sql??????????????????.?????????????????????limit,?????????????????????????????????????????????????????????limit????????????;PageHelper.clearPage();
        PageHelper.clearPage();
        List<VoLeaguerProduct> voUnionProducts = proProductMapper.listUnionProductNoPage(productQuery);
        List<VoLeaguerProduct> newList = new ArrayList<>();
        if (voUnionProducts != null && voUnionProducts.size() > 0) {
            boolean isNullClassifyCode = LocalUtils.isEmptyAndNull(productQuery.getClassifyCode());
            boolean isNullTargetUser = LocalUtils.isEmptyAndNull(productQuery.getTargetUser());
            boolean isNullPriceMin = LocalUtils.isEmptyAndNull(productQuery.getPriceMin());
            boolean isNullPriceMax = LocalUtils.isEmptyAndNull(productQuery.getPriceMax());
            boolean isNullClassifySub = LocalUtils.isEmptyAndNull(productQuery.getClassifySub());
            boolean isNullProName = LocalUtils.isEmptyAndNull(productQuery.getProName());
            boolean isOk = isNullClassifyCode && isNullTargetUser && isNullPriceMin
                    && isNullPriceMax && isNullClassifySub && isNullProName;
            for (VoLeaguerProduct voLeaguerProduct : voUnionProducts) {
                if (!LocalUtils.isEmptyAndNull(voLeaguerProduct.getBsuId())) {
                    voLeaguerProduct.setSmallImg(servicesUtil.formatImgUrl(voLeaguerProduct.getSmallImg(), true));
                    voLeaguerProduct.setSalePrice(null);
                    voLeaguerProduct.setShowTime("???????????????" + voLeaguerProduct.getShowTime());
                    String tradePrice = voLeaguerProduct.getTradePrice();
                    //???????????????,?????????????????????,??????????????????1??????200????????????
                    if ("normal".equals(productQuery.getSortKey()) && isOk) {
                        if (currentPage <= 3 && LocalUtils.isBetween(Double.parseDouble(tradePrice), 1000000, 200000000)) {
                            newList.add(voLeaguerProduct);
                        } else if (currentPage > 3) {
                            newList.add(voLeaguerProduct);
                        }
                    } else {
                        newList.add(voLeaguerProduct);
                    }
                }
            }
        }
        //??????30???;
        List<VoLeaguerProduct> returnList = new ArrayList<>();
        for (int i = pageSize * (currentPage - 1); i < newList.size(); i++) {
            returnList.add(newList.get(i));
            if (returnList.size() == pageSize) {
                break;
            }

        }
        return returnList;
    }

    @Override
    public List<VoShpUser> listUnionMpShareUser() {
        return bizShopUnionMapper.listUnionMpShareUser();
    }

    @Override
    public void backOfUnion(String ids) {
        proProductMapper.updateProUnionState(ids, "0");
    }


    @Override
    public void upOfUnion(String ids) {
        proProductMapper.updateProUnionState(ids, "1");
    }

}
