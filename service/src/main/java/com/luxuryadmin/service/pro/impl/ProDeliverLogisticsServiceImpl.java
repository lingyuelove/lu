package com.luxuryadmin.service.pro.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.sf.LogisticsRoute;
import com.luxuryadmin.common.utils.sf.LogisticsRouteResp;
import com.luxuryadmin.common.utils.sf.SFUtil;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.entity.pro.ProDeliverLogistics;
import com.luxuryadmin.mapper.pro.ProDeliverLogisticsMapper;
import com.luxuryadmin.param.pro.ParamDeliverLogisticsDetail;
import com.luxuryadmin.service.pro.ProDeliverLogisticsService;
import com.luxuryadmin.service.pro.ProDeliverService;
import com.luxuryadmin.vo.pro.VoDeliver;
import org.apache.commons.lang.StringUtils;
import org.jacoco.agent.rt.internal_1f1cc91.core.internal.flow.IFrame;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;


/**
 * 发货物流表 serverImpl
 *
 * @author zhangsai
 * @Date 2021-10-14 17:20:49
 */
@Service
@Transactional
public class ProDeliverLogisticsServiceImpl implements ProDeliverLogisticsService {


    /**
     * 注入dao
     */
    @Resource
    private ProDeliverLogisticsMapper proDeliverLogisticsMapper;


