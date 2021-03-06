package com.luxuryadmin.service.ord.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.*;
import com.luxuryadmin.entity.ord.*;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.op.EnumOpMessageSubType;
import com.luxuryadmin.enums.op.EnumOpMessageType;
import com.luxuryadmin.enums.pro.EnumProAttribute;
import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.enums.pro.EnumProState;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.excel.ExVoOrder;
import com.luxuryadmin.mapper.fin.FinSalaryMapper;
import com.luxuryadmin.mapper.ord.*;
import com.luxuryadmin.mapper.pro.ProProductMapper;
import com.luxuryadmin.param.fin.ParamSalaryDetailQuery;
import com.luxuryadmin.param.ord.*;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.ord.OrdAddressService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.OrdReceiptService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.ProDetailService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpOrderDailyCountService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import com.luxuryadmin.vo.fin.VoFinSalaryDetail;
import com.luxuryadmin.vo.fin.VoSalaryOrdType;
import com.luxuryadmin.vo.ord.*;
import com.luxuryadmin.vo.pro.VoOrderUserInfo;
import com.luxuryadmin.vo.pro.VoProDeliverByPage;
import com.luxuryadmin.vo.pro.VoProRedisNum;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import com.luxuryadmin.entity.pro.ProDeliver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import com.luxuryadmin.service.pro.ProDeliverService;

/**
 * @author monkey king
 * @date 2019-12-25 21:46:38
 */
@Slf4j
@Service
public class OrdOrderServiceImpl implements OrdOrderService {

    @Resource
    private OrdOrderMapper ordOrderMapper;

    @Resource
    private ProProductMapper proProductMapper;

    @Autowired
    private OrdAddressService ordAddressService;

    @Autowired
    private OrdReceiptService ordReceiptService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ProDetailService proDetailService;

    @Autowired
    private ProProductService proProductService;

    @Resource
    private OrdAddressMapper ordAddressMapper;

    @Autowired
    private ShpOrderDailyCountService shpOrderDailyCountService;

    @Resource
    private OrdModifyRecordMapper ordModifyRecordMapper;

    @Resource
    private OrdReceiptMapper ordReceiptMapper;

    @Resource
    private OrdShareReceiptMapper ordShareReceiptMapper;

    @Autowired
    private FinSalaryService finSalaryService;

    @Resource
    private FinSalaryMapper finSalaryMapper;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private OrdTypeService ordTypeService;
    @Autowired
    protected ServicesUtil servicesUtil;
    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private ProDeliverService proDeliverService;


    @Override
    public List<VoOrderLoad> listOrderByCondition(ParamOrderQuery orderQuery) {
        if (orderQuery.getState() != null && "10".equals(orderQuery.getState())) {
            orderQuery.setState(null);
        }
        List<VoOrderLoad> orderLoads = ordOrderMapper.listOrderByCondition(orderQuery);
        if (orderLoads != null && orderLoads.size() > 0) {
            orderLoads.forEach(voOrderLoad -> {
                //????????????????????????????????????
                if (voOrderLoad.getEntrustState() != null && "20".equals(voOrderLoad.getState())) {
                    String entrustStateName = getEntrustState(voOrderLoad.getEntrustState());
                    voOrderLoad.setEntrustStateName(entrustStateName);
                } else {
                    voOrderLoad.setEntrustState("-1");
                }
            });
        }
        return orderLoads;
    }

    @Override
    public List<ExVoOrder> listOrderByConditionExcelExport(ParamOrderQuery orderQuery) {
        return ordOrderMapper.listOrderByConditionExcelExport(orderQuery);
    }

    public static String getEntrustState(String entrustState) {
        String entrustStateName = null;
        if (entrustState != null) {
            switch (entrustState) {
                case "-1":
                    entrustStateName = "?????????????????????";
                    break;
                case "0":
                    entrustStateName = "?????????";
                    break;
                case "1":

                    entrustStateName = "?????????";
                    break;
                default:
                    break;
            }
        }
        return entrustStateName;
    }

    @Override
    public VoSaleRankHomePageData getOrderNumAndPrice(ParamOrderQuery orderQuery) {
        return ordOrderMapper.getOrderNumAndPrice(orderQuery);
    }

