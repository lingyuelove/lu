package com.luxuryadmin.service.op.impl;

import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.entity.op.OpEmployee;
import com.luxuryadmin.entity.op.OpUnionAgent;
import com.luxuryadmin.entity.shp.ShpUser;
import com.luxuryadmin.mapper.op.OpEmployeeMapper;
import com.luxuryadmin.mapper.op.OpUnionAgentMapper;
import com.luxuryadmin.mapper.shp.ShpUserMapper;
import com.luxuryadmin.param.op.ParamUnionAgentAdd;
import com.luxuryadmin.param.op.ParamUnionAgentUpdate;
import com.luxuryadmin.param.op.ParamUsernameAndUserIdQuery;
import com.luxuryadmin.service.op.OpUnionAgentService;
import com.luxuryadmin.vo.biz.VoMpUnionAgencyUser;
import com.luxuryadmin.vo.op.VoOpUnionAgentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @author taoqimin
 */
@Service
public class OpUnionAgentServiceImpl implements OpUnionAgentService {


    @Resource
    private OpUnionAgentMapper opUnionAgentMapper;

    @Resource
    private OpEmployeeMapper opEmployeeMapper;

    @Resource
    private ShpUserMapper shpUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加代理帐号
     *
     * @param params
     */
    @Override
    public void addUnionAgent(ParamUnionAgentAdd params) {
        ShpUser shpUser = (ShpUser) shpUserMapper.getObjectById(params.getShpUserId());
        if (shpUser == null) {
            throw new MyException("代理人员不存在");
        }
        int count = opUnionAgentMapper.existsObject(params.getShpUserId(), null);
        if (count > 0) {
            throw new MyException("代理人员已添加，请勿重复添加!");
        }
        OpEmployee opEmployee = (OpEmployee) opEmployeeMapper.getObjectById(params.getEmployeeId());
        if (opEmployee == null) {
            throw new MyException("关联的工作人员不存在!");
        }
        if (opEmployee.getFkShpUserId().equals(params.getShpUserId())) {
            throw new MyException("不需要绑定自己!");
        }
        OpUnionAgent opUnionAgent = new OpUnionAgent();
        opUnionAgent.setFkOpEmployeeId(params.getEmployeeId());
        opUnionAgent.setFkShpUserId(params.getShpUserId());
        opUnionAgent.setValidDay(params.getValidDay());
        opUnionAgent.setAgentSwitch(params.getAgentSwitch());
        opUnionAgent.setInsertTime(new Date());
        opUnionAgent.setUpdateTime(opUnionAgent.getInsertTime());
        opUnionAgent.setInsertAdmin(params.getCurrentUserId());
        opUnionAgentMapper.saveObject(opUnionAgent);
        refreshUnionMpSharePerm();
    }

    /**
     * 修改代理帐号
     *
     * @param params
     */
    @Override
    public void updateUnionAgent(ParamUnionAgentUpdate params) {
        OpUnionAgent opUnionAgent = (OpUnionAgent) opUnionAgentMapper.getObjectById(params.getId());
        if (LocalUtils.isEmptyAndNull(opUnionAgent)) {
            throw new MyException("记录不存在");
        }
        int count = opUnionAgentMapper.existsObject(opUnionAgent.getFkShpUserId(), params.getId());
        if (count > 0) {
            throw new MyException("代理人员已添加，请勿重复操作");
        }
        OpEmployee opEmployee = (OpEmployee) opEmployeeMapper.getObjectById(params.getEmployeeId());
        if (opEmployee == null) {
            throw new MyException("关联的工作人员不存在!");
        }
        if (opEmployee.getFkShpUserId().equals(opUnionAgent.getFkShpUserId())) {
            throw new MyException("不需要绑定自己!");
        }
        opUnionAgent.setValidDay(params.getValidDay());
        opUnionAgent.setFkOpEmployeeId(params.getEmployeeId());
        opUnionAgent.setUpdateAdmin(params.getCurrentUserId());
        opUnionAgent.setAgentSwitch(params.getAgentSwitch());
        opUnionAgent.setUpdateTime(new Date());
        opUnionAgentMapper.updateObject(opUnionAgent);
        refreshUnionMpSharePerm();
    }

    /**
     * 删除代理帐号
     *
     * @param id
     */
    @Override
    public void deleteUnionAgent(Integer id) {
        opUnionAgentMapper.deleteUnionAgent(id);
        refreshUnionMpSharePerm();
    }

    /**
     * 获取代理人员列表
     *
     * @param params
     * @return
     */
    @Override
    public List<VoOpUnionAgentList> listUnionAgent(ParamUsernameAndUserIdQuery params) {
        String username = "";
        if (StringUtil.isNotBlank(params.getUsername())) {
            username = DESEncrypt.encodeUsername(params.getUsername());
        }
        List<VoOpUnionAgentList> voOpUnionAgentLists = opUnionAgentMapper.listUnionAgent(username, params.getEmployeeId());
        voOpUnionAgentLists.stream().forEach(va -> {
            va.setUsername(DESEncrypt.decodeUsername(va.getUsername()));
        });
        return voOpUnionAgentLists;
    }

    @Override
    public List<VoMpUnionAgencyUser> listVoMpUnionAgencyUser(int userId) {
        return opUnionAgentMapper.listVoMpUnionAgencyUser(userId);
    }

    @Override
    public Integer getUnionAgencyValidDay(int userId) {
        return opUnionAgentMapper.getUnionAgencyValidDay(userId);
    }

    @Override
    public void refreshUnionMpSharePerm() {
        String employeeUserId = opEmployeeMapper.getAllowedEmployeeShareUserId();
        String agencyUserId = opUnionAgentMapper.getAllowedAgencyShareUserId();
        String allowShareUnionUser = employeeUserId + "," + agencyUserId;
        String sharePermKey = ConstantRedisKey.SHOP_UNION_SHARE_PERM;
        redisUtil.set(sharePermKey, allowShareUnionUser);
    }
}
