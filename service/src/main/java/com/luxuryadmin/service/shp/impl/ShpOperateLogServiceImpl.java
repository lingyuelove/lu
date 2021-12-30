package com.luxuryadmin.service.shp.impl;

import com.google.gson.Gson;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.entity.shp.ShpOperateLog;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.entity.shp.ShpUserShopRef;
import com.luxuryadmin.mapper.shp.ShpOperateLogMapper;
import com.luxuryadmin.mapper.shp.ShpShopMapper;
import com.luxuryadmin.mapper.shp.ShpUserShopRefMapper;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.param.shp.ParamShpOperateLogQuery;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.vo.shp.VoEmployee;
import com.luxuryadmin.vo.shp.VoInitShopOperateLog;
import com.luxuryadmin.vo.shp.VoShpOperateLogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 操作日志Service实现类
 */
@Service
@Slf4j
public class ShpOperateLogServiceImpl implements ShpOperateLogService {

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Resource
    private ShpOperateLogMapper shpOperateLogMapper;

    @Autowired
    private Gson gson;

    @Resource
    private ShpShopMapper shpShopMapper;

    @Resource
    private ShpUserShopRefMapper shpUserShopRefMapper;

    @Override
    public VoInitShopOperateLog getInitOperateLogQueryData(Integer shopId) {
        VoInitShopOperateLog vo = new VoInitShopOperateLog();

        //模块名称
        List<String> moduleNameList = new ArrayList<>();
        moduleNameList.add("订单");
        moduleNameList.add("商品");
        moduleNameList.add("维修保养");
        moduleNameList.add("员工");
        moduleNameList.add("薪资");
        moduleNameList.add("记账本");
        moduleNameList.add("友商相册");
        vo.setShpOperateModuleList(moduleNameList);

        //店铺员工列表
        List<VoEmployee> voEmployeeList = shpUserShopRefService.getSalesmanByShopId(shopId);
        vo.setShpUserList(voEmployeeList);

        return vo;
    }

    @Override
    public Integer saveShpOperateLog(ParamAddShpOperateLog paramAddShpOperateLog) {
        try {
            ShpOperateLog shpOperateLog = new ShpOperateLog();

            //店铺ID
            Integer shopId = paramAddShpOperateLog.getShopId();
            shpOperateLog.setFkShpShopId(shopId);

            //操作用户ID
            Integer operateUserId = paramAddShpOperateLog.getOperateUserId();
            shpOperateLog.setOperateUserId(operateUserId);

            //店铺名称
            ShpShop shpShop = (ShpShop) shpShopMapper.getObjectById(shopId);
            if (null == shpShop) {
                throw new Exception("店铺ID对应的店铺不存在,shopId=" + shopId);
            }
            shpOperateLog.setShopName(shpShop.getName());

            //用户所在店铺名称
            ShpUserShopRef shpUserShopRef = shpUserShopRefMapper.getShpUserShopRefByUserId(shopId, operateUserId);
            if (null == shpUserShopRef) {
                throw new Exception("用户对应的店铺关系不存在,shopId=" + shopId + ",userId=" + operateUserId);
            }
            shpOperateLog.setOperateUserShopName(shpUserShopRef.getName());

            //模块名称
            String moduleName = paramAddShpOperateLog.getModuleName();
            shpOperateLog.setModuleName(moduleName);

            //操作业务类型名称
            String operateName = paramAddShpOperateLog.getOperateName();
            shpOperateLog.setOperateTypeName(operateName);

            //操作内容
            String operateContent = paramAddShpOperateLog.getOperateContent();
            shpOperateLog.setOperateContent(operateContent);

            //商品ID
            Integer prodId = paramAddShpOperateLog.getProdId();
            shpOperateLog.setFkProProductId(prodId);

            //订单ID
            Integer orderId = paramAddShpOperateLog.getOrderId();
            shpOperateLog.setFkOrdOrderId(orderId);

            //请求request
            HttpServletRequest request = paramAddShpOperateLog.getRequest();
            shpOperateLog.setRequestDomain(request.getLocalName());
            shpOperateLog.setRequestUri(request.getRequestURI());
            shpOperateLog.setRequestParam(gson.toJson(request.getParameterMap()));
            shpOperateLog.setRequestIp(request.getRemoteAddr());
            shpOperateLog.setRequestMethod(request.getMethod());

            //操作时间
            Date operateTime = new Date();
            shpOperateLog.setInsertTime(operateTime);
            shpOperateLog.setUpdateTime(operateTime);
            shpOperateLog.setOperateDate(DateUtil.formatShort(operateTime));
            shpOperateLog.setOperateTime(operateTime);

            return shpOperateLogMapper.saveObject(shpOperateLog);
        } catch (Exception e) {
            log.error("新增操作日志异常:" + e);
        }

        return -1;
    }

    @Override
    public List<VoShpOperateLogRecord> listShpOperateLog(ParamShpOperateLogQuery paramShpOperateLogQuery) {
        return shpOperateLogMapper.selectShpOperateLogList(paramShpOperateLogQuery);
    }

    @Override
    public int deleteShpOperateLogByShopId(int shopId) {
        return shpOperateLogMapper.deleteShpOperateLogByShopId(shopId);
    }
}