    @Override
    public int saveOrdOrder(OrdOrder ordOrder) {
        return ordOrderMapper.saveObject(ordOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirmOrder(ProProduct pro, OrdOrder order, OrdAddress address) {
        VoUserShopBase shopInfo = shpShopService.getShopInfoToOrdReceipt(pro.getFkShpShopId());
        try {
            String orderType = order.getType();
            int proState = 43;
            if (null != orderType) {
                switch (orderType) {
                    case "????????????":
                        proState = 40;
                        break;
                    case "????????????":
                        proState = 41;
                        break;
                    case "????????????":
                        proState = 42;
                        break;
                    default:
                        break;
                }
            }
            BigDecimal orderPrice = order.getFinishPrice();
            BigDecimal finishPrice = pro.getFinishPrice();
            Object finishPriceObj = LocalUtils.isEmptyAndNull(finishPrice) ? "0" : finishPrice;
            BigDecimal newFinishPrice = LocalUtils.calcNumber(orderPrice, "+", finishPriceObj);
            //????????????id???????????????,????????????????????????,??????????????????
            if (order.getInsertAdmin() >= 0) {
                //??????????????????????????????,???????????????????????????;
                pro.setFinishPrice(newFinishPrice);
                int totalNum = pro.getTotalNum();
                int saleNum = order.getTotalNum();
                int newTotalNum = totalNum - saleNum;
                if (newTotalNum < 0) {
                    throw new MyException("???????????????????????????" + newTotalNum);
                }
                pro.setTotalNum(newTotalNum);
                pro.setSaleNum(pro.getSaleNum() + saleNum);
                Date finishTime = new Date();
                if (newTotalNum == 0) {
                    pro.setFinishTime(finishTime);
                    pro.setFkProStateCode(proState);
                }
                if (null == order.getFinishTime()) {
                    order.setFinishTime(finishTime);
                }
            }
            order.setState("20");
            order.setNumber(order.getFkShpShopId() + "" + System.currentTimeMillis());
            proProductMapper.updateProProductByShopIdBizId(pro);
            proDetailService.updateUniqueCodeByProId(pro.getId(), LocalUtils.returnEmptyStringOrString(order.getUniqueCode()));
            //?????????????????????????????????,??????????????????
            Date sDate = pro.getInsertTime();
            //????????????????????????
            Date eDate = order.getFinishTime();
            //????????????;
            int day = DateUtil.getDifferDays(sDate, eDate);
            String yearRate = LocalUtils.calcYearRate(pro.getInitPrice(), order.getFinishPrice(), day);
            order.setYearRate(yearRate);
            saveOrdOrder(order);
            Integer orderId = order.getId();
            String partyB = "";
            if (!LocalUtils.isEmptyAndNull(address)) {
                address.setFkOrdOrderId(orderId);
                //partyB = address.getName();
                ordAddressService.saveOrdAddress(address);
            }
            //????????????
            OrdReceipt ordReceipt = new OrdReceipt();
            ordReceipt.setName(pro.getName());
            ordReceipt.setUnitPrice(order.getFinishPrice());
            ordReceipt.setTotalPrice(order.getFinishPrice());
            ordReceipt.setNum(order.getTotalNum());
            ordReceipt.setAddress(shopInfo.getShopAddress());
            ordReceipt.setPhone(shopInfo.getPhone());
            ordReceipt.setState("10");
            ordReceipt.setPartyA(shopInfo.getShopName());
            ordReceipt.setPartyB(partyB);
            ordReceipt.setProUniqueCode(order.getUniqueCode());
            ordReceipt.setFkShpUserId(order.getFkShpUserId());
            ordReceipt.setFkOrdOrderId(orderId);
            ordReceipt.setFkOrdOrderNumber(order.getNumber());
            ordReceipt.setFkShpShopId(order.getFkShpShopId());
            ordReceipt.setInsertTime(order.getInsertTime());
            ordReceipt.setInsertAdmin(order.getInsertAdmin());
            ordReceipt.setVersions(1);
            ordReceipt.setDel("0");
            ordReceiptService.saveOrUpdateOrdReceipt(ordReceipt);
            return order.getNumber();
        } catch (Exception e) {
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("????????????: " + e.getMessage());
        }
    }

    @Override
    public VoOrderLoad getOrderDetailByNumber(int shopId, String number) {
        VoOrderLoad voOrderLoad = ordOrderMapper.getOrderDetailByNumber(shopId, number);
        if (voOrderLoad == null) {
            return null;
        }
        if (null != voOrderLoad && LocalUtils.isEmptyAndNull(voOrderLoad.getReceiveAddress())) {
            String receiveAddress = "";
            String customer = voOrderLoad.getCustomer();
            customer = null == customer ? "" : customer;
            String phone = voOrderLoad.getContact();
            phone = null == phone ? "" : phone;
            String address = voOrderLoad.getAddress();
            address = null == address ? "" : address;
            receiveAddress = customer + phone + address;
            voOrderLoad.setReceiveAddress(receiveAddress);
        }
        if (voOrderLoad.getEntrustState() != null) {

            voOrderLoad.setEntrustStateName(getEntrustState(voOrderLoad.getEntrustState()));
        }
        //??????????????????(?????????????????????)
        voOrderLoad.setAttributeShortCn(servicesUtil.getAttributeCn(voOrderLoad.getAttributeUs(), false));
        if (!LocalUtils.isEmptyAndNull(voOrderLoad.getEntrustImg())) {
            //???????????????????????????
            String smallImg = voOrderLoad.getEntrustImg();
            List<String> smallImgList = Arrays.asList(smallImg.split(";"));
            List<String> smallImgs = new ArrayList<>();
            smallImgList.forEach(s -> {
                if (!s.contains("http")) {
                    smallImgs.add(servicesUtil.formatImgUrl(s, true));
                } else {
                    smallImgs.add(s);
                }
            });
            voOrderLoad.setEntrustImgList(smallImgs);
            String tagNameList = StringUtils.join(smallImgs, ",");
            voOrderLoad.setEntrustImg(tagNameList);
        }
        if (voOrderLoad.getRecycleAdmin() != null && voOrderLoad.getRecycleAdmin() == 0) {
            voOrderLoad.setRecycleAdmin(null);
        }
        return voOrderLoad;
    }

    @Override
    public List<VoOrder> queryOrdOrderList(ParamOrdOrder paramOrdOrder) {
        return ordOrderMapper.listOrderByPara(paramOrdOrder);
    }

    @Override
    public VoOrder getOrdOrderInfo(String id) {
        return ordOrderMapper.getOrdOrderInfo(LocalUtils.strParseInt(id));
    }

    /**
     * ???????????????????????????
     *
     * @param shopId   ??????number
     * @param ordOrder ??????number
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Integer shopId, OrdOrder ordOrder, String cancelReason, Integer cancelPerson, BasicParam basicParam) {
        /**
         * 1.????????????????????????????????????
         * 2.?????????????????????????????????
         * 3.????????????????????????????????????????????????????????????
         * 4.?????????????????????redis?????????????????????
         */

        String orderBizId = ordOrder.getNumber();
        //???????????????????????????
        ordOrder.setState("-20");
        ordOrder.setCancelReason(cancelReason);
        ordOrder.setCancelPerson(cancelPerson);
        ordOrder.setCancelTime(new Date());
        ordOrderMapper.updateObject(ordOrder);

        Integer productId = ordOrder.getFkProProductId();
        ProProduct proProduct = (ProProduct) proProductMapper.getObjectById(productId);
        //??????????????????
        Integer stateCode = proProduct.getFkProStateCode();
        //??????????????????????????????=??????????????????-??????????????????????????????
        proProduct.setSaleNum(proProduct.getSaleNum() - ordOrder.getTotalNum());
        //??????????????????????????????=??????????????????+??????????????????????????????
        if ("-20".equals(ordOrder.getState()) && proProduct.getFkProStateCode() < 0) {
            //??????????????????=??????????????????????????????
            proProduct.setTotalNum(ordOrder.getTotalNum());
        } else {
            //??????????????????=??????????????????+??????????????????????????????
            proProduct.setTotalNum(proProduct.getTotalNum() + ordOrder.getTotalNum());
        }
        //????????????????????????
        String bizId = proProduct.getBizId();
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
                if (proRedisNum.getLockNum() > 0) {
                    proProductService.subtractProRedisLeftNum(shopId, bizId, 0 - ordOrder.getTotalNum());
                } else {
                    //????????????????????????
                    proProductService.deleteProRedisLockNum(shopId, bizId);
                }
                //2.6.5??????????????????????????????????????????
                //??????????????????????????????
                proProduct.setFkProStateCode(EnumProState.STAND_BY_11.getCode());
            } else {
                //????????????,?????????????????????????????????????????????, ????????????????????????????????????????????????; ??????,??????????????????;
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
                if (proRedisNum.getLockNum() > 0) {
                    proProductService.subtractProRedisLeftNum(shopId, bizId, 0 - ordOrder.getTotalNum());
                } else {
                    //????????????????????????
                    proProductService.deleteProRedisLockNum(shopId, bizId);
                    //??????????????????????????????
                    proProduct.setFkProStateCode(EnumProState.STAND_BY_11.getCode());
                }
            }
        } catch (Exception e) {
            log.info("????????????" + e);
        }

