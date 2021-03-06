package com.luxuryadmin.service.shp.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.DecimalFormatUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.shp.ShpServiceRecord;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import com.luxuryadmin.entity.shp.ShpServiceType;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.enums.fin.EnumFinShopRecordInoutType;
import com.luxuryadmin.enums.fin.EnumFinShopRecordType;
import com.luxuryadmin.enums.shp.EnumServiceStatus;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.ord.OrdShareReceiptMapper;
import com.luxuryadmin.mapper.shp.ShpServiceRecordMapper;
import com.luxuryadmin.mapper.shp.ShpServiceTypeMapper;
import com.luxuryadmin.param.data.ParamDataRecycleQuery;
import com.luxuryadmin.param.fin.ParamFinShopRecordAdd;
import com.luxuryadmin.param.fin.ParamFundRecordAdd;
import com.luxuryadmin.param.pro.ParamProductClassifySunAddList;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.*;
import com.luxuryadmin.service.fin.FinFundRecordService;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.shp.*;
import com.luxuryadmin.vo.shp.*;
import org.jacoco.agent.rt.internal_1f1cc91.core.internal.flow.IFrame;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Classname ShpServiceServiceImpl
 * @Description ????????????Service ?????????
 * @Date 2020/9/18 15:47
 * @Created by sanjin145
 */
@Service
public class ShpServiceServiceImpl implements ShpServiceService {

    @Resource
    private ShpServiceRecordMapper shpServiceRecordMapper;

    @Resource
    private ShpServiceTypeMapper shpServiceTypeMapper;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private FinShopRecordService finShopRecordService;

    @Resource
    private OrdShareReceiptMapper ordShareReceiptMapper;

    @Autowired
    private OrdOrderService ordOrderService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private FinFundRecordService fundRecordService;
    @Autowired
    private ShpServiceRecordCostService shpServiceRecordCostService;

    @Override
    public VoInitShopService getInitAddShpServiceData(Integer shopId, BasicParam basicParam) {
        VoInitShopService voInitShopService = new VoInitShopService();
        List<VoShpServiceType> shpServiceTypeList = shpServiceTypeMapper.selectAllServiceTypeByShopId(shopId);
        voInitShopService.setShpServiceTypeList(shpServiceTypeList);
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                voInitShopService.setTotalUserList(voEmployeeList);
                List<VoEmployee> listReceiveUser = shpServiceRecordMapper.listReceiveUser(shopId);
                voInitShopService.setReceiveUserList(listReceiveUser);
                List<VoEmployee> listServiceUser = shpServiceRecordMapper.listServiceUser(shopId);
                voInitShopService.setShpServiceUserList(listServiceUser);
            } else {
                voInitShopService.setShpServiceUserList(voEmployeeList);
            }
        } catch (Exception e) {
            throw new MyException("???????????????????????????" + e);
        }


