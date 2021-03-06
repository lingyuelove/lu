package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.pro.ProConveyProduct;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.entity.ord.OrdAddress;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.ord.OrdAddressMapper;
import com.luxuryadmin.mapper.pro.ProConveyProductMapper;
import com.luxuryadmin.mapper.pro.ProLockRecordMapper;
import com.luxuryadmin.param.pro.ParamLockProductQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.param.pro.ParamProductLock;
import com.luxuryadmin.param.pro.ParamProductLockRemark;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.ord.OrdAddressService;
import com.luxuryadmin.service.pro.ProConveyProductService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.vo.pro.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.luxuryadmin.service.pro.ProDeliverService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-05-29 19:28:37
 */
@Slf4j
@Service
public class ProLockRecordServiceImpl implements ProLockRecordService {


    @Resource
    private ProLockRecordMapper proLockRecordMapper;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpOperateLogService shpOperateLogService;
    @Autowired
    private OrdAddressService ordAddressService;
    @Resource
    private OrdAddressMapper ordAddressMapper;

    @Autowired
    private ProDeliverService proDeliverService;
    @Autowired
    private ProConveyProductService proConveyProductService;
    @Override
    public int saveProLockRecord(ProLockRecord proLockRecord) {
        proLockRecordMapper.saveObject(proLockRecord);
        return proLockRecord.getId();
    }

    @Override
    public int updateProLockRecord(ProLockRecord proLockRecord) {
        //????????????id??????
        if (LocalUtils.isEmptyAndNull(proLockRecord.getFkOrdAddressId())) {
            //???????????????????????????
            if (!LocalUtils.isEmptyAndNull(proLockRecord.getAddress())) {
                Integer addressId = getAddressId(proLockRecord.getAddress(), proLockRecord.getFkShpShopId());
                proLockRecord.setFkOrdAddressId(addressId);
            }
        } else {
            //????????????id?????????
            //??????????????????
            if (LocalUtils.isEmptyAndNull(proLockRecord.getAddress())) {
                proLockRecord.setAddress("");
            }
            OrdAddress ads = new OrdAddress();
            ads.setReceiveAddress(proLockRecord.getAddress());
            ads.setId(proLockRecord.getFkOrdAddressId());
            ordAddressMapper.updateObject(ads);
        }
        return proLockRecordMapper.updateObject(proLockRecord);
    }

    @Override
    public int getProductLockNumByProId(int proId) {
        return proLockRecordMapper.getProductLockNumByProId(proId);
    }

    /**
     * ????????????????????????
     *
     * @param proLockRecord
     */
    private void saveOrUpdate(ProLockRecord proLockRecord) {
        if (LocalUtils.isEmptyAndNull(proLockRecord.getId())) {
            proLockRecordMapper.saveObject(proLockRecord);
        } else {
            proLockRecordMapper.updateObject(proLockRecord);
        }
    }

    @Override
    public void lockProductByUserId(ProProduct pro, int userId, int myLockNum, ParamProductLock lockParam, HttpServletRequest request) {

        int proId = pro.getId();
        int shopId = pro.getFkShpShopId();
        int state = pro.getFkProStateCode();
        int codeStart = 20;
        int codeEnd = 39;
        if (!LocalUtils.isBetween(state, codeStart, codeEnd)) {
            throw new MyException("???????????????,????????????????????????????????????");
        }

        String bizId = pro.getBizId();

        VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
        //????????????redis
        int leftNum = proRedisNum.getLeftNum();
        int lockNum = proRedisNum.getLockNum();
        int totalNum = proRedisNum.getTotalNum();
        if (myLockNum > leftNum) {
            throw new MyException("??????????????????????????????????????????????????????" + leftNum);
        }
        if (lockNum >= totalNum) {
            throw new MyException("??????????????????????????????????????????????????????????????????" + lockNum + ":" + leftNum);
        }
        ProLockRecord lockRecord = proLockRecordMapper.getProLockRecordByBizIdAndUserId(bizId, userId);
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            lockRecord = new ProLockRecord();
            lockRecord.setFkProProductId(proId);
            lockRecord.setFkShpShopId(shopId);
            lockRecord.setLockUserId(userId);
            lockRecord.setLockNum(0);
            lockRecord.setInsertTime(new Date());
            lockRecord.setUpdateTime(lockRecord.getInsertTime());
            lockRecord.setProBizId(bizId);
        }
        lockRecord.setState(0);
        lockRecord.setLockNum(lockRecord.getLockNum() + myLockNum);
        lockRecord.setRemark(lockParam.getLockReason());
        saveOrUpdate(lockRecord);
        proProductService.updateProRedisLockNum(shopId, bizId, myLockNum, true);