        if (!LocalUtils.isEmptyAndNull(ordOrder.getEntrustState()) && "1".equals(ordOrder.getEntrustState())) {
            //????????????????????????????????? ?????????????????????????????????????????????
            String attributeCode = EnumProAttribute.ENTRUST.getCode().toString();
            if (proProduct != null && attributeCode.equals(proProduct.getFkProAttributeCode())) {
                proProduct.setFkProAttributeCode(EnumProAttribute.OWN.getCode().toString());
            }
        }

        //???????????????????????????????????????
        if (stateCode > 0) {
            proProductMapper.updateObject(proProduct);
        } else {
            proProduct.setTotalNum(ordOrder.getTotalNum());
            proProductMapper.updateObject(proProduct);
        }

    }

    @Override
    public Integer updateOrder(OrdOrder order, String receiveAddressNew, VoOrderLoad orderOld) {
        Integer shopId = order.getFkShpShopId();
        Integer userId = order.getUpdateAdmin();

        //????????????
        OrdReceipt ordReceipt = new OrdReceipt();
        VoOrdReceipt voOrdReceipt = ordReceiptService.getOrdReceiptByOrderNumber(order.getFkShpShopId(), order.getNumber());
        ordReceipt.setId(voOrdReceipt.getId());
        ordReceipt.setUnitPrice(order.getFinishPrice());
        ordReceipt.setTotalPrice(order.getFinishPrice());
        ordReceipt.setNum(order.getTotalNum());
        ordReceipt.setState(order.getState());
        ordReceipt.setFkShpUserId(order.getFkShpUserId());
        ordReceipt.setFkOrdOrderId(order.getId());
        ordReceipt.setFkOrdOrderNumber(order.getNumber());
        ordReceipt.setFkShpShopId(shopId);
        ordReceipt.setVersions(1);
        ordReceipt.setProUniqueCode(order.getUniqueCode());
        ordReceiptService.saveOrUpdateOrdReceipt(ordReceipt);

        //??????????????????
        Integer orderId = order.getId();
        OrdAddress ordAddress = ordAddressMapper.selectByOrderId(orderId);
        String receiveAddressOld = "";
        Integer result = 0;
        if (null == ordAddress) {
            //??????????????????;??????????????????
            ordAddress = new OrdAddress();
            ordAddress.setFkOrdOrderId(orderId);
            ordAddress.setReceiveAddress(receiveAddressNew);
            ordAddress.setFkShpShopId(order.getFkShpShopId());
            ordAddress.setInsertTime(new Date());
            result = ordAddressMapper.saveObject(ordAddress);
        } else {
            receiveAddressOld = ordAddress.getReceiveAddress();
            ordAddress.setReceiveAddress(receiveAddressNew);
            ordAddress.setUpdateTime(new Date());
            result = ordAddressMapper.updateObject(ordAddress);
        }
        //?????????????????????;?????????2021-09-17 17:10:22
        ProProduct pro = proProductService.getProProductById(orderOld.getFkProProductId());
        if (pro != null) {
            //?????????????????????????????????,??????????????????
            Date sDate = pro.getInsertTime();
            //????????????????????????
            Date eDate = order.getFinishTime();
            //????????????;
            int day = DateUtil.getDifferDays(sDate, eDate);
            String yearRate = LocalUtils.calcYearRate(pro.getInitPrice(), order.getFinishPrice(), day);
            order.setYearRate(yearRate);
        }
        //????????????????????????
        saveOrdOrderModifyRecord(shopId, userId, order, orderOld, receiveAddressOld, receiveAddressNew);
        return ordOrderMapper.updateObject(order);
    }

    @Override
    public Integer updateOrderEntrust(ParamOrderUploadEntrust orderUploadEntrust) {
        OrdOrder ordOrder = new OrdOrder();
        ordOrder.setId(Integer.parseInt(orderUploadEntrust.getOrderBizId()));
        ordOrder.setEntrustState("1");
        ordOrder.setEntrustRemark(orderUploadEntrust.getEntrustRemark());
        ordOrder.setEntrustImg(orderUploadEntrust.getEntrustImg());
        ordOrder.setUpdateTime(new Date());
        ordOrder.setUpdateAdmin(orderUploadEntrust.getUserId());
        ordOrder.setEntrustAdmin(orderUploadEntrust.getUserId());
        ordOrder.setEntrustTime(new Date());
        return ordOrderMapper.updateObject(ordOrder);
    }

    @Override
    public OrdOrder getOrdOrderDetail(int shopId, String orderBizId) {
        Map<String, Object> params = new HashMap<>();
        params.put("shopId", shopId);
        params.put("number", orderBizId);
        return ordOrderMapper.getOrdOrderDetail(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dailyCountShopOrder(Date countDate) {
        //??????????????????????????????????????????
        if (null == countDate) {
            countDate = DateUtil.addDaysFromOldDate(new Date(), -1).getTime();
        }

        //???????????????????????????????????????JOB
        Boolean isHaveGenerate = shpOrderDailyCountService.getCountByCountDate(countDate) > 0;
        if (isHaveGenerate) {
            throw new MyException("????????????=" + DateUtil.formatShort(countDate) + "???????????????????????????????????????????????????????????????");
        }

        //??????????????????ID
        List<Integer> shopIdList = shpShopService.queryShpShopIdList();
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.generateShopOrderDailyCount(shopId, countDate);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushDailyCountShopOrderMsg(Date countDate) {
        //???????????????????????????????????????JOB
        Boolean isHavePush = shpOrderDailyCountService.getMsgCountByCountDate(EnumOpMessageType.SHOP.getCode(), "shopOrderDailyCount", countDate) > 0;
        if (isHavePush) {
            throw new MyException("????????????=" + DateUtil.formatShort(countDate) + "?????????????????????????????????????????????????????????????????????");
        }

        //??????????????????ID
        List<Integer> shopIdList = shpOrderDailyCountService.queryShpShopIdListByCountDate(countDate);
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.pushDailyCountShopOrderMsg(shopId, countDate);
        }
    }

    @Override
    public void pushDailyCountShopOrderMsgForMonth(Date countDate) {
        //???????????????????????????????????????JOB
        Boolean isHavePush = shpOrderDailyCountService.getMsgCountByCountDate(EnumOpMessageType.SHOP.getCode(), EnumOpMessageSubType.DAILY_REPORT_MONTH.getCode(), countDate) > 0;
        if (isHavePush) {
            throw new MyException("????????????=" + DateUtil.formatShort(countDate) + "?????????????????????????????????????????????????????????????????????");
        }
        //??????????????????ID
        List<Integer> shopIdList = shpOrderDailyCountService.queryShpShopIdListByCountDate(countDate);
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.pushDailyCountShopOrderMsgForMonth(shopId, countDate);
        }
    }

    @Override
    public Integer saveOrdOrderModifyRecord(int shopId, int userId, OrdOrder ordUpdate, VoOrderLoad ordOld,
                                            String receiveAddressOld, String receiveAddressNew) {
        OrdModifyRecord ordModifyRecord = new OrdModifyRecord();
        //??????????????????
        ordModifyRecord.setFkOrdOrderId(ordOld.getOrderId());
        ordModifyRecord.setFkShpShopId(shopId);
        ordModifyRecord.setFkProProductId(ordOld.getFkProProductId());
        ordModifyRecord.setCreateUserId(ordOld.getInsertAdmin());
        ordModifyRecord.setUpdateUserId(userId);

        //??????????????????
        ordModifyRecord.setUniqueCodeBefore(ordOld.getUniqueCode());
        ordModifyRecord.setUniqueCodeAfter(ordUpdate.getUniqueCode());
        String oldType = ordOld.getOrderType();
        String newType = convertOrdType(ordUpdate.getType());
        ordModifyRecord.setTypeBefore(oldType);
        ordModifyRecord.setTypeAfter(newType);
        ordModifyRecord.setFinishPriceBefore(new BigDecimal(ordOld.getSalePrice()));
        ordModifyRecord.setFinishPriceAfter(ordUpdate.getFinishPrice());
        ordModifyRecord.setSaleUserIdBefore(ordOld.getSaleUserId());
        ordModifyRecord.setSaleUserIdAfter(ordUpdate.getFkShpUserId());


        ordModifyRecord.setAfterSaleGuaranteeBefore(ordOld.getAfterSaleGuarantee());
        ordModifyRecord.setAfterSaleGuaranteeAfter(ordUpdate.getAfterSaleGuarantee());
        ordModifyRecord.setRemarkBefore(ordOld.getRemark());
        ordModifyRecord.setRemarkAfter(ordUpdate.getRemark());
        ordModifyRecord.setAddressBefore(receiveAddressOld);
        ordModifyRecord.setAddressAfter(receiveAddressNew);

        //??????????????????
        ordModifyRecord.setInsertTime(new Date());
        ordModifyRecord.setUpdateTime(new Date());

        return ordModifyRecordMapper.insertSelective(ordModifyRecord);
    }

    private String convertOrdType(String orderType) {
        String orderTypeReturn = "";
        if (LocalUtils.isEmptyAndNull(orderType)) {
            return orderTypeReturn;
        }

        switch (orderType) {
            case "KH":
                orderTypeReturn = "????????????";
                break;
            case "DL":
                orderTypeReturn = "????????????";
                break;
            case "YS":
                orderTypeReturn = "????????????";
                break;
            case "QT":
                orderTypeReturn = "????????????";
                break;
            default:
                orderTypeReturn = orderType;
                break;
        }
        return orderTypeReturn;
    }

    @Override
    public List<VoOrderModifyRecord> listOrderModifyRecordById(Integer shopId, String orderBizId, boolean hasSeeSalePricePerm) {
        //????????????ID
        VoOrderLoad voOrderLoad = ordOrderMapper.getOrderDetailByNumber(shopId, orderBizId);
        if (null == voOrderLoad) {
            log.info("????????????????????????????????????????????????????????????????????????orderBizId=" + orderBizId);
            throw new MyException("???????????????");
        }

        Integer orderId = voOrderLoad.getOrderId();
        List<VoOrderModifyRecord> recordList = ordModifyRecordMapper.selectRecordListByOrderIdShopId(orderId, shopId);
        List<VoOrderModifyRecord> voOrderModifyRecordList = formatOrdModifyRecord(recordList, hasSeeSalePricePerm);
        return voOrderModifyRecordList;
    }

    @Override
    public List<List<VoSalaryOrdType>> countSaleAndGrossProfitMoney(ParamSalaryDetailQuery salaryDetailQuery) {
        List<VoFinSalaryDetail> list = ordOrderMapper.countSaleAndGrossProfitMoney(salaryDetailQuery);
        List<VoSalaryOrdType> saleOrderTypeList = new ArrayList<>();
        List<VoSalaryOrdType> grossOrderTypeList = new ArrayList<>();
        if (!LocalUtils.isEmptyAndNull(list)) {
            for (VoFinSalaryDetail sa : list) {
                BigDecimal saleMoney = sa.getSaleMoney();
                BigDecimal grossProfitMoney = sa.getGrossProfitMoney();

                int num = sa.getNum();
                String orderType = sa.getOrderType();
                String proAttr = sa.getProAttr();

                VoSalaryOrdType saleOrdType = pickVoSalaryOrdType(num, orderType, proAttr, saleMoney);
                saleOrderTypeList.add(saleOrdType);
                VoSalaryOrdType grossOrdType = pickVoSalaryOrdType(num, orderType, proAttr, grossProfitMoney);
                grossOrderTypeList.add(grossOrdType);
            }
        }
        //??????????????????
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(salaryDetailQuery.getShopId());
        //?????????????????????????????????????????????,????????????0;
        if (!LocalUtils.isEmptyAndNull(ordTypeList)) {
            StringBuilder saleOrderType = new StringBuilder();
            StringBuilder grossOrderType = new StringBuilder();
            for (OrdType ordType : ordTypeList) {

                for (VoSalaryOrdType voOrdType : saleOrderTypeList) {
                    //?????????????????????????????????
                    if (ordType.getName().equals(voOrdType.getOrderType())) {
                        saleOrderType.append(ordType.getName()).append(",");
                    }
                }

                for (VoSalaryOrdType voOrdType : grossOrderTypeList) {
                    //?????????????????????????????????
                    if (ordType.getName().equals(voOrdType.getOrderType())) {
                        grossOrderType.append(ordType.getName()).append(",");
                    }
                }
            }
            String sale = saleOrderType.toString();
            String gross = grossOrderType.toString();

            for (OrdType ordType : ordTypeList) {
                if (!sale.contains(ordType.getName())) {
                    VoSalaryOrdType vo = pickVoSalaryOrdType(0, ordType.getName(), "10", new BigDecimal(0));
                    saleOrderTypeList.add(vo);
                }

                if (!gross.contains(ordType.getName())) {
                    VoSalaryOrdType vo = pickVoSalaryOrdType(0, ordType.getName(), "10", new BigDecimal(0));
                    grossOrderTypeList.add(vo);
                }
            }

        }

        List<List<VoSalaryOrdType>> mainList = new ArrayList<>();
        mainList.add(saleOrderTypeList);
        mainList.add(grossOrderTypeList);
        return mainList;
    }

    private VoSalaryOrdType pickVoSalaryOrdType
            (int num, String orderType, String proAttr, BigDecimal money) {
        VoSalaryOrdType saleOrdType = new VoSalaryOrdType();
        saleOrdType.setNum(num);
        saleOrdType.setOrderType(orderType);
        saleOrdType.setProAttr(proAttr);
        saleOrdType.setMoney(money);
        return saleOrdType;
    }

    @Override
    public Object countRecycleProfit(int shopId, int recycleUserId, String proAttr, String startDateTime, String endDateTime) {
        return ordOrderMapper.countRecycleProfit(shopId, recycleUserId, proAttr, startDateTime, endDateTime);
    }

    @Override
    public void deleteOrder(int shopId, int userId, String orderNumber, HttpServletRequest request) {
        OrdOrder ord = ordOrderMapper.getOrdOrderByShopIdAndOrdNumber(shopId, orderNumber);
        if (null != ord) {
            Integer proId = ord.getFkProProductId();
            //??????????????????
            ordReceiptMapper.deleteOrdReceipt(orderNumber);
            //????????????????????????
            ordShareReceiptMapper.deleteShareReceipt(orderNumber);
            //????????????
            ordOrderMapper.deleteOrderByOrderNumber(shopId, orderNumber);

            //??????????????????????????????-??????????????????
            ProProduct proProduct = proProductService.getProProductById(proId);
            if (null != proProduct) {
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
                paramAddShpOperateLog.setOperateName("????????????");
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
                paramAddShpOperateLog.setOrderId(ord.getId());
                paramAddShpOperateLog.setRequest(request);
                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
            //???????????????????????????????????????, ????????????,????????????????????????,?????????????????????
            int num = ordOrderMapper.countOrderByProId(shopId, proId);
            if (num == 0) {
                int stateCode = proProduct.getFkProStateCode();
                //????????????????????????????????????;???????????????;
                boolean isBetween = LocalUtils.isBetween(stateCode, 10, 39);
                //???????????????????????????,??????????????????
                if (!isBetween) {
                    //???????????????????????????(????????????????????????????????????????????????????????????????????????)???
                    proProductService.deleteProProductById(proId.toString());
                }
            }
            //refreshOrd(ord);
        }
    }

    @Override
    public void deleteOrderForFalse(ParamOrderDelete delete, HttpServletRequest request) {
        OrdOrder ord = ordOrderMapper.getOrdOrderByShopIdAndOrdNumber(delete.getShopId(), delete.getOrderBizId());
        if (null != ord) {
            //?????????????????? ??????
            OrdOrder ordUp = new OrdOrder();
            ordUp.setId(ord.getId());
            ordUp.setDeleteRemark(delete.getDeleteRemark());
            ordUp.setState("-90");
            ordUp.setUpdateAdmin(delete.getUserId());
            ordUp.setUpdateTime(new Date());
            ordOrderMapper.updateObject(ordUp);
            //??????????????????????????????-??????????????????
            Integer proId = ord.getFkProProductId();
            Integer shopId = delete.getShopId();
            Integer userId = delete.getUserId();
            ProProduct proProduct = proProductService.getProProductById(proId);
            if (null != proProduct) {
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
                paramAddShpOperateLog.setOperateName("????????????");
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
                paramAddShpOperateLog.setOrderId(ord.getId());
                paramAddShpOperateLog.setRequest(request);
                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
            //??????????????????
            proDeliverService.deleteDeliverInfoByOrderInfo(ord.getId());
        }

    }

    @Override
    public void deleteOrderForDel(ParamOrderDelete delete) {
        OrdOrder ord = ordOrderMapper.getOrdOrderByShopIdAndOrdNumber(delete.getShopId(), delete.getOrderBizId());
        if (null != ord) {
            ord.setDeleteRemark(delete.getDeleteRemark());
            ord.setState("-90");
            ord.setUpdateAdmin(delete.getUserId());
            ord.setDel("1");
            ord.setUpdateTime(new Date());
            ordOrderMapper.updateObject(ord);
        }
    }

    @Override
    public void deleteBatchOrder(int shopId, String[] orderNumberArray) {
        String orderNumbers = LocalUtils.packString(orderNumberArray);
        List<OrdOrder> ordList = ordOrderMapper.listOrdOrderByShopIdAndOrdNumber(shopId, orderNumbers);
        if (!LocalUtils.isEmptyAndNull(ordList)) {

            //??????????????????
            ordReceiptMapper.deleteOrdReceipt(orderNumbers);
            //????????????????????????
            ordShareReceiptMapper.deleteShareReceipt(orderNumbers);
            //????????????
            ordOrderMapper.deleteOrderByOrderNumber(shopId, orderNumbers);
            //????????????????????????id
            ArrayList<Integer> proIdList = new ArrayList<>();
            for (OrdOrder order : ordList) {
                int proId = order.getFkProProductId();
                if (proIdList.contains(proId)) {
                    continue;
                }
                proIdList.add(proId);
                ProProduct proProduct = proProductService.getProProductById(proId);
                //???????????????????????????????????????, ????????????,????????????????????????,?????????????????????
                int num = ordOrderMapper.countOrderByProId(shopId, proId);
                if (num == 0 && proProduct != null) {
                    int stateCode = proProduct.getFkProStateCode();
                    //????????????????????????????????????;???????????????;
                    boolean isBetween = LocalUtils.isBetween(stateCode, 10, 39);
                    //???????????????????????????,??????????????????
                    if (!isBetween) {
                        //???????????????????????????(????????????????????????????????????????????????????????????????????????)???
                        proProductService.deleteProProductById(proId + "");
                    }
                }
            }
        }
    }

    @Override
    public void deleteBatchOrderForFalse(ParamOrderDelete delete, HttpServletRequest request) {
        List<String> list = Arrays.asList(delete.getOrderBizId().split(","));
        if (list != null && list.size() > 0) {
            list.forEach(s -> {
                //?????????????????????????????????
                delete.setOrderBizId(s);
                OrdOrder ord = ordOrderMapper.getOrdOrderByShopIdAndOrdNumber(delete.getShopId(), delete.getOrderBizId());
                if (!LocalUtils.isEmptyAndNull(ord)) {
                    OrdOrder ordUp = new OrdOrder();
                    ordUp.setId(ord.getId());
                    ordUp.setDeleteRemark(delete.getDeleteRemark());
                    ordUp.setState("-90");
                    ordUp.setUpdateAdmin(delete.getUserId());
                    ordUp.setUpdateTime(new Date());
                    ordOrderMapper.updateObject(ordUp);
                }


            });
        }
    }


    @Override
    public void deleteOrderByShopId(int shopId) {
        //????????????
        ordOrderMapper.deleteOrderByShopId(shopId);
        //??????????????????
        ordReceiptMapper.deleteOrdReceiptByShopId(shopId);
        //????????????????????????
        ordShareReceiptMapper.deleteShareReceiptByShopId(shopId);
    }

    //@Override
    //public void refreshSalaryByOrderId(Integer ordId) {
    //    if (null == ordId) {
    //        return;
    //    }
    //    OrdOrder ord = (OrdOrder) ordOrderMapper.getObjectById(ordId);
    //    if (null == ord) {
    //        return;
    //    }
    //    refreshOrd(ord);
    //
    //}
    //
    //private void refreshOrd(OrdOrder ord) {
    //    try {
    //        //????????????id
    //        Integer saleUserId = ord.getFkShpUserId();
    //        Integer ordProId = ord.getFkProProductId();
    //        System.err.println("---proId: " + ordProId);
    //        ProProduct pro = proProductService.getProProductById(ordProId);
    //        Integer recycleUserId = 0;
    //        if (null != pro) {
    //            //????????????id
    //            recycleUserId = pro.getRecycleAdmin();
    //        }
    //        int shopId = ord.getFkShpShopId();
    //        System.err.println("---shopId: " + shopId);
    //        refreshSalaryForUser(shopId, saleUserId, ord.getInsertAdmin(), ord.getFinishTime());
    //        if (!saleUserId.equals(recycleUserId) && recycleUserId != 0) {
    //            refreshSalaryForUser(shopId, recycleUserId, ord.getInsertAdmin(), ord.getFinishTime());
    //        }
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        log.error(e.getMessage(), e);
    //    }
    //}

    //@Override
    //public void refreshSalaryForUser(int shopId, int userId, int localUserId, Date startDate) {
    //    //????????????,???????????????????????????,????????????????????????; ???????????????????????????; ??????????????????;
    //    ThreadUtils.getInstance().executorService.execute(() -> {
    //        finSalaryService.refreshFinSalary(shopId, userId, localUserId, startDate);
    //    });
    //}

    @Override
    public Integer getTotalOrderNumByShopId(Integer shopId) {
        return ordOrderMapper.getTotalOrderNumByShopId(shopId);
    }

    @Override
    public VoOrderForTempPage getOrderForTemp(ParamOrdForTempSearch ordForTempSearch) {
        PageHelper.startPage(Integer.parseInt(ordForTempSearch.getPageNum()), 10);
        List<VoOrderForTemp> orderForTemps = ordOrderMapper.getOrderForTemp(ordForTempSearch);
        if (orderForTemps != null && orderForTemps.size() > 0) {
            orderForTemps.forEach(orderForTemp -> {
                //?????????
                String smallImg = orderForTemp.getSmallImg();

                orderForTemp.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

                //??????????????????(?????????????????????)
                orderForTemp.setAttributeShortCn(servicesUtil.getAttributeCn(orderForTemp.getAttributeCodeCn(), false));
            });
        }
        PageInfo<VoOrderForTemp> pageInfo = new PageInfo(orderForTemps);
        VoOrderForTempPage orderForTempPage = new VoOrderForTempPage();
        orderForTempPage.setPageNum(pageInfo.getPageNum());
        orderForTempPage.setPageSize(pageInfo.getPageSize());
        if (pageInfo.getNextPage() > 0) {
            orderForTempPage.setHasNextPage(true);
        } else {
            orderForTempPage.setHasNextPage(false);
        }
        orderForTempPage.setList(orderForTemps);
        return orderForTempPage;
    }

    @Override
    public Integer getOrderNumByTemp(Integer shopId, Integer productId, Integer tempId) {
        return ordOrderMapper.getOrderNumByTemp(shopId, productId, tempId);
    }

    @Override
    public VoOrder getLastOrderInfo(int shopId, Integer userId) {
        return ordOrderMapper.getLastOrderInfo(shopId, userId);
    }

    @Override
    public void deleteOrderAndReceipt(int shopId, String startDateTime, String endDateTime) {
        //????????????????????????, ???????????????????????????????????????
        List<String> numberList = ordOrderMapper.listOrderNumber(shopId, startDateTime, endDateTime);
        //?????????????????????????????????id????????????;
        List<String> proIdList = ordOrderMapper.listProductId(shopId, startDateTime, endDateTime);
        if (!LocalUtils.isEmptyAndNull(numberList)) {
            //????????????
            ordOrderMapper.deleteOrderByDateTime(shopId, startDateTime, endDateTime);
            String numberStr = LocalUtils.packString(numberList.toArray());
            //??????????????????
            ordReceiptMapper.deleteOrdReceipt(numberStr);
            //????????????????????????
            ordShareReceiptMapper.deleteShareReceipt(numberStr);
            String proIds = LocalUtils.packString(proIdList.toArray());
            //???????????????????????????
            proProductService.deleteProProductById(proIds);
        }
    }

    @Override
    public List<VoEmployee> listSaleUserByShopId(int shopId) {
        return ordOrderMapper.listSaleUserByShopId(shopId);
    }

    @Override
    public void initYearRate() {
        ThreadUtils.getInstance().executorService.execute(() -> {
            log.info("=====????????????====");
            List<VoOrderLoad> list = ordOrderMapper.listNullYearRateOrder();
            if (!LocalUtils.isEmptyAndNull(list)) {
                List<OrdOrder> listOrd = new ArrayList<>();
                for (VoOrderLoad vo : list) {
                    try {
                        OrdOrder ord = new OrdOrder();
                        ord.setId(vo.getOrderId());
                        int days = DateUtil.getDifferDays(DateUtil.parse(vo.getUploadStoreTime()), vo.getInsertTime());
                        String s = LocalUtils.calcYearRate(new BigDecimal(vo.getInitPrice()), new BigDecimal(vo.getSalePrice()), days);
                        ord.setYearRate(s);
                        if (!LocalUtils.isEmptyAndNull(s)) {
                            listOrd.add(ord);
                        }
                        if (listOrd.size() == 5000) {
                            log.info("=====--????????????????????????====");
                            ordOrderMapper.updateBatchYearRate(listOrd);
                            listOrd.clear();
                        }
                    } catch (Exception e) {

                        log.error(e.getMessage() + "#" + vo.getUploadStoreTime(), e);
                    }
                }
                //????????????20000?????????,???????????????
                if (!LocalUtils.isEmptyAndNull(listOrd)) {
                    log.info("=====???????????????--????????????????????????====");
                    ordOrderMapper.updateBatchYearRate(listOrd);
                }
                log.info("======???????????????-====");
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    public List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param) {
        if (StringUtil.isNotBlank(param.getOrderType())) {
            String[] split = param.getOrderType().split(";");
            List<String> strings = Arrays.asList(split);
            param.setType(strings);
        }
        List<VoProDeliverByPage> proDeliverInfo = ordOrderMapper.getProDeliverInfo(param);
        return proDeliverInfo;
    }

    /**
     * ?????????????????????
     *
     * @param ordOrder
     */
    @Override
    public void savProDeliver(OrdOrder ordOrder) {
        OrdAddress ordAddress = ordAddressMapper.selectByOrderId(ordOrder.getId());
        ProDeliver proDeliver = new ProDeliver();
        proDeliver.setFkOrdOrderId(ordOrder.getId());
        proDeliver.setFkShpShopId(ordOrder.getFkShpShopId());
        proDeliver.setFkProProductId(ordOrder.getFkProProductId());
        proDeliver.setInsertAdmin(ordOrder.getInsertAdmin());
        proDeliver.setNum(ordOrder.getTotalNum());
        proDeliver.setDeliverSource(EnumProDeliverSource.ORDER.name());
        proDeliver.setNumber(LocalUtils.getTimestamp());
        proDeliver.setState(0);
        if (ordAddress != null) {
            proDeliver.setFkOrdAddressId(ordAddress.getId());
        }
        proDeliver.setInsertTime(new Date());
        proDeliver.setUpdateTime(new Date());
        proDeliver.setDel("0");
        proDeliverService.saveProDeliver(proDeliver);
    }

    /**
     * ?????????????????????????????????
     *
     * @param orderNumber
     * @return
     */
    @Override
    public OrdOrder getOrderInfoByNumber(String orderNumber) {
        return ordOrderMapper.getOrderInfoByNumber(orderNumber);
    }

    /**
     * ????????????id???????????????????????????
     *
     * @param shopId
     * @return
     */
    @Override
    public List<VoOrderUserInfo> getFiltrateinfoByShopId(Integer shopId) {
        return ordOrderMapper.getFiltrateinfoByShopId(shopId);
    }

    /**
     * ??????id??????????????????
     *
     * @param fkOrdOrderId
     * @return
     */
    @Override
    public OrdOrder getOrdOrderInfoById(Integer fkOrdOrderId) {
        return ordOrderMapper.getOrdOrderInfoById(fkOrdOrderId);
    }

    /**
     * 2.6.6????????????
     *
     * @param ids
     * @param state
     * @return
     */
    @Override
    public List<VoProDeliverByPage> listProDeliverInfo(List<Integer> ids, String state) {
        return ordOrderMapper.listProDeliverInfo(ids, state);
    }

    /**
     * ???????????????????????????
     *
     * @param ids
     * @return
     */
    @Override
    public Integer getOrderSum(List<Integer> ids) {
        return ordOrderMapper.getOrderSum(ids);
    }


    /**
     * ????????????????????????????????????VO
     *
     * @param recordList
     * @return
     */
    private List<VoOrderModifyRecord> formatOrdModifyRecord(List<VoOrderModifyRecord> recordList, boolean hasSeeSalePricePerm) {
        List<VoOrderModifyRecord> voOrderModifyRecordList = new ArrayList<>();
        if (CollectionUtils.isEmpty(recordList)) {
            return null;
        }

        for (VoOrderModifyRecord voOrderModifyRecord : recordList) {
            String updateTimeStr = DateUtil.format(voOrderModifyRecord.getUpdateTime());

            String updateUserName = voOrderModifyRecord.getUpdateUserName();
            String title = updateTimeStr + updateUserName + "???????????????";
            voOrderModifyRecord.setTitle(title);
            voOrderModifyRecordList.add(voOrderModifyRecord);

            //????????????????????????????????????
            if (!hasSeeSalePricePerm) {
                voOrderModifyRecord.setFinishPriceBefore(null);
                voOrderModifyRecord.setFinishPriceAfter(null);
            }
        }

        return voOrderModifyRecordList;
    }


}
