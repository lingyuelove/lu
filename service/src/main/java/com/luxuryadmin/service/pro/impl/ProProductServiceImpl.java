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
        //寄卖传送大于一种类型即为全部类型
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
                    //临时仓添加商品判断是否为此临时仓商品
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
            //更新商品扩展信息
            if (!LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
                //更新商品扩展信息
                ParamProductClassifyAdd productClassifyAdd = new ParamProductClassifyAdd();
                productClassifyAdd.setProductId(proId);
                productClassifyAdd.setShopId(pro.getFkShpShopId());
                //String转Json转List实体类
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
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("添加商品失败");
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
                throw new MyException("更新商品失败: 找不到商品详情!");
            }
            proDetail.setId(proDetailId);
            ProProduct dbPro = proProductMapper.getProProductByShopIdBizId(pro.getFkShpShopId(), pro.getBizId());
            long autoNumber = LocalUtils.calcNumber(dbPro.getId(), "+", 1000000).longValue();
            proDetail.setAutoNumber(autoNumber + "");
            proDetailService.updateProDetail(proDetail);
            if (!LocalUtils.isEmptyAndNull(productClassifySunAddLists)) {
                ProProduct product = getProProductByShopIdBizId(pro.getFkShpShopId(), pro.getBizId());
                //更新商品扩展信息
                ParamProductClassifyAdd productClassifyAdd = new ParamProductClassifyAdd();
                productClassifyAdd.setProductId(product.getId());
                productClassifyAdd.setShopId(pro.getFkShpShopId());
                //String转Json转List实体类
                JSONArray jsonArray = JSONObject.parseArray(productClassifySunAddLists);
                List<ParamProductClassifySunAddList> classifyTypeSunAdds = JSONObject.parseArray(jsonArray.toJSONString(), ParamProductClassifySunAddList.class);
                //判断集合是否为空
                if (!LocalUtils.isEmptyAndNull(classifyTypeSunAdds)) {
                    productClassifyAdd.setProductClassifySunAddLists(classifyTypeSunAdds);
                    productClassifyAdd.setUserId(pro.getUpdateAdmin());
                    proStandardService.addProductClassifyTypeList(productClassifyAdd);
                } else {
                    proStandardService.deleteStandard(product.getId());
                }

            }

        } catch (Exception e) {
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("更新商品失败");
        }
        return rows;
    }

    @Override
    public Integer updateProProductPrice(ParamProductUploadPrice productUploadPrice, Integer shopId, Integer userId) {
        ProProduct proProductOld = this.getProProductByShopIdBizId(shopId, productUploadPrice.getBizId());
        if (proProductOld == null) {
            throw new MyException("未查询到该商品");
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
        //对账单的修改
        Integer total = proProductOld.getTotalNum() - proProduct.getTotalNum();
        int compareResult = 0;
        //比较新老【商品成本价】大小，0表示相等，1表示修改后的价格大，-1表示修改后的价格小
        BigDecimal finishPriceOld = proProductOld.getInitPrice();
        BigDecimal finishPriceNew = proProduct.getInitPrice();
        BigDecimal changePrice = null;
        if (null != finishPriceNew) {
            changePrice = finishPriceNew.subtract(finishPriceOld);
            compareResult = finishPriceNew.compareTo(finishPriceOld);
        }
        if (compareResult != 0 || total != 0) {
            //新增商品修改账单记录
            ParamFundRecordAdd paramFundRecordAddPro = new ParamFundRecordAdd();
            paramFundRecordAddPro.setFkShpShopId(shopId);
            paramFundRecordAddPro.setUserId(userId);
            paramFundRecordAddPro.setMoney(proProductOld.getInitPrice().toString());
            paramFundRecordAddPro.setInitPrice(proProduct.getInitPrice().toString());
//            paramFundRecordAddPro.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAddPro.setFundType("10");
            paramFundRecordAddPro.setCount(proProductOld.getTotalNum().toString());
            paramFundRecordAddPro.setFinClassifyName("更新商品入库记录");
            paramFundRecordAddPro.setAttributeCode(proProductOld.getFkProAttributeCode());
            fundRecordService.addFundRecordForUpdateProduct(paramFundRecordAddPro, proProduct.getTotalNum().toString());
        }

        //添加账单流水-【价差调整】-【修改商品成本价】
        //等于0表示价格没有修改，不生成记账流水
        if (compareResult != 0) {
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //修改后的价格大，说明收入增加了
            if (compareResult == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //修改后的价格小
            else if (compareResult == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            if (null != changePrice) {
                paramFinShopRecordAdd.setChangeAmount(changePrice.divide(new BigDecimal(100.00)).toString());
            }
            paramFinShopRecordAdd.setType("价差调整");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "【" + proProductOld.getName() + "】商品成本价修改";
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
                //新增商品添加账单记录
                ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
                paramFundRecordAdd.setFkShpShopId(shopId);
                paramFundRecordAdd.setUserId(userId);
                paramFundRecordAdd.setMoney(proProduct.getInitPrice().toString());
                paramFundRecordAdd.setInitPrice("0");
                paramFundRecordAdd.setState(EnumFinShopRecordInoutType.IN.getCode());
                paramFundRecordAdd.setFundType("10");
                paramFundRecordAdd.setCount(proProduct.getTotalNum().toString());
                paramFundRecordAdd.setFinClassifyName("删除入库商品");
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
        //数据为空
        if (LocalUtils.isEmptyAndNull(classifyForAnalyses)) {
            return voProForAnalysisShow;
        }
        String showTotalPrice = "";
        //权限控制成本是否可看; 不可看
        if (flag != null && !flag) {
            for (VoProClassifyForAnalysis classifyForAnalysis : classifyForAnalyses) {
                classifyForAnalysis.setShowPrice("*****");
                classifyForAnalysis.setShowProportion("*****");
            }
            showTotalPrice = "*****";
        } else {
            //可看
            //价格除以100显示
            for (VoProClassifyForAnalysis classifyForAnalysis : classifyForAnalyses) {
                if (LocalUtils.isEmptyAndNull(classifyForAnalysis.getTotalPrice())) {
                    classifyForAnalysis.setTotalPrice("0");
                }
                BigDecimal totalPrice = new BigDecimal(classifyForAnalysis.getTotalPrice()).divide(new BigDecimal(100));
                classifyForAnalysis.setTotalPriceForHide(totalPrice);
                String showPrice = DecimalFormatUtil.formatString(totalPrice, null);
                classifyForAnalysis.setShowPrice(showPrice);
            }
            //计算总成本
            BigDecimal totalPrice = classifyForAnalyses.stream()
                    // 将user对象的age取出来map为Bigdecimal
                    .map(VoProClassifyForAnalysis::getTotalPriceForHide)
                    // 使用reduce()聚合函数,实现累加器
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            //计算总成本
            showTotalPrice = totalPrice.toString();

            //计算百分比
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
                //计算展示价格
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
        //显示可用库存数量+锁单库存数量
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
            //如果redis没找到;则数据库查找并更新redis,此举是为了兼容之前已经上架的商品;防止缓存穿透
            ProProduct product = getProProductByShopIdBizId(shopId, bizId);
            if (!LocalUtils.isEmptyAndNull(product)) {
                Integer stateCode = product.getFkProStateCode();
                boolean isBetween = LocalUtils.isBetween(stateCode, 20, 39);
                if (isBetween) {
                    int totalNumDb = product.getTotalNum();
                    //获取该商品的锁单数量
                    lockNum = proLockRecordService.getProductLockNumByProId(product.getId());
                    leftNum = totalNumDb - lockNum;
                    if (leftNum < 0) {
                        log.info("与DB对比，锁单数量异常！{}:{}", totalNumDb, lockNum);
                        throw new MyException("与DB对比，锁单数量异常！" + totalNumDb + ":" + lockNum);
                    }
                } else if (LocalUtils.isBetween(stateCode, 10, 19)) {
                    //未上架的商品不具备锁单记录,所有总数就等于可用数量
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
            throw new MyException("库存超卖！" + leftNumRedis + ":" + leftNum);
        }
        proRedisNum.setLeftNum(newLeftNum);
        proRedisNum.setTotalNum(newTotalNum);
        String lockRedisKey = ConstantRedisKey.getLockProductByBizId(shopId, bizId);
        if (newLeftNum == 0 && proRedisNum.getLockNum() == 0) {
            //库存和锁单都已为0;商品已售光,清空redis相对应的商品库存缓存
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
        //可用库存redis
        int leftNum = proRedisNum.getLeftNum();
        int lockNum = proRedisNum.getLockNum();
        //更新可用库存和锁单数量
        if (isLock) {
            //锁单
            leftNum -= myLockNum;
            lockNum += myLockNum;
        } else {
            //解锁
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
        //1.删除商品的db锁单记录;
        proLockRecordService.deleteProLockRecord(shopId, bizId);
        deleteProRedisLockNum(shopId, bizId);
        //2.删除商品的redis锁单数量
    }

    /**
     * 删除Redis商品库存缓存
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
            //修改为友商价必须可以看; 销售价不可以看;2021-07-31 00:35:51不受权限影响;
            voLeaguerProduct.setSalePrice(null);
        } else {
            //是否可以查看友商价
            //我的店铺ID 10052 友商店铺ID 10087
            Boolean isCanSeeTradePrice = bizLeaguerService.isCanSeeTradePrice(leaguerShopId, shopId);
            //如果不可以查看友商价，则隐藏【友商价】字段
            isCanSeeTradePrice = null == isCanSeeTradePrice ? Boolean.FALSE : isCanSeeTradePrice;
            if (!isCanSeeTradePrice) {
                voLeaguerProduct.setTradePrice(null);
            }

            //是否可以查看销售价
            Boolean isCanSeeSalePrice = bizLeaguerService.isCanSeeSalePrice(leaguerShopId, shopId);
            //如果不可以查看销售价，则隐藏【销售价】字段
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
            //联盟商家商品不显示销售价
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
        //detailListAll.get(0).setProdTypeName("合计");
        detailList.addAll(detailListAll);
        return detailList;
    }

    /**
     * 质押商品赎回
     *
     * @param proRedeem
     * @return
     */
    @Override
    public int redeemProduct(ProProduct proRedeem, Integer userId) {
        /**
         * 修改
         *  finish_price为【赎回价】
         *  fk_pro_state_code为【44】质押商品已赎回
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
         * 查询所有的到期质押商品bizId
         */
        List<VoExpireProd> expireProdList = proProductMapper.selectAllExpireProdBizId(jobDayStart);
        System.out.println("proBizIdList=" + expireProdList);

        proProductMapper.updateProdExpire(jobDayStart);

        /**
         * 发送【质押商品过期】消息通知
         */
        opPushService.pushExpireProductMsg(expireProdList);
    }

    @Override
    public Integer countProductByStateCode(Integer shopId, String stateCode) {
        return proProductMapper.countProductByStateCode(shopId, stateCode);
    }

    /**
     * 获取商品列表中第一个商品的图片URL
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
     * 对前端参数进行封装;
     * 1.商品图片,属性,库存;<br/>
     * 2.商品描述,商品名称,适用人群,分类,保卡(保卡年份);<br/>
     * 注意: 满足第一点,方可保存到仓库;<br/>
     * 同时满足第一,第二点,方可立即上架;<br/>
     * 在没有满足上架的条件时,隐藏上架按钮;
     *
     * @param voPro
     * @param isSave true:新增 | false:更新
     * @return
     */
    @Override
    public ProProduct pickProProduct(ParamProductUpload voPro, boolean isSave, Integer shopId, Integer userId) {
        //商品关联店铺
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
        //根据状态来判断商品是 未上架 还是 已上架
        int state = Integer.parseInt(voPro.getState());
        product.setFkProStateCode(state);

        if (LocalUtils.isBetween(state, 10, 19)) {
            //下架时,清空上架时间
            product.setReleaseTime(null);
        } else if (LocalUtils.isBetween(state, 20, 29)) {
            //上架时,重新赋值上架时间
            product.setReleaseTime(newDate);
        }

        product.setFkProAttributeCode(voPro.getAttribute());
        product.setFkProClassifyCode(LocalUtils.isEmptyAndNull(voPro.getClassify()) ? "QT" : voPro.getClassify());
        String name = LocalUtils.returnEmptyStringOrString(voPro.getName());
        String description = LocalUtils.returnEmptyStringOrString(voPro.getDescription());
        //去除特殊不可见符号;\u200B等
        //name = name.replaceAll("\\p{C}", "");
        //description = description.replaceAll("\\p{C}", "");

        //修改于2020-12-07 23:19:40; 仅去掉不可见符号,保留emoji👿符号
        name = name.replaceAll("\u200B", "");
        description = description.replaceAll("\u200B", "");
        //如果没有填写商品名称,则截取商品描述前50个字符;
        if (LocalUtils.isEmptyAndNull(name) && !LocalUtils.isEmptyAndNull(description)) {
            int length = description.length();
            //不足20个字符,则全部作为名称
            name = description.substring(0, Math.min(length, 50));
        }
        product.setName(LocalUtils.isEmptyAndNull(name) ? "（未填写商品名称）" : name);
        product.setDescription(description);
        product.setQuality(voPro.getQuality());
        product.setTargetUser(voPro.getTargetUser());
        product.setTag(voPro.getTag());
        product.setTotalNum(Integer.parseInt(voPro.getTotalNum()));
        //价格(分)入库
        product.setInitPrice(LocalUtils.formatBigDecimal(voPro.getInitPrice()));
        product.setTradePrice(LocalUtils.formatBigDecimal(voPro.getTradePrice()));
        product.setAgencyPrice(LocalUtils.formatBigDecimal(voPro.getAgencyPrice()));
        product.setSalePrice(LocalUtils.formatBigDecimal(voPro.getSalePrice()));
        product.setSmallImg(voPro.getSmallImgUrl());
        product.setRemark(voPro.getRemark());
        Integer recycleAdmin = voPro.getRecycleAdmin();
        product.setRecycleAdmin(LocalUtils.isEmptyAndNull(recycleAdmin) ? 0 : recycleAdmin);
        String classifySubName = voPro.getClassifySub();
        //判断添加商品二级分类id
        product.setFkProClassifySubName(classifySubName);

        //判断添加商品系列
        product.setFkProSubSeriesName(voPro.getSubSeriesName());
        product.setFkProSeriesModelName(voPro.getSeriesModelName());
        String saveEndTime = voPro.getSaveEndTime();

        String attribute = EnumProAttribute.PAWN.getCode().toString();
        if (attribute.equals(product.getFkProAttributeCode())) {
            if (LocalUtils.isEmptyAndNull(saveEndTime)) {
                throw new MyException("请填写【质押到期时间】");
            }
            Date date;
            try {
                date = DateUtil.parseShort(saveEndTime);
            } catch (Exception e) {
                throw new MyException("【质押到期时间】格式错误");
            }
            //2.6.4限制必须大于当前日期去掉;
//            if (date.before(newDate)) {
//                throw new MyException("【质押到期时间】必须大于当前日期");
//            }
            product.setSaveEndTime(date);
            //未上架(商品存于质押期间)
            product.setFkProStateCode(12);
        }
        return product;
    }

    /**
     * 对前端参数进行封装;
     *
     * @param voPro
     * @param isSave isSave true:新增 | false:更新
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
        //默认没保卡; 如果有保卡但未填写时间,则默认为"空白保卡";
        if (ConstantCommon.ONE.equals(repCard)) {
            //有保卡,且要和保卡有效时间一同出现;
            if (LocalUtils.isEmptyAndNull(repairCardTime)) {
                repairCardTime = "空白保卡";
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
                //缩略图
                String smallImg = shareShopProduct.getSmallImg();
                if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                    shareShopProduct.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
                }
                //盘点是否通用
//                String targetUser = (LocalUtils.isEmptyAndNull(shareShopProduct.getTargetUser()) || "通用".equals(shareShopProduct.getTargetUser())) ? null : LocalUtils.formatParamForSqlInQuery(shareShopProduct.getTargetUser());
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
                //缩略图
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
        //设置排名
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
            //缩略图
            String smallImg = recycleProductList.getSmallImg();
            recycleProductList.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

            if (!hasPermInitPrice) {
                recycleProductList.setInitPrice("******");
            }
            if (!LocalUtils.isEmptyAndNull(recycleProductList.getInsertTime())) {
                recycleProductList.setShowTime("回收时间:" + DateUtil.format(recycleProductList.getInsertTime()));
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
        //商品
        if ("1".equals(productOrOrderForDeleteSearch.getDeleteType())) {
            productOrOrderForDeletes = proProductMapper.getProductForDeleteList(productOrOrderForDeleteSearch);
        }
        //订单
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
                    //查看成本价
                    String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showInitPrice);
                    productOrOrderForDelete.setInitPrice(hasPermission ? productOrOrderForDelete.getInitPrice() : null);
                    //查看友商价
                    String showTradePrice = ConstantPermission.CHK_PRICE_TRADE;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showTradePrice);
                    productOrOrderForDelete.setTradePrice(hasPermission ? productOrOrderForDelete.getTradePrice() : null);
                    //查看代理价
                    String showAgencyPrice = ConstantPermission.CHK_PRICE_AGENCY;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showAgencyPrice);
                    productOrOrderForDelete.setAgencyPrice(hasPermission ? productOrOrderForDelete.getAgencyPrice() : null);
                    //查看销售价
                    String showSalePrice = ConstantPermission.CHK_PRICE_SALE;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showSalePrice);
                    productOrOrderForDelete.setSalePrice(hasPermission ? productOrOrderForDelete.getSalePrice() : null);
                }
                if ("2".equals(productOrOrderForDeleteSearch.getDeleteType())) {
                    boolean hasPermission = true;
                    //查看成本价
                    String showInitPrice = ConstantPermission.CHK_PRICE_INIT;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, showInitPrice);
                    productOrOrderForDelete.setInitPrice(hasPermission ? productOrOrderForDelete.getInitPrice() : null);
                    //查看销售价
                    String finishPrice = ConstantPermission.CHK_PRICE_FINISH;
                    hasPermission = servicesUtil.hasPermission(shopId, userId, finishPrice);
                    productOrOrderForDelete.setFinishPrice(hasPermission ? productOrOrderForDelete.getFinishPrice() : null);
                    String stateCn = productOrOrderForDelete.getState();
                    productOrOrderForDelete.setState(stateCn);
                    if ("20".equals(stateCn)) {
                        stateCn = "已开单";
                    } else if ("-20".equals(stateCn)) {
                        stateCn = "已退货";
                    } else if ("-90".equals(stateCn)) {
                        stateCn = "已删除";
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
                //商品属性
                productOrOrderForDelete.setAttributeShortCn(servicesUtil.getAttributeCn(productOrOrderForDelete.getAttributeCn(), false));
            });
        }
        PageInfo<VoOrganizationTempPageByApp> pageInfo = new PageInfo(productOrOrderForDeletes);
        VoProductOrOrderForDeletePage voProductOrOrderForDeletePage = new VoProductOrOrderForDeletePage();
        voProductOrOrderForDeletePage.setList(productOrOrderForDeletes);
        voProductOrOrderForDeletePage.setPageNum(pageInfo.getPageNum());
        voProductOrOrderForDeletePage.setPageSize(pageInfo.getPageSize());
        //订单
        productOrOrderForDeleteSearch.setPageSize(1);
        productOrOrderForDeleteSearch.setPageNum(null);
        //商品类型
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
        //商品
        //订单类型
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
        //商品
        if ("1".equals(productOrOrderForDelete.getDeleteType())) {
            //根据分号分隔转化为list
            List<String> bizIdList = Arrays.asList(productOrOrderForDelete.getBizId().split(";"));
            if (bizIdList != null && bizIdList.size() > 0) {
                bizIdList.forEach(bizId -> {
                    //商品删除 商品假删 del=1
                    ProProduct product = proProductMapper.getProProductForDeleteByShopIdBizId(productOrOrderForDelete.getShopId(), bizId);
                    if (product != null) {
                        product.setDel("1");
                        proProductMapper.updateObject(product);
                    }
                });
            }


        }
        //订单
        if ("2".equals(productOrOrderForDelete.getDeleteType())) {
            //根据分号分隔转化为list
            ParamOrderDelete delete = new ParamOrderDelete();
            delete.setShopId(productOrOrderForDelete.getShopId());

            List<String> orderNumberList = Arrays.asList(productOrOrderForDelete.getOrderNumber().split(";"));
            if (orderNumberList != null && orderNumberList.size() > 0) {
                orderNumberList.forEach(orderNumber -> {
                    //订单删除 物理删除
                    delete.setOrderBizId(orderNumber);
                    ordOrderService.deleteOrderForDel(delete);

                });
            }
        }
    }

    @Override
    public void updateProductOrOrder(ParamProductOrOrderForUpdate productOrOrderForUpdate, HttpServletRequest request) {
        //商品
        if ("1".equals(productOrOrderForUpdate.getDeleteType())) {
            //商品编辑删除备注 只能删除的人修改
            ProProduct product = proProductMapper.getProProductForDeleteByShopIdBizId(productOrOrderForUpdate.getShopId(), productOrOrderForUpdate.getBizId());
            int deleteUser = product.getUpdateAdmin();
            int updateUser = productOrOrderForUpdate.getUserId();
            if (deleteUser != updateUser) {
                throw new MyException("非删除此商品的用户 不能进行修改");
            }
            product.setDeleteRemark(productOrOrderForUpdate.getDeleteRemark());
            proProductMapper.updateObject(product);
        }
        //订单
        if ("2".equals(productOrOrderForUpdate.getDeleteType())) {
            //订单编辑删除备注 只能删除的人修改
            OrdOrder order = ordOrderService.getOrdOrderDetail(productOrOrderForUpdate.getShopId(), productOrOrderForUpdate.getOrderNumber());
            int deleteUser = order.getUpdateAdmin();
            int updateUser = productOrOrderForUpdate.getUserId();
            if (deleteUser != updateUser) {
                throw new MyException("非删除此订单的用户 不能进行修改");
            }
            order.setDeleteRemark(productOrOrderForUpdate.getDeleteRemark());
            ordOrderMapper.updateObject(order);
        }
    }

    @Override
    public void recycleProductAdd(ParamRecycleProductAdd recycleProductAdd, String recycleState) {
        ProProduct product = proProductMapper.getProProductByShopIdBizId(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());

        if (product == null) {
            throw new MyException("暂无此商品");
        }

        //查询是否有锁单数量 锁单数量清空
        proLockRecordService.deleteProLockRecord(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //清空锁单数量清空
        deleteProRedisLockNum(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //重新赋值redis商品可用数量
        this.getProRedisNum(recycleProductAdd.getShopId(), recycleProductAdd.getBizId());
        //已取回 新增商品状态
        int recycleUser = 0;
        if (product.getFkShpRetrieveUserId() != null) {
            recycleUser = product.getFkShpRetrieveUserId();
        }
        Integer userId = recycleProductAdd.getUserId();
        if ("1".equals(recycleState) && userId != recycleUser) {
            throw new MyException("非取回此商品的用户 不能进行修改");
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
        //String b = "测试商品??\u200B[旺柴]\uD83D\uDE0D\uD83E\uDD70";
        String c = "测试商品??[旺柴]\uD83D\uDE0D\uD83E\uDD70";
        String b = "测试商品??\u200B[旺柴]\uD83D\uDE0D\uD83E\uDD70";
        //System.out.println(b.contains("\u200B"));
        System.out.println(b);
        System.out.println(b.length());
        b = b.replaceAll("\u200B", "");
        System.out.println(b.length());
        System.out.println(b);

    }
}
