package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.entity.shp.ShpUserInvite;
import com.luxuryadmin.mapper.pro.ProShareSeeUserAgencyMapper;
import com.luxuryadmin.mapper.pro.ProShareSeeUserMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.mapper.shp.ShpUserInviteMapper;
import com.luxuryadmin.service.op.OpEmployeeService;
import com.luxuryadmin.service.shp.ShpUserInviteService;
import com.luxuryadmin.service.sys.SysJobWxService;
import com.luxuryadmin.service.sys.SysUserService;
import com.luxuryadmin.service.util.mongo.BaseSearch;
import com.luxuryadmin.service.util.mongo.TenantBaseStatistics;
import com.luxuryadmin.vo.mongo.UserBindShopCensus;
import com.luxuryadmin.vo.mongo.UserShareAppletCensus;
import com.luxuryadmin.vo.op.VoOpEmployeeList;
import com.luxuryadmin.vo.sys.VoSysJobWx;
import com.luxuryadmin.vo.usr.VoInviteDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author monkey king
 * @date 2019-12-11 16:40:36
 */
@Slf4j
@Service
public class ShpUserInviteServiceImpl implements ShpUserInviteService {


    @Resource
    private ShpUserInviteMapper shpUserInviteMapper;

    @Autowired
    private ShpUserInviteService shpUserInviteService;
    @Autowired
    private SysJobWxService sysJobWxService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private OpEmployeeService opEmployeeService;
    @Resource
    private ShpShopMapper shpShopMapper;
    @Resource
    private ProShareSeeUserAgencyMapper proShareSeeUserAgencyMapper;
    @Resource
    private ProShareSeeUserMapper proShareSeeUserMapper;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void saveShpUserInvite(ShpUserInvite shpUserInvite) {
        shpUserInviteMapper.saveObject(shpUserInvite);
    }

    @Override
    public ShpUserInvite createShpUserInvite(int inviteUserId, int beInviteUserId) {
        ShpUserInvite shpUserInvite = new ShpUserInvite();
        shpUserInvite.setFkInviteUserId(inviteUserId);
        shpUserInvite.setFkBeInviteUserId(beInviteUserId);
        shpUserInvite.setState("1");
        shpUserInvite.setRewardState("10");
        shpUserInvite.setInsertTime(new Date());
        shpUserInvite.setVersions(1);
        shpUserInvite.setDel("0");
        return shpUserInvite;
    }

    @Override
    public Integer getInvitePersonCountByUserId(int userId) {
        return shpUserInviteMapper.selectInvitePersonCountByUserId(userId);
    }

    @Override
    public List<VoInviteDetail> getInviteDetailListByUserId(int userId) {
        return shpUserInviteMapper.selectInviteDetailListByUserId(userId);
    }

    @Override
    public Integer getParentInviteCode(int userId) {
        return shpUserInviteMapper.getParentInviteCode(userId);
    }

    @Override
    public void inviteUser(final int inviteUserId, final int beInviteUserId) {
        if (inviteUserId == beInviteUserId) {
            throw new MyException("自己不能邀请自己~");
        }
        //添加好友邀请关系;启动线程;不阻塞主线程注册成功;
        ScheduledExecutorService executorService = ThreadUtils.getInstance().executorService;
        executorService.execute(() -> {
            log.info("========【添加邀请关系】==========邀请人:" + inviteUserId + " | 被邀请人:" + beInviteUserId);
            ShpUserInvite shpUserInvite = shpUserInviteService.createShpUserInvite(inviteUserId, beInviteUserId);
            shpUserInviteService.saveShpUserInvite(shpUserInvite);
        });
    }

    @Override
    public List<Integer> listBeInviteUserIdByUserId(Integer userId) {
        return shpUserInviteMapper.listBeInviteUserIdByUserId(userId);
    }

    @Override
    public void addOrUpdateCensus() {
        //更新运营绑定店铺数据统计
        List<VoSysJobWx> voSysJobWxList = sysJobWxService.listVoSysJobWx();
        if (!LocalUtils.isEmptyAndNull(voSysJobWxList)){
            voSysJobWxList.forEach(voSysJobWx -> {
                UserShareAppletCensus userShareAppletCensus =getUserShareAppletCensusForJobWx(voSysJobWx);
                saveOrUpdateShareAppletCensus(userShareAppletCensus);

            });
        }
        //更新小程序分享统计
        List<VoOpEmployeeList> voOpBizComments = opEmployeeService.listEmployee();
        if (LocalUtils.isEmptyAndNull(voSysJobWxList)){
            return;
        }
        voOpBizComments.forEach(voOpEmployeeList -> {
            UserBindShopCensus userBindShopCensus = getUserBindShopCensusForEmployee(voOpEmployeeList);
            saveOrUpdateUserBindShopCensus(userBindShopCensus);
        });
    }

