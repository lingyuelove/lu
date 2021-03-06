package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RandomNumber;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.shp.ShpNumberRecord;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpShopNumber;
import com.luxuryadmin.enums.shp.EnumNumberRecordType;
import com.luxuryadmin.enums.shp.EnumNumberType;
import com.luxuryadmin.mapper.shp.ShpShopNumberMapper;
import com.luxuryadmin.service.shp.ShpNumberRecordService;
import com.luxuryadmin.service.shp.ShpShopNumberService;
import com.luxuryadmin.service.shp.ShpShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author monkey king
 * @date 2019-12-19 21:00:28
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpShopNumberServiceImpl implements ShpShopNumberService {

    @Resource
    private ShpShopNumberMapper shpShopNumberMapper;

    @Autowired
    private ShpNumberRecordService shpNumberRecordService;

    private static final String SHP_SHOP_NUMBER_ID_KEY = ConstantRedisKey.SHP_SHOP_NUMBER_ID;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public int saveBatchNewShopNumber(List<ShpShopNumber> list) {
        return LocalUtils.isEmptyAndNull(list) ? 0 : shpShopNumberMapper.saveBatch(list);
    }

    @Override
    public synchronized int generateShopRandomNumber(int startNumber, int endNumber) {
        int numberCount = 0;
        Integer lastNumber = getLastShopNumber();
        if (!LocalUtils.isEmptyAndNull(lastNumber)) {
            if (startNumber <= lastNumber) {
                throw new NumberFormatException("startNumber???????????????endNumber????????????; ??????endNumber???:" + lastNumber);
            }
        }
        try {
            long st = System.currentTimeMillis();
            //??????????????????
            List<Integer> integerList = RandomNumber.generatorNumber(startNumber, endNumber);
            //????????????
            Map<String, List<Integer>> listMap = RandomNumber.filterNiceNumber(integerList);
            String sameKey = "same";
            String moreWithOneKey = "moreWithOne";
            String moreWithMoreKey = "moreWithMore";
            String ascOrDescKey = "ascOrDesc";
            String normalKey = "normal";
            List<Integer> same = listMap.get(sameKey);
            List<Integer> moreWithOne = listMap.get(moreWithOneKey);
            List<Integer> moreWithMore = listMap.get(moreWithMoreKey);
            List<Integer> ascOrDesc = listMap.get(ascOrDescKey);
            List<Integer> normal = listMap.get(normalKey);
            //?????????
            EnumNumberType normalCode = EnumNumberType.NORMAL;
            EnumNumberType sameCode = EnumNumberType.SAME;
            EnumNumberType ascOrDescCode = EnumNumberType.ASC_OR_DESC;
            EnumNumberType moreWithOneCode = EnumNumberType.MORE_WITH_ONE;
            EnumNumberType moreWithMoreCode = EnumNumberType.MORE_WITH_MORE;
            //????????????????????????
            List<ShpShopNumber> normalList = generateShpShopNumber(normal, normalCode);
            List<ShpShopNumber> sameList = generateShpShopNumber(same, sameCode);
            List<ShpShopNumber> ascOrDescList = generateShpShopNumber(ascOrDesc, ascOrDescCode);
            List<ShpShopNumber> moreWithOneList = generateShpShopNumber(moreWithOne, moreWithOneCode);
            List<ShpShopNumber> moreWithMoreList = generateShpShopNumber(moreWithMore, moreWithMoreCode);
            //????????????
            saveBatchNewShopNumber(normalList);
            saveBatchNewShopNumber(sameList);
            saveBatchNewShopNumber(ascOrDescList);
            saveBatchNewShopNumber(moreWithOneList);
            saveBatchNewShopNumber(moreWithMoreList);
            long et = System.currentTimeMillis();
            numberCount = integerList.size();
            //??????????????????;
            EnumNumberRecordType typeEnum = EnumNumberRecordType.SHOP;
            ShpNumberRecord shpNumberRecord = new ShpNumberRecord();
            shpNumberRecord.setType(typeEnum.getCode());
            shpNumberRecord.setInsertTime(new Date());
            shpNumberRecord.setStartNumber(startNumber);
            shpNumberRecord.setEndNumber(endNumber);
            shpNumberRecord.setTotalNum(numberCount);
            shpNumberRecord.setDel("0");
            String sb = "?????????" + numberCount + "?????????;??????" + (et - st) + "??????.";
            shpNumberRecord.setRemark(sb);
            shpNumberRecordService.SaveShpNumberRecord(shpNumberRecord);
            log.info(sb);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            //??????try catch???????????????; ???????????????????????????????????????;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return numberCount;
    }

    @Override
    public Integer getLastShopNumber() {
        return shpShopNumberMapper.getLastShopNumber();
    }

    @Override
    public synchronized ShpShopNumber getShpShopNumberOverId() {
        String lockId = "shpShopNumber";
        //String lockValue = redisUtil.get(lockId);
        //?????????????????????,?????????;
        int i = 0;
        while (!LocalUtils.isEmptyAndNull(redisUtil.get(lockId))) {
            try {
                i++;
                log.info("============????????????????????????shpShopNumber: " + i);
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //10???????????????;??????;
        redisUtil.setExSECONDS(lockId, lockId, 10);
        //???????????????,????????????;
        String shpShopNumberIdValue = redisUtil.get(SHP_SHOP_NUMBER_ID_KEY);
        int shpShopNumberId = LocalUtils.isEmptyAndNull(shpShopNumberIdValue) ? 0 : Integer.parseInt(shpShopNumberIdValue);
        ShpShopNumber shpShopNumber = shpShopNumberMapper.getShpShopNumberOverId(shpShopNumberId);
        //???????????????????????????????????????;
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        redisUtil.set(SHP_SHOP_NUMBER_ID_KEY, shpShopNumber.getId() + "");
        //?????????;
        redisUtil.delete(lockId);
        return shpShopNumber;
    }

    @Override
    public int updateShpShopNumber(ShpShopNumber shpShopNumber) {
        return shpShopNumberMapper.updateObject(shpShopNumber);
    }

    @Override
    public int usedShpShopNumber(int shopId, ShpShopNumber shpShopNumber) {
        shpShopNumber.setState("1");
        shpShopNumber.setShopId(shopId);
        shpShopNumber.setUpdateTime(new Date());
        return shpShopNumberMapper.updateObject(shpShopNumber);
    }

    @Override
    public void useShopNumber(int shopId, String newShopNumber) {
        ShpShopNumber shpShopNumber = existsShpShopNumber(newShopNumber);
        if (LocalUtils.isEmptyAndNull(shpShopNumber)) {
            //??????????????????,??????????????????,??????????????????????????????,
            ShpShopNumber ssn = new ShpShopNumber();
            ssn.setNumber(newShopNumber);
            ssn.setShopId(shopId);
            ssn.setState("1");
            String nice = RandomNumber.filterNiceNumber(newShopNumber);
            ssn.setNice(nice);
            ssn.setInsertTime(new Date());
            ssn.setUpdateTime(ssn.getInsertTime());
            ssn.setVersions(1);
            ssn.setDel("0");
            ssn.setRemark("????????????????????????;");
            shpShopNumberMapper.saveObject(ssn);
        } else {
            String state = shpShopNumber.getState();
            if ("2".equals(state)) {
                throw new MyException("???????????????????????????!");
            }
            if ("1".equals(state)) {
                throw new MyException(newShopNumber + " ????????????????????????!");
            }
            //????????????????????????????????????
            if ("0".equals(state)) {
                shpShopNumber.setState("1");
                shpShopNumber.setShopId(shopId);
                shpShopNumber.setUpdateTime(new Date());
                shpShopNumber.setRemark("?????????????????????;");
                shpShopNumberMapper.updateObject(shpShopNumber);
            }
        }
        //?????????????????????;
        Map<String, Object> map = shpShopService.getShopNumberAndShopNameByShopId(shopId);
        //???????????????;
        ShpShop shp = new ShpShop();
        shp.setId(shopId);
        shp.setUpdateTime(new Date());
        shp.setNumber(newShopNumber);
        shpShopService.updateShpShop(shp);
        //????????????
        String shopNumberKey = ConstantRedisKey.getShopNumberRedisKeyByShopId(shopId);
        redisUtil.set(shopNumberKey, newShopNumber);
        //????????????????????????
        Object oldShopNumber = map.get("shopNumber");
        ShpShopNumber oldShpShopNumber = existsShpShopNumber(oldShopNumber.toString());
        if (!LocalUtils.isEmptyAndNull(oldShpShopNumber)) {
            oldShpShopNumber.setState("2");
            oldShpShopNumber.setUpdateTime(new Date());
            String newRemark = "???????????????" + newShopNumber + "???????????????;";
            oldShpShopNumber.setRemark(oldShpShopNumber.getRemark() + newRemark);
            shpShopNumberMapper.updateObject(oldShpShopNumber);
        }
    }

    @Override
    public ShpShopNumber existsShpShopNumber(String shopNumber) {
        return shpShopNumberMapper.existsShpShopNumber(shopNumber);
    }

    /**
     * ???????????????????????????;
     *
     * @param listNumber
     * @param niceEnum   {@link EnumNumberType}
     * @return
     */
    private List<ShpShopNumber> generateShpShopNumber(List<Integer> listNumber, EnumNumberType niceEnum) {
        Date insertTime = new Date();
        List<ShpShopNumber> shpShopNumberList = new ArrayList<>();
        for (Integer number : listNumber) {
            ShpShopNumber shpShopNumber = new ShpShopNumber();
            shpShopNumber.setState("0");
            shpShopNumber.setNice(niceEnum.getCode());
            shpShopNumber.setInsertTime(insertTime);
            shpShopNumber.setNumber(number + "");
            shpShopNumber.setVersions(1);
            shpShopNumber.setDel("0");
            shpShopNumberList.add(shpShopNumber);
        }
        return shpShopNumberList;
    }
}
