package com.luxuryadmin.service.util;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.pro.ParamProductUpload;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.pro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 商品格式化图片地址
 *
 * @author monkey king
 * @date 2020-01-02 16:04:45
 */
@Slf4j
public class ProProductBaseController extends BaseController {

    @Autowired
    private ProTempProductService proTempProductService;

    @Autowired
    protected ProProductService proProductService;

    @Autowired
    private ProModifyRecordService proModifyRecordService;

    @Autowired
    private ProDynamicService proDynamicService;

    @Autowired
    private ProStandardService proStandardService;
    @Autowired
    private ShpUserShopRefService shpUserShopRefService;
    /**
     * 格式化前端商品列表请求的查询参数;
     *
     * @param queryParam
     */
    protected void formatQueryParam(ParamProductQuery queryParam) {
        queryParam.setShopId(getShopId());
        queryParam.setUserId(getUserId());
        //查看托管客户
        //判断是否为寄卖商品质押商品
        Boolean flag = !LocalUtils.isEmptyAndNull(queryParam.getAttributeCode()) && (queryParam.getAttributeCode().contains("30") || queryParam.getAttributeCode().contains("20"));
        if (!LocalUtils.isEmptyAndNull(queryParam.getProName()) && flag) {
            String showProductCustomer = ConstantPermission.CHK_PRODUCT_CUSTOMER;
            String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
            if (hasPermWithCurrentUser(userPerms, showProductCustomer)) {
                queryParam.setCustomerUser(queryParam.getProName());
            }
        }
        Boolean addResult = false;
        try {
            //添加版本判断
            BasicParam basicParam = getBasicParam();

            if (!LocalUtils.isEmptyAndNull(queryParam.getClassifySub())) {
                if (basicParam.getAppVersion() != null && (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.4") >= 0)) {
                    List<String> classifySubList = Arrays.asList(queryParam.getClassifySub().split(","));
                    if (classifySubList.size() > 10) {
                        addResult = true;
                    }
                }
            }
            if (addResult) {
//                return   BaseResult.errorResult("商品二级分类筛选最多十个");
                throw new MyException("商品二级分类筛选最多十个");
            }
        } catch (Exception e) {
            if (addResult) {
//                return   BaseResult.errorResult("商品二级分类筛选最多十个");
                throw new MyException("商品二级分类筛选最多十个");
            } else {
                throw new MyException("商品列表查询异常" + e);
            }

        }
        try {
            String initPriceKeyWord = queryParam.getProName();
            //目前兼容界面无法筛选成本范围
            if (!LocalUtils.isEmptyAndNull(initPriceKeyWord) && initPriceKeyWord.startsWith("@")) {
                queryParam.setProName(null);
                //开启快捷查询模式;
                String[] tempInitPrice = initPriceKeyWord.split("@");
                if (tempInitPrice.length >= 2) {
                    queryParam.setSortKey("initPrice");
                    queryParam.setPriceType("initPrice");
                    queryParam.setSortValue("asc");
                    String minPriceStr = tempInitPrice[1];
                    String maxPriceStr = "";
                    if (tempInitPrice.length >= 3) {
                        maxPriceStr = tempInitPrice[2];
                    }
                    String minPrice100 = LocalUtils.calcNumber(minPriceStr, "*", 100).toString();
                    queryParam.setPriceMin(minPrice100);
                    if (!LocalUtils.isEmptyAndNull(maxPriceStr)) {
                        String maxPrice100 = LocalUtils.calcNumber(maxPriceStr, "*", 100).toString();
                        queryParam.setPriceMax(maxPrice100);
                        if (Double.parseDouble(minPriceStr) > Double.parseDouble(maxPriceStr)) {
                            //反过来搜索
                            queryParam.setSortValue("desc");
                            queryParam.setPriceMin(maxPrice100);
                            queryParam.setPriceMax(minPrice100);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        //对多选的参数(回收人员)进行逗号分开;
        queryParam.setRecycleUserId(LocalUtils.formatParamForSqlInQuery(queryParam.getRecycleUserId()));
        //对多选的参数(商品属性)进行逗号分开;
        queryParam.setAttributeCode(LocalUtils.formatParamForSqlInQuery(queryParam.getAttributeCode()));
        //如果有分类参数
        String classifyCode = queryParam.getClassifyCode();
        if (!LocalUtils.isEmptyAndNull(classifyCode) && classifyCode.length() >= 2) {
            classifyCode = classifyCode.substring(0, 2) + "%";
            queryParam.setClassifyCode(classifyCode);
        }
        //如果有查询名称参数
        String name = queryParam.getProName();
        if (!LocalUtils.isEmptyAndNull(name)) {
            name = name.trim();
            name = name.replaceAll("\\s+", ".*");
            queryParam.setProName(name);
        }

        //对多选的参数(适用人群)进行逗号分开;
        String targetUser = queryParam.getTargetUser();
        targetUser = (LocalUtils.isEmptyAndNull(targetUser) || "通用".equals(targetUser)) ? null : LocalUtils.formatParamForSqlInQuery(targetUser);
        queryParam.setTargetUser(targetUser);

        //对多选的参数(商品成色)进行逗号分开;
        queryParam.setQuality(LocalUtils.formatParamForSqlInQuery(queryParam.getQuality()));

        //对多选的参数(品牌分类)进行逗号分开;
        queryParam.setClassifySub(LocalUtils.formatParamForSqlInQuery(queryParam.getClassifySub(), ","));
        //对多选的参数(商品成色)进行逗号分开;
        queryParam.setRetrieveUserId(LocalUtils.formatParamForSqlInQuery(queryParam.getRetrieveUserId()));
        String sortValue = queryParam.getSortValue();
        final String desc = "DESC";
        //sortKey包含: normal,price, time,notDown
        String sortKey = queryParam.getSortKey();
        String sortKeyDb = "pro.`update_time` ASC, pro.`insert_time` ASC";
        switch (sortKey + "") {
            //按照销售价格排序
            case "initPrice":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`init_price` DESC,pro.`sale_price` DESC";
                } else {
                    sortKeyDb = "pro.`init_price` ASC,pro.`sale_price` ASC";
                }
                break;
            //按照销售价格排序
            case "price":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`sale_price` DESC,pro.`init_price` DESC";
                } else {
                    sortKeyDb = "pro.`sale_price` ASC,pro.`init_price` ASC";
                }
                break;
            //按照"最久未被一键下载"排序
            case "notDown":
                //sortKey = "pro.`insert_time` DESC";
                queryParam.setNotDown("notDown");
                //queryParam.setNotDownUserId(getUserId());
                break;
            //按照时间排序或者按照默认排序;
            case "time":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`insert_time` DESC, pro.`id` DESC";
                } else {
                    sortKeyDb = "pro.`insert_time` ASC, pro.`id` ASC";
                }
                break;
            case "updateTime":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`update_time` DESC";
                } else {
                    sortKeyDb = "pro.`update_time` ASC";
                }
                break;
            case "finishTime":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`finish_time` DESC";
                } else {
                    sortKeyDb = "pro.`finish_time` ASC";
                }
                break;
            case "finishPrice":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`finish_price` DESC";
                } else {
                    sortKeyDb = "pro.`finish_price` ASC";
                }
                break;
            default:
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "pro.`update_time` DESC, pro.`insert_time` DESC";
                } else {
                    sortKeyDb = "pro.`update_time` ASC, pro.`insert_time` ASC";
                }
                break;
        }

