package com.luxuryadmin.service.fin.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.fin.FinSalary;
import com.luxuryadmin.entity.fin.FinSalaryDetail;
import com.luxuryadmin.mapper.fin.FinSalaryDetailMapper;
import com.luxuryadmin.mapper.fin.FinSalaryMapper;
import com.luxuryadmin.param.fin.ParamCreateSalaryQuery;
import com.luxuryadmin.param.fin.ParamSalaryQuery;
import com.luxuryadmin.service.fin.FinSalaryDetailService;
import com.luxuryadmin.service.fin.FinSalaryService;
import com.luxuryadmin.vo.fin.VoCreateSalaryDetail;
import com.luxuryadmin.vo.fin.VoFinSalary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-09-24 0:02:19
 */
@Slf4j
@Service
public class FinSalaryServiceImpl implements FinSalaryService {

    @Resource
    private FinSalaryMapper finSalaryMapper;

    @Resource
    private FinSalaryDetailMapper finSalaryDetailMapper;

    @Autowired
    private FinSalaryDetailService finSalaryDetailService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<VoFinSalary> listFinSalaryByDate(ParamSalaryQuery salaryQuery) {
        return finSalaryMapper.listFinSalaryByDate(salaryQuery);
    }

    @Override
    public Long countSalaryMoney(ParamSalaryQuery salaryQuery) {
        return finSalaryMapper.countSalaryMoney(salaryQuery);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSalary(FinSalary finSalary, FinSalaryDetail finSalaryDetail) {
        try {
            finSalaryMapper.saveObject(finSalary);
            int finSalaryId = finSalary.getId();
            finSalaryDetail.setFkFinSalaryId(finSalaryId);
            finSalaryDetailMapper.saveObject(finSalaryDetail);
        } catch (Exception e) {
            log.info("=====??????????????????--??????: shopId:{};userId{}", finSalary.getFkShpShopId(), finSalary.getFkShpUserId());
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSalary(FinSalary finSalary, FinSalaryDetail finSalaryDetail) {
        try {
            finSalaryDetailMapper.updateObject(finSalaryDetail);
            finSalary.setId(finSalaryDetail.getFkFinSalaryId());
            finSalaryMapper.updateObject(finSalary);
        } catch (Exception e) {
            log.info("=====??????????????????--??????: shopId:{};userId{}", finSalary.getFkShpShopId(), finSalary.getFkShpUserId());
            log.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public List<Integer> listAlreadyCreateSalaryUserId(int shopId, String startDate, String endDate) {
        return finSalaryMapper.listAlreadyCreateSalaryUserId(
                shopId, startDate + " 00:00:00", endDate + " 23:59:59");
    }

    @Override
    public void deleteFinSalary(int shopId, String startDateTime, String endDateTime) {
        //???????????????????????????
        List<Integer> finSalaryIdList = finSalaryMapper.listFinSalaryId(shopId, startDateTime, endDateTime);
        if (!LocalUtils.isEmptyAndNull(finSalaryIdList)) {
            String finSalaryIds = LocalUtils.packString(finSalaryIdList.toArray());
            //????????????
            finSalaryMapper.deleteFinSalary(finSalaryIds);
            //??????????????????
            finSalaryDetailMapper.deleteFinSalaryDetail(finSalaryIds);
        }
    }

    @Override
    public void refreshFinSalary(int shopId, int userId, int localUserId, Date startDate) {
        try {
            HashMap<String, String> map = getStartDateAndEndDate(startDate);
            String st = map.get("startDate");
            String et = map.get("endDate");
            FinSalaryDetail fd = finSalaryDetailService.getFinSalaryDetail(shopId, userId, st);
            if (null != fd) {
                FinSalary salary = (FinSalary) finSalaryMapper.getObjectById(fd.getFkFinSalaryId());
                //???????????????????????????
                if (null != salary && ConstantCommon.ZERO.equals(salary.getSalaryState())) {
                    try {
                        ParamCreateSalaryQuery param = new ParamCreateSalaryQuery();
                        param.setLocalUserId(localUserId);
                        param.setShopId(fd.getFkShpShopId());
                        param.setUserId(fd.getFkShpUserId() + "");
                        param.setProAttr(fd.getProductAttr());
                        param.setBasicMoney(fd.getBasicMoney().toString());
                        param.setElseMoney(fd.getElseMoney().toString());
                        param.setSchemeType(fd.getSchemeType());
                        param.setRecycleInitPricePercent(fd.getRecycleInitPricePercent().toString());
                        param.setRecycleUnitPrice(fd.getRecycleUnitPrice().toString());
                        param.setRecycleProfitPercent(fd.getRecycleProfitPercent().toString());
                        param.setServiceProfitPercent(fd.getServiceProfitPercent().toString());

                        param.setStartDate(st);
                        param.setEndDate(et);
                        finSalaryDetailService.createSalary(param, null, null);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private HashMap<String, String> getStartDateAndEndDate(Date startDate) throws ParseException {
        //????????????, ?????????1?????????
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String pattern = "yyyy-MM";
        //??????????????????
        String st = DateUtil.formatShort(cal.getTime());
        //??????????????????
        String et;
        Date now = new Date();
        now = DateUtil.parse(DateUtil.format(now, pattern), pattern);

        Date mySt = DateUtil.parse(DateUtil.format(cal.getTime(), pattern), pattern);

        if (now.equals(mySt) || mySt.after(now)) {
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            et = DateUtil.formatShort(cal.getTime());
        } else {
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            et = DateUtil.formatShort(cal.getTime());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("startDate", st);
        map.put("endDate", et);
        return map;
    }

    @Override
    public void initFinSalary(int shopId, Integer userId, Date startDate) {
        try {

            HashMap<String, String> map = getStartDateAndEndDate(startDate);
            String st = map.get("startDate");
            String et = map.get("endDate");
            //String st = DateUtil.formatShort(startDate);
            //?????????????????????????????????;
            VoCreateSalaryDetail fd = finSalaryDetailService.getCreateSalaryDetail(shopId, userId, st);

            String createState = "0";
            if (fd != null) {
                //??????????????????
                createState = "1";
            } else {
                fd = finSalaryDetailService.getOwnLastCreateSalaryDetail(shopId, userId);
                if (fd == null) {
                    fd = finSalaryDetailService.getShopLastCreateSalaryDetail(shopId);
                }
            }
            //?????????????????????????????????
            if ("0".equals(createState)) {
                ParamCreateSalaryQuery param = new ParamCreateSalaryQuery();
                String proAttr = "10,20";
                String basicMoney = "0";
                //????????????????????????????????????,??????:??????????????????????????????
                String elseMoney = "0";
                String schemeType = "";
                String recycleInitPricePercent = "0";
                String recycleUnitPrice = "0";
                String recycleProfitPercent = "0";
                String serviceProfitPercent = "0";
                if (null != fd) {
                    proAttr = fd.getProAttr();
                    basicMoney = fd.getBasicMoney();
                    schemeType = fd.getSchemeType();
                    recycleInitPricePercent = fd.getRecycleInitPricePercent();
                    recycleUnitPrice = fd.getRecycleUnitPrice();
                    recycleProfitPercent = fd.getRecycleProfitPercent();
                    serviceProfitPercent = fd.getServiceProfitPercent();
                }
                param.setLocalUserId(-1);
                param.setShopId(shopId);
                param.setUserId(userId + "");
                param.setProAttr(proAttr);
                param.setBasicMoney(basicMoney);
                param.setElseMoney(elseMoney);
                param.setSchemeType(schemeType);
                param.setRecycleInitPricePercent(recycleInitPricePercent);
                param.setRecycleUnitPrice(recycleUnitPrice);
                param.setRecycleProfitPercent(recycleProfitPercent);
                param.setServiceProfitPercent(serviceProfitPercent);
                param.setSalaryState("0");
                param.setStartDate(st);
                param.setEndDate(et);
                param.setUpdateOrSave("0");
                finSalaryDetailService.createSalary(param, null, null);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void realTimeFinSalary(int shopId, String startDate, String endDate) {

        List<Integer> userIdList = listAlreadyCreateSalaryUserId(shopId, startDate, endDate);
        if (LocalUtils.isEmptyAndNull(userIdList)) {
            return;
        }
        try {
            int userSize = userIdList.size();
            //5???????????????;
            int threadGroup = userSize / 5;
            //??????20?????????; ??????????????????5?????????; ???????????????????????????,?????????for??????????????????;?????????20???,???????????????;
            int threadSize = Math.min(Math.max(threadGroup, 1), 20);
            //????????????????????????????????????;
            int threadTaskSize = userSize / threadSize;

            String key = "_refreshFinSalary:" + shopId + ":" + LocalUtils.getUUID();
            redisUtil.set(key, "0");
            for (int i = 1; i <= threadSize; i++) {
                List<Integer> newList = new ArrayList<>();
                if (i == threadSize) {
                    //??????????????????;????????????????????????
                    newList.addAll(userIdList);
                } else {
                    for (int j = 0; j < threadTaskSize; j++) {
                        newList.add(userIdList.get(j));
                    }
                    userIdList.removeAll(newList);
                }
                ThreadUtils.getInstance().executorService.execute(() -> {
                    for (Integer userId : newList) {
                        try {
                            refreshFinSalary(shopId, userId, -1, DateUtil.parseShort(startDate));
                        } catch (ParseException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                    synchronized (key) {
                        String finishNum = redisUtil.get(key);
                        int finishNumInt = Integer.parseInt(finishNum);
                        finishNumInt++;
                        redisUtil.set(key, finishNumInt + "");
                    }
                });
            }
            //13???????????????
            for (int i = 1; i <= 26; i++) {
            //for(;;){
                Thread.sleep(50);
                String finishNum = redisUtil.get(key);
                int finishNumInt = Integer.parseInt(finishNum);
                if (finishNumInt == threadSize) {
                    redisUtil.delete(key);
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteFinSalaryForDestroyShop(int shopId) {
        finSalaryMapper.deleteFinSalaryByShopId(shopId);
        finSalaryDetailMapper.deleteFinSalaryDetailByShopId(shopId);
    }
}

