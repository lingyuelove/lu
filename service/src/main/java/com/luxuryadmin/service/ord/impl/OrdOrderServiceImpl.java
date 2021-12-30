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
                //结款状态不为空并且已开单
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
                    entrustStateName = "结款状态未填写";
                    break;
                case "0":
                    entrustStateName = "未结款";
                    break;
                case "1":

                    entrustStateName = "已结款";
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
                    case "客户订单":
                        proState = 40;
                        break;
                    case "代理订单":
                        proState = 41;
                        break;
                    case "友商订单":
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
            //修改人员id为负数的话,则代表是导入数据,不需要做判断
            if (order.getInsertAdmin() >= 0) {
                //如果该商品有多个数量,则成家价格进行累加;
                pro.setFinishPrice(newFinishPrice);
                int totalNum = pro.getTotalNum();
                int saleNum = order.getTotalNum();
                int newTotalNum = totalNum - saleNum;
                if (newTotalNum < 0) {
                    throw new MyException("库存不足，请解锁！" + newTotalNum);
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
            //入库时间是年月日时分秒,转化成年月日
            Date sDate = pro.getInsertTime();
            //销售时间是年月日
            Date eDate = order.getFinishTime();
            //库存天数;
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
            //添加收据
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
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage(), e);
            throw new MyException("开单失败: " + e.getMessage());
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
        //查看商品属性(长属性和短属性)
        voOrderLoad.setAttributeShortCn(servicesUtil.getAttributeCn(voOrderLoad.getAttributeUs(), false));
        if (!LocalUtils.isEmptyAndNull(voOrderLoad.getEntrustImg())) {
            //收款凭证图片的转化
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
     * 取消订单。退货退款
     *
     * @param shopId   订单number
     * @param ordOrder 订单number
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Integer shopId, OrdOrder ordOrder, String cancelReason, Integer cancelPerson, BasicParam basicParam) {
        /**
         * 1.修改订单状态为【已撤销】
         * 2.修改商品状态为【下架】
         * 3.修改数据库商品库存，添加订单里售出的件数
         * 4.修改（或删除）redis里的商品缓存？
         */

        String orderBizId = ordOrder.getNumber();
        //设置订单状态为退款
        ordOrder.setState("-20");
        ordOrder.setCancelReason(cancelReason);
        ordOrder.setCancelPerson(cancelPerson);
        ordOrder.setCancelTime(new Date());
        ordOrderMapper.updateObject(ordOrder);

        Integer productId = ordOrder.getFkProProductId();
        ProProduct proProduct = (ProProduct) proProductMapper.getObjectById(productId);
        //查询商品状态
        Integer stateCode = proProduct.getFkProStateCode();
        //修改商品【售出数量】=【售出数量】-【该笔订单售出数量】
        proProduct.setSaleNum(proProduct.getSaleNum() - ordOrder.getTotalNum());
        //修改商品【可用库存】=【可用库存】+【该笔订单售出数量】
        if ("-20".equals(ordOrder.getState()) && proProduct.getFkProStateCode() < 0) {
            //【可用库存】=【该笔订单售出数量】
            proProduct.setTotalNum(ordOrder.getTotalNum());
        } else {
            //【可用库存】=【可用库存】+【该笔订单售出数量】
            proProduct.setTotalNum(proProduct.getTotalNum() + ordOrder.getTotalNum());
        }
        //删除商品库存缓存
        String bizId = proProduct.getBizId();
        try {
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.5") >= 0) {
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
                if (proRedisNum.getLockNum() > 0) {
                    proProductService.subtractProRedisLeftNum(shopId, bizId, 0 - ordOrder.getTotalNum());
                } else {
                    //删除商品库存缓存
                    proProductService.deleteProRedisLockNum(shopId, bizId);
                }
                //2.6.5有锁单情况退货依旧要下架商品
                //修改商品状态为未上架
                proProduct.setFkProStateCode(EnumProState.STAND_BY_11.getCode());
            } else {
                //对于缓存,如果退单的商品还有锁单数量的话, 则把退单的数量加到可用数量里面去; 反之,删除商品缓存;
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
                if (proRedisNum.getLockNum() > 0) {
                    proProductService.subtractProRedisLeftNum(shopId, bizId, 0 - ordOrder.getTotalNum());
                } else {
                    //删除商品库存缓存
                    proProductService.deleteProRedisLockNum(shopId, bizId);
                    //修改商品状态为未上架
                    proProduct.setFkProStateCode(EnumProState.STAND_BY_11.getCode());
                }
            }
        } catch (Exception e) {
            log.info("退货错误" + e);
        }

        if (!LocalUtils.isEmptyAndNull(ordOrder.getEntrustState()) && "1".equals(ordOrder.getEntrustState())) {
            //判断商品是否是寄卖商品 寄卖商品结款退货时变为自有商品
            String attributeCode = EnumProAttribute.ENTRUST.getCode().toString();
            if (proProduct != null && attributeCode.equals(proProduct.getFkProAttributeCode())) {
                proProduct.setFkProAttributeCode(EnumProAttribute.OWN.getCode().toString());
            }
        }

        //未删除商品才可进行状态修改
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

        //修改收据
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

        //更新收货地址
        Integer orderId = order.getId();
        OrdAddress ordAddress = ordAddressMapper.selectByOrderId(orderId);
        String receiveAddressOld = "";
        Integer result = 0;
        if (null == ordAddress) {
            //有一项不为空;需要添加地址
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
        //增加年化收益率;修改于2021-09-17 17:10:22
        ProProduct pro = proProductService.getProProductById(orderOld.getFkProProductId());
        if (pro != null) {
            //入库时间是年月日时分秒,转化成年月日
            Date sDate = pro.getInsertTime();
            //销售时间是年月日
            Date eDate = order.getFinishTime();
            //库存天数;
            int day = DateUtil.getDifferDays(sDate, eDate);
            String yearRate = LocalUtils.calcYearRate(pro.getInitPrice(), order.getFinishPrice(), day);
            order.setYearRate(yearRate);
        }
        //保存订单修改记录
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
        //如果统计日期为空，默认为昨天
        if (null == countDate) {
            countDate = DateUtil.addDaysFromOldDate(new Date(), -1).getTime();
        }

        //查询指定日期，是否已经生成JOB
        Boolean isHaveGenerate = shpOrderDailyCountService.getCountByCountDate(countDate) > 0;
        if (isHaveGenerate) {
            throw new MyException("统计日期=" + DateUtil.formatShort(countDate) + "的【店铺订单统计数据】已生成，不能重复执行");
        }

        //查询所有店铺ID
        List<Integer> shopIdList = shpShopService.queryShpShopIdList();
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.generateShopOrderDailyCount(shopId, countDate);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushDailyCountShopOrderMsg(Date countDate) {
        //查询指定日期，是否已经生成JOB
        Boolean isHavePush = shpOrderDailyCountService.getMsgCountByCountDate(EnumOpMessageType.SHOP.getCode(), "shopOrderDailyCount", countDate) > 0;
        if (isHavePush) {
            throw new MyException("统计日期=" + DateUtil.formatShort(countDate) + "的【店铺订单统计数据】消息已推送，不能重复执行");
        }

        //查询所有店铺ID
        List<Integer> shopIdList = shpOrderDailyCountService.queryShpShopIdListByCountDate(countDate);
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.pushDailyCountShopOrderMsg(shopId, countDate);
        }
    }

    @Override
    public void pushDailyCountShopOrderMsgForMonth(Date countDate) {
        //查询指定日期，是否已经生成JOB
        Boolean isHavePush = shpOrderDailyCountService.getMsgCountByCountDate(EnumOpMessageType.SHOP.getCode(), EnumOpMessageSubType.DAILY_REPORT_MONTH.getCode(), countDate) > 0;
        if (isHavePush) {
            throw new MyException("统计日期=" + DateUtil.formatShort(countDate) + "的【店铺订单统计数据】消息已推送，不能重复执行");
        }
        //查询所有店铺ID
        List<Integer> shopIdList = shpOrderDailyCountService.queryShpShopIdListByCountDate(countDate);
        for (Integer shopId : shopIdList) {
            shpOrderDailyCountService.pushDailyCountShopOrderMsgForMonth(shopId, countDate);
        }
    }

    @Override
    public Integer saveOrdOrderModifyRecord(int shopId, int userId, OrdOrder ordUpdate, VoOrderLoad ordOld,
                                            String receiveAddressOld, String receiveAddressNew) {
        OrdModifyRecord ordModifyRecord = new OrdModifyRecord();
        //订单基础信息
        ordModifyRecord.setFkOrdOrderId(ordOld.getOrderId());
        ordModifyRecord.setFkShpShopId(shopId);
        ordModifyRecord.setFkProProductId(ordOld.getFkProProductId());
        ordModifyRecord.setCreateUserId(ordOld.getInsertAdmin());
        ordModifyRecord.setUpdateUserId(userId);

        //订单业务信息
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

        //订单时间信息
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
                orderTypeReturn = "客户订单";
                break;
            case "DL":
                orderTypeReturn = "代理订单";
                break;
            case "YS":
                orderTypeReturn = "友商订单";
                break;
            case "QT":
                orderTypeReturn = "其它订单";
                break;
            default:
                orderTypeReturn = orderType;
                break;
        }
        return orderTypeReturn;
    }

    @Override
    public List<VoOrderModifyRecord> listOrderModifyRecordById(Integer shopId, String orderBizId, boolean hasSeeSalePricePerm) {
        //获取订单ID
        VoOrderLoad voOrderLoad = ordOrderMapper.getOrderDetailByNumber(shopId, orderBizId);
        if (null == voOrderLoad) {
            log.info("查询订单修改记录列表，没有查询到对应的订单信息。orderBizId=" + orderBizId);
            throw new MyException("订单不存在");
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
        //店铺订单类型
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(salaryDetailQuery.getShopId());
        //如果该用户没有此订单类型的数据,则默认为0;
        if (!LocalUtils.isEmptyAndNull(ordTypeList)) {
            StringBuilder saleOrderType = new StringBuilder();
            StringBuilder grossOrderType = new StringBuilder();
            for (OrdType ordType : ordTypeList) {

                for (VoSalaryOrdType voOrdType : saleOrderTypeList) {
                    //如果用户包含此订单类型
                    if (ordType.getName().equals(voOrdType.getOrderType())) {
                        saleOrderType.append(ordType.getName()).append(",");
                    }
                }

                for (VoSalaryOrdType voOrdType : grossOrderTypeList) {
                    //如果用户包含此订单类型
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
            //删除订单凭证
            ordReceiptMapper.deleteOrdReceipt(orderNumber);
            //删除分享凭证记录
            ordShareReceiptMapper.deleteShareReceipt(orderNumber);
            //删除订单
            ordOrderMapper.deleteOrderByOrderNumber(shopId, orderNumber);

            //添加【店铺操作日志】-【删除订单】
            ProProduct proProduct = proProductService.getProProductById(proId);
            if (null != proProduct) {
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
                paramAddShpOperateLog.setOperateName("删除订单");
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
                paramAddShpOperateLog.setOrderId(ord.getId());
                paramAddShpOperateLog.setRequest(request);
                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
            //查询是否还有订单关联此商品, 如果没有,则删除此商品信息,如果有则不删除
            int num = ordOrderMapper.countOrderByProId(shopId, proId);
            if (num == 0) {
                int stateCode = proProduct.getFkProStateCode();
                //如果该商品还存在可用数量;则不可删除;
                boolean isBetween = LocalUtils.isBetween(stateCode, 10, 39);
                //不在上述状态范围内,则可物理删除
                if (!isBetween) {
                    //删除订单对应的商品(要放在最后一步操作，不然前面的操作会查询不到订单)。
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
            //订单单个删除 真删
            OrdOrder ordUp = new OrdOrder();
            ordUp.setId(ord.getId());
            ordUp.setDeleteRemark(delete.getDeleteRemark());
            ordUp.setState("-90");
            ordUp.setUpdateAdmin(delete.getUserId());
            ordUp.setUpdateTime(new Date());
            ordOrderMapper.updateObject(ordUp);
            //添加【店铺操作日志】-【删除订单】
            Integer proId = ord.getFkProProductId();
            Integer shopId = delete.getShopId();
            Integer userId = delete.getUserId();
            ProProduct proProduct = proProductService.getProProductById(proId);
            if (null != proProduct) {
                ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
                paramAddShpOperateLog.setShopId(shopId);
                paramAddShpOperateLog.setOperateUserId(userId);
                paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.ORDER.getName());
                paramAddShpOperateLog.setOperateName("删除订单");
                paramAddShpOperateLog.setOperateContent(proProduct.getName());
                paramAddShpOperateLog.setProdId(proProduct.getId());
                paramAddShpOperateLog.setOrderId(ord.getId());
                paramAddShpOperateLog.setRequest(request);
                shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
            }
            //删除发货信息
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

            //删除订单凭证
            ordReceiptMapper.deleteOrdReceipt(orderNumbers);
            //删除分享凭证记录
            ordShareReceiptMapper.deleteShareReceipt(orderNumbers);
            //删除订单
            ordOrderMapper.deleteOrderByOrderNumber(shopId, orderNumbers);
            //已经查询过的产品id
            ArrayList<Integer> proIdList = new ArrayList<>();
            for (OrdOrder order : ordList) {
                int proId = order.getFkProProductId();
                if (proIdList.contains(proId)) {
                    continue;
                }
                proIdList.add(proId);
                ProProduct proProduct = proProductService.getProProductById(proId);
                //查询是否还有订单关联此商品, 如果没有,则删除此商品信息,如果有则不删除
                int num = ordOrderMapper.countOrderByProId(shopId, proId);
                if (num == 0 && proProduct != null) {
                    int stateCode = proProduct.getFkProStateCode();
                    //如果该商品还存在可用数量;则不可删除;
                    boolean isBetween = LocalUtils.isBetween(stateCode, 10, 39);
                    //不在上述状态范围内,则可物理删除
                    if (!isBetween) {
                        //删除订单对应的商品(要放在最后一步操作，不然前面的操作会查询不到订单)。
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
                //订单多个删除不添加日志
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
        //删除订单
        ordOrderMapper.deleteOrderByShopId(shopId);
        //删除订单凭证
        ordReceiptMapper.deleteOrdReceiptByShopId(shopId);
        //删除分享凭证记录
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
    //        //销售人员id
    //        Integer saleUserId = ord.getFkShpUserId();
    //        Integer ordProId = ord.getFkProProductId();
    //        System.err.println("---proId: " + ordProId);
    //        ProProduct pro = proProductService.getProProductById(ordProId);
    //        Integer recycleUserId = 0;
    //        if (null != pro) {
    //            //回收人员id
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
    //    //后台线程,刷新与之相关的薪资,找到影响提成的人; 销售人员和回收人员; 找到开单时间;
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
                //缩略图
                String smallImg = orderForTemp.getSmallImg();

                orderForTemp.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));

                //查看商品属性(长属性和短属性)
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
        //物理删除订单数据, 包括订单凭证和分享凭证记录
        List<String> numberList = ordOrderMapper.listOrderNumber(shopId, startDateTime, endDateTime);
        //提前把订单所关联的商品id查找出来;
        List<String> proIdList = ordOrderMapper.listProductId(shopId, startDateTime, endDateTime);
        if (!LocalUtils.isEmptyAndNull(numberList)) {
            //删除订单
            ordOrderMapper.deleteOrderByDateTime(shopId, startDateTime, endDateTime);
            String numberStr = LocalUtils.packString(numberList.toArray());
            //删除订单凭证
            ordReceiptMapper.deleteOrdReceipt(numberStr);
            //删除分享凭证记录
            ordShareReceiptMapper.deleteShareReceipt(numberStr);
            String proIds = LocalUtils.packString(proIdList.toArray());
            //删除对应的售罄商品
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
            log.info("=====启动线程====");
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
                            log.info("=====--初始化年化收益率====");
                            ordOrderMapper.updateBatchYearRate(listOrd);
                            listOrd.clear();
                        }
                    } catch (Exception e) {

                        log.error(e.getMessage() + "#" + vo.getUploadStoreTime(), e);
                    }
                }
                //最后不足20000条记录,一次性更新
                if (!LocalUtils.isEmptyAndNull(listOrd)) {
                    log.info("=====一次性更新--初始化年化收益率====");
                    ordOrderMapper.updateBatchYearRate(listOrd);
                }
                log.info("======初始化结束-====");
            }
        });
    }

    /**
     * 获取发货信息列表
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
     * 添加到发货列表
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
     * 根据订单号查询订单信息
     *
     * @param orderNumber
     * @return
     */
    @Override
    public OrdOrder getOrderInfoByNumber(String orderNumber) {
        return ordOrderMapper.getOrderInfoByNumber(orderNumber);
    }

    /**
     * 根据店铺id获取开单人员简信息
     *
     * @param shopId
     * @return
     */
    @Override
    public List<VoOrderUserInfo> getFiltrateinfoByShopId(Integer shopId) {
        return ordOrderMapper.getFiltrateinfoByShopId(shopId);
    }

    /**
     * 根据id获取订单信息
     *
     * @param fkOrdOrderId
     * @return
     */
    @Override
    public OrdOrder getOrdOrderInfoById(Integer fkOrdOrderId) {
        return ordOrderMapper.getOrdOrderInfoById(fkOrdOrderId);
    }

    /**
     * 2.6.6发货列表
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
     * 获取代发货订单数量
     *
     * @param ids
     * @return
     */
    @Override
    public Integer getOrderSum(List<Integer> ids) {
        return ordOrderMapper.getOrderSum(ids);
    }


    /**
     * 将【订单修改记录】转换为VO
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
            String title = updateTimeStr + updateUserName + "修改了订单";
            voOrderModifyRecord.setTitle(title);
            voOrderModifyRecordList.add(voOrderModifyRecord);

            //如果没有查看成交价的权限
            if (!hasSeeSalePricePerm) {
                voOrderModifyRecord.setFinishPriceBefore(null);
                voOrderModifyRecord.setFinishPriceAfter(null);
            }
        }

        return voOrderModifyRecordList;
    }


}