        queryParam.setSortKeyDb(sortKeyDb);
    }


    /**
     * 格式化商品图片路径;返回端上绝对路径;<br/>
     * 当分享商品出去的时候, 请设置为true
     *
     * @param vo
     * @param isShare 是否分享出去的产品; true:分享 | false:不分享
     */
    protected void formatVoProductLoad(String appVersion, VoProductLoad vo, boolean isShare, String showTimeField) {
        String userPerms = "";
        if (!isShare) {
            userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        }
        formatVoProductLoad(appVersion, vo, isShare, userPerms, showTimeField);
    }


    /**
     * 格式化商品图片路径;返回端上绝对路径;<br/>
     * 当分享商品出去的时候, 请设置为true
     *
     * @param vo
     * @param isShare 是否分享出去的产品; true:分享 | false:不分享
     */
    protected void formatVoProductLoad(String appVersion, VoProductLoad vo, boolean isShare, String userPerms, String showTimeField) {
        if (!LocalUtils.isEmptyAndNull(vo)) {

            //商品属性
            vo.setAttributeShortCn(servicesUtil.getAttributeCn(vo.getAttributeUs(), false));
            //商品状态
            vo.setStateCn(servicesUtil.getStateCn(vo.getStateUs()));
            //商品分类
            vo.setClassifyCn(servicesUtil.getClassifyCn(vo.getClassifyUs()));
            //品牌分类
            if (!LocalUtils.isEmptyAndNull(vo.getClassifySub())) {
                if (!vo.getClassifySub().equals(vo.getClassifySubName()) && !LocalUtils.isEmptyAndNull(vo.getClassifySubName())) {
                    vo.setClassifySubName(vo.getClassifySub() + "/" + vo.getClassifySubName());
                } else {
                    vo.setClassifySubName(vo.getClassifySub());
                }
            }

            //缩略图
            String smallImg = vo.getSmallImg();
            if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                vo.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
            }
            //商品图片
            String productImg = vo.getProductImg();
            if (!LocalUtils.isEmptyAndNull(productImg)) {
                String[] productImgArray = productImg.split(";");
                for (int i = 0; i < productImgArray.length; i++) {
                    if (!productImgArray[i].contains("http")) {
                        productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                    }
                }
                vo.setProductImgList(productImgArray);
            }

            //备注图片
            String remarkImgUrl = vo.getRemarkImgUrl();
            if (!LocalUtils.isEmptyAndNull(remarkImgUrl)) {
                String[] remarkImgUrlArray = remarkImgUrl.split(";");
                for (int i = 0; i < remarkImgUrlArray.length; i++) {
                    if (!remarkImgUrlArray[i].contains("http")) {
                        remarkImgUrlArray[i] = servicesUtil.formatImgUrl(remarkImgUrlArray[i], false);
                    }
                }
                vo.setRemarkImgList(remarkImgUrlArray);
            }

            //保卡图片
            String cardCodeImg = vo.getCardCodeImg();
            if (!LocalUtils.isEmptyAndNull(cardCodeImg)) {
                String[] cardCodeImgArray = cardCodeImg.split(";");
                for (int i = 0; i < cardCodeImgArray.length; i++) {
                    if (!cardCodeImgArray[i].contains("http")) {
                        //保卡不做压缩处理
                        cardCodeImgArray[i] = servicesUtil.formatImgUrl(cardCodeImgArray[i]);
                    }
                }
                vo.setCardCodeImgList(cardCodeImgArray);
            }

            //视频
            String videoUrl = vo.getVideoUrl();
            if (!LocalUtils.isEmptyAndNull(videoUrl) && !videoUrl.contains("http")) {
                vo.setVideoUrl(servicesUtil.formatImgUrl(videoUrl));
            }

            String tag = vo.getTag();
            if (!LocalUtils.isEmptyAndNull(tag)) {
                String[] tags = tag.split(";");
                vo.setTags(tags);
            }

            //托管客户信息,兼容就旧版本
            String customerInfo = vo.getCustomerInfo();
            String oldCustomerInfo = LocalUtils.returnEmptyStringOrString(vo.getCustomerName()) + LocalUtils.returnEmptyStringOrString(vo.getCustomerPhone()) + LocalUtils.returnEmptyStringOrString(vo.getCustomerRemark());
            String newInfo = LocalUtils.isEmptyAndNull(customerInfo) ? oldCustomerInfo : customerInfo;
            vo.setCustomerInfo(LocalUtils.isEmptyAndNull(newInfo) ? null : newInfo);

            //非分享状态
            if (!isShare) {
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(vo.getShopId(), vo.getBizId());
                vo.setLeftNum(proRedisNum.getLeftNum());
                vo.setLockNum(proRedisNum.getLockNum());

                //查看权限
                showProByPerm(vo, userPerms);

                try {
                    //2.4.1版本以上支持
                    //显示时间
                    if ("notDown".equals(showTimeField)) {
                        //最久未被下载,显示下载时间,如果没下载过,则下载时间未空
                        Date downloadTime = vo.getRecentDownloadTime();
                        String downloadTimeStr = LocalUtils.isEmptyAndNull(downloadTime) ?
                                "从未下载过此商品图片" : "上次下载时间：" + DateUtil.format(downloadTime);
                        vo.setShowTime(downloadTimeStr);
                    } else if ("time".equals(showTimeField)) {
                        //显示入库时间;
                        //最久未被下载,显示下载时间,如果没下载过,则下载时间未空
                        Date insertTime = vo.getInsertTime();
                        vo.setShowTime("入库时间：" + DateUtil.format(insertTime));
                    } else {
                        Date updateTime = vo.getUpdateTime();
                        updateTime = LocalUtils.isEmptyAndNull(updateTime) ? vo.getInsertTime() : updateTime;
                        vo.setShowTime("更新时间：" + DateUtil.format(updateTime));
                    }
                } catch (Exception e) {
                    log.error("产品列表格式化时间错误: " + e.getMessage(), e);
                }
                ParamClassifyTypeSearch classifyTypeSearch = new ParamClassifyTypeSearch();
//            Integer productId = proProductService.getShopIdByBizId(vo.getBizId());
                classifyTypeSearch.setBizId(vo.getBizId());
                classifyTypeSearch.setShopId(vo.getShopId());
                classifyTypeSearch.setProductId(vo.getProId());
                VoClassifyTypeSon classifyTypeSon = proStandardService.getClassifyTypeList(classifyTypeSearch);
                if (!LocalUtils.isEmptyAndNull(classifyTypeSon)){
                    vo.setClassifyTypeList(classifyTypeSon.getClassifyTypeSonLists());
                    if (!LocalUtils.isEmptyAndNull(classifyTypeSon.getPublicPrice()) && !"0".equals(classifyTypeSon.getPublicPrice())){
                        vo.setPublicPrice("￥"+classifyTypeSon.getPublicPrice());
                    }
                }
            }
            vo.setCardCodeImg(null);
            vo.setProductImg(null);
            vo.setRemarkImgUrl(null);

            //保卡时间详情
            formatRepairCardTime(vo);

            //如果是已赎回的质押商品，设置赎回时间
            String proAttribute = vo.getAttributeUs();
            String proState = vo.getStateUs();
            if (null != proAttribute && null != proState && EnumProAttribute.PAWN.getCode().equals(Integer.parseInt(proAttribute))
                    && EnumProState.SALE_44.getCode().equals(Integer.parseInt(proState))) {
                Date updateTime = vo.getUpdateTime();
                vo.setRedeemTime(DateUtil.format(updateTime));
            }

            //全店通店铺,隐藏商家具体操作人员;10684全店通店铺id
            if (getShopId() == 10684) {
                vo.setUploadUser("无");
                vo.setRecycleName("无");
                vo.setUpdateUser("无");
            }
            //判断是否具有删除权限
            Boolean falg = vo.getStateUs() != null && "-90".equals(vo.getStateUs()) && vo.getUpdateUserId() != null && vo.getUpdateUserId() == getUserId();
            if (falg) {
                vo.setUpdateDeleteState("1");
            } else {
                vo.setUpdateDeleteState("0");
            }
            //判断是否具有取回备注权限
            Boolean retrieveFlag = vo.getRetrieveUserId() != null && vo.getRetrieveUserId() == getUserId();
            if (retrieveFlag) {
                vo.setUpdateRetrieveState("1");
            } else {
                vo.setUpdateRetrieveState("0");
            }




        }
    }

    /**
     * 格式化商品图片路径;返回端上绝对路径;<br/>
     * 当分享商品出去的时候, 请设置为true
     *
     * @param vo
     */
    protected void formatLockProduct(VoProductLoad vo) {
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        if (!LocalUtils.isEmptyAndNull(vo)) {
            //商品属性
            vo.setAttributeShortCn(servicesUtil.getAttributeCn(vo.getAttributeUs(), false));
            //缩略图
            String smallImg = vo.getSmallImg();
            if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                vo.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
            }
            //商品图片
            String productImg = vo.getProductImg();
            if (!LocalUtils.isEmptyAndNull(productImg)) {
                String[] productImgArray = productImg.split(";");
                for (int i = 0; i < productImgArray.length; i++) {
                    if (!productImgArray[i].contains("http")) {
                        productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                    }
                }
                vo.setProductImgList(productImgArray);
            }
            try {
                if (!LocalUtils.isEmptyAndNull(vo.getPreMoney())) {
                    String preMoney = LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getPreMoney(), "*", "0.01").doubleValue());
                    vo.setPreMoney(preMoney);
                }
                if (!LocalUtils.isEmptyAndNull(vo.getPreFinishMoney())) {
                    String preFinishMoney = LocalUtils.formatPriceSpilt(LocalUtils.calcNumber(vo.getPreFinishMoney(), "*", "0.01").doubleValue());
                    vo.setPreFinishMoney(preFinishMoney);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            //查看权限
            showProByPerm(vo, userPerms);
        }
    }


    protected void formatLockProduct(List<VoProductLoad> list) {
        if (!LocalUtils.isEmptyAndNull(list)) {
            for (VoProductLoad productLoad : list) {
                formatLockProduct(productLoad);
            }
        }
    }

    /**
     * 权限控制商品字段显示;
     *
     * @param voPro
     */
    private void showProByPerm(VoProductLoad voPro, String userPerms) {

        //查看成本价
        String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
        voPro.setInitPrice(hasPermWithCurrentUser(userPerms, showInitPrice) ? voPro.getInitPrice() : null);
        //查看友商价
        String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
        voPro.setTradePrice(hasPermWithCurrentUser(userPerms, showTradePrice) ? voPro.getTradePrice() : null);
        //查看代理价
        String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
        voPro.setAgencyPrice(hasPermWithCurrentUser(userPerms, showAgencyPrice) ? voPro.getAgencyPrice() : null);
        //查看销售价
        String showSalePrice = ConstantPermission.CHK_PRICE_SALE;
        voPro.setSalePrice(hasPermWithCurrentUser(userPerms, showSalePrice) ? voPro.getSalePrice() : null);
        //查看成交价, 临时处理成星号
        String showFinishPrice = ConstantPermission.CHK_PRICE_FINISH;
        voPro.setFinishPrice(hasPermWithCurrentUser(userPerms, showFinishPrice) ? voPro.getFinishPrice() : null);
        //查看商品属性(长属性和短属性)
        String showProductAttribute = ConstantPermission.CHK_PRODUCT_ATTRIBUTE;
        voPro.setAttributeShortCn(hasPermWithCurrentUser(userPerms, showProductAttribute) ? voPro.getAttributeShortCn() : null);
        voPro.setAttributeCn(hasPermWithCurrentUser(userPerms, showProductAttribute) ? voPro.getAttributeCn() : null);
        //查看托管客户
        String showProductCustomer = ConstantPermission.CHK_PRODUCT_CUSTOMER;
        if (!hasPermWithCurrentUser(userPerms, showProductCustomer)) {
            voPro.setCustomerName(null);
            voPro.setCustomerPhone(null);
            voPro.setCustomerRemark(null);
        }
        //查看备注(目前和商品属性共用一个权限)
        voPro.setRemark(hasPermWithCurrentUser(userPerms, showProductAttribute) ? voPro.getRemark() : null);
        //查看备注图片(目前和商品属性共用一个权限)
        voPro.setRemarkImgList(hasPermWithCurrentUser(userPerms, showProductAttribute) ? voPro.getRemarkImgList() : null);
    }

    protected void formatVoProductLoad(String appVersion, VoProductLoad voProductLoad, String showTimeField) {
        formatVoProductLoad(appVersion, voProductLoad, false, showTimeField);
    }

    protected void formatVoProductLoad(String appVersion, List<VoProductLoad> voProductLoadList, String showTimeField) {
        if (!LocalUtils.isEmptyAndNull(voProductLoadList)) {
            String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
            for (VoProductLoad voProductLoad : voProductLoadList) {
                if (voProductLoad.getShopId()!=null&&voProductLoad.getBizId()!=null){
                    formatVoProductLoad(appVersion, voProductLoad, false, userPerms, showTimeField);
                }
            }
        }
    }

    /**
     * 格式化保卡时间
     *
     * @param vo
     */
    private void formatRepairCardTime(VoProductLoad vo) {
        try {
            BasicParam basicParam = getBasicParam();
            String platform = basicParam.getPlatform();
            String appVersion = basicParam.getAppVersion();

            //获取保卡时间字段，如果为空，则显示【无保卡】
            String repairCardTime = vo.getRepairCardTime();
            if (StringUtils.isBlank(repairCardTime)) {
                vo.setRepairCardTime("");
            } else if (!repairCardTime.equals("空白保卡")) {
                //判断版本号，低于指定版本号，则处理
                Boolean isOldVersion = Boolean.FALSE;
                if (StringUtil.isNotBlank(appVersion)) {
                    if ("android".equals(platform)) {
                        isOldVersion = VersionUtils.compareVersion(appVersion, "2.1.0") < 0;
                    } else if ("ios".equals(platform)) {
                        isOldVersion = VersionUtils.compareVersion(appVersion, "2.2.1") < 0;
                    }
                }
                if (isOldVersion) {
                    vo.setRepairCardTime(repairCardTime + "年");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    protected ProProduct getProProductByShopIdBizId(int shopId, String bizId) {
        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);
        if (LocalUtils.isEmptyAndNull(pro)) {
            throw new MyException("找不到商品信息!");
        }
        return pro;
    }

    /**
     * 获取所有商品属性,逗号拼接, 除了质押商品之外
     *
     * @return
     */
    protected String getAllProAttributeCodeExcludePawnPro(String symbol) {
        return EnumProAttribute.OWN.getCode() + symbol +
                EnumProAttribute.ENTRUST.getCode() + symbol + EnumProAttribute.OTHER.getCode();
    }

    ///**
    // * 获取商品列表时间字段
    // *
    // * @param sortKey
    // * @param notDown
    // * @return
    // */
    //private String getShowTimeField(String sortKey, String notDown) {
    //    if ("notDown".equals(sortKey)) {
    //        return "recentDownloadTime";
    //    } else if ("time".equals(sortKey)) {
    //        return "uploadTime";
    //    } else {
    //        return "updateTime";
    //    }
    //}


    protected void formatTempPro(int shopId, int tempId, VoProductLoad vo) {
        VoProductLoad temp = proTempProductService.getVoProductFromTempProduct(shopId, tempId, vo.getProId());
        String defaultRequest = "";
        if (redisUtil.hasKey(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)))) {
            defaultRequest = redisUtil.get(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)));
        } else {
            defaultRequest = "tradePrice";
        }
        if (!LocalUtils.isEmptyAndNull(temp)) {
            String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
            //查看成本价
            String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
            //查看友商价
            String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
            //查看代理价
            String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
            //查看销售价
            String showSalePrice = ConstantPermission.CHK_PRICE_SALE;

            String name = temp.getName();
            String description = temp.getDescription();
            String initPrice = temp.getShowInitPrice();
            String tradePrice = temp.getTradePrice();
            String agencyPrice = temp.getAgencyPrice();
            String salePrice = temp.getSalePrice();
            Integer totalNum = temp.getTotalNum();
            vo.setName(LocalUtils.isEmptyAndNull(name) ? vo.getName() : name);
            vo.setDescription(LocalUtils.isEmptyAndNull(description) ? vo.getDescription() : description);

            vo.setShowInitPrice(hasPermWithCurrentUser(userPerms, showInitPrice) ? (LocalUtils.isEmptyAndNull(initPrice) ? vo.getInitPrice() : initPrice) : null);
            vo.setTradePrice(hasPermWithCurrentUser(userPerms, showTradePrice) ? (LocalUtils.isEmptyAndNull(tradePrice) ? vo.getTradePrice() : tradePrice) : null);
            vo.setAgencyPrice(hasPermWithCurrentUser(userPerms, showAgencyPrice) ? (LocalUtils.isEmptyAndNull(agencyPrice) ? vo.getAgencyPrice() : agencyPrice) : null);
            vo.setSalePrice(hasPermWithCurrentUser(userPerms, showSalePrice) ? (LocalUtils.isEmptyAndNull(salePrice) ? vo.getSalePrice() : salePrice) : null);
            //vo.setInitPrice(hasPermWithCurrentUser(userPerms, showInitPrice) ? (LocalUtils.isEmptyAndNull(tradePrice) ? vo.getInitPrice() : initPrice) : null);
            vo.setTotalNum(LocalUtils.isEmptyAndNull(totalNum) ? vo.getTotalNum() : totalNum);

            switch (defaultRequest) {
                case "initPrice"://成本价
                    vo.setShowPrice(hasPermWithCurrentUser(userPerms, showInitPrice) ? vo.getInitPrice() : null);
                    break;
                case "tradePrice"://友商价
                    vo.setShowPrice(vo.getTradePrice());
                    break;
                case "agencyPrice"://代理价
                    vo.setShowPrice(vo.getAgencyPrice());
                    break;
                case "salePrice"://销售价
                    vo.setShowPrice(vo.getSalePrice());
                    break;
                default://无价格类型时默认为0
                    vo.setShowPrice("0.00");
                    break;
            }
            //vo.setShowInitPrice(vo.getInitPrice());
        }
    }

    /**
     * 设置价格权限显示
     * @param conveyId
     * @param vo
     */
    public void getConveyPrice( String conveyId, VoProductLoad vo){
        String defaultPrice;
        String userPerms = servicesUtil.getUserPerms(getShopId(), getUserId());
        //判断redis是否有key值
        if (redisUtil.hasKey(ConstantRedisKey.getProConveyKeyByKey(conveyId))) {
            //获取接收参数---是否为登录后初次请求接口
            defaultPrice = redisUtil.get(ConstantRedisKey.getProConveyKeyByKey(conveyId));
        } else {
            defaultPrice = "tradePrice";
        }
        //查看成本价
        String showPrice ="";
        if ("initPrice".equals(defaultPrice)){
            showPrice = ConstantPermission.CHK_PRICE_INIT;
        }

        //查看友商价
        if ("tradePrice".equals(defaultPrice)){
            showPrice = ConstantPermission.CHK_PRICE_TRADE;
        }
        //查看代理价
        if ("agencyPrice".equals(defaultPrice)){
            showPrice = ConstantPermission.CHK_PRICE_AGENCY;
        }
        //查看销售价
        if ("salePrice".equals(defaultPrice)){
            showPrice = ConstantPermission.CHK_PRICE_SALE;
        }
        String showPriceNow = hasPermWithCurrentUser(userPerms, showPrice)?vo.getShowPrice():null;
        vo.setShowPrice(showPriceNow);
    }
    /**
     * 此方法仅针对分享接口不设权限
     *
     * @param shopId
     * @param tempId
     * @param vo
     */
    protected void formatTempProNew(int shopId, int tempId, VoProductLoad vo) {
        VoProTempProduct temp = proTempProductService.getNewProTempProduct(shopId, tempId, vo.getProId());
        String defaultRequest = "";
        if (redisUtil.hasKey(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)))) {
            defaultRequest = redisUtil.get(ConstantRedisKey.getProDefaultRequest(String.valueOf(tempId)));
        } else {
            defaultRequest = "tradePrice";
        }
        if (!LocalUtils.isEmptyAndNull(temp)) {
            String name = temp.getName();
            String description = temp.getDescription();
            String initPrice = temp.getInitPrice();
            String tradePrice = temp.getTradePrice();
            String agencyPrice = temp.getAgencyPrice();
            String salePrice = temp.getSalePrice();
            vo.setName(LocalUtils.isEmptyAndNull(name) ? vo.getName() : name);
            vo.setDescription(LocalUtils.isEmptyAndNull(description) ? vo.getDescription() : description);
            switch (defaultRequest) {
                case "initPrice"://成本价
                    vo.setShowPrice(initPrice);
                    break;
                case "tradePrice"://友商价
                    vo.setShowPrice(tradePrice);
                    break;
                case "agencyPrice"://代理价
                    vo.setShowPrice(agencyPrice);
                    break;
                case "salePrice"://销售价
                    vo.setShowPrice(salePrice);
                    break;
                default://无价格类型时默认为0
                    vo.setShowPrice("0.00");
                    break;
            }
            //vo.setShowInitPrice(vo.getInitPrice());
        }
        log.info("参数" + vo);
    }

    /**
     * 修改商品记录, 封装实体
     *
     * @param proId
     * @param type
     * @param attributeName
     * @param beforeValue
     * @param afterValue
     * @return
     */
    ProModifyRecord packProModifyRecord(int proId, String type, String attributeName, Object beforeValue, Object afterValue,String source) {
        ProModifyRecord pmr = new ProModifyRecord();
        pmr.setFkShpShopId(getShopId());
        pmr.setFkShpUserId(getUserId());
        pmr.setFkProProductId(proId);
        pmr.setType(type);
        pmr.setAttributeName(attributeName);
        pmr.setBeforeValue(LocalUtils.returnFormatString(beforeValue));
        pmr.setAfterValue(LocalUtils.returnFormatString(afterValue));
        pmr.setInsertTime(new Date());
        pmr.setRemark("");
        pmr.setSource(source);
        return pmr;
    }


    /**
     * 添加商品修改记录;<br/>
     * 商品的每次改动,都需要调用此方法
     *
     * @param proId
     * @param type
     * @param beforeValue
     * @param afterValue
     */
    protected void addProModifyRecord(int proId, String type, Object beforeValue, Object afterValue,String source) {
        ProModifyRecord proModifyRecord = packProModifyRecord(proId, type, "", beforeValue, afterValue,source);
        proModifyRecordService.saveProModifyRecord(proModifyRecord);
    }


    /**
     * 添加商品修改记录;<br/>
     * 商品的每次改动,都需要调用此方法
     *
     * @param proId
     * @param type
     * @param attributeName
     * @param beforeValue
     * @param afterValue
     */
    protected void addProModifyRecord(int proId, String type, String attributeName, Object beforeValue, Object afterValue,String source) {
        ProModifyRecord proModifyRecord = packProModifyRecord(proId, type, attributeName, beforeValue, afterValue,source);
        proModifyRecordService.saveProModifyRecord(proModifyRecord);
    }

    public void equalsMoreProduct(ProProduct oldPro, ProDetail oldDetail, ParamProductUpload newPro ,String source) {
        int proId = oldPro.getId();
        //图片对比
        if (null != newPro.getProImgUrl()) {
            changeAndSave(proId, "商品图片", oldDetail.getProductImg(), newPro.getProImgUrl(),source);
        }
        //视频地址
        if (null != newPro.getVideoUrl()) {
            changeAndSave(proId, "商品视频", oldDetail.getVideoUrl(), newPro.getVideoUrl(),source);
        }
        //商品名称
        changeAndSave(proId, "名称", oldPro.getName(), newPro.getName(),source);
        //商品描述
        changeAndSave(proId, "描述", oldPro.getDescription(), newPro.getDescription(),source);
        //商品属性
        changeAndSave(proId, "属性", servicesUtil.getAttributeCn(oldPro.getFkProAttributeCode(), true), servicesUtil.getAttributeCn(newPro.getAttribute(), true),source);
        //委托方
        changeAndSave(proId, "委托方", oldDetail.getCustomerInfo(), newPro.getCustomerInfo(),source);
        //商品分类
        changeAndSave(proId, "分类", servicesUtil.getClassifyCn(oldPro.getFkProClassifyCode()), servicesUtil.getClassifyCn(newPro.getClassify()),source);

        //回收人员,需要做特殊处理;
        Integer oldRecycleAdmin = oldPro.getRecycleAdmin() == 0 ? null : oldPro.getRecycleAdmin();
        Integer newRecycleAdmin = newPro.getRecycleAdmin();

        String before = LocalUtils.returnFormatString(oldRecycleAdmin);
        String after = LocalUtils.returnFormatString(newRecycleAdmin);

        boolean isChange = !before.equals(after);
        if (isChange) {
            String oldRecycleName = shpUserShopRefService.getNameFromShop(getShopId(), oldRecycleAdmin);
            String newRecycleName = shpUserShopRefService.getNameFromShop(getShopId(), newRecycleAdmin);
            addProModifyRecord(proId, "修改", "回收人员", oldRecycleName, newRecycleName,source);
        }

        //数量和4个价格的操作记录
        equalsPriceAndNum(oldPro, newPro,source,newPro.getChangeInitPriceRemark());
        //适用人群
        changeAndSave(proId, "适用人群", oldPro.getTargetUser(), newPro.getTargetUser(),source);
        //独立编码
        changeAndSave(proId, "独立编码", oldDetail.getUniqueCode(), newPro.getUniqueCode(),source);
        //商品保卡

        changeAndSave(proId, "保卡", "0".equals(oldDetail.getRepairCard()) ? "没有" : "有", "0".equals(newPro.getRepairCard()) ? "没有" : "有",source);
        //保卡年份
        changeAndSave(proId, "保卡年份", oldDetail.getRepairCardTime(), newPro.getRepairCardTime(),source);
        //保卡照片
        if (null != newPro.getCardCodeImgUrl()) {
            changeAndSave(proId, "保卡图片", oldDetail.getCardCodeImg(), newPro.getCardCodeImgUrl(),source);
        }
        //商品备注
        changeAndSave(proId, "商品备注", oldPro.getRemark(), newPro.getRemark(),source);
        //同步友商
        changeAndSave(proId, "同步友商", "10".equals(oldPro.getShare()) ? "不分享" : "分享", "10".equals(newPro.getShareState()) ? "不分享" : "分享",source);
        //备注照片
        if (null != newPro.getRemarkImgUrl()) {
            changeAndSave(proId, "备注图片", oldDetail.getRemarkImgUrl(), newPro.getRemarkImgUrl(),source);
        }
        //品牌
        changeAndSave(proId, "品牌", oldPro.getFkProClassifySubName(), newPro.getClassifySub(),source);
        //系列
        changeAndSave(proId, "系列", oldPro.getFkProSubSeriesName(), newPro.getSubSeriesName(),source);
        //型号
        changeAndSave(proId, "型号", oldPro.getFkProSeriesModelName(), newPro.getSeriesModelName(),source);

    }
    /**
     * 添加商品价格和库存的操作记录
     *
     * @param oldPro
     * @param newPro
     */
    public void equalsPriceAndNum(ProProduct oldPro, ParamProductUpload newPro,String source,String initPriceRemark) {
        int proId = oldPro.getId();
        String initPriceAfter = formatPrice(newPro.getInitPrice()).toString();
        if (!LocalUtils.isEmptyAndNull(initPriceRemark)){
            initPriceAfter = initPriceAfter+"，成本价修改备注:"+initPriceRemark;
        }
        //库存
        changeAndSave(proId, "库存", LocalUtils.formatPrice(oldPro.getTotalNum()), LocalUtils.formatPrice(newPro.getTotalNum()),source);
        //成本价
        changeAndSave(proId, "成本价", formatPrice(oldPro.getInitPrice()),initPriceAfter ,source);
        //同行价
        changeAndSave(proId, "同行价", formatPrice(oldPro.getTradePrice()), formatPrice(newPro.getTradePrice()),source);
        //代理价
        changeAndSave(proId, "代理价", formatPrice(oldPro.getAgencyPrice()), formatPrice(newPro.getAgencyPrice()),source);
        //销售价
        changeAndSave(proId, "销售价", formatPrice(oldPro.getSalePrice()), formatPrice(newPro.getSalePrice()),source);

    }
    public BigDecimal formatPrice(Object object) {
        try {
            return LocalUtils.formatPrice(LocalUtils.calcNumber(LocalUtils.isEmptyAndNull(object) ? 0 : object, "*", 0.01));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new BigDecimal(0);
    }
    /**
     * 如果商品的值前后不一致,则添加操作记录
     *
     * @param proId
     * @param attributeName
     * @param beforeValue
     * @param afterValue
     */
    private void changeAndSave(int proId, String attributeName, Object beforeValue, Object afterValue,String source) {
        String before = LocalUtils.returnFormatString(beforeValue);
        String after = LocalUtils.returnFormatString(afterValue);
        if (!before.equals(after)) {
            //添加商品操作记录
            addProModifyRecord(proId, "修改", attributeName, beforeValue, afterValue,source);
        }
    }
    public static void main(String[] args) throws Exception {
        String temp = "@1";
        String[] temps = temp.split("@");
        System.out.println(temps.length);
    }

}
