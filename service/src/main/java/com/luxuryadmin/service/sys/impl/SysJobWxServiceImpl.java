package com.luxuryadmin.service.sys.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.sys.SysJobWx;
import com.luxuryadmin.mapper.sys.SysJobWxMapper;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.service.sys.SysJobWxService;
import com.luxuryadmin.service.util.mongo.BaseSearch;
import com.luxuryadmin.service.util.mongo.TenantBaseStatistics;
import com.luxuryadmin.vo.mongo.UserBindShopCensus;
import com.luxuryadmin.vo.mongo.UserShareAppletCensus;
import com.luxuryadmin.vo.sys.VoSysJobWx;
import com.luxuryadmin.vo.sys.VoSysJobWxCensus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-08-03 20:44:31
 */
@Slf4j
@Service
public class SysJobWxServiceImpl implements SysJobWxService {

    @Resource
    private SysJobWxMapper sysJobWxMapper;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void saveOrUpdateSysJobWx(SysJobWx sysJobWx) {
        Integer id = sysJobWx.getId();
        if (id == null) {
            sysJobWxMapper.saveObject(sysJobWx);
        } else {
            sysJobWxMapper.updateObject(sysJobWx);
            updateJobWxCensuses(sysJobWx);
        }

    }

    @Override
    public SysJobWx getSysJobWxById(String id) {
        return (SysJobWx) sysJobWxMapper.getObjectById(Integer.parseInt(id));
    }

    @Override
    public List<VoSysJobWx> listVoSysJobWx() {
        return sysJobWxMapper.listVoSysJobWx();
    }

    @Override
    public List<VoSysJobWx> listBindJobWx() {
        return sysJobWxMapper.listBindJobWx();
    }

    @Override
    public List<VoSysJobWxCensus> listJobWxCensuses(ParamJobWxCensuses param) {
        List<VoSysJobWx> sysJobWxList = sysJobWxMapper.listVoSysJobWx();
        List<VoSysJobWxCensus> sysJobWxCensusesList =new ArrayList<>();
        if (LocalUtils.isEmptyAndNull(sysJobWxList)){
            return sysJobWxCensusesList;
        }
        updateTime(param);
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        BeanUtils.copyProperties(param,tenantBaseStatistics);
        tenantBaseStatistics.setUserIdName("jobWxId");
        tenantBaseStatistics.setDateFieldName("insertTime");
        String[] fields = {"userId","jobWxId"};


        GroupOperation groupOperation = Aggregation.group()
                .sum("registerShopCount").as("registerShopCount")
                .sum("buyMemShopCount").as("buyMemShopCount")
                .sum("buyMemForMonthCount").as("buyMemForMonthCount")
                .sum("buyMemForHundredCount").as("buyMemForHundredCount")
                .sum("buyMemForOldCount").as("buyMemForOldCount")
                .sum("memShopCount").as("memShopCount")
                .sum("comMemShopCount").as("comMemShopCount")
                .sum("useShopCount").as("useShopCount")
                .sum("unUseShopCount").as("unUseShopCount")
                .sum("totalShopCount").as("totalShopCount")
                .sum("unExpiredCount").as("unExpiredCount")
                .sum("expiredCount").as("expiredCount");
        Object o = BaseSearch.sumMongo(mongoTemplate,tenantBaseStatistics,"user_share_applet_census",null,groupOperation,UserShareAppletCensus.class);
        List<UserShareAppletCensus> userShareAppletCensusList = (List<UserShareAppletCensus>) o;

        if (LocalUtils.isEmptyAndNull(userShareAppletCensusList) || userShareAppletCensusList.size()<=0){
            return sysJobWxCensusesList;
        }
        VoSysJobWxCensus voSysJobWxCensus =new VoSysJobWxCensus();
        UserShareAppletCensus userShareAppletCensus = userShareAppletCensusList.get(0);
        BeanUtils.copyProperties(userShareAppletCensus,voSysJobWxCensus);
        sysJobWxCensusesList.add(voSysJobWxCensus);
        GroupOperation groupOperationNow = Aggregation.group(fields)
                .sum("userId").as("userId")
                .sum("jobWxId").as("jobWxId")
                .sum("registerShopCount").as("registerShopCount")
                .sum("buyMemShopCount").as("buyMemShopCount")
                .sum("buyMemForMonthCount").as("buyMemForMonthCount")
                .sum("buyMemForHundredCount").as("buyMemForHundredCount")
                .sum("buyMemForOldCount").as("buyMemForOldCount")
                .sum("memShopCount").as("memShopCount")
                .sum("comMemShopCount").as("comMemShopCount")
                .sum("useShopCount").as("useShopCount")
                .sum("unUseShopCount").as("unUseShopCount")
                .sum("totalShopCount").as("totalShopCount")
                .sum("unExpiredCount").as("unExpiredCount")
                .sum("expiredCount").as("expiredCount");

        Field[] fields1=UserShareAppletCensus.class.getDeclaredFields();
        Object oList = BaseSearch.sumMongo(mongoTemplate,tenantBaseStatistics,"user_share_applet_census",null,groupOperationNow,UserShareAppletCensus.class);
        List<UserShareAppletCensus> shareAppletCensusUserList = (List<UserShareAppletCensus>) oList;
        if (LocalUtils.isEmptyAndNull(shareAppletCensusUserList)){
            return sysJobWxCensusesList;
        }
        shareAppletCensusUserList.forEach(userShareAppletCensus1 -> {
            tenantBaseStatistics.setUserId(userShareAppletCensus1.getJobWxId());
            getSysJobWxCensus(userShareAppletCensus1,tenantBaseStatistics);
            VoSysJobWxCensus voSysJobWxCensusNew =new VoSysJobWxCensus();
            BeanUtils.copyProperties(userShareAppletCensus1,voSysJobWxCensusNew);
            sysJobWxCensusesList.add(voSysJobWxCensusNew);
        });
        return sysJobWxCensusesList;
    }

