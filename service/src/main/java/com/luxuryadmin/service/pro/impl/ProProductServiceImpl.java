package com.luxuryadmin.service.pro.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProDetail;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.entity.pro.ProTempProduct;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.mapper.pro.ProTempProductMapper;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamSpecificLeaguerProductQuery;
import com.luxuryadmin.param.data.ParamDataRecycleQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.ord.ParamOrdOrder;
import com.luxuryadmin.param.ord.ParamOrderDelete;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.data.OpDataSaleRankServiceImpl;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.impl.OrdOrderServiceImpl;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.vo.biz.VoCanSeeLeaguerPriceInfo;
import com.luxuryadmin.vo.data.VoRecycleClassifyHome;
import com.luxuryadmin.vo.data.VoRecycleProductList;
import com.luxuryadmin.vo.data.VoRecycleUserList;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.ord.VoOrder;
import com.luxuryadmin.vo.org.VoOrganizationTempPageByApp;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.share.VoShareShopProduct;
import com.luxuryadmin.vo.shp.VoEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author monkey king
 * @date 2019-12-23 16:34:07
 */
@Slf4j
@Service
public class ProProductServiceImpl implements ProProductService {
    @Autowired
    protected ServicesUtil servicesUtil;
    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private ProDetailService proDetailService;

    @Autowired
    private FinFundRecordService fundRecordService;
    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private BizLeaguerService bizLeaguerService;

    @Autowired
    private OpPushService opPushService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProTempService proTempService;

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Resource
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ProProductClassifyService proProductClassifyService;
    @Autowired
    private ProStandardService proStandardService;
    @Resource
    private ProTempProductMapper proTempProductMapper;

    @Override
    public ProProduct getProProductById(int id) {
        return (ProProduct) proProductMapper.getObjectById(id);
    }

    @Override
    public ProProduct getProProductByShopIdBizId(int shopId, String bizId) {
        return proProductMapper.getProProductByShopIdBizId(shopId, bizId);
    }

    @Override
    public ProProduct getProProductForDeleteByShopIdBizId(int shopId, String bizId) {
        return proProductMapper.getProProductForDeleteByShopIdBizId(shopId, bizId);
    }


    @Override
    public List<VoProductLoad> listProProductByVoProductQueryParam(ParamProductQuery queryParam) {
        //????????????????????????????????????????????????
        if (!LocalUtils.isEmptyAndNull(queryParam.getConveyState())) {
            List<String> conveyState = Arrays.asList(queryParam.getConveyState().split(","));
            if (conveyState.size() > 1) {
                queryParam.setConveyState(null);
            }
        }
        List<VoProductLoad> productLoads = proProductMapper.listProProductByVoProductQueryParam(queryParam);
        if (productLoads != null && productLoads.size() > 0) {
            productLoads.forEach(voProductLoad -> {
                String tempName = proTempService.getTempName(voProductLoad.getShopId(), voProductLoad.getProId());
                voProductLoad.setTempName(tempName);
                if (!LocalUtils.isEmptyAndNull(queryParam.getTempId())) {
                    Integer tempId = Integer.parseInt(queryParam.getTempId());
                    //??????????????????????????????????????????????????????
                    ProTempProduct tempProduct = proTempProductMapper.getProTempProduct(voProductLoad.getShopId(), tempId, voProductLoad.getProId());
                    if (!LocalUtils.isEmptyAndNull(tempProduct)) {
                        voProductLoad.setTempState("1");
                    }
                }

            });
        }
        return productLoads;
    }