        //??????????????????????????????-????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("??????");
        paramAddShpOperateLog.setOperateContent(pro.getName());
        paramAddShpOperateLog.setProdId(proId);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        //?????????????????????
        ProDeliver proDeliver = setProDeliver(lockRecord);
        proDeliverService.saveProDeliver(proDeliver);
    }

    @Override
    public void unlockProductByUserId(ProLockRecord lockRecord, int unlockUserId, int myLockNum, HttpServletRequest request) {

        int shopId = lockRecord.getFkShpShopId();
        int proId = lockRecord.getFkProProductId();
        ProProduct pro = proProductService.getProProductById(proId);
        String bizId = pro.getBizId();
        //2.6.5????????????????????????????????????
//        int state = pro.getFkProStateCode();
//        int codeStart = 20;
//        int codeEnd = 39;
//
//        if (!LocalUtils.isBetween(state, codeStart, codeEnd)) {
//            throw new MyException("????????????,????????????????????????????????????");
//        }

        int maxUnlockNum = lockRecord.getLockNum();
        if (myLockNum > maxUnlockNum) {
            throw new MyException("????????????,????????????????????????;???????????????: " + maxUnlockNum);
        }
        int leftNumDb = maxUnlockNum - myLockNum;
        //0:?????????; 1:???????????????
        if (leftNumDb == 0) {
            lockRecord.setState(1);
        }
        lockRecord.setLockNum(leftNumDb);
        lockRecord.setUnlockUserId(unlockUserId);
        lockRecord.setUnlockTime(new Date());
        proLockRecordMapper.updateObject(lockRecord);
        proProductService.updateProRedisLockNum(shopId, bizId, myLockNum, false);

        //??????????????????????????????-????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(unlockUserId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("??????");
        paramAddShpOperateLog.setOperateContent(pro.getName());
        paramAddShpOperateLog.setProdId(proId);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);
        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
    }

    @Override
    public List<VoProductLoad> listLockProductByShopId(int shopId, String proName, String uniqueCode) {
        return proLockRecordMapper.listLockProductByShopId(shopId, proName, uniqueCode);
    }

    @Override
    public VoProductLoad getProductDetail(int shopId, String bizId) {
        return proLockRecordMapper.getProductDetail(shopId, bizId);
    }

    @Override
    public List<VoProLockRecord> listVoProLockRecordByBizId(int shopId, String bizId) {
        return proLockRecordMapper.listVoProLockRecordByBizId(shopId, bizId);
    }

    @Override
    public List<ProLockRecord> lisProLockRecordByBizId(int shopId, String bizId) {
        return proLockRecordMapper.lisProLockRecordByBizId(shopId, bizId);
    }

    @Override
    public Integer getLockIdByBizId(int shopId, String bizId) {
        return proLockRecordMapper.getLockIdByBizId(shopId, bizId);
    }

    @Override
    public int deleteProLockRecord(int shopId, String bizId) {
        //?????????????????????????????????
        List<ProLockRecord> proLockRecord = proLockRecordMapper.getProLockRecordByProId(shopId, bizId);
        for (ProLockRecord pl :
                proLockRecord) {
            proDeliverService.deleteDeliverInfoByLockId(pl.getId());
            //????????????????????????????????? ????????????
            proConveyProductService.lockRecordForConvey(pl,null);


        }
        return proLockRecordMapper.deleteProLockRecordByProId(shopId, bizId);
    }

    @Override
    public ProLockRecord getProLockRecordById(int lockId) {
        return (ProLockRecord) proLockRecordMapper.getObjectById(lockId);
    }

    @Override
    public VoProLockRecord getProLockRecordByIdAndShopId(int shopId, int lockId) {
        return proLockRecordMapper.getProLockRecordByIdAndShopId(shopId, lockId);
    }

    @Override
    public VoProLockRecord getLockRecordByIdAndShopId(int shopId, int lockId) {
        return proLockRecordMapper.getLockRecordByIdAndShopId(shopId, lockId);
    }

    @Override
    public void deleteProLockRecordByShopId(int shopId) {
        proLockRecordMapper.deleteProLockRecordByShopId(shopId);
    }

    @Override
    public void updateProLockRecordForRemark(ParamProductLockRemark productLockRemark) {
        ProLockRecord lockRecord = new ProLockRecord();
        lockRecord.setId(Integer.parseInt(productLockRemark.getLockId()));
        lockRecord.setRemark(productLockRemark.getLockReason());
        proLockRecordMapper.updateObject(lockRecord);
    }

    @Override
    public List<VoProLockRecord> listLockUser(int shopId) {
        return proLockRecordMapper.listLockUser(shopId);
    }


    @Override
    public VoProLockTotal countProLockTotal(ParamLockProductQuery params) {
        return proLockRecordMapper.countProLockTotal(params);
    }

    @Override
    public int countMyLockNum(int shopId, int userId) {
        return proLockRecordMapper.countMyLockNum(shopId, userId);
    }

    @Override
    public int countConveyProLockNum(int shopId, int proId, String type) {
        return proLockRecordMapper.countConveyProLockNum(shopId, proId,type);
    }

    @Override
    public List<VoProductLoad> listLockProductByParam(ParamLockProductQuery params) {
        return proLockRecordMapper.listLockProductByParam(params);
    }

    @Override
    public int lockProductReturnId(ParamProductLock lockParam, HttpServletRequest request) {
        int shopId = lockParam.getShopId();
        int userId = lockParam.getUserId();
        String bizId = lockParam.getBizId();
        int myLockNum = Integer.parseInt(lockParam.getLockNum());

        ProProduct pro = proProductService.getProProductByShopIdBizId(shopId, bizId);
        if (LocalUtils.isEmptyAndNull(pro)) {
            throw new MyException("???????????????!");
        }
        int proId = pro.getId();
        //2.6.5???????????????????????????????????????=
//        int state = pro.getFkProStateCode();
//        int codeStart = 20;
//        int codeEnd = 39;
//        if (!LocalUtils.isBetween(state, codeStart, codeEnd)) {
//            throw new MyException("???????????????,????????????????????????????????????");
//        }


        VoProRedisNum proRedisNum = proProductService.getProRedisNum(shopId, bizId);
        //????????????redis
        int leftNum = proRedisNum.getLeftNum();
        int lockNum = proRedisNum.getLockNum();
        int totalNum = proRedisNum.getTotalNum();
        if (myLockNum > leftNum) {
            throw new MyException("??????????????????????????????????????????????????????" + leftNum);
        }
        if (lockNum >= totalNum) {
            throw new MyException("??????????????????????????????????????????????????????????????????" + lockNum + ":" + leftNum);
        }
        ProLockRecord lockRecord = new ProLockRecord();
        lockRecord.setFkProProductId(proId);
        lockRecord.setFkShpShopId(shopId);
        lockRecord.setLockUserId(userId);
        lockRecord.setInsertTime(new Date());
        lockRecord.setUpdateTime(lockRecord.getInsertTime());
        lockRecord.setProBizId(bizId);
        lockRecord.setState(0);
        lockRecord.setLockNum(myLockNum);
        lockRecord.setRemark(lockParam.getLockReason());
        lockRecord.setPreMoney(LocalUtils.formatBigDecimal(lockParam.getPreMoney()));
        lockRecord.setPreFinishMoney(LocalUtils.formatBigDecimal(lockParam.getPreFinishMoney()));
        lockRecord.setSonRecordId(lockParam.getSonRecordId());
        lockRecord.setConveyLockType(lockParam.getConveyLockType());
        Integer addressId = null;
        //???????????????????????????
        if (!LocalUtils.isEmptyAndNull(lockParam.getAddress())) {
            addressId = getAddressId(lockParam.getAddress(), lockParam.getShopId());
        }
        lockRecord.setFkOrdAddressId(addressId);
        int lockId = saveProLockRecord(lockRecord);

        //??????????????????
        proProductService.updateProRedisLockNum(shopId, bizId, myLockNum, true);

        //??????????????????????????????-????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
        paramAddShpOperateLog.setOperateName("??????");
        paramAddShpOperateLog.setOperateContent(pro.getName());
        paramAddShpOperateLog.setProdId(proId);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        //??????????????????????????????????????? ?????????????????????????????????????????? ???????????????????????????
        if (LocalUtils.isEmptyAndNull(lockRecord.getConveyLockType())){
            ProDeliver proDeliver = setProDeliver(lockRecord);
            proDeliverService.saveProDeliver(proDeliver);
        }

        return lockId;

    }

    public Integer getAddressId(String address, Integer shopId) {
        OrdAddress ads = new OrdAddress();
        ads.setReceiveAddress(address);
        ads.setFkShpShopId(shopId);
        ads.setInsertTime(new Date());
        ads.setFkOrdOrderId(0);
        Integer addressId = ordAddressService.saveOrdAddress(ads);
        return addressId;
    }

    /**
     * ??????????????????
     *
     * @param proLockRecord
     * @return
     */
    public ProDeliver setProDeliver(ProLockRecord proLockRecord) {
        ProDeliver proDeliver = new ProDeliver();
        proDeliver.setFkProLockRecordId(proLockRecord.getId());
        proDeliver.setFkProProductId(proLockRecord.getFkProProductId());
        proDeliver.setFkShpShopId(proLockRecord.getFkShpShopId());
        proDeliver.setInsertAdmin(proLockRecord.getLockUserId());
        proDeliver.setNum(proLockRecord.getLockNum());
        if (proLockRecord.getFkOrdAddressId() != null) {
            proDeliver.setFkOrdAddressId(proLockRecord.getFkOrdAddressId());
        }
        proDeliver.setDeliverSource(EnumProDeliverSource.LOCK_RECORD.name());
        proDeliver.setNumber(LocalUtils.getTimestamp());
        proDeliver.setState(0);
        proDeliver.setInsertTime(new Date());
        proDeliver.setUpdateTime(new Date());
        proDeliver.setDel("0");
        return proDeliver;
    }

    /**
     * ????????????????????????
     *
     * @param param
     * @return
     */
    @Override
    public List<VoProDeliverByPage> getProDeliverInfo(ParamProPageDeliver param) {
        return proLockRecordMapper.getProDeliverInfo(param);
    }

    /**
     * ????????????id???????????????????????????
     *
     * @param shopId
     * @return
     */
    @Override
    public List<VoOrderUserInfo> getFiltrateinfoByShopId(Integer shopId) {
        return proLockRecordMapper.getFiltrateinfoByShopId(shopId);
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
        return proLockRecordMapper.listProDeliverInfo(ids, state);
    }

    /**
     * ????????????id??????????????????
     *
     * @param ids
     * @param shopId
     * @return
     */
    @Override
    public List<VoLockNumByProId> getLockByProId(List<Integer> ids, Integer shopId) {
        return proLockRecordMapper.getLockByProId(ids, shopId);
    }

    /**
     * ??????????????????
     *
     * @param ids
     * @return
     */
    @Override
    public Integer getLockSum(List<Integer> ids) {
        return proLockRecordMapper.getLockSum(ids);
    }
}
