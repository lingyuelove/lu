package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RandomNumber;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.entity.shp.ShpNumberRecord;
import com.luxuryadmin.entity.shp.ShpUserNumber;
import com.luxuryadmin.enums.shp.EnumNumberRecordType;
import com.luxuryadmin.enums.shp.EnumNumberType;
import com.luxuryadmin.mapper.shp.ShpUserNumberMapper;
import com.luxuryadmin.service.shp.ShpNumberRecordService;
import com.luxuryadmin.service.shp.ShpUserNumberService;
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
 * @date 2019-12-19 16:40:58
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpUserNumberServiceImpl implements ShpUserNumberService {

    @Resource
    private ShpUserNumberMapper shpUserNumberMapper;

    @Autowired
    private ShpNumberRecordService shpNumberRecordService;

    @Autowired
    private RedisUtil redisUtil;

    private static final String USER_NUMBER_ID_KEY = ConstantRedisKey.SHP_USER_NUMBER_ID;

    @Override
    public int saveBatchNewUserNumber(List<ShpUserNumber> list) {
        return LocalUtils.isEmptyAndNull(list) ? 0 : shpUserNumberMapper.saveBatch(list);
    }

    @Override
    public synchronized int generateUserRandomNumber(int startNumber, int endNumber) {
        int numberCount = 0;
        Integer lastNumber = getLastUserNumber();
        if (!LocalUtils.isEmptyAndNull(lastNumber)) {
            if (startNumber <= lastNumber) {
                throw new NumberFormatException("startNumber需要比上次endNumber的数值大; 上次endNumber为:" + lastNumber);
            }
        }
        try {
            long st = System.currentTimeMillis();
            //生成随机编号
            List<Integer> integerList = RandomNumber.generatorNumber(startNumber, endNumber);
            //过滤靓号
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
            //枚举类
            EnumNumberType normalCode = EnumNumberType.NORMAL;
            EnumNumberType sameCode = EnumNumberType.SAME;
            EnumNumberType ascOrDescCode = EnumNumberType.ASC_OR_DESC;
            EnumNumberType moreWithOneCode = EnumNumberType.MORE_WITH_ONE;
            EnumNumberType moreWithMoreCode = EnumNumberType.MORE_WITH_MORE;
            //生成待入库的数据
            List<ShpUserNumber> normalList = generateShpUserNumber(normal, normalCode);
            List<ShpUserNumber> sameList = generateShpUserNumber(same, sameCode);
            List<ShpUserNumber> ascOrDescList = generateShpUserNumber(ascOrDesc, ascOrDescCode);
            List<ShpUserNumber> moreWithOneList = generateShpUserNumber(moreWithOne, moreWithOneCode);
            List<ShpUserNumber> moreWithMoreList = generateShpUserNumber(moreWithMore, moreWithMoreCode);
            //编号入库
            saveBatchNewUserNumber(normalList);
            saveBatchNewUserNumber(sameList);
            saveBatchNewUserNumber(ascOrDescList);
            saveBatchNewUserNumber(moreWithOneList);
            saveBatchNewUserNumber(moreWithMoreList);
            long et = System.currentTimeMillis();
            numberCount = integerList.size();
            //添加操作记录;
            EnumNumberRecordType typeEnum = EnumNumberRecordType.SHOP_USER;
            ShpNumberRecord shpNumberRecord = new ShpNumberRecord();
            shpNumberRecord.setType(typeEnum.getCode());
            shpNumberRecord.setInsertTime(new Date());
            shpNumberRecord.setStartNumber(startNumber);
            shpNumberRecord.setEndNumber(endNumber);
            shpNumberRecord.setTotalNum(numberCount);
            shpNumberRecord.setDel("0");
            String sb = "共生成" + numberCount + "个号码;耗时" + (et - st) + "毫秒.";
            shpNumberRecord.setRemark(sb);
            shpNumberRecordService.SaveShpNumberRecord(shpNumberRecord);
            log.info(sb);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            //对于try catch的方法里面; 事务要回滚必须显性操作回滚;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return numberCount;
    }

    @Override
    public Integer getLastUserNumber() {
        return shpUserNumberMapper.getLastNumber();
    }

    @Override
    public synchronized ShpUserNumber getShpUserNumberOverId() {
        String lockId = "shpUserNumber";
        //String lockValue = redisUtil.get(lockId);
        //如果正在执行中,则等待;
        int i = 0;
        while (!LocalUtils.isEmptyAndNull(redisUtil.get(lockId))) {
            try {
                i++;
                log.info("============等待上一个用户取shpUserNumber: " + i);
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //10秒过期时间;加锁;
        redisUtil.setExSECONDS(lockId, lockId, 10);
        //没有被锁住,方可执行;
        String userNumberIdValue = redisUtil.get(USER_NUMBER_ID_KEY);
        int userNumberId = LocalUtils.isEmptyAndNull(userNumberIdValue) ? 0 : Integer.parseInt(userNumberIdValue);
        ShpUserNumber shpUserNumber = shpUserNumberMapper.getShpUserNumberOverId(userNumberId);
        //此睡眠时间是测试分布式并发;
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        redisUtil.set(USER_NUMBER_ID_KEY, shpUserNumber.getId() + "");
        //释放锁;
        redisUtil.delete(lockId);
        return shpUserNumber;
    }

    @Override
    public int updateShpUserNumber(ShpUserNumber shpUserNumber) {
        return shpUserNumberMapper.updateObject(shpUserNumber);
    }

    @Override
    public int usedShpUserNumber(int userId, ShpUserNumber shpUserNumber) {
        shpUserNumber.setState("1");
        shpUserNumber.setUserId(userId);
        shpUserNumber.setUpdateTime(new Date());
        return shpUserNumberMapper.updateObject(shpUserNumber);
    }

    @Override
    public int initShpUserNumber() {
        return 0;
    }

    /**
     * 生成待入库的实体类;
     *
     * @param listNumber
     * @param niceEnum   {@link EnumNumberType}
     * @return
     */
    private List<ShpUserNumber> generateShpUserNumber(List<Integer> listNumber, EnumNumberType niceEnum) {
        Date insertTime = new Date();
        List<ShpUserNumber> shpUserNumberList = new ArrayList<>();
        for (Integer number : listNumber) {
            ShpUserNumber shpUserNumber = new ShpUserNumber();
            shpUserNumber.setState("0");
            shpUserNumber.setNice(niceEnum.getCode());
            shpUserNumber.setInsertTime(insertTime);
            shpUserNumber.setNumber(number);
            shpUserNumber.setVersions(1);
            shpUserNumber.setDel("0");
            shpUserNumberList.add(shpUserNumber);
        }
        return shpUserNumberList;
    }
}