    @Override
    public void deleteJobWxCensuses(Integer userId) {
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        tenantBaseStatistics.setUserId(userId);
        tenantBaseStatistics.setUserIdName("userId");
        Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics,null);
        Query query = new Query();
        query.addCriteria(criteria);
        Update update =new Update();
        update.set("del","1");
        mongoTemplate.updateMulti(query,update,UserShareAppletCensus.class);
    }
    public void updateJobWxCensuses(SysJobWx sysJobWx) {
        SysJobWx  sysJobWx1 =this.getSysJobWxById(sysJobWx.getId().toString());
        Boolean ture = sysJobWx.getNickname().equals(sysJobWx1.getNickname());
        Boolean tureUser = sysJobWx.getFkSysUserId().equals(sysJobWx1.getFkSysUserId());
        if (ture && tureUser){
            return;
        }
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        tenantBaseStatistics.setUserId(sysJobWx1.getId());
        tenantBaseStatistics.setUserIdName("jobWxId");
        Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics,null);
        Query query = new Query();
        query.addCriteria(criteria);
        Update update =new Update();
        update.set("userId",sysJobWx.getFkSysUserId());
        update.set("wxNickname",sysJobWx.getNickname());
        mongoTemplate.updateMulti(query,update,UserShareAppletCensus.class);
    }
    public void updateTime(ParamJobWxCensuses param){
        if (LocalUtils.isEmptyAndNull(param.getStartTime())) {
            param.setStartTime(DateUtil.format(DateUtil.getTodayTime()));
        }
        if (LocalUtils.isEmptyAndNull(param.getEndTime())) {
            param.setEndTime(DateUtil.format(new Date()));
        }
    }

    public void getSysJobWxCensus(UserShareAppletCensus userShareAppletCensus, TenantBaseStatistics tenantBaseStatistics){

        try {
            Date startDate = DateUtil.parseShort(tenantBaseStatistics.getEndTime());
            tenantBaseStatistics.setStartTime(DateUtil.format(startDate));
            tenantBaseStatistics.setUserIdName("jobWxId");
            Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics, "insertTime");
            Query query = new Query();
            query.addCriteria(criteria);
            List<UserShareAppletCensus> userShareAppletCensusList = mongoTemplate.find(query, UserShareAppletCensus.class);
//        List<UserShareAppletCensus> shareAppletCensusUserList = (List<UserShareAppletCensus>) oList;
            UserShareAppletCensus userShareAppletCensusNew = null;
            if (userShareAppletCensusList != null && userShareAppletCensusList.size() > 0) {
                userShareAppletCensusNew = userShareAppletCensusList.get(0);
            }
            if (userShareAppletCensusNew != null) {
                userShareAppletCensus.setMemShopCount(userShareAppletCensusNew.getMemShopCount());
                userShareAppletCensus.setComMemShopCount(userShareAppletCensusNew.getComMemShopCount());
                userShareAppletCensus.setUseShopCount(userShareAppletCensusNew.getUseShopCount());
                userShareAppletCensus.setUnUseShopCount(userShareAppletCensusNew.getUnUseShopCount());
                userShareAppletCensus.setTotalShopCount(userShareAppletCensusNew.getTotalShopCount());
                userShareAppletCensus.setUnExpiredCount(userShareAppletCensusNew.getUnExpiredCount());
                userShareAppletCensus.setExpiredCount(userShareAppletCensusNew.getExpiredCount());
                userShareAppletCensus.setWxNickname(userShareAppletCensusNew.getWxNickname());
            }
        } catch (ParseException e) {
            log.info("查询参数错误" + e);
        }
    }
}
