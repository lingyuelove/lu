package com.luxuryadmin.service.shp.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.RedisUtil;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.mapper.shp.ShpUserShopRefMapper;
import com.luxuryadmin.param.shp.ParamEmployee;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.shp.VoEmployee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2019-12-25 14:48:36
 */
@Slf4j
@Service
public class ShpUserShopRefServiceImpl implements ShpUserShopRefService {

    @Resource
    private ShpUserShopRefMapper shpUserShopRefMapper;

    @Autowired
    private ServicesUtil servicesUtil;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public int saveShpUserShopRef(int userId, int shopId, String name, int userTypeId, int updateUserId) {
        ShpUserShopRef shpUserShopRef = new ShpUserShopRef();
        shpUserShopRef.setFkShpShopId(shopId);
        shpUserShopRef.setFkShpUserId(userId);
        shpUserShopRef.setName(name);
        shpUserShopRef.setFkShpUserTypeId(userTypeId);
        shpUserShopRef.setState("1");
        shpUserShopRef.setInsertTime(new Date());
        shpUserShopRef.setInsertAdmin(updateUserId);
        return shpUserShopRefMapper.saveObject(shpUserShopRef);
    }

    @Override
    public boolean existsShpUserShopRef(int shopId, Integer userId) {
        return shpUserShopRefMapper.existsShpUserShopRef(shopId, userId) > 0;
    }

    @Override
    public List<VoEmployee> getSalesmanByShopId(int shopId) {
        return shpUserShopRefMapper.getSalesmanByShopId(shopId);
    }

    @Override
    public List<VoEmployee> listUserShopRefByShopId(ParamEmployee paramEmployee) {
        return shpUserShopRefMapper.listUserShopRefByShopId(paramEmployee);
    }

    @Override
    public int modifyEmployeeState(int shopId, int userId, int refId, String state) {
        ShpUserShopRef userShopRef = new ShpUserShopRef();
        userShopRef.setId(refId);
        userShopRef.setState(state);
        userShopRef.setFkShpUserId(userId);
        userShopRef.setFkShpShopId(shopId);
        userShopRef.setUpdateTime(new Date());
        return shpUserShopRefMapper.fireEmployee(userShopRef);
    }

    @Override
    public int updateUserShopRef(ShpUserShopRef shpUserShopRef) {
        return shpUserShopRefMapper.updateObject(shpUserShopRef);
    }

    @Override
    public String getUserTypeByShopIdAndUserId(int shopId, int userId) {
        return shpUserShopRefMapper.getUserTypeByShopIdAndUserId(shopId, userId);
    }

    @Override
    public List<VoEmployee> listUploadUser(int shopId) {
        return shpUserShopRefMapper.listUploadUser(shopId);
    }

    @Override
    public ShpUserShopRef getShpUserShopRefByUserId(int shopId, int userId) {
        return shpUserShopRefMapper.getShpUserShopRefByUserId(shopId, userId);
    }

    @Override
    public ShpUserShopRef getShpUserShopRefOnJobByUserId(int shopId, int userId) {
        return shpUserShopRefMapper.getShpUserShopRefOnJobByUserId(shopId, userId);
    }

    @Override
    public Map<String, String> countEmployee(int shopId) {
        return shpUserShopRefMapper.countEmployee(shopId);
    }

    @Override
    public void updateUserShopRefState(ShpUserShopRef shpUserShopRef) {
        shpUserShopRefMapper.updateUserShopRefState(shpUserShopRef);
    }

    @Override
    public String getNameFromShop(int shopId, Integer userId) {
        return shpUserShopRefMapper.getNameFromShop(shopId, userId);
    }

    @Override
    public List<Integer> listUserIdByUserTypeId(int shopId, int userTypeId) {
        return shpUserShopRefMapper.listUserIdByUserTypeId(shopId, userTypeId);
    }

    @Override
    public void isEmployeeLimit(int shopId) {
        int employeeLimit = servicesUtil.getEmployeeLimit(shopId);
        Map<String, String> countEmployee = countEmployee(shopId);
        Object employeeNum = countEmployee.get("nomal");
        if (employeeLimit <= Integer.parseInt(employeeNum.toString())) {
            throw new MyException("员工上限: " + employeeLimit + "个!如需增加,请联系客服!");
        }
    }

    @Override
    public void updateBossUserShopRef(int shopId, int userId) {
        //临时处理: 2020-09-11 23:58:49 兼容旧数据;
        ShpUserShopRef userShopRef = getShpUserShopRefByUserId(shopId, userId);
        if (!LocalUtils.isEmptyAndNull(userShopRef) && userShopRef.getFkShpUserTypeId() != -9) {
            Integer dbBoosUserId = shpShopService.getBossUserIdByShopId(shopId);
            if (dbBoosUserId == userId) {
                //当前登录的是经营者
                userShopRef.setFkShpUserTypeId(-9);
                updateUserShopRef(userShopRef);
            }
        }
    }

    @Override
    public void deleteUserShopRefByShopId(int shopId) {
        shpUserShopRefMapper.deleteUserShopRefByShopId(shopId);
    }

    @Override
    public List<Integer> listAllUserIdByShopId(int shopId, String state) {
        return shpUserShopRefMapper.listAllUserIdByShopId(shopId, state);
    }

    @Override
    public String getUserTypeRoleNameByShopIdAndUserId(int shopId, Integer userId) {
        return shpUserShopRefMapper.getUserTypeRoleNameByShopIdAndUserId(shopId,userId);
    }

    @Override
    public List<String> listAllUserNameByShopId(int shopId, String state) {
        return shpUserShopRefMapper.listAllUserNameByShopId(shopId,state);
    }

    @Override
    public void updateObject(ShpUserShopRef shpUserShopRef) {
        shpUserShopRefMapper.updateObject(shpUserShopRef);
    }

    @Override
    public List<ShpUserShopRef> listAllUserShopRefExceptBoss() {
        return shpUserShopRefMapper.listAllUserShopRefExceptBoss();
    }

}