//        List<VoEmployee> totalUserList = shpUserShopRefService.getSalesmanByShopId(shopId);

        return voInitShopService;
    }

    @Override
    public Integer initAllShpServiceType() {
        List<Integer> shopIdList = shpServiceTypeMapper.selectAllNeedInitShopId();
        Integer totalResultCount = 0;
        for (Integer shopId : shopIdList) {
            totalResultCount++;
            initSingleShpServiceType(shopId, -1);
        }
        return totalResultCount;
    }

    @Override
    public Integer initSingleShpServiceType(int shopId, int userId) {
        //????????????????????????
        String[] defaultServiceTypeNameArr = {"????????????", "????????????", "??????"};
        for (int i = 0; i < 3; i++) {
            addShpServiceType(shopId, userId, defaultServiceTypeNameArr[i]);
        }
        return 1;
    }

    /**
     * @param shopId
     * @param userId
     * @param serviceTypeName
     * @return
     */
    @Override
    public ShpServiceType addShpServiceType(Integer shopId, int userId, String serviceTypeName) {
        ShpServiceType shpServiceType = new ShpServiceType();
        shpServiceType.setFkShpShopId(shopId);
        shpServiceType.setServiceName(serviceTypeName);
        shpServiceType.setInsertAdmin(userId);
        shpServiceType.setUpdateAdmin(userId);
        shpServiceType.setInsertTime(new Date());
        shpServiceType.setUpdateTime(new Date());
        shpServiceType.setDel(ConstantCommon.DEL_OFF);

        shpServiceTypeMapper.insertSelective(shpServiceType);
        return shpServiceType;
    }

    @Override
    public ShpServiceRecord addShpServiceRecord(ParamShpServiceRecordAdd serviceInfo, HttpServletRequest request) {
        Integer shopId = serviceInfo.getShopId();
        Integer userId = serviceInfo.getUserId();
        ShpServiceRecord shpServiceRecord = new ShpServiceRecord();
        BeanUtils.copyProperties(serviceInfo, shpServiceRecord);
        shpServiceRecord.setFkShpShopId(shopId);
        shpServiceRecord.setServiceStatus(EnumServiceStatus.IN_SERVICE.getCode());
        shpServiceRecord.setServiceNumber(shopId + "" + System.currentTimeMillis());
        shpServiceRecord.setInsertTime(new Date());
        shpServiceRecord.setUpdateTime(new Date());
        shpServiceRecord.setInsertAdmin(userId);
        shpServiceRecord.setUpdateAdmin(userId);
        shpServiceRecord.setDel(ConstantCommon.DEL_OFF);

        shpServiceRecordMapper.insertSelective(shpServiceRecord);
        if (!LocalUtils.isEmptyAndNull(serviceInfo.getCostAddOrUpList())) {
            // ??????/??????????????????
            JSONArray jsonArray = JSONObject.parseArray(serviceInfo.getCostAddOrUpList());
            List<ParamServiceRecordCostAddOrUp> costAddOrUps = JSONObject.parseArray(jsonArray.toJSONString(), ParamServiceRecordCostAddOrUp.class);
            serviceInfo.setCostAddOrUps(costAddOrUps);
            serviceInfo.setId(shpServiceRecord.getId());
            shpServiceRecordCostService.addOrUpdate(serviceInfo);
        }

        //??????????????????????????????-????????????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SERVICE.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent(serviceInfo.getProdName());
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        //????????????-??????????????????
        //ordOrderService.refreshSalaryForUser(shopId, serviceInfo.getServiceShpUserId(), userId, new Date());
        return shpServiceRecord;
    }

    @Override
    public VoShpServiceRecordForPage listShpServiceRecord(ParamShpServiceQuery paramShpServiceQuery, BasicParam basicParam) {
        String serviceStatus = paramShpServiceQuery.getServiceStatus();
        String orderClause = constructOrderClauseByServiceStatus(paramShpServiceQuery);
        paramShpServiceQuery.setOrderClause(orderClause);
        //??????????????????(????????????)??????????????????;
        paramShpServiceQuery.setServiceShpUserId(LocalUtils.formatParamForSqlInQuery(paramShpServiceQuery.getServiceShpUserId()));
        paramShpServiceQuery.setReceiveShpUserId(LocalUtils.formatParamForSqlInQuery(paramShpServiceQuery.getReceiveShpUserId()));
        if (!LocalUtils.isEmptyAndNull(paramShpServiceQuery.getTypeName())) {
            String typeName = "'" + paramShpServiceQuery.getTypeName().replaceAll(";", "|");
            typeName = typeName.replaceAll(" ", "") + "'";
            paramShpServiceQuery.setTypeName(typeName);
        }
        List<VoShpServiceRecord> list = shpServiceRecordMapper.selectShpServiceRecord(paramShpServiceQuery);
        for (VoShpServiceRecord voShpServiceRecord : list) {
            String prodImgUrl = voShpServiceRecord.getProdImgUrl();
            voShpServiceRecord.setProdImgUrl(ConstantCommon.ossDomain + prodImgUrl);
            updateVoShpServiceRecord(voShpServiceRecord);

        }
//        setDefaultQueryDateRange(paramShpServiceQuery);
        PageInfo pageInfo = new PageInfo(list);
        VoShpServiceRecordForPage shpServiceRecordForPage = new VoShpServiceRecordForPage();
        shpServiceRecordForPage.setList(list);
        shpServiceRecordForPage.setPageSize(pageInfo.getPageSize());
        shpServiceRecordForPage.setPageNum(pageInfo.getPageNum());
        if (pageInfo.getNextPage() > 0) {
            shpServiceRecordForPage.setHasNextPage(true);
        } else {
            shpServiceRecordForPage.setHasNextPage(false);
        }

        try {
            //2.6.5????????????
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                selectPriceNew(shpServiceRecordForPage, paramShpServiceQuery);
            } else {
                //2.6.5????????????
                selectPrice(shpServiceRecordForPage, paramShpServiceQuery);
            }
        } catch (Exception e) {
            throw new MyException("??????????????????????????????" + e);
        }

        return shpServiceRecordForPage;
    }

    public void selectPrice(VoShpServiceRecordForPage shpServiceRecordForPage, ParamShpServiceQuery paramShpServiceQuery) {
        VoShpServiceRecordPrice serviceRecordPrice = shpServiceRecordMapper.getProfitPrice(paramShpServiceQuery);
        if (serviceRecordPrice == null) {
            serviceRecordPrice = new VoShpServiceRecordPrice();

        }
        if (serviceRecordPrice.getServiceFinishPrice() == null) {
            serviceRecordPrice.setServiceFinishPrice(new BigDecimal(0));
        }
        if (serviceRecordPrice.getServiceInitPrice() == null) {
            serviceRecordPrice.setServiceInitPrice(new BigDecimal(0));
        }
        if (serviceRecordPrice.getServiceNum() == null) {
            serviceRecordPrice.setServiceNum(0);
        }
        if ("inService".equals(paramShpServiceQuery.getServiceStatus())) {
            shpServiceRecordForPage.setServiceNum(serviceRecordPrice.getServiceNum().toString());
            shpServiceRecordForPage.setServiceInitPrice(serviceRecordPrice.getServiceFinishPrice().divide(new BigDecimal(100)).toString());
            if (DateUtil.getTodayTime().equals(paramShpServiceQuery.getStartTime())) {
                shpServiceRecordForPage.setCountName("????????????");
                shpServiceRecordForPage.setServicePriceName("????????????");
            } else {
                shpServiceRecordForPage.setCountName("????????????");
                shpServiceRecordForPage.setServicePriceName("???????????????");
            }
        }
        if ("finish".equals(paramShpServiceQuery.getServiceStatus())) {
            shpServiceRecordForPage.setServiceNum(serviceRecordPrice.getServiceNum().toString());
            shpServiceRecordForPage.setServiceInitPrice(serviceRecordPrice.getServiceInitPrice().divide(new BigDecimal(100)).toString());
            BigDecimal serviceProfitPrice = serviceRecordPrice.getServiceFinishPrice().subtract(serviceRecordPrice.getServiceInitPrice()).divide(new BigDecimal(100));
            shpServiceRecordForPage.setServiceProfitPrice(serviceProfitPrice.toString());
            if (DateUtil.getTodayTime().equals(paramShpServiceQuery.getStartTime())) {
                shpServiceRecordForPage.setCountName("????????????");
                shpServiceRecordForPage.setServicePriceName("????????????");
                shpServiceRecordForPage.setProfitPriceName("????????????");
            } else {
                shpServiceRecordForPage.setCountName("????????????");
                shpServiceRecordForPage.setServicePriceName("????????????");
                shpServiceRecordForPage.setProfitPriceName("???????????????");
            }
        }
    }

    public void selectPriceNew(VoShpServiceRecordForPage shpServiceRecordForPage, ParamShpServiceQuery paramShpServiceQuery) {
        VoShpServiceRecordPrice serviceRecordPrice = shpServiceRecordMapper.getServiceRecordPrice(paramShpServiceQuery);
        if (serviceRecordPrice == null) {
            serviceRecordPrice = new VoShpServiceRecordPrice();

        }
        if (serviceRecordPrice.getServiceFinishPrice() == null) {
            serviceRecordPrice.setServiceFinishPrice(new BigDecimal(0));
        }
        if (serviceRecordPrice.getServiceInitPrice() == null) {
            serviceRecordPrice.setServiceInitPrice(new BigDecimal(0));
        }
        if (serviceRecordPrice.getTotalCost() == null) {
            serviceRecordPrice.setTotalCost(new BigDecimal(0));
        }
        if (serviceRecordPrice.getServiceNum() == null) {
            serviceRecordPrice.setServiceNum(0);
        }
        if ("inService".equals(paramShpServiceQuery.getServiceStatus())) {
            shpServiceRecordForPage.setServiceNum(serviceRecordPrice.getServiceNum().toString());
            //????????????
            BigDecimal totalCostPrice = serviceRecordPrice.getTotalCost();
            String totalCost = DecimalFormatUtil.formatString(totalCostPrice, null);
            shpServiceRecordForPage.setServiceInitPrice(totalCost);

            //??????????????????
            BigDecimal serviceProfitPrice = serviceRecordPrice.getServiceFinishPrice().divide(new BigDecimal(100));
            String serviceProfit = DecimalFormatUtil.formatString(serviceProfitPrice, null);
            shpServiceRecordForPage.setServiceProfitPrice(serviceProfit);


            shpServiceRecordForPage.setCountName("??????");
            shpServiceRecordForPage.setServicePriceName("????????????");
            shpServiceRecordForPage.setProfitPriceName("????????????");
        }
        if ("finish".equals(paramShpServiceQuery.getServiceStatus())) {
            shpServiceRecordForPage.setServiceNum(serviceRecordPrice.getServiceNum().toString());
            //????????????
            BigDecimal serviceInitPrice = serviceRecordPrice.getServiceInitPrice().divide(new BigDecimal(100));
            String serviceInit = DecimalFormatUtil.formatString(serviceInitPrice, null);
            shpServiceRecordForPage.setServiceInitPrice(serviceInit);
            //??????????????????
            BigDecimal serviceProfitPrice = serviceRecordPrice.getServiceFinishPrice().divide(new BigDecimal(100));
            String serviceProfit = DecimalFormatUtil.formatString(serviceProfitPrice, null);
            shpServiceRecordForPage.setServiceProfitPrice(serviceProfit);
            shpServiceRecordForPage.setCountName("????????????");
            shpServiceRecordForPage.setServicePriceName("????????????");
            shpServiceRecordForPage.setProfitPriceName("????????????");

        }
    }

    public void updateVoShpServiceRecord(VoShpServiceRecord serviceRecord) {
        if ("finish".equals(serviceRecord.getServiceStatus())) {
            serviceRecord.setShowTime(serviceRecord.getFinishTime());
            if (!LocalUtils.isEmptyAndNull(serviceRecord.getServiceShpUserName())) {
                serviceRecord.setShowUserName("????????????:" + serviceRecord.getServiceShpUserName());
            }
        }
        if ("inService".equals(serviceRecord.getServiceStatus())) {
            serviceRecord.setShowTime(serviceRecord.getInsertTime());
            if (!LocalUtils.isEmptyAndNull(serviceRecord.getReceiveShpUserName())) {
                serviceRecord.setShowUserName("????????????:" + serviceRecord.getReceiveShpUserName());
            }
            if (!LocalUtils.isEmptyAndNull(serviceRecord.getTotalCost())) {
                BigDecimal costAmount = new BigDecimal(serviceRecord.getTotalCost()).multiply(new BigDecimal(100));
                serviceRecord.setCostAmount(costAmount.toString());
            } else {
                serviceRecord.setCostAmount("0");
            }

        }
        if ("cancel".equals(serviceRecord.getServiceStatus())) {
            serviceRecord.setShowTime(serviceRecord.getCancelTime());
            if (!LocalUtils.isEmptyAndNull(serviceRecord.getReceiveShpUserName())) {
                serviceRecord.setShowUserName("????????????:" + serviceRecord.getReceiveShpUserName());
            }

            if (!LocalUtils.isEmptyAndNull(serviceRecord.getTotalCost())) {
                BigDecimal costAmount = new BigDecimal(serviceRecord.getTotalCost()).multiply(new BigDecimal(100));
                serviceRecord.setCostAmount(costAmount.toString());
            } else {
                serviceRecord.setCostAmount("0");
            }
        }
    }

    private void setDefaultQueryDateRange(ParamShpServiceQuery paramShpServiceQuery) {
        if (null == paramShpServiceQuery.getStartTime() && null == paramShpServiceQuery.getEndTime()) {
            Date startDate = DateUtil.getTodayTime();
            Date endDate = new Date();
            paramShpServiceQuery.setStartTime(startDate);
            paramShpServiceQuery.setEndTime(endDate);
        }

    }

    /**
     * ??????????????????????????????????????????
     *
     * @param paramShpServiceQuery
     * @return
     */
    private String constructOrderClauseByServiceStatus(ParamShpServiceQuery paramShpServiceQuery) {
        String orderByClause = " ";
        String sortValue = paramShpServiceQuery.getSortValue();
        switch (paramShpServiceQuery.getServiceStatus()) {
            case "inService":
                if (!LocalUtils.isEmptyAndNull(sortValue) && "asc".equalsIgnoreCase(sortValue)) {
                    orderByClause += "insert_time asc";
                } else {
                    orderByClause += "insert_time desc";
                }
                break;
            case "finish":
                if (!LocalUtils.isEmptyAndNull(sortValue) && "asc".equalsIgnoreCase(sortValue)) {
                    orderByClause += "finish_time asc";
                } else {
                    orderByClause += "finish_time desc";
                }

                break;
            case "cancel":
                if (!LocalUtils.isEmptyAndNull(sortValue) && "asc".equalsIgnoreCase(sortValue)) {
                    orderByClause += "cancel_time asc";
                } else {
                    orderByClause += "cancel_time desc";
                }

                break;
            default:
                orderByClause += "insert_time desc";
                break;
        }
        return orderByClause;
    }

    @Override
    public Integer cancelShpService(Integer shopId, Integer userId, Integer shpServiceId, HttpServletRequest request) {
        if (!isCanUpdateServiceStatus(shpServiceId)) {
            throw new MyException("?????????????????????????????????????????????");
        }

        ShpServiceRecord recordCancel = new ShpServiceRecord();
        recordCancel.setId(shpServiceId);
        recordCancel.setFkShpShopId(shopId);
        recordCancel.setServiceStatus(EnumServiceStatus.CANCEL.getCode());
        recordCancel.setCancelTime(new Date());
        recordCancel.setUpdateAdmin(userId);
        recordCancel.setUpdateTime(new Date());
        shpServiceRecordMapper.updateByPrimaryKeySelective(recordCancel);


        //??????????????????????????????-????????????????????????
        ShpServiceRecord shpServiceRecord = shpServiceRecordMapper.selectByPrimaryKey(shpServiceId);
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SERVICE.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent(shpServiceRecord.getProdName());
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        return shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
    }

    @Override
    public VoShpServiceRecordDetail getShpServiceById(Integer shopId, Integer shpServiceId) {
        VoShpServiceRecordDetail recordDetail = shpServiceRecordMapper.selectShpServiceById(shopId, shpServiceId);
        if (null == recordDetail) {
            return recordDetail;
        }
        List<VoServiceRecordCost> recordCosts = shpServiceRecordCostService.listCostForServiceRecordId(recordDetail.getId());
        recordDetail.setRecordCosts(recordCosts);
        if (!LocalUtils.isEmptyAndNull(recordCosts)) {
            recordCosts.forEach(voServiceRecordCost -> {
                String totalCost = DecimalFormatUtil.formatString(new BigDecimal(voServiceRecordCost.getServiceCost()), "####.##");
                voServiceRecordCost.setServiceCost(totalCost);
            });
            Double total = recordCosts.stream().mapToDouble(VoServiceRecordCost::getServiceCostPrice).sum();
            total = total * 100;
//            String totalCost =DecimalFormatUtil.formatString(new BigDecimal(total),null);
            recordDetail.setTotalCost(total.toString());
        } else {
            recordDetail.setTotalCost("0");
        }

        //??????????????????
        ShpShop shpShop = shpShopService.getShpShopById("" + shopId);
        if (null == shpShop) {
            throw new MyException("?????????????????????");
        }
        recordDetail.setShopName(shpShop.getName());
        recordDetail.setShopAddress(shpShop.getAddress());
        recordDetail.setShopContact(shpShop.getContact());

        //????????????????????????
        List<String> prodImgUrlList = new ArrayList<>();
        recordDetail.setProdImgUrlList(prodImgUrlList);
        String prodImgUrls = recordDetail.getProdImgUrls();
        String[] prodImgArr = prodImgUrls.split(";");
        for (String prodImgUrl : prodImgArr) {
            prodImgUrlList.add(ConstantCommon.ossDomain + prodImgUrl);
        }

        return recordDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer finishShpService(Integer shopId, Integer userId, ParamShpServiceRecordFinish recordFinish, HttpServletRequest request) {
        Integer shpServiceId = recordFinish.getServiceRecordId();
        if (!isCanUpdateServiceStatus(shpServiceId)) {
            throw new MyException("?????????????????????????????????????????????");
        }

        Integer shpServiceRecordId = recordFinish.getServiceRecordId();
        VoShpServiceRecordDetail voShpServiceRecordDetail = shpServiceRecordMapper.selectShpServiceById(shopId, shpServiceRecordId);

        String[] imgUrlArr = voShpServiceRecordDetail.getProdImgUrls().split(";");
        String imgUrl = imgUrlArr[0];

        //??????????????????-??????????????????
        ParamFinShopRecordAdd paramFinShopRecordAddCost = new ParamFinShopRecordAdd();
        paramFinShopRecordAddCost.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
        paramFinShopRecordAddCost.setChangeAmount(recordFinish.getCostAmount().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAddCost.setType("????????????");
        paramFinShopRecordAddCost.setHappenTime(DateUtil.format(new Date()));
        String noteCost = "???" + voShpServiceRecordDetail.getProdName() + "????????????????????????";
        paramFinShopRecordAddCost.setNote(noteCost);

        finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAddCost, EnumFinShopRecordType.SYSTEM, imgUrl, "" + shpServiceRecordId);
        //??????????????????????????????
        ParamFundRecordAdd paramFundRecordAdd = new ParamFundRecordAdd();
        paramFundRecordAdd.setFkShpShopId(shopId);
        paramFundRecordAdd.setUserId(userId);
        paramFundRecordAdd.setMoney(recordFinish.getCostAmount().toString());
        paramFundRecordAdd.setInitPrice("0");
        paramFundRecordAdd.setState(EnumFinShopRecordInoutType.OUT.getCode());
        paramFundRecordAdd.setFundType("30");
        paramFundRecordAdd.setCount("1");
        paramFundRecordAdd.setFinClassifyName("????????????");
        fundRecordService.addOtherFundRecord(paramFundRecordAdd);

        //??????????????????-??????????????????
        ParamFinShopRecordAdd paramFinShopRecordAddReceive = new ParamFinShopRecordAdd();
        paramFinShopRecordAddReceive.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
        paramFinShopRecordAddReceive.setChangeAmount(recordFinish.getRealReceiveAmount().divide(new BigDecimal(100.00)).toString());
        paramFinShopRecordAddReceive.setType("????????????");
        paramFinShopRecordAddReceive.setHappenTime(DateUtil.format(new Date()));
        String noteReceive = "???" + voShpServiceRecordDetail.getProdName() + "???????????????????????????";
        paramFinShopRecordAddReceive.setNote(noteReceive);

        finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAddReceive, EnumFinShopRecordType.SYSTEM, imgUrl, "" + shpServiceRecordId);
        //????????????????????????????????????
        ParamFundRecordAdd paramFundRecordAddNew = new ParamFundRecordAdd();
        paramFundRecordAddNew.setFkShpShopId(shopId);
        paramFundRecordAddNew.setUserId(userId);
        paramFundRecordAddNew.setMoney(recordFinish.getRealReceiveAmount().toString());
        paramFundRecordAddNew.setInitPrice("0");
        paramFundRecordAddNew.setState(EnumFinShopRecordInoutType.IN.getCode());
        paramFundRecordAddNew.setFundType("30");
        paramFundRecordAddNew.setCount("1");
        paramFundRecordAddNew.setFinClassifyName("????????????");
        fundRecordService.addOtherFundRecord(paramFundRecordAddNew);

        ShpServiceRecord recordUpdate = new ShpServiceRecord();
        recordUpdate.setId(recordFinish.getServiceRecordId());
        recordUpdate.setFkShpShopId(shopId);
        recordUpdate.setServiceStatus(EnumServiceStatus.FINISH.getCode());
        recordUpdate.setCostAmount(recordFinish.getCostAmount());
        recordUpdate.setRealReceiveAmount(recordFinish.getRealReceiveAmount());
        recordUpdate.setFinishTime(new Date());
        recordUpdate.setUpdateAdmin(userId);
        recordUpdate.setUpdateTime(new Date());
        shpServiceRecordMapper.updateByPrimaryKeySelective(recordUpdate);

        //??????????????????????????????-????????????????????????
        ShpServiceRecord shpServiceRecord = shpServiceRecordMapper.selectByPrimaryKey(shpServiceId);
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SERVICE.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent(shpServiceRecord.getProdName());
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        return shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
    }

    @Override
    public Integer updateShpService(ParamShpServiceRecordAdd serviceInfo, HttpServletRequest request) {
        Integer shopId = serviceInfo.getShopId();
        Integer userId = serviceInfo.getUserId();
        VoShpServiceRecordDetail serviceRecordOld = shpServiceRecordMapper.selectShpServiceById(shopId, serviceInfo.getId());
        String[] imgUrlArr = serviceRecordOld.getProdImgUrls().split(";");
        String imgUrl = imgUrlArr[0];

        //???????????????????????????????????????0???????????????1??????????????????????????????-1???????????????????????????
        BigDecimal costAmountOld = serviceRecordOld.getCostAmount();
        BigDecimal costAmountNew = serviceInfo.getCostAmount();
        BigDecimal changePriceCostAmount = costAmountOld.subtract(costAmountNew);
        int compareResultCostAmount = costAmountOld.compareTo(costAmountNew);

        //??????????????????
        String operateContent = serviceRecordOld.getProdName();
        String operateUpdateFieldContent = "";
        //????????????????????????
        Integer updateFieldCount = 0;
        //??????????????????-??????????????????-??????????????????-??????????????????
        //??????0????????????????????????????????????????????????
        if (compareResultCostAmount != 0) {
            updateFieldCount++;
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //?????????????????????????????????????????????
            if (compareResultCostAmount == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //?????????????????????
            else if (compareResultCostAmount == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            String changePriceStr = changePriceCostAmount.divide(new BigDecimal(100.00)).toString();
            paramFinShopRecordAdd.setChangeAmount(changePriceStr);
            paramFinShopRecordAdd.setType("????????????");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "???" + serviceInfo.getProdName() + "??????????????????????????????";
            paramFinShopRecordAdd.setNote(note);
            finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, "" + serviceRecordOld.getId());
            //??????????????????-??????????????????-??????????????????
            ParamFundRecordAdd paramFundRecordAddNew = new ParamFundRecordAdd();
            paramFundRecordAddNew.setFkShpShopId(shopId);
            paramFundRecordAddNew.setUserId(userId);
            paramFundRecordAddNew.setMoney(changePriceCostAmount.toString());
            paramFundRecordAddNew.setInitPrice("0");
            paramFundRecordAddNew.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAddNew.setFundType("30");
            paramFundRecordAddNew.setCount("1");
            paramFundRecordAddNew.setFinClassifyName("??????????????????");
            fundRecordService.addOtherFundRecord(paramFundRecordAddNew);

            //????????????????????????????????????????????????????????????????????????","
            if (!updateFieldCount.equals(1)) {
                operateUpdateFieldContent += ",";
            }
            operateUpdateFieldContent += "????????????????????????" + costAmountNew.divide(new BigDecimal(100.00)).toString() + "???";
        }

        //???????????????????????????????????????0???????????????1??????????????????????????????-1???????????????????????????
        BigDecimal realReceiveAmountOld = serviceRecordOld.getRealReceiveAmount();
        BigDecimal realReceiveAmountNew = serviceInfo.getRealReceiveAmount();
        BigDecimal changePriceRealReceiveAmount = realReceiveAmountNew.subtract(realReceiveAmountOld);
        int compareResultRealReceiveAmount = realReceiveAmountNew.compareTo(realReceiveAmountOld);

        //??????????????????-??????????????????-??????????????????-????????????????????????
        //??????0????????????????????????????????????????????????
        if (compareResultRealReceiveAmount != 0) {
            updateFieldCount++;
            ParamFinShopRecordAdd paramFinShopRecordAdd = new ParamFinShopRecordAdd();
            //?????????????????????????????????????????????
            if (compareResultRealReceiveAmount == 1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.IN.getCode());
            }
            //?????????????????????
            else if (compareResultRealReceiveAmount == -1) {
                paramFinShopRecordAdd.setInoutType(EnumFinShopRecordInoutType.OUT.getCode());
            }

            String changePriceStr = changePriceRealReceiveAmount.divide(new BigDecimal(100.00)).toString();
            paramFinShopRecordAdd.setChangeAmount(changePriceStr);
            paramFinShopRecordAdd.setType("????????????");
            paramFinShopRecordAdd.setHappenTime(DateUtil.format(new Date()));
            String note = "???" + serviceInfo.getProdName() + "?????????????????????????????????";
            paramFinShopRecordAdd.setNote(note);
            finShopRecordService.addFinShopRecord(shopId, userId, paramFinShopRecordAdd, EnumFinShopRecordType.SYSTEM, imgUrl, "" + serviceRecordOld.getId());
            //??????????????????-??????????????????-??????????????????
            ParamFundRecordAdd paramFundRecordAddNew = new ParamFundRecordAdd();
            paramFundRecordAddNew.setFkShpShopId(shopId);
            paramFundRecordAddNew.setUserId(userId);
            paramFundRecordAddNew.setMoney(changePriceRealReceiveAmount.toString());
            paramFundRecordAddNew.setInitPrice("0");
            paramFundRecordAddNew.setState(paramFinShopRecordAdd.getInoutType());
            paramFundRecordAddNew.setFundType("30");
            paramFundRecordAddNew.setCount("1");
            paramFundRecordAddNew.setFinClassifyName("??????????????????");
            fundRecordService.addOtherFundRecord(paramFundRecordAddNew);

            //????????????????????????????????????????????????????????????????????????","
            if (!updateFieldCount.equals(1)) {
                operateUpdateFieldContent += ",";
            }
            operateUpdateFieldContent += "????????????????????????" + realReceiveAmountNew.divide(new BigDecimal(100.00)).toString() + "???";
        }

        //??????????????????????????????????????????
        Integer serviceUserIdOld = serviceRecordOld.getServiceShpUserId();
        Integer serviceUserIdNew = serviceInfo.getServiceShpUserId();
        serviceUserIdNew = null == serviceUserIdNew ? 0 : serviceUserIdNew;
        serviceInfo.setServiceShpUserId(serviceUserIdNew);
        Boolean isUpdateServiceUserId = !serviceUserIdOld.equals(serviceUserIdNew);
        if (isUpdateServiceUserId) {
            updateFieldCount++;
            //????????????????????????????????????????????????????????????????????????","
            if (!updateFieldCount.equals(1)) {
                operateUpdateFieldContent += ",";
            }
            String serviceUserName = shpUserShopRefService.getNameFromShop(shopId, serviceUserIdNew);
            if (LocalUtils.isEmptyAndNull(serviceUserName)) {
                String serviceUserNameOld = shpUserShopRefService.getNameFromShop(shopId, serviceUserIdOld);
                operateUpdateFieldContent += "????????????????????????" + serviceUserNameOld + "???";
            } else {
                operateUpdateFieldContent += "????????????????????????" + serviceUserName + "???";
            }
        }

        ShpServiceRecord shpServiceRecord = new ShpServiceRecord();
        BeanUtils.copyProperties(serviceInfo, shpServiceRecord);
        shpServiceRecord.setUpdateTime(new Date());
        shpServiceRecord.setUpdateAdmin(userId);
        shpServiceRecordMapper.updateByPrimaryKeySelective(shpServiceRecord);
        if (!LocalUtils.isEmptyAndNull(serviceInfo.getCostAddOrUpList())) {
            // ??????/??????????????????
            JSONArray jsonArray = JSONObject.parseArray(serviceInfo.getCostAddOrUpList());
            List<ParamServiceRecordCostAddOrUp> costAddOrUps = JSONObject.parseArray(jsonArray.toJSONString(), ParamServiceRecordCostAddOrUp.class);
            serviceInfo.setCostAddOrUps(costAddOrUps);

        }
        shpServiceRecordCostService.addOrUpdate(serviceInfo);
        //????????????
        //ordOrderService.refreshSalaryForUser(shopId,serviceInfo.getServiceShpUserId(),userId,shpServiceRecord.getUpdateTime());

        //??????????????????????????????-????????????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SERVICE.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");

        //?????????????????????
        if (updateFieldCount > 0) {
            operateUpdateFieldContent = "(" + operateUpdateFieldContent + ")";
            operateContent += operateUpdateFieldContent;
        }
        paramAddShpOperateLog.setOperateContent(operateContent);
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        return shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
    }

    @Override
    public Integer deleteShpServiceType(Integer shopId, Integer userId, Integer shpServiceTypeId) {
        //????????????????????????????????????????????????????????????
//        String serviceTypeName = shpServiceTypeMapper.selectTypeNameById(shpServiceTypeId);
//        Boolean isUsed = shpServiceRecordMapper.selectShpServiceRecordByShopIdTypeName(shopId, serviceTypeName) > 0;
//        if (isUsed) {
//            throw new MyException("????????????????????????????????????");
//        }
        List<ShpServiceType> shpServiceTypes = shpServiceTypeMapper.getServiceType(shopId);
        if (shpServiceTypes != null && shpServiceTypes.size() == 1) {
            throw new MyException("?????????????????????????????????");
        }
        ShpServiceType shpServiceType = new ShpServiceType();
        shpServiceType.setId(shpServiceTypeId);
        shpServiceType.setUpdateAdmin(userId);
        shpServiceType.setUpdateTime(new Date());
        shpServiceType.setDel(ConstantCommon.DEL_ON);

        return shpServiceTypeMapper.updateByPrimaryKeySelective(shpServiceType);
    }

    @Override
    public BigDecimal getTotalFinishServiceProfit(Integer shopId, Integer userId, String startTimeStr, String endTimeStr) {
        return shpServiceRecordMapper.selectTotalFinishServiceProfit(shopId, userId, startTimeStr, endTimeStr);
    }

    @Override
    public Integer deleteShpServiceByDateRange(Integer shopId, Integer userId, String startDateTime, String endDateTime) {
        //??????????????????????????????, ????????????????????????
        List<String> numberList = shpServiceRecordMapper.listServiceNumber(shopId, startDateTime, endDateTime);
        if (!LocalUtils.isEmptyAndNull(numberList)) {
            //refreshSalaryForDelShpServiceRecord(shopId,userId,numberList);
            String numberStr = LocalUtils.packString(numberList.toArray());
            //????????????????????????
            shpServiceRecordMapper.deleteShpService(shopId, numberStr);
            //????????????????????????
            ordShareReceiptMapper.deleteShareReceipt(numberStr);
        }
        return 1;
    }

    ///**
    // * ??????????????????????????????????????????
    // * @param numberList
    // */
    //private void refreshSalaryForDelShpServiceRecord(Integer shopId,Integer userId,List<String> numberList) {
    //    if(!CollectionUtils.isEmpty(numberList)){
    //        for (String shpServiceRecordId : numberList) {
    //            ShpServiceRecord shpServiceRecord = shpServiceRecordMapper.selectByPrimaryKey(Integer.parseInt(shpServiceRecordId));
    //            //????????????-??????????????????????????????
    //            ordOrderService.refreshSalaryForUser(shopId,shpServiceRecord.getServiceShpUserId(),userId,shpServiceRecord.getUpdateTime());
    //        }
    //    }
    //}

    @Override
    public void deleteShpServiceTypeByShopId(int shopId) {
        shpServiceTypeMapper.deleteShpServiceTypeByShopId(shopId);
    }

    @Override
    public void deleteShpServiceRecordByShopId(int shopId) {
        List<String> numberList = shpServiceRecordMapper.listServiceNumber(shopId, null, null);
        //refreshSalaryForDelShpServiceRecord(shopId,null,numberList);
        shpServiceRecordMapper.deleteShpServiceRecordByShopId(shopId);
    }

    @Override
    public Integer deleteShpService(Integer shopId, Integer userId, Integer shpServiceId, HttpServletRequest request) {
        List<Integer> list = new ArrayList<>();
        list.add(shpServiceId);

        //List<String> numberList = new ArrayList<>();
        //numberList.add(""+shpServiceId);
        //refreshSalaryForDelShpServiceRecord(shopId,userId,numberList);

        String numberStr = LocalUtils.packString(list.toArray());

        //??????????????????????????????-????????????????????????
        ShpServiceRecord shpServiceRecord = shpServiceRecordMapper.selectByPrimaryKey(shpServiceId);
        //??????????????????????????????????????? ????????????
        if (LocalUtils.isEmptyAndNull(shpServiceRecord)) {
            return null;
        }
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SERVICE.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent(shpServiceRecord.getProdName());
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

        //????????????????????????????????????????????????????????????????????????
        return shpServiceRecordMapper.deleteShpService(shopId, numberStr);
    }

    @Override
    public VoShpServiceSettleCount countTodaySettle(ParamProductQuery queryParam) {
        return shpServiceRecordMapper.countTodaySettle(queryParam);
    }

    /**
     * ??????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private Boolean isCanUpdateServiceStatus(Integer shpServiceId) {
        ShpServiceRecord recordOld = shpServiceRecordMapper.selectByPrimaryKey(shpServiceId);
        if (null != recordOld && EnumServiceStatus.IN_SERVICE.getCode().equals(recordOld.getServiceStatus())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