    @Override
    public void addOrUpdateList(Integer shopId) throws UnsupportedEncodingException {
        List<ProDeliverLogistics> deliverLogisticsList = proDeliverLogisticsMapper.listDeliverLogisticsByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(deliverLogisticsList)) {
            return;
        }
        //创建完任务之后,更新或新增物流信息就交给后台线程异步处理
        ThreadUtils.getInstance().executorService.execute(() -> {
            List<String> logisticsNumbers = new ArrayList<>();
            List<String> checkPhoneNos = new ArrayList<>();
            deliverLogisticsList.forEach(deliver -> {
                logisticsNumbers.add(deliver.getLogisticsNumber());
                checkPhoneNos.add(deliver.getPhone());
            });
            String checkPhoneNo = StringUtils.join(checkPhoneNos, ",");
            try {
                List<LogisticsRouteResp> logisticsRouteResps = SFUtil.listShow(logisticsNumbers, checkPhoneNo);

                deliverLogisticsList.forEach(deliver -> {
                    //更新发货物流
                    addOrUpdate(deliver, logisticsRouteResps);
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public VoDeliver getDeliverByLogisticsNumber(String logisticsNumber) {
        ProDeliverLogistics deliverLogistics = proDeliverLogisticsMapper.getDeliverLogistByLogisticsNumber(logisticsNumber);
        if (deliverLogistics == null) {
            return null;
        }
        VoDeliver voDeliver = new VoDeliver();
        BeanUtils.copyProperties(deliverLogistics, voDeliver);
        voDeliver.setExpressName("顺丰快递");
        voDeliver.setLogisticsStateCname(SFUtil.getOpCodeCname(deliverLogistics.getLogisticsState().toString()));
        List<LogisticsRoute> logisticsRoutes = JSON.parseArray(deliverLogistics.getContext(), LogisticsRoute.class);
        if (!LocalUtils.isEmptyAndNull(logisticsRoutes)){
            //发货路由反转
            Collections.reverse(logisticsRoutes);

            voDeliver.setRoutes(logisticsRoutes);
        }

        return voDeliver;
    }

    @Override
    public void addDeliverLogistics(String logisticsNumber, Integer shopId, String phone) throws UnsupportedEncodingException{
        ProDeliverLogistics deliverLogistics = proDeliverLogisticsMapper.getDeliverLogistByLogisticsNumber(logisticsNumber);
        if (deliverLogistics == null) {
            ProDeliverLogistics deliverLogisticsNew = new ProDeliverLogistics();
            Date date = new Date();
            deliverLogisticsNew.setInsertTime(date);
            deliverLogisticsNew.setLogisticsNumber(logisticsNumber);

            deliverLogisticsNew.setFkShpShopId(shopId);

            deliverLogisticsNew.setPhone(phone);
            //手机号和运单号获取物流信息
            getLogistics(deliverLogisticsNew);
            add(deliverLogisticsNew);
        } else {
            //手机号和运单号获取物流信息
            if (deliverLogistics.getLogisticsState()<50){
                deliverLogistics.setPhone(phone);
                getLogistics(deliverLogistics);
            }
            proDeliverLogisticsMapper.updateObject(deliverLogistics);
        }

    }

    @Override
    public VoDeliver getDeliverByLogisticsNumAndPhone(ParamDeliverLogisticsDetail params) throws UnsupportedEncodingException{
        ProDeliverLogistics deliverLogistics = proDeliverLogisticsMapper.getDeliverLogistByLogisticsNumber(params.getLogisticsNumber());
        VoDeliver voDeliver = new VoDeliver();
        if (LocalUtils.isEmptyAndNull(deliverLogistics)){
//            voDeliver.setLogisticsState(2);
//            voDeliver.setLogisticsStateCname("暂无物流信息");
             return null;
        }
        if (LocalUtils.isEmptyAndNull(deliverLogistics.getLogisticsNumber())){
            return null;
        }
        if (LocalUtils.isEmptyAndNull(deliverLogistics.getLogisticsState())){
            voDeliver.setLogisticsState(2);
        }

        voDeliver.setExpressName("顺丰快递");

        if (deliverLogistics.getLogisticsState()<50){
            deliverLogistics.setPhone(params.getPhone());
            getLogistics(deliverLogistics);
            if (2 != deliverLogistics.getLogisticsState()){
                proDeliverLogisticsMapper.updateObject(deliverLogistics);
            }

        }
        BeanUtils.copyProperties(deliverLogistics, voDeliver);
        List<LogisticsRoute> logisticsRoutes = JSON.parseArray(deliverLogistics.getContext(), LogisticsRoute.class);
        //发货路由反转
        if (!LocalUtils.isEmptyAndNull(logisticsRoutes) ){
            Collections.reverse(logisticsRoutes);

            voDeliver.setRoutes(logisticsRoutes);
        }
        voDeliver.setLogisticsStateCname(SFUtil.getOpCodeCname(deliverLogistics.getLogisticsState().toString()));
        return voDeliver;
    }

    @Override
    public VoDeliver getDeliverByOrderNumber(int shopId, String orderBizId)  {
        VoDeliver voDeliver =proDeliverLogisticsMapper.getDeliverByOrderNumber(shopId,orderBizId);
        if (LocalUtils.isEmptyAndNull(voDeliver)){
            return null;
        }
        //是顺丰快递
        if (!LocalUtils.isEmptyAndNull(voDeliver.getLogisticsState())){
            voDeliver.setLogisticsStateCname(SFUtil.getOpCodeCname(voDeliver.getLogisticsState().toString()));
        }

        if (LocalUtils.isEmptyAndNull(voDeliver.getContext()) && !LocalUtils.isEmptyAndNull(voDeliver.getExpressName())){
            return voDeliver;
        }
        voDeliver.setExpressName("顺丰快递");
        List<LogisticsRoute> logisticsRoutes = JSON.parseArray(voDeliver.getContext(), LogisticsRoute.class);
        if (LocalUtils.isEmptyAndNull(logisticsRoutes)){

            return voDeliver;
        }
        //发货路由反转
        if (!LocalUtils.isEmptyAndNull(logisticsRoutes) ){
            Collections.reverse(logisticsRoutes);

            voDeliver.setRoutes(logisticsRoutes);
        }

        return voDeliver;
    }

    public void getLogistics( ProDeliverLogistics deliverLogistics) throws UnsupportedEncodingException{
        //手机号和运单号获取物流信息
        List<String> logisticsNumbers = new ArrayList<>();
        List<String> checkPhoneNos = new ArrayList<>();
        logisticsNumbers.add(deliverLogistics.getLogisticsNumber());
        checkPhoneNos.add(deliverLogistics.getPhone());
        String checkPhoneNo = StringUtils.join(checkPhoneNos, ",");
        List<LogisticsRouteResp> logisticsRouteResps = SFUtil.listShow(logisticsNumbers, checkPhoneNo);
        if (!LocalUtils.isEmptyAndNull(logisticsRouteResps) && !LocalUtils.isEmptyAndNull(logisticsRouteResps.get(0).getRoutes())  && logisticsRouteResps.get(0).getRoutes().size()>0){
            String routes = getRoutes(logisticsRouteResps.get(0).getRoutes(), deliverLogistics.getInsertTime());
            deliverLogistics.setContext(routes);
            LogisticsRoute LogisticsRoute = logisticsRouteResps.get(0).getRoutes().get(logisticsRouteResps.get(0).getRoutes().size() - 1);
            deliverLogistics.setLogisticsState(Integer.parseInt(LogisticsRoute.getOpCode()));
        }else {
            deliverLogistics.setContext("");
            deliverLogistics.setLogisticsState(2);
        }
    }
    public void addOrUpdate(ProDeliverLogistics deliver, List<LogisticsRouteResp> logisticsRouteResps) {
        //获取此次物流信息
        Optional<LogisticsRouteResp> logisticsRouteRespList = logisticsRouteResps.stream().filter(item -> item.getMailNo().equals(deliver.getLogisticsNumber())).findFirst();
        LogisticsRouteResp logisticsRouteResp = null;
        if (logisticsRouteRespList.isPresent()) {
            // 存在
            logisticsRouteResp = logisticsRouteRespList.get();
        }
        ProDeliverLogistics deliverLogistics = proDeliverLogisticsMapper.getDeliverLogistByLogisticsNumber(deliver.getLogisticsNumber());
        if (LocalUtils.isEmptyAndNull(deliverLogistics)) {
            ProDeliverLogistics deliverLogisticsNew = new ProDeliverLogistics();
            Date date = new Date();
            deliverLogisticsNew.setInsertTime(date);
            deliverLogisticsNew.setLogisticsNumber(deliver.getLogisticsNumber());
            List<LogisticsRoute> routesList = logisticsRouteResp.getRoutes();
            if (!LocalUtils.isEmptyAndNull(routesList)){
                String routes = getRoutes(routesList, date);
                deliverLogisticsNew.setContext(routes);
                LogisticsRoute LogisticsRoute = logisticsRouteResps.get(0).getRoutes().get(logisticsRouteResps.get(0).getRoutes().size() - 1);
                deliverLogisticsNew.setLogisticsState(Integer.parseInt(LogisticsRoute.getOpCode()));
            }else {
                deliverLogisticsNew.setContext("");
                deliverLogisticsNew.setLogisticsState(2);
            }
            deliverLogisticsNew.setFkShpShopId(deliver.getFkShpShopId());
            add(deliverLogisticsNew);
        } else {
            String routes = getRoutes(logisticsRouteResp.getRoutes(), deliverLogistics.getInsertTime());
            deliverLogistics.setContext(routes);
            LogisticsRoute LogisticsRoute = logisticsRouteResp.getRoutes().get(logisticsRouteResp.getRoutes().size() - 1);
            deliverLogistics.setLogisticsState(Integer.parseInt(LogisticsRoute.getOpCode()));
            proDeliverLogisticsMapper.updateObject(deliverLogistics);
        }

    }

    /**
     * 物流节点的新增首个
     *
     * @param routes
     * @return
     */
    public String getRoutes(List<LogisticsRoute> routes, Date date) {
        if (LocalUtils.isEmptyAndNull(routes) && routes.size() <=0){
            return "";
        }

        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(routes);
        return jsonArray.toJSONString();
    }

    public void add(ProDeliverLogistics deliverLogistics) {
        proDeliverLogisticsMapper.saveObject(deliverLogistics);
    }
}
