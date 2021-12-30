package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpEmployee;

import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.entity.sys.SysJobWx;
import com.luxuryadmin.mapper.op.OpEmployeeMapper;
import com.luxuryadmin.mapper.op.OpUnionAgentMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.param.op.ParamEmployeeAdd;
import com.luxuryadmin.param.op.ParamUnionAgentQuery;
import com.luxuryadmin.param.op.ParamUnionAgentUpdate;
import com.luxuryadmin.param.sys.ParamJobWxCensuses;
import com.luxuryadmin.service.op.OpEmployeeService;
import com.luxuryadmin.service.op.OpUnionAgentService;
import com.luxuryadmin.service.util.mongo.BaseSearch;
import com.luxuryadmin.service.util.mongo.TenantBaseStatistics;
import com.luxuryadmin.vo.mongo.UserBindShopCensus;
import com.luxuryadmin.vo.mongo.UserShareAppletCensus;
import com.luxuryadmin.vo.op.VoOpEmployeeCensus;
import com.luxuryadmin.vo.op.VoOpEmployeeList;
import com.luxuryadmin.vo.shp.VoShpUser;
import com.luxuryadmin.vo.sys.VoSysJobWxCensus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.Document;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author taoqimin
 */
@Service
public class OpEmployeeServiceImpl implements OpEmployeeService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private OpEmployeeMapper opEmployeeMapper;

    @Resource
    private ShpUserMapper shpUserMapper;

    @Autowired
    private OpUnionAgentService opUnionAgentService;
    @Resource
    private OpUnionAgentMapper opUnionAgentMapper;
    /**
     * 添加员工帐号
     *
     * @param params
     * @return
     */
    @Override
    public void addEmployeeAccount(ParamEmployeeAdd params) {
        ShpUser shpUser = (ShpUser) shpUserMapper.getObjectById(params.getShpUserId());
        if (shpUser == null) {
            throw new MyException("工作人员不存在");
        }
        int count = opEmployeeMapper.count(params.getShpUserId());
        if (count > 0) {
            throw new MyException("工作人员已添加，请勿重复添加");
        }
        OpEmployee opEmployee = new OpEmployee();
        opEmployee.setFkShpUserId(params.getShpUserId());
        opEmployee.setType(params.getType());
        opEmployee.setUnionSwitch(params.getUnionSwitch());
        opEmployee.setInsertTime(new Date());
        opEmployee.setUpdateTime(opEmployee.getInsertTime());
        opEmployee.setInsertAdmin(params.getCurrentUserId());
        opEmployee.setDel("0");
        opEmployeeMapper.saveObject(opEmployee);
        opUnionAgentService.refreshUnionMpSharePerm();
    }

    /**
     * 修改商家联盟分享开关
     *
     * @param params
     */
    @Override
    public void opEmployeeService(ParamUnionAgentQuery params) {
        OpEmployee opEmployee = (OpEmployee) opEmployeeMapper.getObjectById(params.getId());
        if (LocalUtils.isEmptyAndNull(opEmployee)) {
            throw new MyException("记录不存在");
        }
        opEmployee.setUpdateAdmin(params.getCurrentUserId());
        opEmployee.setUpdateTime(new Date());
        opEmployee.setUnionSwitch(params.getUnionSwitch());
        opEmployeeMapper.updateObject(opEmployee);
        opUnionAgentService.refreshUnionMpSharePerm();
    }

    /**
     * 刪除商家联盟分享开关
     *
     * @param id
     */
    @Override
    public void deleteUnionSwitch(Integer id) {
        opEmployeeMapper.deleteUnionSwitch(id);
        opUnionAgentService.refreshUnionMpSharePerm();
        deleteUserBindShopCensus(id);
    }

    /**
     * 获取工作人员列表
     *
     * @return
     */
    @Override
    public List<VoOpEmployeeList> listEmployee() {
        List<VoOpEmployeeList> voOpEmployeeLists = opEmployeeMapper.listEmployee();
        for (VoOpEmployeeList ve : voOpEmployeeLists) {
            ve.setUsername(DESEncrypt.decodeUsername(ve.getUsername()));
        }
        return voOpEmployeeLists;
    }

    @Override
    public List<VoOpEmployeeCensus> listEmployeeCensus(ParamJobWxCensuses param) {

        List<VoOpEmployeeCensus> voOpEmployeeCensuses =new ArrayList<>();

        updateTime(param);
        //设置mongodb查询参数
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        BeanUtils.copyProperties(param,tenantBaseStatistics);
        tenantBaseStatistics.setDateFieldName("insertTime");
        tenantBaseStatistics.setUserIdName("opEmployeeId");
        //设置mongo表名
        String collectionName ="user_bind_shop_census";
        //获取统计参数
        String[] fields = {"userId"};
        //获取统计参数
        GroupOperation groupOperation = Aggregation.group()
                .sum("registerUserCount").as("registerUserCount")
                .sum("shopCount").as("shopCount")
                .sum("visitShopAppletCount").as("visitShopAppletCount")
                .sum("newVisitAppletCount").as("newVisitAppletCount");
        Object o = BaseSearch.sumMongo(mongoTemplate,tenantBaseStatistics,collectionName,null,groupOperation, UserBindShopCensus.class);
        List<UserBindShopCensus> userBindShopCensusList = (List<UserBindShopCensus>) o;

        if (LocalUtils.isEmptyAndNull(userBindShopCensusList) || userBindShopCensusList.size()<=0){
            return voOpEmployeeCensuses;
        }
        VoOpEmployeeCensus voOpEmployeeCensus =new VoOpEmployeeCensus();
        UserBindShopCensus userShareAppletCensus = userBindShopCensusList.get(0);
        BeanUtils.copyProperties(userShareAppletCensus,voOpEmployeeCensus);
        voOpEmployeeCensuses.add(voOpEmployeeCensus);
        GroupOperation groupOperationNow = Aggregation.group(fields)
                .sum("userId").as("userId")
                .sum("opEmployeeId").as("opEmployeeId")
                .sum("nickname").as("nickname")
                .sum("phone").as("phone")
                .sum("inviteUserNumber").as("inviteUserNumber")
                .sum("registerUserCount").as("registerUserCount")
                .sum("shopCount").as("shopCount")
                .sum("visitShopAppletCount").as("visitShopAppletCount")
                .sum("newVisitAppletCount").as("newVisitAppletCount");
//        String[] files = {"userId","opEmployeeId","nickname","phone","inviteUserNumber","registerUserCount","shopCount","visitShopAppletCount","newVisitAppletCount"};

        Object oNow = BaseSearch.sumMongo(mongoTemplate,tenantBaseStatistics,collectionName,  null,groupOperationNow, UserBindShopCensus.class);
        List<UserBindShopCensus> bindUserShopCensusList = (List<UserBindShopCensus>) oNow;
        if (LocalUtils.isEmptyAndNull(bindUserShopCensusList) || bindUserShopCensusList.size()<=0){
            return voOpEmployeeCensuses;
        }
        bindUserShopCensusList.forEach(userBindShopCensus -> {
            tenantBaseStatistics.setUserId(userBindShopCensus.getUserId());
            getBindShopCensus(userBindShopCensus,tenantBaseStatistics);
            VoOpEmployeeCensus voSysJobWxCensusNew =new VoOpEmployeeCensus();
            BeanUtils.copyProperties(userBindShopCensus,voSysJobWxCensusNew);
            voOpEmployeeCensuses.add(voSysJobWxCensusNew);
        });
        return voOpEmployeeCensuses;
    }
    public void deleteUserBindShopCensus(Integer opEmployeeId) {
        TenantBaseStatistics tenantBaseStatistics =new TenantBaseStatistics();
        tenantBaseStatistics.setUserId(opEmployeeId);
        tenantBaseStatistics.setUserIdName("opEmployeeId");
        Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics,null);
        Query query = new Query();
        query.addCriteria(criteria);
        Update update =new Update();
        update.set("del","1");
        mongoTemplate.updateMulti(query,update,UserBindShopCensus.class);
    }
    public void getBindShopCensus(UserBindShopCensus userBindShopCensus, TenantBaseStatistics tenantBaseStatistics){
        try {
            tenantBaseStatistics.setUserIdName("userId");
            Date startTime =DateUtil.parse(tenantBaseStatistics.getEndTime());
            tenantBaseStatistics.setStartTime(DateUtil.formatDaySt(startTime));
            tenantBaseStatistics.setEndTime(DateUtil.formatDayEnd(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }


            Criteria criteria = BaseSearch.buildBaseCriteria(tenantBaseStatistics, "insertTime");
            Query query = new Query();
            query.addCriteria(criteria);
            List<UserBindShopCensus> userBindShopCensusList = mongoTemplate.find(query, UserBindShopCensus.class);
//        List<UserShareAppletCensus> shareAppletCensusUserList = (List<UserShareAppletCensus>) oList;
        UserBindShopCensus userBindShopCensusNew = null;
            if (userBindShopCensusList != null && userBindShopCensusList.size() > 0) {
                userBindShopCensusNew = userBindShopCensusList.get(0);
            }
            if (userBindShopCensusNew != null) {
                userBindShopCensus.setNickname(userBindShopCensusNew.getNickname());
                userBindShopCensus.setInviteUserNumber(userBindShopCensusNew.getInviteUserNumber());
                userBindShopCensus.setPhone(userBindShopCensusNew.getPhone());
            }

    }

    public void updateTime(ParamJobWxCensuses param){
        if (LocalUtils.isEmptyAndNull(param.getStartTime())) {
            param.setStartTime(DateUtil.format(DateUtil.getTodayTime()));
        }
        if (LocalUtils.isEmptyAndNull(param.getEndTime())) {
            param.setEndTime(DateUtil.format(new Date()));
        }
    }

}
