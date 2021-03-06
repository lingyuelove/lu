package com.luxuryadmin.service.ord.impl;

import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.enums.ord.EnumOrdTypeType;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.ord.OrdTypeMapper;
import com.luxuryadmin.param.ord.ParamOrdTypeUpFopApp;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2019-12-26 22:58:25
 */
@Slf4j
@Service
public class OrdTypeServiceImpl implements OrdTypeService {

    @Resource
    private OrdTypeMapper ordTypeMapper;

    @Resource
    private OrdOrderMapper ordOrderMapper;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Override
    public int initOrdTypeByShopIdAndUserId(int shopId, int userId) {
        return ordTypeMapper.initOrdTypeByShopIdAndUserId(shopId, userId);
    }

    @Override
    public List<OrdType> listOrdTypeByShopId(int shopId) {
        return ordTypeMapper.listOrdTypeByShopId(shopId);
    }

    @Override
    public OrdType addOrdType(Integer shopId, Integer userId, String ordTypeName, HttpServletRequest request) {
        OrdType ordType = new OrdType();
        ordType.setFkShpShopId(shopId);
        ordType.setDescription(ordTypeName);
        ordType.setCode(ordTypeName);
        ordType.setName(ordTypeName);
        ordType.setType(EnumOrdTypeType.USER.getCode());
        ordType.setInsertAdmin(userId);
        ordType.setUpdateAdmin(userId);
        ordType.setInsertTime(new Date());
        ordType.setUpdateTime(new Date());
        ordType.setDel(ConstantCommon.DEL_OFF);
        ordType.setState(1);
        ordType.setSort(0);
        ordType.setVersions(0);
        ordType.setRemark("");
        try {
            ordTypeMapper.saveObject(ordType);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                log.error("********?????????????????????***********");
                throw new MyException("?????????????????????");
            } else {
                throw new MyException(e.getMessage());
            }
        }

        //??????????????????????????????-????????????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent("???????????????" + ordTypeName + "???");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return ordType;
    }

    @Override
    public void updateOrderType(ParamOrdTypeUpFopApp ordTypeUpFopApp) {
        OrdType ordType = ordTypeMapper.selectOrdTypeById(ordTypeUpFopApp.getShopId(), ordTypeUpFopApp.getId());
        String ordTypeNameOld = ordType.getName();
        if (ordType == null){
            throw new MyException("???????????????????????????");
        }
        String ordTypeName = ordTypeUpFopApp.getOrdTypeName();
        ordType.setDescription(ordTypeName);
        ordType.setCode(ordTypeName);
        ordType.setName(ordTypeName);
        ordType.setUpdateTime(new Date());
        ordType.setUpdateAdmin(ordTypeUpFopApp.getUserId());
        //??????????????????
        ordTypeMapper.updateObject(ordType);
        //?????????????????????????????????
        ordTypeUpFopApp.setOrdTypeNameOld(ordTypeNameOld);
        ordOrderMapper.updateOrderType(ordTypeUpFopApp);
        //??????????????????????????????-????????????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(ordTypeUpFopApp.getShopId());
        paramAddShpOperateLog.setOperateUserId(ordTypeUpFopApp.getUserId());
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent("????????????????????????" + ordTypeNameOld + "???,????????????????????????" + ordTypeName + "???");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(ordTypeUpFopApp.getRequest());

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
    }

    @Override
    public Integer deleteOrdType(Integer shopId, Integer userId, Integer ordTypeId, HttpServletRequest request) {
        //??????????????????????????????????????????????????????
        Integer typeCount = ordTypeMapper.selectCountByShopId(shopId);
        if (typeCount.equals(1)) {
            throw new MyException("????????????????????????1???");
        }

        //????????????????????????????????????????????????
        OrdType ordType = ordTypeMapper.selectOrdTypeById(shopId, ordTypeId);
        if (null == ordType) {
            throw new MyException("????????????????????????????????????");
        }
//        String ordTypeName = ordType.getName();
//        Boolean isUsed = ordOrderMapper.selectOrdCountByTypeName(shopId,ordTypeName) > 0;
//        if(isUsed){
//            throw new MyException("????????????????????????????????????");
//        }

//        ordType.setUpdateAdmin(userId);
//        ordType.setUpdateTime(new Date());
//        ordType.setDel(ConstantCommon.DEL_ON);
//        ordType.setState(-1);//?????????

        //??????????????????????????????-????????????????????????
        ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
        paramAddShpOperateLog.setShopId(shopId);
        paramAddShpOperateLog.setOperateUserId(userId);
        paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.SHOP.getName());
        paramAddShpOperateLog.setOperateName("??????????????????");
        paramAddShpOperateLog.setOperateContent("???????????????" + ordType.getName() + "???");
        paramAddShpOperateLog.setProdId(null);
        paramAddShpOperateLog.setOrderId(null);
        paramAddShpOperateLog.setRequest(request);

        shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        return ordTypeMapper.deleteObject(ordTypeId);
    }

    @Override
    public void deleteOrdTypeByShopId(int shopId) {
        ordTypeMapper.deleteOrdTypeByShopId(shopId);
    }
}