    @Override
    public List<ExVoProduct> listProProductByVoProductQueryParamExcelExport(ParamProductQuery queryParam) {
        return proProductMapper.listProProductByVoProductQueryParamExcelExport(queryParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveProProductReturnId(ProProduct pro, ProDetail proDetail, String productClassifySunAddLists) {
        try {
            proProductMapper.saveObject(pro);
            int proId = pro.getId();
            proDetail.setFkProProductId(proId);
            proDetail.setFkShpShopId(pro.getFkShpShopId());
            proDetail.setInsertTime(pro.getInsertTime());
            long autoNumber = LocalUtils.calcNumber(proId, "+", 1000000).longValue();
            proDetail.setAutoNumber(autoNumber + "");
            proDetailService.saveProDetail(proDetail);
            //????????????????????????
            if (!LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
                //????????????????????????
                ParamProductClassifyAdd productClassifyAdd = new ParamProductClassifyAdd();
                productClassifyAdd.setProductId(proId);
                productClassifyAdd.setShopId(pro.getFkShpShopId());
                //String???Json???List?????????
                JSONArray jsonArray = JSONObject.parseArray(productClassifySunAddLists);
                List<ParamProductClassifySunAddList> classifyTypeSunAdds = JSONObject.parseArray(jsonArray.toJSONString(), ParamProductClassifySunAddList.class);
                if (!LocalUtils.isEmptyAndNull(classifyTypeSunAdds)) {
                    productClassifyAdd.setProductClassifySunAddLists(classifyTypeSunAdds);
                    productClassifyAdd.setUserId(pro.getUpdateAdmin());
                    proStandardService.addProductClassifyTypeList(productClassifyAdd);
                }
            }

            return proId;
        } catch (Exception e) {
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("??????????????????");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProProduct(ProProduct pro, ProDetail proDetail, String productClassifySunAddLists) {
        int rows = 0;
        try {
            rows = proProductMapper.updateProProductByShopIdBizId(pro);
            Integer proDetailId = proDetailService.getProDetailIdByShopIdAndBizId(pro.getFkShpShopId(), pro.getBizId());
            if (null == proDetailId) {
                throw new MyException("??????????????????: ?????????????????????!");
            }
            proDetail.setId(proDetailId);
            ProProduct dbPro = proProductMapper.getProProductByShopIdBizId(pro.getFkShpShopId(), pro.getBizId());
            long autoNumber = LocalUtils.calcNumber(dbPro.getId(), "+", 1000000).longValue();
            proDetail.setAutoNumber(autoNumber + "");
            proDetailService.updateProDetail(proDetail);
            if (!LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
                ProProduct product = getProProductByShopIdBizId(pro.getFkShpShopId(), pro.getBizId());
                //????????????????????????
                ParamProductClassifyAdd productClassifyAdd = new ParamProductClassifyAdd();
                productClassifyAdd.setProductId(product.getId());
                productClassifyAdd.setShopId(pro.getFkShpShopId());
                //String???Json???List?????????
                JSONArray jsonArray = JSONObject.parseArray(productClassifySunAddLists);
                List<ParamProductClassifySunAddList> classifyTypeSunAdds = JSONObject.parseArray(jsonArray.toJSONString(), ParamProductClassifySunAddList.class);
                //????????????????????????
                if (!LocalUtils.isEmptyAndNull(classifyTypeSunAdds)) {
                    productClassifyAdd.setProductClassifySunAddLists(classifyTypeSunAdds);
                    productClassifyAdd.setUserId(pro.getUpdateAdmin());
                    proStandardService.addProductClassifyTypeList(productClassifyAdd);
                } else {
                    proStandardService.deleteStandard(product.getId());
                }

            }

        } catch (Exception e) {
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("??????????????????");
        }
        return rows;
    }

    @Override
    public Integer updateProProductPrice(ParamProductUploadPrice productUploadPrice, Integer shopId, Integer userId) {
        ProProduct proProductOld = this.getProProductByShopIdBizId(shopId, productUploadPrice.getBizId());
        if (proProductOld == null) {
            throw new MyException("?????????????????????");
        }
        ProProduct proProduct = new ProProduct();
        proProduct.setBizId(productUploadPrice.getBizId());
        proProduct.setFkShpShopId(shopId);
        proProduct.setAgencyPrice(new BigDecimal(productUploadPrice.getAgencyPrice()));
        proProduct.setInitPrice(new BigDecimal(productUploadPrice.getInitPrice()));
        proProduct.setSalePrice(new BigDecimal(productUploadPrice.getSalePrice()));
        proProduct.setTradePrice(new BigDecimal(productUploadPrice.getTradePrice()));
        proProduct.setTotalNum(Integer.parseInt(productUploadPrice.getTotalNum()));
        proProductMapper.updateProProductByShopIdBizId(proProduct);
        //??????????????????
        Integer total = proProductOld.getTotalNum() - proProduct.getTotalNum();
        int compareResult = 0;
        //??????????????????????????????????????????0???????????????1??????????????????????????????-1???????????????????????????
        BigDecimal finishPriceOld = proProductOld.getInitPrice();
        BigDecimal finishPriceNew = proProduct.getInitPrice();
        BigDecimal changePrice = null;
        if (null != finishPriceNew) {
            changePrice = finishPriceNew.subtract(finishPriceOld);
            compareResult = finishPriceNew.compareTo(finishPriceOld);
        }
        if (compareResult != 0 || total != 0) {
            //??????????????????????????????
            ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
            paramFundRecordAddPro.setFkShpShopId(shopId);
            paramFundRecordAddPro.setUserId(userId);
            paramFundRecordAddPro.setMoney(proProductOld.getInitPrice().toString());
            paramFundRecordAddPro.setInitPrice(proProduct.getInitPrice().toString());
//            paramFundRecordAddPro.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAddPro.setFundType("10");
            paramFundRecordAddPro.setCount(proProductOld.getTotalNum().toString());
            paramFundRecordAddPro.setFinClassifyName("????????????????????????");
            paramFundRecordAddPro.setAttributeCode(proProductOld.getFkProAttributeCode());
            fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAddPro, proProduct.getTotalNum().toString());
        }

        //??????????????????-??????????????????-???????????????????????????
        //??????0????????????????????????????????????????????????
        if (compareResult != 0) {
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //?????????????????????????????????????????????
            if (compareResult == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //?????????????????????
            else if (compareResult == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            if (null != changePrice) {
                paramFinShopRecordAdd.setChangeAmount(changePrice.divide(new BigDecimal(100.00)).toString());
            }
            paramFinShopRecordAdd.setType("????????????");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "???" + proProductOld.getName() + "????????????????????????";
            paramFinShopRecordAdd.setNote(note);
            String imgUrl = proProductOld.getSmallImg();
            finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, proProductOld.getBizId());
        }

        return 0;
    }

    @Override
    public int deleteBatchProProductByShopIdBizId(int shopId, int userId, List<String> list, String deleteRemark) {

        if (list != null && list.size() > 0) {
            list.forEach(s -> {
                ProProduct proProduct = proProductMapper.getProProductForDeleteByShopIdBizId(shopId, s);
                //??????????????????????????????
                ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
                paramFundRecordAdd.setFkShpShopId(shopId);
                paramFundRecordAdd.setUserId(userId);
                paramFundRecordAdd.setMoney(proProduct.getInitPrice().toString());
                paramFundRecordAdd.setInitPrice("0");
                paramFundRecordAdd.setState(EnumFinShopRecordInoutType.IN.getCode());
                paramFundRecordAdd.setFundType("10");
                paramFundRecordAdd.setCount(proProduct.getTotalNum().toString());
                paramFundRecordAdd.setFinClassifyName("??????????????????");
                paramFundRecordAdd.setAttributeCode(proProduct.getFkProAttributeCode());
                fundRecordService.addProductFundRecord(paramFundRecordAdd);
            });
        }
        int result = proProductMapper.deleteBatchProProductByShopIdBizId(shopId, userId, list, deleteRemark);
        return result;
    }

    @Override
    public int releaseBatchProProductByShopIdBizId(int shopId, int userId, List<String> list) {
        int releaseSuccessCount = proProductMapper.releaseBatchProProductByShopIdBizId(shopId, userId, list);
        return releaseSuccessCount;
    }

    @Override
    public int backOffBatchProProductByShopIdBizId(int shopId, int userId, List<String> list) {
        return proProductMapper.backOffBatchProProductByShopIdBizId(shopId, userId, list);
    }

    @Override
    public int movePrivateProToStore(int shopId, int userId, List<String> list) {
        return proProductMapper.movePrivateProToStore(shopId, userId, list);
    }

    @Override
    public VoProductLoad getProductDetailByShopIdBizId(int shopId, String bizId) {
        return proProductMapper.getProductDetailByShopIdBizId(shopId, bizId);
    }

    @Override
    public VoProductLoad getProductDetailByShopIdId(int shopId, String autoNumber) {
        return proProductMapper.getProductDetailByShopIdId(shopId, autoNumber);
    }

    @Override
    public Integer countTodayUpload(ParamProductQuery queryParam) {
        return proProductMapper.countTodayUpload(queryParam);
    }

    @Override
    public Integer countTodayOnRelease(ParamProductQuery queryParam) {
        return proProductMapper.countTodayOnRelease(queryParam);
    }

    @Override
    public Integer countTodayExpire(ParamProductQuery queryParam) {
        return proProductMapper.countTodayExpire(queryParam);
    }

    @Override
    public Map<String, String> countLeftNumAndInitPrice(ParamProductQuery queryParam) {
        Map<String, String> stringStringMap = proProductMapper.countLeftNumAndInitPrice(queryParam);
        return stringStringMap;
    }

    @Override
    public VoProForAnalysisShow countClassifyNumAndPrice(int shopId, String attributeCode, Boolean flag) {

        List<VoProClassifyForAnalysis> classifyForAnalyses = proProductMapper.countClassifyNumAndPrice(shopId, attributeCode);
        VoProForAnalysisShow voProForAnalysisShow = new VoProForAnalysisShow();
        //????????????
        if (LocalUtils.isEmptyAndNull(classifyForAnalyses)) {
            return voProForAnalysisShow;
        }
        String showTotalPrice = "";
        //??????????????????????????????; ?????????
        if (flag != null && !flag) {
            for (VoProClassifyForAnalysis classifyForAnalysis : classifyForAnalyses) {
                classifyForAnalysis.setShowPrice("*****");
                classifyForAnalysis.setShowProportion("*****");
            }
            showTotalPrice = "*****";
        } else {
            //??????
            //????????????100??????
            for (VoProClassifyForAnalysis classifyForAnalysis : classifyForAnalyses) {
                if (LocalUtils.isEmptyAndNull(classifyForAnalysis.getTotalPrice())) {
                    classifyForAnalysis.setTotalPrice("0");
                }
                BigDecimal totalPrice = new BigDecimal(classifyForAnalysis.getTotalPrice()).divide(new BigDecimal(100));
                classifyForAnalysis.setTotalPriceForHide(totalPrice);
                String showPrice = DecimalFormatUtil.formatString(totalPrice, null);
                classifyForAnalysis.setShowPrice(showPrice);
            }
            //???????????????
            BigDecimal totalPrice = classifyForAnalyses.stream()
                    // ???user?????????age?????????map???Bigdecimal
                    .map(VoProClassifyForAnalysis::getTotalPriceForHide)
                    // ??????reduce()????????????,???????????????
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            //???????????????
            showTotalPrice = totalPrice.toString();

            //???????????????
            for (VoProClassifyForAnalysis classifyForAnalysis : classifyForAnalyses) {
                if (totalPrice.compareTo(BigDecimal.ZERO) == 0) {
                    classifyForAnalysis.setShowProportion("0.00");
                } else {
                    BigDecimal showProportion = classifyForAnalysis.getTotalPriceForHide().divide(totalPrice, 4).multiply(new BigDecimal(100));
                    classifyForAnalysis.setShowProportion(showProportion.toString());
                }

            }
        }

        voProForAnalysisShow.setShowTotalPrice(showTotalPrice);
        voProForAnalysisShow.setClassifyList(classifyForAnalyses);
        return voProForAnalysisShow;
    }

    @Override
    public List<VoProClassifyForAnalysis> countAttributeNumAndPrice(int shopId, Boolean flag, String totalPrice) {
        List<VoProClassifyForAnalysis> classifyForAnalyses = proProductMapper.countAttributeNumAndPrice(shopId);
        if (LocalUtils.isEmptyAndNull(classifyForAnalyses)) {
            return classifyForAnalyses;
        }
        BigDecimal showTotalPrice = new BigDecimal(0);
        if (!LocalUtils.isEmptyAndNull(totalPrice) && !"*****".equals(totalPrice)) {
            showTotalPrice = new BigDecimal(totalPrice);
        }
        BigDecimal finalShowTotalPrice = showTotalPrice;

        if (!flag) {
            classifyForAnalyses.forEach(voProClassifyForAnalysis -> {
                voProClassifyForAnalysis.setShowPrice("*****");
                voProClassifyForAnalysis.setShowProportion("*****");
            });
        } else {
            classifyForAnalyses.forEach(voProClassifyForAnalysis -> {
                BigDecimal totalNowPrice = new BigDecimal(voProClassifyForAnalysis.getTotalPrice()).divide(new BigDecimal(100));
                //??????????????????
                String showPrice = DecimalFormatUtil.formatString(totalNowPrice, null);
                voProClassifyForAnalysis.setShowPrice(showPrice);
                if (!(finalShowTotalPrice.compareTo(BigDecimal.ZERO) == 0)) {

                    BigDecimal showProportion = totalNowPrice.divide(finalShowTotalPrice, 4).multiply(new BigDecimal(100));
                    voProClassifyForAnalysis.setShowProportion(showProportion.toString());
                } else {
                    voProClassifyForAnalysis.setShowProportion("0.00");
                }
            });
        }
        return classifyForAnalyses;
    }


    @Override
    public List<VoLeaguerProduct> listBizLeaguerProduct(String shopIds, ParamLeaguerProductQuery queryParam) {
        return proProductMapper.listBizLeaguerProduct(shopIds, queryParam);
    }

    @Override
    public List<VoLeaguerProduct> listSpecificBizLeaguerProduct(ParamSpecificLeaguerProductQuery queryParam) {
        return proProductMapper.listSpecificBizLeaguerProduct(queryParam);
    }

    @Override
    public VoProductLoad getShareProductDetailByShopIdBizId(String shopId, String bizId) {
        return proProductMapper.getShareProductDetailByShopIdBizId(shopId, bizId);
    }

    @Override
    public VoProductLoad getShareProductDetailByShopIdBizId(String bizId) {
        return proProductMapper.getShareProductDetailByShopIdBizId(null, bizId);
    }

    @Override
    public boolean existsProductByBizId(int shopId, String bizId) {
        return proProductMapper.existsProductByBizId(shopId, bizId) > 0;
    }

    @Override
    public VoProRedisNum getProRedisNum(int shopId, String bizId) {
        VoProRedisNum proRedisNum = new VoProRedisNum();
        //????????????????????????+??????????????????
        int leftNum = 0;
        int lockNum = 0;
        String lockRedisKey = ConstantRedisKey.getLockProductByBizId(shopId, bizId);
        String lockRedisValue = redisUtil.get(lockRedisKey);
        JSONObject lockJson;
        if (!LocalUtils.isEmptyAndNull(lockRedisValue)) {
            lockJson = JSONObject.parseObject(lockRedisValue);
            leftNum = Integer.parseInt(lockJson.get(ConstantRedisKey.LEFT_NUM).toString());
            lockNum = Integer.parseInt(lockJson.get(ConstantRedisKey.LOCK_NUM).toString());
        } else {
            //??????redis?????????;???????????????????????????redis,????????????????????????????????????????????????;??????????????????
            ProProduct product = getProProductByShopIdBizId(shopId, bizId);
            if (!LocalUtils.isEmptyAndNull(product)) {
                Integer stateCode = product.getFkProStateCode();
                boolean isBetween = LocalUtils.isBetween(stateCode, 20, 39);
                if (isBetween) {
                    int totalNumDb = product.getTotalNum();
                    //??????????????????????????????
                    lockNum = proLockRecordService.getProductLockNumByProId(product.getId());
                    leftNum = totalNumDb - lockNum;
                    if (leftNum < 0) {
                        log.info("???DB??????????????????????????????{}:{}", totalNumDb, lockNum);
                        throw new MyException("???DB??????????????????????????????" + totalNumDb + ":" + lockNum);
                    }
                } else if (LocalUtils.isBetween(stateCode, 10, 19)) {
                    //???????????????????????????????????????,?????????????????????????????????
                    leftNum = product.getTotalNum();
                }
                lockJson = new JSONObject();
                lockJson.put(ConstantRedisKey.LEFT_NUM, leftNum);
                lockJson.put(ConstantRedisKey.LOCK_NUM, lockNum);
                redisUtil.setEx(lockRedisKey, lockJson.toString(), 10);
            }
        }
        proRedisNum.setLeftNum(leftNum);
        proRedisNum.setLockNum(lockNum);
        int totalNum = leftNum + lockNum;
        proRedisNum.setTotalNum(totalNum);
        return proRedisNum;
    }

    @Override
    public VoProRedisNum subtractProRedisLeftNum(int shopId, String bizId, int leftNum) {
        VoProRedisNum proRedisNum = getProRedisNum(shopId, bizId);
        int leftNumRedis = proRedisNum.getLeftNum();
        int totalNumRedis = proRedisNum.getTotalNum();
        int newLeftNum = leftNumRedis - leftNum;
        int newTotalNum = totalNumRedis - leftNum;
        if (newLeftNum < 0 || newTotalNum < 0) {
            throw new MyException("???????????????" + leftNumRedis + ":" + leftNum);
        }
        proRedisNum.setLeftNum(newLeftNum);
        proRedisNum.setTotalNum(newTotalNum);
        String lockRedisKey = ConstantRedisKey.getLockProductByBizId(shopId, bizId);
        if (newLeftNum == 0 && proRedisNum.getLockNum() == 0) {
            //????????????????????????0;???????????????,??????redis??????????????????????????????
            redisUtil.delete(lockRedisKey);
        } else {
            String lockRedisValue = redisUtil.get(lockRedisKey);
            JSONObject lockJson = JSONObject.parseObject(lockRedisValue);
            lockJson.put(ConstantRedisKey.LEFT_NUM, newLeftNum);
            lockJson.put(ConstantRedisKey.TOTAL_NUM, newTotalNum);
            redisUtil.setEx(lockRedisKey, lockJson.toString(), 10);
        }
        return proRedisNum;
    }

    @Override
    public VoProRedisNum updateProRedisLockNum(int shopId, String bizId, int myLockNum, boolean isLock) {
        String lockRedisKey = ConstantRedisKey.getLockProductByBizId(shopId, bizId);
        VoProRedisNum proRedisNum = getProRedisNum(shopId, bizId);
        //????????????redis
        int leftNum = proRedisNum.getLeftNum();
        int lockNum = proRedisNum.getLockNum();
        //?????????????????????????????????
        if (isLock) {
            //??????
            leftNum -= myLockNum;
            lockNum += myLockNum;
        } else {
            //??????
            leftNum += myLockNum;
            lockNum -= myLockNum;
        }
        JSONObject lockJson = new JSONObject();
        lockJson.put(ConstantRedisKey.LEFT_NUM, leftNum);
        lockJson.put(ConstantRedisKey.LOCK_NUM, lockNum);
        redisUtil.setEx(lockRedisKey, lockJson.toString(), 10);
        proRedisNum.setLeftNum(leftNum);
        proRedisNum.setLockNum(lockNum);
        int totalNum = leftNum + lockNum;
        proRedisNum.setTotalNum(totalNum);
        return proRedisNum;
    }

    @Override
    public void clearProLockNumByBizId(int shopId, String bizId) {
        //1.???????????????db????????????;
        proLockRecordService.deleteProLockRecord(shopId, bizId);
        deleteProRedisLockNum(shopId, bizId);
        //2.???????????????redis????????????
    }

    /**
     * ??????Redis??????????????????
     *
     * @param shopId
     * @param bizId
     */
    @Override
    public void deleteProRedisLockNum(int shopId, String bizId) {
        String lockProKey = ConstantRedisKey.getLockProductByBizId(shopId, bizId);
        redisUtil.delete(lockProKey);
    }

    @Override
    public List<VoProduct> queryShpUserRelList(ParamProProduct paramProProduct) {
        long st = System.currentTimeMillis();
        List<VoProduct> voProducts = proProductMapper.listShpUserRel(paramProProduct);
        long et = System.currentTimeMillis();
        log.info("========queryShpUserRelList: {}", et - st);
        return voProducts;
    }

    @Override
    public VoProduct getProProductInfo(String id) {
        VoProduct voProduct = proProductMapper.getProProductInfo(LocalUtils.strParseInt(id));
        return voProduct;
    }

    @Override
    public void updateShpShop(ProProduct proProduct) {
        proProductMapper.updateObject(proProduct);
    }

    @Override
    public VoLeaguerProduct getBizLeaguerProductDetailByBizId(String bizId, Integer leaguerShopId, Integer shopId, String type) {
        VoLeaguerProduct voLeaguerProduct = proProductMapper.getBizLeaguerProductDetailByBizId(bizId);
        if (LocalUtils.isEmptyAndNull(voLeaguerProduct)) {
            return null;
        }
        if (!LocalUtils.isEmptyAndNull(type) && "union".equals(type)) {
            VoCanSeeLeaguerPriceInfo canSeeLeaguerPriceInfo = bizLeaguerService.isCanSeePriceForUnion(leaguerShopId, -9);
            //?????????????????????????????????; ?????????????????????;2021-07-31 00:35:51??????????????????;
            voLeaguerProduct.setSalePrice(null);
        } else {
            //???????????????????????????
            //????????????ID 10052 ????????????ID 10087
            Boolean isCanSeeTradePrice = bizLeaguerService.isCanSeeTradePrice(leaguerShopId, shopId);
            //???????????????????????????????????????????????????????????????
            isCanSeeTradePrice = null == isCanSeeTradePrice ? Boolean.FALSE : isCanSeeTradePrice;
            if (!isCanSeeTradePrice) {
                voLeaguerProduct.setTradePrice(null);
            }

            //???????????????????????????
            Boolean isCanSeeSalePrice = bizLeaguerService.isCanSeeSalePrice(leaguerShopId, shopId);
            //???????????????????????????????????????????????????????????????
            if (!isCanSeeSalePrice) {
                voLeaguerProduct.setSalePrice(null);
            }
        }

        return voLeaguerProduct;
    }

    @Override
    public VoLeaguerProduct getUnionProductDetailByBizId(String bizId, Integer leaguerShopId) {
        VoLeaguerProduct voLeaguerProduct = proProductMapper.getBizLeaguerProductDetailByBizId(bizId);
        if (!LocalUtils.isEmptyAndNull(voLeaguerProduct)) {
            //????????????????????????????????????
            voLeaguerProduct.setSalePrice(null);
        }
        return voLeaguerProduct;
    }

    @Override
    public Integer getOnSaleProductNumByShopId(String shopId) {
        return proProductMapper.selectOnSaleProductNumByShopId(shopId);
    }

    @Override
    public Integer getOnSaleProductNumByShopIdByAdmin(Integer shopId) {
        return proProductMapper.countOnSaleProductNumByShopIdByAdmin(shopId);
    }

    @Override
    public Integer getShopIdByBizId(String bizId) {
        return proProductMapper.selectShopIdByBizId(bizId);
    }

    @Override
    public List<ProProduct> listAllShopProProduct() {
        return proProductMapper.listAllShopProProduct();
    }

    @Override
    public void updateProProduct(ProProduct product) {
        proProductMapper.updateObject(product);
    }

    @Override
    public Integer getAllUploadProductNumByShopId(Integer shopId) {
        return proProductMapper.countAllUploadProductNumByShopId(shopId);
    }

    @Override
    public List<VoProductAnalyzeDetail> countProductAnalyzeDetail(int shopId) {
        List<VoProductAnalyzeDetail> detailList = proProductMapper.countProductAnalyzeDetail(shopId);
        List<VoProductAnalyzeDetail> detailListAll = proProductMapper.countProductAnalyzeDetailAllClassify(shopId);
        //detailListAll.get(0).setProdTypeName("??????");
        detailList.addAll(detailListAll);
        return detailList;
    }

    /**
     * ??????????????????
     *
     * @param proRedeem
     * @return
     */
    @Override
    public int redeemProduct(ProProduct proRedeem, Integer userId) {
        /**
         * ??????
         *  finish_price??????????????????
         *  fk_pro_state_code??????44????????????????????????
         */
        proRedeem.setFkProStateCode(EnumProState.SALE_44.getCode());
        proRedeem.setUpdateTime(new Date());
        proRedeem.setUpdateAdmin(userId);
        proProductMapper.updateProProductByShopIdBizId(proRedeem);
        return proRedeem.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dailyExpireProd(Date jobDayStart) {
        /**
         * ?????????????????????????????????bizId
         */
        List<VoExpireProd> expireProdList = proProductMapper.selectAllExpireProdBizId(jobDayStart);
        System.out.println("proBizIdList=" + expireProdList);

        proProductMapper.updateProdExpire(jobDayStart);

        /**
         * ??????????????????????????????????????????
         */
        opPushService.pushExpireProductMsg(expireProdList);
    }

    @Override
    public Integer countProductByStateCode(Integer shopId, String stateCode) {
        return proProductMapper.countProductByStateCode(shopId, stateCode);
    }

    /**
     * ?????????????????????????????????????????????URL
     *
     * @param queryParam
     * @return
     */
    @Override
    public String getFirstProdImg(ParamProductQuery queryParam) {
        return proProductMapper.getFirstProdImg(queryParam);
    }

    @Override
    public void deleteProProductByShopId(int shopId) {
        proProductMapper.deleteProProductByShopId(shopId);
    }

    @Override
    public void deleteProProductById(String ids) {
        proProductMapper.deleteProProductByIds(ids);
        proDetailService.deleteProDetailByProIds(ids);
    }

    @Override
    public Map<String, Object> countRecycleInitPriceAndNum(
            int shopId, int userId, String proAttr, String startDateTime, String endDateTime) {
        return proProductMapper.countRecycleInitPriceAndNum(shopId, userId, proAttr, startDateTime, endDateTime);
    }

    /**
     * ???????????????????????????;
     * 1.????????????,??????,??????;<br/>
     * 2.????????????,????????????,????????????,??????,??????(????????????);<br/>
     * ??????: ???????????????,?????????????????????;<br/>
     * ??????????????????,?????????,??????????????????;<br/>
     * ?????????????????????????????????,??????????????????;
     *
     * @param voPro
     * @param isSave true:?????? | false:??????
     * @return
     */
    @Override
    public ProProduct pickProProduct(ParamProductUpload voPro, boolean isSave, Integer shopId, Integer userId) {
        //??????????????????
        ProProduct product = new ProProduct();
        product.setBizId(voPro.getBizId());
        Date newDate = new Date();
        if (isSave) {
            product.setBizId(LocalUtils.getUUID());
            product.setHot("0");
            product.setFinishPrice(new BigDecimal(0));
            if (!LocalUtils.isEmptyAndNull(product.getInsertTime())) {
                newDate = product.getInsertTime();
            }
            product.setInsertTime(newDate);
            product.setInsertAdmin(userId);
            product.setVersions(1);
            product.setDel("0");
        }
        product.setShare(LocalUtils.isEmptyAndNull(voPro.getShareState()) ? "22" : voPro.getShareState());
        product.setUpdateTime(newDate);
        product.setUpdateAdmin(userId);
        product.setFkShpShopId(shopId);
        //?????????????????????????????? ????????? ?????? ?????????
        int state = Integer.parseInt(voPro.getState());
        product.setFkProStateCode(state);

        if (LocalUtils.isBetween(state, 10, 19)) {
            //?????????,??????????????????
            product.setReleaseTime(null);
        } else if (LocalUtils.isBetween(state, 20, 29)) {
            //?????????,????????????????????????
            product.setReleaseTime(newDate);
        }

        product.setFkProAttributeCode(voPro.getAttribute());
        product.setFkProClassifyCode(LocalUtils.isEmptyAndNull(voPro.getClassify()) ? "QT" : voPro.getClassify());
        String name = LocalUtils.returnEmptyStringOrString(voPro.getName());
        String description = LocalUtils.returnEmptyStringOrString(voPro.getDescription());
        //???????????????????????????;\u200B???
        //name = name.replaceAll("\\p{C}", "");
        //description = description.replaceAll("\\p{C}", "");

        //?????????2020-12-07 23:19:40; ????????????????????????,??????emoji??????????
        name = name.replaceAll("\u200B", "");
        description = description.replaceAll("\u200B", "");
        //??????????????????????????????,????????????????????????50?????????;
        if (LocalUtils.isEmptyAndNull(name) && !LocalUtils.isEmptyAndNull(description)) {
            int length = description.length();
            //??????20?????????,?????????????????????
            name = description.substring(0, Math.min(length, 50));
        }
        product.setName(LocalUtils.isEmptyAndNull(name) ? "???????????????????????????" : name);
        product.setDescription(description);
        product.setQuality(voPro.getQuality());
        product.setTargetUser(voPro.getTargetUser());
        product.setTag(voPro.getTag());
        product.setTotalNum(Integer.parseInt(voPro.getTotalNum()));
        //??????(???)??????
        product.setInitPrice(LocalUtils.formatBigDecimal(voPro.getInitPrice()));
        product.setTradePrice(LocalUtils.formatBigDecimal(voPro.getTradePrice()));
        product.setAgencyPrice(LocalUtils.formatBigDecimal(voPro.getAgencyPrice()));
        product.setSalePrice(LocalUtils.formatBigDecimal(voPro.getSalePrice()));
        product.setSmallImg(voPro.getSmallImgUrl());
        product.setRemark(voPro.getRemark());
        Integer recycleAdmin = voPro.getRecycleAdmin();
        product.setRecycleAdmin(LocalUtils.isEmptyAndNull(recycleAdmin) ? 0 : recycleAdmin);
        String classifySubName = voPro.getClassifySub();
        //??????????????????????????????id
        product.setFkProClassifySubName(classifySubName);

        //????????????????????????
        product.setFkProSubSeriesName(voPro.getSubSeriesName());
        product.setFkProSeriesModelName(voPro.getSeriesModelName());
        String saveEndTime = voPro.getSaveEndTime();

        String attribute = EnumProAttribute.PAWN.getCode().toString();
        if (attribute.equals(product.getFkProAttributeCode())) {
            if (LocalUtils.isEmptyAndNull(saveEndTime)) {
                throw new MyException("?????????????????????????????????");
            }
            Date date;
            try {
                date = DateUtil.parseShort(saveEndTime);
            } catch (Exception e) {
                throw new MyException("????????????????????????????????????");
            }
            //2.6.4????????????????????????????????????;
//            if (date.before(newDate)) {
//                throw new MyException("????????????????????????????????????????????????");
//            }
            product.setSaveEndTime(date);
            //?????????(????????????????????????)
            product.setFkProStateCode(12);
        }
        return product;
    }

    /**
     * ???????????????????????????;
     *
     * @param voPro
     * @param isSave isSave true:?????? | false:??????
     * @return
     */
    @Override
    public ProDetail pickProDetail(ParamProductUpload voPro, boolean isSave) {
        String repCard = voPro.getRepairCard();
        //String repairCard = LocalUtils.isEmptyAndNull(repCard) || ConstantCommon.ZERO.equals(repCard) ? "0" : "1";
        String repairCardTime = voPro.getRepairCardTime();
        ProDetail proDetail = new ProDetail();
        if (isSave) {
            proDetail.setVersions(1);
            proDetail.setDel("0");
        }
        proDetail.setAccessory(voPro.getAccessory());
        proDetail.setSource(voPro.getSource());

        String cName = voPro.getCName();
        cName = null == cName ? "" : cName;
        String cPhone = voPro.getCPhone();
        cPhone = null == cPhone ? "" : cPhone;
        String cRemark = voPro.getCRemark();
        cRemark = null == cRemark ? "" : cRemark;
        proDetail.setCustomerName(cName);
        proDetail.setCustomerPhone(cPhone);
        proDetail.setCustomerRemark(cRemark);
        //2020-09-02 sanjin145
        String customerInfo = voPro.getCustomerInfo();
        if (LocalUtils.isEmptyAndNull(customerInfo)) {
            customerInfo = cName + cPhone + cRemark;
        }
        proDetail.setCustomerInfo(customerInfo);
        //???????????????; ?????????????????????????????????,????????????"????????????";
        if (ConstantCommon.ONE.equals(repCard)) {
            //?????????,???????????????????????????????????????;
            if (LocalUtils.isEmptyAndNull(repairCardTime)) {
                repairCardTime = "????????????";
            }
            proDetail.setRepairCardTime(repairCardTime);
        }
        proDetail.setRepairCard(repCard);
        proDetail.setUniqueCode(voPro.getUniqueCode());
        proDetail.setCardCodeImg(voPro.getCardCodeImgUrl());
        proDetail.setProductImg(voPro.getProImgUrl());
        proDetail.setVideoUrl(voPro.getVideoUrl());
        proDetail.setRemarkImgUrl(voPro.getRemarkImgUrl());
        return proDetail;
    }

    @Override
    public void oneKeyReleaseProduct(int shopId, int userId) {
        proProductMapper.oneKeyReleaseProduct(shopId, userId);
    }

    @Override
    public void oneKeyBackOffProduct(int shopId, int userId) {
        proProductMapper.oneKeyBackOffProduct(shopId, userId);
    }

    @Override
    public List<String> listOnSaleProductBizId(int shopId) {
        return proProductMapper.listOnSaleProductBizId(shopId);
    }

    @Override
    public List<VoShareShopProduct> getShareShopProductListByShopId(Integer shopNumber) {
        List<VoShareShopProduct> shareShopProducts = proProductMapper.getShareShopProductListByShopId(shopNumber);
        if (shareShopProducts != null && shareShopProducts.size() > 0) {
            shareShopProducts.forEach(shareShopProduct -> {
                //?????????
                String smallImg = shareShopProduct.getSmallImg();
                if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                    shareShopProduct.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
                }
                //??????????????????
//                String targetUser = (LocalUtils.isEmptyAndNull(shareShopProduct.getTargetUser()) || "??????".equals(shareShopProduct.getTargetUser())) ? null : LocalUtils.formatParamForSqlInQuery(shareShopProduct.getTargetUser());
//                shareShopProduct.setTargetUser(targetUser);
            });
        }
        return shareShopProducts;
    }

    @Override
    public void updateShareStare(Integer shopId, String share) {
        proProductMapper.updateShareStare(shopId, share);
    }

    @Override
    public List<VoRecycleUserList> getRecycleUserList(ParamDataRecycleQuery dataRecycleQuery, Page page) {
        List<VoRecycleUserList> recycleUserLists = proProductMapper.getRecycleUserList(dataRecycleQuery);

        if (recycleUserLists != null && recycleUserLists.size() > 0) {
            String sortType = dataRecycleQuery.getSortType();
            Integer index = 0;
            for (VoRecycleUserList voRecycleUserList : recycleUserLists) {
                index = setRanking(page, sortType, index, voRecycleUserList);
                //?????????
                String headImgUrl = voRecycleUserList.getHeadImgUrl();
                if (!LocalUtils.isEmptyAndNull(headImgUrl) && !headImgUrl.contains("http")) {
                    voRecycleUserList.setHeadImgUrl(servicesUtil.formatImgUrl(headImgUrl, true));
                }
                if (voRecycleUserList.getInitAllPrice() == null) {
                    voRecycleUserList.setInitAllPrice(new BigDecimal(0));
                }
                if (voRecycleUserList.getRecycleCount() == null) {
                    voRecycleUserList.setRecycleCount(0);
                }
            }
        }
        return recycleUserLists;
    }

    private static Integer setRanking(Page page, String sortType, Integer index, VoRecycleUserList recycleUserList) {
        //????????????
        index++;
        if (StringUtil.isBlank(sortType) || "desc".equals(sortType)) {
            recycleUserList.setRanking(page.getStartRow() + index);
        } else if (StringUtil.isNotBlank(sortType) && "asc".equals(sortType)) {
            Integer ranking = (new Long(page.getTotal() - (page.getPageNum() * page.getPageNum() + index - 2))).intValue();
            recycleUserList.setRanking(ranking);
        }
        return index;
    }

    @Override
    public List<VoRecycleProductList> getRecycleProductList(ParamDataRecycleQuery dataRecycleQuery, boolean hasPermInitPrice) {

        List<VoRecycleProductList> recycleProductLists = proProductMapper.getRecycleProductList(dataRecycleQuery);
        if (LocalUtils.isEmptyAndNull(recycleProductLists)) {
            return recycleProductLists;
        }
        recycleProductLists.forEach(recycleProductList -> {
            //?????????
            String smallImg = recycleProductList.getSmallImg();
            recycleProductList.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

            if (!hasPermInitPrice) {
                recycleProductList.setInitPrice("******");
            }
            if (!LocalUtils.isEmptyAndNull(recycleProductList.getInsertTime())) {
                recycleProductList.setShowTime("????????????:" + DateUtil.format(recycleProductList.getInsertTime()));
            }
        });
        return recycleProductLists;
    }

    @Override
    public List<VoRecycleClassifyHome> getRecycleClassifyHome(ParamDataRecycleQuery dataRecycleQuery) {
        List<VoRecycleClassifyHome> recycleClassifyHomes = proProductMapper.getRecycleClassifyHome(dataRecycleQuery);
        recycleClassifyHomes.forEach(recycleClassifyHome -> {
            dataRecycleQuery.setClassifyCode(recycleClassifyHome.getClassifyCode());
            Map<String, Object> classifyCount = proProductMapper.getRecycleClassifyCount(dataRecycleQuery);
            BigDecimal totalNum = new BigDecimal(0);
            if (!LocalUtils.isEmptyAndNull(classifyCount)) {
                totalNum = (BigDecimal) classifyCount.get("totalNum");
            }
            DecimalFormat df = new DecimalFormat(",##0.##");
            recycleClassifyHome.setClassifyCount(Integer.parseInt(df.format(totalNum)));
            recycleClassifyHome.setClassifyCodeImg(getRecycleClassifyImg(recycleClassifyHome.getClassifyCode()));
        });
        return recycleClassifyHomes;
    }

    @Override
    public Map<String, Object> getRecycleClassifyCount(ParamDataRecycleQuery dataRecycleQuery) {
        Map<String, Object> classifyCount = proProductMapper.getRecycleClassifyCount(dataRecycleQuery);
        return classifyCount;
    }

    @Override
    public VoProductOrOrderForDeletePage getProductOrOrderForDeletePage(ParamProductOrOrderForDeleteSearch productOrOrderForDeleteSearch) {
        List<VoProductOrOrderForDelete> productOrOrderForDeletes = new ArrayList<>();
        PageHelper.startPage(Integer.parseInt(productOrOrderForDeleteSearch.getPageNum()), productOrOrderForDeleteSearch.getPageSize());
        //??????
        if ("1".equals(productOrOrderForDeleteSearch.getDeleteType())) {
            productOrOrderForDeletes = proProductMapper.getProductForDeleteList(productOrOrderForDeleteSearch);
        }
        //??????
        if ("2".equals(productOrOrderForDeleteSearch.getDeleteType())) {
            productOrOrderForDeletes = ordOrderMapper.getOrderForDeleteList(productOrOrderForDeleteSearch);
        }
        Integer shopId = productOrOrderForDeleteSearch.getShopId();
        int userId = productOrOrderForDeleteSearch.getUserId();
        if (productOrOrderForDeletes != null && productOrOrderForDeletes.size() > 0) {
            productOrOrderForDeletes.forEach(productOrOrderForDelete -> {
                String smallImg = productOrOrderForDelete.getSmallImg();

                smallImg = servicesUtil.formatImgUrl(smallImg, true);

                productOrOrderForDelete.setSmallImg(smallImg);
                if ("1".equals(productOrOrderForDeleteSearch.getDeleteType())) {
                    boolean hasPermission = true;
                    //???????????????
                    String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showInitPrice);
                    productOrOrderForDelete.setInitPrice(hasPermission ? productOrOrderForDelete.getInitPrice() : null);
                    //???????????????
                    String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showTradePrice);
                    productOrOrderForDelete.setTradePrice(hasPermission ? productOrOrderForDelete.getTradePrice() : null);
                    //???????????????
                    String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showAgencyPrice);
                    productOrOrderForDelete.setAgencyPrice(hasPermission ? productOrOrderForDelete.getAgencyPrice() : null);
                    //???????????????
                    String showSalePrice = ConstantPermission.CHK_PRICE_SALE;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showSalePrice);
                    productOrOrderForDelete.setSalePrice(hasPermission ? productOrOrderForDelete.getSalePrice() : null);
                }
                if ("2".equals(productOrOrderForDeleteSearch.getDeleteType())) {
                    boolean hasPermission = true;
                    //???????????????
                    String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showInitPrice);
                    productOrOrderForDelete.setInitPrice(hasPermission ? productOrOrderForDelete.getInitPrice() : null);
                    //???????????????
                    String finishPrice = ConstantPermission.CHK_PRICE_FINISH;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, finishPrice);
                    productOrOrderForDelete.setFinishPrice(hasPermission ? productOrOrderForDelete.getFinishPrice() : null);
                    String stateCn = productOrOrderForDelete.getState();
                    productOrOrderForDelete.setState(stateCn);
                    if ("20".equals(stateCn)) {
                        stateCn = "?????????";
                    } else if ("-20".equals(stateCn)) {
                        stateCn = "?????????";
                    } else if ("-90".equals(stateCn)) {
                        stateCn = "?????????";
                    }
                    productOrOrderForDelete.setStateCn(stateCn);
                    productOrOrderForDelete.setEntrustStateName(OrdOrderServiceImpl.getEntrustState(productOrOrderForDelete.getEntrustState()));
                }
                Integer deleteUser = productOrOrderForDelete.getDeleteAdmin();
                if (deleteUser == userId) {
                    productOrOrderForDelete.setUpdateDeleteState("1");
                } else {
                    productOrOrderForDelete.setUpdateDeleteState("0");
                }
                //????????????
                productOrOrderForDelete.setAttributeShortCn(servicesUtil.getAttributeCn(productOrOrderForDelete.getAttributeCn(), false));
            });
        }
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(productOrOrderForDeletes);
        VoProductOrOrderForDeletePage voProductOrOrderForDeletePage = new VoProductOrOrderForDeletePage();
        voProductOrOrderForDeletePage.setList(productOrOrderForDeletes);
        voProductOrOrderForDeletePage.setPageNum(pageInfo.getPageNum());
        voProductOrOrderForDeletePage.setPageSize(pageInfo.getPageSize());
        //??????
        productOrOrderForDeleteSearch.setPageSize(1);
        productOrOrderForDeleteSearch.setPageNum(null);
        //????????????
        ParamProductOrOrderForDeleteSearch orderForDeleteSearch = new ParamProductOrOrderForDeleteSearch();
        if ("1".equals(productOrOrderForDeleteSearch.getDeleteType())) {
            orderForDeleteSearch.setShopId(productOrOrderForDeleteSearch.getShopId());
        } else {
            BeanUtils.copyProperties(productOrOrderForDeleteSearch, orderForDeleteSearch);
        }

        Integer orderCount = ordOrderMapper.getOrderForDeleteCount(orderForDeleteSearch);
        if (orderCount == null) {
            orderCount = 0;
        }
        voProductOrOrderForDeletePage.setTotalOrder((long) orderCount);
        //??????
        //????????????
        ParamProductOrOrderForDeleteSearch productForDeleteSearch = new ParamProductOrOrderForDeleteSearch();
        if ("2".equals(productOrOrderForDeleteSearch.getDeleteType())) {
            productForDeleteSearch.setShopId(productOrOrderForDeleteSearch.getShopId());
        } else {
            BeanUtils.copyProperties(productOrOrderForDeleteSearch, productForDeleteSearch);
        }
        Integer productForDeleteCount = proProductMapper.getProductForDeleteCount(productForDeleteSearch);
        if (productForDeleteCount == null) {
            productForDeleteCount = 0;
        }
        voProductOrOrderForDeletePage.setTotalProduct((long) productForDeleteCount);
        if (pageInfo.getNextPage() > 0) {
            voProductOrOrderForDeletePage.setHasNextPage(true);
        } else {
            voProductOrOrderForDeletePage.setHasNextPage(false);
        }
        return voProductOrOrderForDeletePage;
    }

    @Override
    public void deleteProductOrOrder(ParamProductOrOrderForDelete productOrOrderForDelete, HttpServletRequest request) {
        LocalUtils.formatParamForSqlInQuery(productOrOrderForDelete.getBizId());
        //??????
        if ("1".equals(productOrOrderForDelete.getDeleteType())) {
            //???????????????????????????list
            List<String> bizIdList = Arrays.asList(productOrOrderForDelete.getBizId().split(";"));
            if (bizIdList != null && bizIdList.size() > 0) {
                bizIdList.forEach(bizId -> {
                    //???????????? ???????????? del=1
                    ProProduct product = proProductMapper.getProProductForDeleteByShopIdBizId(productOrOrderForDelete.getShopId(), bizId);
                    if (product != null) {
                        product.setDel("1");
                        proProductMapper.updateObject(product);
                    }
                });
            }


        }
        //??????
        if ("2".equals(productOrOrderForDelete.getDeleteType())) {
            //???????????????????????????list
            ParamOrderDelete delete = new ParamOrderDelete();
            delete.setShopId(productOrOrderForDelete.getShopId());

            List<String> orderNumberList = Arrays.asList(productOrOrderForDelete.getOrderNumber().split(";"));
            if (orderNumberList != null && orderNumberList.size() > 0) {
                orderNumberList.forEach(orderNumber -> {
                    //???????????? ????????????
                    delete.setOrderBizId(orderNumber);
                    ordOrderService.deleteOrderForDel(delete);

                });
            }
        }
    }

    @Override
    public void updateProductOrOrder(ParamProductOrOrderForUpdate productOrOrderForUpdate, HttpServletRequest request) {
        //??????
        if ("1".equals(productOrOrderForUpdate.getDeleteType())) {
            //???????????????????????? ????????????????????????
            ProProduct product = proProductMapper.getProProductForDeleteByShopIdBizId(productOrOrderForUpdate.getShopId(), productOrOrderForUpdate.getBizId());
            int deleteUser = product.getUpdateAdmin();
            int updateUser = productOrOrderForUpdate.getUserId();
            if (deleteUser != updateUser) {
                throw new MyException("??????????????????????????? ??????????????????");
            }
            product.setDeleteRemark(productOrOrderForUpdate.getDeleteRemark());
            proProductMapper.updateObject(product);
        }
        //??????
        if ("2".equals(productOrOrderForUpdate.getDeleteType())) {
            //???????????????????????? ????????????????????????
            OrdOrder order = ordOrderService.getOrdOrderDetail(productOrOrderForUpdate.getShopId(), productOrOrderForUpdate.getOrderNumber());
            int deleteUser = order.getUpdateAdmin();
            int updateUser = productOrOrderForUpdate.getUserId();
            if (deleteUser != updateUser) {
                throw new MyException("??????????????????????????? ??????????????????");
            }
            order.setDeleteRemark(productOrOrderForUpdate.getDeleteRemark());
            ordOrderMapper.updateObject(order);
        }
    }

    @Override
    public void recycleProductAdd(ParamRecycleProductAdd recycleProductAdd, String recycleState) {
        ProProduct product = proProductMapper.getProProductByShopIdBizId(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());

        if (product == null) {
            throw new MyException("???????????????");
        }

        //??????????????????????????? ??????????????????
        proLockRecordService.deleteProLockRecord(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //????????????????????????
        deleteProRedisLockNum(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //????????????redis??????????????????
        this.getProRedisNum(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //????????? ??????????????????
        int recycleUser = 0;
        if (product.getFkShpRetrieveUserId() != null) {
            recycleUser = product.getFkShpRetrieveUserId();
        }
        Integer userId = recycleProductAdd.getUserId();
        if ("1".equals(recycleState) && userId != recycleUser) {
            throw new MyException("??????????????????????????? ??????????????????");
        }
        product.setFkProStateCode(-90);
        product.setRetrieveRemark(recycleProductAdd.getRetrieveRemark());
        product.setFkShpRetrieveUserId(recycleProductAdd.getUserId());
        product.setRetrieveTime(new Date());
        proProductMapper.updateObject(product);

    }

    public String getRecycleClassifyImg(String classifyCode) {
        String recycleClassifyImg = null;
        if (!LocalUtils.isEmptyAndNull(classifyCode)) {
            switch (classifyCode) {
                case "WB":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/surface.png";
                    break;
                case "XB":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/bag.png";
                    break;
                case "FS":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/clothes.png";
                    break;
                case "XX":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/shoes.png";
                    break;
                case "ZB":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/jewellery.png";
                    break;
                case "PS":
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/ornament.png";
                    break;
//                case "QT":
//                    recycleClassifyImg = "other.png";
//                    break;
                default:
                    recycleClassifyImg = "https://luxuryadmin-test.oss-cn-hangzhou.aliyuncs.com/default/classifyImg/other.png";
                    break;
            }
        }

        return recycleClassifyImg;
    }

    @Override
    public List<VoEmployee> listRecycleUser(int shopId) {
        return proProductMapper.listRecycleUser(shopId);
    }

    public static void main(String[] args) {
        //String b = "??????????????\u200B[??????]\uD83D\uDE0D\uD83E\uDD70";
        String c = "??????????????[??????]\uD83D\uDE0D\uD83E\uDD70";
        String b = "??????????????\u200B[??????]\uD83D\uDE0D\uD83E\uDD70";
        //System.out.println(b.contains("\u200B"));
        System.out.println(b);
        System.out.println(b.length());
        b = b.replaceAll("\u200B", "");
        System.out.println(b.length());
        System.out.println(b);

    }
}