    public void saveOrUpdateShareAppletCensus(UserShareAppletCensus userShareAppletCensus){
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        String dateFieldName="updateTime";
        tenantBaseStatistics.setStartTime(DateUtil.format(DateUtil.getTodayTime()));
        tenantBaseStatistics.setEndTime(DateUtil.format(new Date()));
        tenantBaseStatistics.setUserId(userShareAppletCensus.getJobWxId());
        tenantBaseStatistics.setUserIdName("jobWxId");
        Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics,dateFieldName);
        Query query = new Query();
        query.addCriteria(criteria);
        List<UserShareAppletCensus> mongoDOS = mongoTemplate.find(query, UserShareAppletCensus.class);

        if (!LocalUtils.isEmptyAndNull(mongoDOS)){
            Update update =new Update();
            update.set("userId",userShareAppletCensus.getUserId());
            update.set("registerShopCount",userShareAppletCensus.getRegisterShopCount());
            update.set("buyMemShopCount",userShareAppletCensus.getBuyMemShopCount());
            update.set("buyMemForMonthCount",userShareAppletCensus.getBuyMemForMonthCount());
            update.set("buyMemForHundredCount",userShareAppletCensus.getBuyMemForHundredCount());
            update.set("buyMemForOldCount",userShareAppletCensus.getBuyMemForOldCount());
            update.set("memShopCount",userShareAppletCensus.getMemShopCount());
            update.set("comMemShopCount",userShareAppletCensus.getComMemShopCount());
            update.set("useShopCount",userShareAppletCensus.getUseShopCount());
            update.set("unUseShopCount",userShareAppletCensus.getUnUseShopCount());
            update.set("totalShopCount",userShareAppletCensus.getTotalShopCount());
            update.set("unExpiredCount",userShareAppletCensus.getUnExpiredCount());
            update.set("expiredCount",userShareAppletCensus.getExpiredCount());
            update.set("updateTime",DateUtil.format(new Date()));
            mongoTemplate.updateFirst(query,update,UserShareAppletCensus.class);
        }else {
            mongoTemplate.save(userShareAppletCensus);
        }
    }
    public void saveOrUpdateUserBindShopCensus(UserBindShopCensus userBindShopCensus){
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        String dateFieldName="updateTime";
        tenantBaseStatistics.setStartTime(DateUtil.format(DateUtil.getTodayTime()));
        tenantBaseStatistics.setEndTime(DateUtil.format(new Date()));
        tenantBaseStatistics.setUserId(userBindShopCensus.getOpEmployeeId());
        tenantBaseStatistics.setUserIdName("opEmployeeId");
        Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics,dateFieldName);
        Query query = new Query();
        query.addCriteria(criteria);
        List<UserBindShopCensus> mongoDOS = mongoTemplate.find(query, UserBindShopCensus.class);

        if (!LocalUtils.isEmptyAndNull(mongoDOS)){
            Update update =new Update();
            update.set("registerUserCount",userBindShopCensus.getRegisterUserCount());
            update.set("shopCount",userBindShopCensus.getShopCount());
            update.set("visitShopAppletCount",userBindShopCensus.getVisitShopAppletCount());
            update.set("newVisitAppletCount",userBindShopCensus.getNewVisitAppletCount());
            update.set("updateTime",DateUtil.format(new Date()));
            mongoTemplate.updateFirst(query,update,UserBindShopCensus.class);
        }else {
            mongoTemplate.save(userBindShopCensus);
        }
    }
    public UserShareAppletCensus getUserShareAppletCensusForJobWx(VoSysJobWx sysJobWx){
        UserShareAppletCensus  userShareAppletCensus =new UserShareAppletCensus();
        Integer sysJobWxId = Integer.parseInt(sysJobWx.getSysJobWxId());
        if (!LocalUtils.isEmptyAndNull(sysJobWx.getSysUserId())){
            userShareAppletCensus.setUserId(Integer.parseInt(sysJobWx.getSysUserId()));
        }else {
            userShareAppletCensus.setUserId(0);
        }
        if (!LocalUtils.isEmptyAndNull(sysJobWx.getWxNickname())){
            userShareAppletCensus.setWxNickname(sysJobWx.getWxNickname());
        }else {
            userShareAppletCensus.setWxNickname("");
        }
        userShareAppletCensus.setJobWxId(sysJobWxId);

        userShareAppletCensus.setInsertTime(DateUtil.format(new Date()));
        userShareAppletCensus.setUpdateTime(DateUtil.format(new Date()));
        userShareAppletCensus.setDel("0");
        //获取当天注册店铺的数量
        Integer registerShopCount =shpShopMapper.getRegisterShopCount(sysJobWxId);
        if (registerShopCount == null){
            registerShopCount = 0;
        }
        userShareAppletCensus.setRegisterShopCount(registerShopCount);
        //当日购买会员店铺数
        Integer buyMemShopCount =shpShopMapper.getMemShopCount(sysJobWxId,1);
        if (buyMemShopCount == null){
            buyMemShopCount = 0;
        }
        userShareAppletCensus.setBuyMemShopCount(buyMemShopCount);
        //当天月内转化会员量
        Integer buyMemForMonthCount =shpShopMapper.getMemShopCount(sysJobWxId,2);
        if (buyMemForMonthCount == null){
            buyMemForMonthCount = 0;
        }
        userShareAppletCensus.setBuyMemForMonthCount(buyMemForMonthCount);
        //当天百日内转化会员量
        Integer buyMemForHundredCount =shpShopMapper.getMemShopCount(sysJobWxId,3);
        if (buyMemForHundredCount == null){
            buyMemForHundredCount = 0;
        }
        userShareAppletCensus.setBuyMemForHundredCount(buyMemForHundredCount);
        //当天召回会员
        Integer buyMemForOldCount =shpShopMapper.getMemShopCount(sysJobWxId,4);
        if (buyMemForOldCount == null){
            buyMemForOldCount = 0;
        }
        userShareAppletCensus.setBuyMemForOldCount(buyMemForOldCount);
        //在使用所有店铺的数量
        Integer memShopCount =shpShopMapper.getUseShopCount(sysJobWxId,1);
        if (memShopCount == null){
            memShopCount = 0;
        }
        userShareAppletCensus.setMemShopCount(memShopCount);
        //在使用非会员店铺数
        Integer comMemShopCount =shpShopMapper.getUseShopCount(sysJobWxId,2);
        if (comMemShopCount == null){
            comMemShopCount = 0;
        }
        userShareAppletCensus.setComMemShopCount(comMemShopCount);
        //十日内使用店铺数量 会员加非会员
        Integer useShopCount =shpShopMapper.getUseShopCount(sysJobWxId,3);
        if (useShopCount == null){
            useShopCount = 0;
        }
        userShareAppletCensus.setUseShopCount(useShopCount);
        //十日内不使用店铺数量 会员加非会员
        Integer unUseShopCount =shpShopMapper.getUseShopCount(sysJobWxId,4);
        if (unUseShopCount == null){
            unUseShopCount = 0;
        }
        userShareAppletCensus.setUnUseShopCount(unUseShopCount);
        //客户总量 会员加非会员
        Integer totalShopCount =shpShopMapper.getUseShopCount(sysJobWxId,null);
        if (totalShopCount == null){
            totalShopCount = 0;
        }
        userShareAppletCensus.setTotalShopCount(totalShopCount);
        //未逾期店铺 非会员
        Integer unExpiredCount =shpShopMapper.getUseShopCount(sysJobWxId,5);
        if (unExpiredCount == null){
            unExpiredCount = 0;
        }
        userShareAppletCensus.setUnExpiredCount(unExpiredCount);
        //逾期店铺 非会员
        Integer expiredCount =shpShopMapper.getUseShopCount(sysJobWxId,6);
        if (expiredCount == null){
            expiredCount = 0;
        }
        userShareAppletCensus.setExpiredCount(expiredCount);
        return userShareAppletCensus;
    }

    public UserBindShopCensus getUserBindShopCensusForEmployee(VoOpEmployeeList opEmployeeList){
        UserBindShopCensus userBindShopCensus =new UserBindShopCensus();
        Integer userId =opEmployeeList.getUserId();
        userBindShopCensus.setUserId(userId);
        userBindShopCensus.setOpEmployeeId(opEmployeeList.getId());
        userBindShopCensus.setNickname(opEmployeeList.getNickname());
        userBindShopCensus.setPhone(opEmployeeList.getUsername());
        userBindShopCensus.setInviteUserNumber(opEmployeeList.getUserNumber());
        userBindShopCensus.setInsertTime(DateUtil.format(new Date()));
        userBindShopCensus.setUpdateTime(DateUtil.format(new Date()));
        userBindShopCensus.setDel("0");
        //当日邀请注册用户数
        Integer registerUserCount = shpUserInviteMapper.selectRegisterUserCountByUserId(userId);
        if (registerUserCount == null){
            registerUserCount = 0;
        }
        userBindShopCensus.setRegisterUserCount(registerUserCount);
        //当日新开店数
        Integer shopCount = shpUserInviteMapper.selectNewShopCountByUserId(userId);
        if (shopCount == null){
            shopCount = 0;
        }
        userBindShopCensus.setShopCount(shopCount);
        //当日新用户访问人数
        Integer newVisitAppletCount = proShareSeeUserAgencyMapper.getNewVisitAppletCount(userId);
        if (newVisitAppletCount == null){
            newVisitAppletCount = 0;
        }
        userBindShopCensus.setNewVisitAppletCount(newVisitAppletCount);
        //当日小程序访问人数
        Integer visitShopAppletCount = proShareSeeUserMapper.getVisitShopAppletCount(userId);
        if (visitShopAppletCount == null){
            visitShopAppletCount = 0;
        }
        userBindShopCensus.setVisitShopAppletCount(visitShopAppletCount);
        return userBindShopCensus;
    }
}
