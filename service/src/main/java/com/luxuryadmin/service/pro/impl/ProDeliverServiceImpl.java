package com.luxuryadmin.service.pro.impl;

import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.sf.SFUtil;
import com.luxuryadmin.entity.ord.OrdOrder;
import com.luxuryadmin.entity.ord.OrdType;
import com.luxuryadmin.entity.pro.ProDeliver;
import com.luxuryadmin.entity.pro.ProDeliverLogistics;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.enums.pro.EnumProDeliverSource;
import com.luxuryadmin.mapper.pro.ProDeliverLogisticsMapper;
import com.luxuryadmin.mapper.pro.ProDeliverMapper;
import com.luxuryadmin.param.op.ParamIdQuery;
import com.luxuryadmin.param.pro.ParamProPageDeliver;
import com.luxuryadmin.param.pro.ParamDeliverLogisticsDetail;
import com.luxuryadmin.param.pro.ParamSendProDeliver;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.ord.OrdTypeService;
import com.luxuryadmin.service.pro.ProDeliverLogisticsService;
import com.luxuryadmin.service.pro.ProDeliverService;
import com.luxuryadmin.service.pro.ProLockRecordService;
import com.luxuryadmin.service.pro.ProModifyRecordService;
import com.luxuryadmin.vo.pro.VoDeliver;
import com.luxuryadmin.vo.pro.VoFiltrateInfo;
import com.luxuryadmin.vo.pro.VoOrderUserInfo;
import com.luxuryadmin.vo.pro.VoProDeliverDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 发货表 serverImpl
 *
 * @author taoqimin
 * @Date 2021-09-24 16:28:40
 */
@Service
public class ProDeliverServiceImpl implements ProDeliverService {


    /**
     * 注入dao
     */
    @Resource
    private ProDeliverMapper proDeliverMapper;

    @Resource
    private ProLockRecordService proLockRecordService;

    @Resource
    private OrdOrderService ordOrderService;

    @Resource
    private OrdTypeService ordTypeService;

    @Resource
    private ServicesUtil servicesUtil;

    @Resource
    private ProModifyRecordService proModifyRecordService;
    @Resource
    private ProDeliverLogisticsMapper proDeliverLogisticsMapper;
    @Autowired
    private ProDeliverLogisticsService proDeliverLogisticsService;

    /**
     * 添加代发货信息
     *
     * @param proDeliver
     */
    @Override
    public void saveProDeliver(ProDeliver proDeliver) {
        proDeliverMapper.saveObject(proDeliver);
    }

    /**
     * 根据锁单id删除发货信息
     *
     * @param lockId
     */
    @Override
    public void deleteDeliverInfoByLockId(Integer lockId) {
        proDeliverMapper.deleteDeliverInfoByLockId(lockId);
    }

    /**
     * 根据锁单id变化订单来源
     *
     * @param lockId
     * @param ordOrder
     */
    @Override
    public void updateSourceByLockId(Integer lockId, OrdOrder ordOrder) {
        ProDeliver proDeliver = proDeliverMapper.getDeliverByLockId(lockId);
        if (proDeliver != null && proDeliver.getState() == 0) {
            proDeliver.setFkProLockRecordId(0);
            proDeliver.setFkOrdOrderId(ordOrder.getId());
            proDeliver.setDeliverSource(EnumProDeliverSource.ORDER.name());
            proDeliver.setInsertTime(new Date());
            proDeliver.setUpdateTime(new Date());
            proDeliver.setUpdateAdmin(ordOrder.getUpdateAdmin());
            proDeliverMapper.updateObject(proDeliver);
        }
    }

    /**
     * 根据订单信息删除发货信息
     *
     * @param orderId
     */
    @Override
    public void deleteDeliverInfoByOrderInfo(Integer orderId) {
        proDeliverMapper.deleteDeliverInfoByOrderInfo(orderId);
    }

    @Override
    public void sendProDeliver(ParamSendProDeliver param) throws UnsupportedEncodingException {
        ProDeliver objectById = proDeliverMapper.getObjectById(param.getDeliverId());
        if (objectById == null) {
            throw new MyException("发货信息不存在");
        }
        objectById.setFkShpUserId(param.getCurrentUserId());
        objectById.setState(1);
        if (StringUtil.isNotBlank(param.getDeliverImgs())) {
            String[] split = param.getDeliverImgs().split(";");
            if (split.length > 9) {
                throw new MyException("发货凭证图过多");
            }
            objectById.setDeliverImgs(param.getDeliverImgs());
        }
        objectById.setDeliverTime(new Date());
        objectById.setDeliverType(param.getDeliverType());
        objectById.setUpdateTime(new Date());
        objectById.setUpdateAdmin(param.getCurrentUserId());
        objectById.setLogisticsNumber(param.getLogisticsNumber());
        proDeliverMapper.updateObject(objectById);
        if (!LocalUtils.isEmptyAndNull(param.getLogisticsNumber())) {

            proDeliverLogisticsService.addDeliverLogistics(param.getLogisticsNumber(), objectById.getFkShpShopId(), param.getCheckPhoneNo());
        }
        //操作日志
        ProModifyRecord proModifyRecord = new ProModifyRecord();
        proModifyRecord.setFkShpShopId(objectById.getFkShpShopId());
        proModifyRecord.setFkShpUserId(param.getCurrentUserId());
        if (objectById.getDeliverSource().equals(EnumProDeliverSource.ORDER.name())) {
            OrdOrder ordOrder = ordOrderService.getOrdOrderInfoById(objectById.getFkOrdOrderId());
            proModifyRecord.setFkProProductId(ordOrder.getFkProProductId());
        } else {
            ProLockRecord proLockRecordById = proLockRecordService.getProLockRecordById(objectById.getFkProLockRecordId());
            proModifyRecord.setFkProProductId(proLockRecordById.getFkProProductId());

        }
        proModifyRecord.setType("发货");
        proModifyRecord.setBeforeValue("待发货");
        proModifyRecord.setAfterValue("已发货");
        proModifyRecord.setInsertTime(new Date());
        proModifyRecord.setRemark("");
        proModifyRecordService.saveProModifyRecord(proModifyRecord);
    }

    /**
     * 获取发货详情
     *
     * @param params
     * @return
     */
    @Override
    public VoProDeliverDetail getProDeliverDetail(ParamIdQuery params) throws UnsupportedEncodingException {
        ProDeliver proDeliver = proDeliverMapper.getObjectById(Integer.parseInt(params.getId()));
        if (proDeliver == null) {
            throw new MyException("暂无此发货");
        }
        VoProDeliverDetail voProDeliverDetail;
        if (proDeliver.getDeliverSource().equals(EnumProDeliverSource.ORDER.name())) {
            voProDeliverDetail = proDeliverMapper.getProDeliverOrderDetail(Integer.parseInt(params.getId()));
        } else {
            voProDeliverDetail = proDeliverMapper.getProDeliverLockDetail(Integer.parseInt(params.getId()));
        }
        if (StringUtil.isNotBlank(voProDeliverDetail.getDeliverImgStr())) {
            String[] split = voProDeliverDetail.getDeliverImgStr().split(";");
            List<String> deliverImgs = new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                if (!split[i].contains("http")) {
                    deliverImgs.add(servicesUtil.formatImgUrl(split[i], false));
                }
            }
            voProDeliverDetail.setDeliverImgs(deliverImgs);
            voProDeliverDetail.setDeliverImgStr(null);
        }
        voProDeliverDetail.setLogisticsNumber(proDeliver.getLogisticsNumber());
        //判断物流单号是否为空
        if (LocalUtils.isEmptyAndNull(voProDeliverDetail.getLogisticsNumber())) {
            return voProDeliverDetail;
        }
        ProDeliverLogistics deliverLogistics = proDeliverLogisticsMapper.getDeliverLogistByLogisticsNumber(voProDeliverDetail.getLogisticsNumber());
        if (deliverLogistics == null) {
            return voProDeliverDetail;
        }
        //物流已签收直接返回
        if (deliverLogistics.getLogisticsState() >= 50) {
            voProDeliverDetail.setPhone(deliverLogistics.getPhone());
            voProDeliverDetail.setLogisticsState(deliverLogistics.getLogisticsState());
            String logisticsStateCname = SFUtil.getOpCodeCname(deliverLogistics.getLogisticsState().toString());
            voProDeliverDetail.setLogisticsStateCname(logisticsStateCname);
            return voProDeliverDetail;
        }
        if (LocalUtils.isEmptyAndNull(deliverLogistics.getPhone())) {
            return voProDeliverDetail;
        }
        //物流未签收
        ParamDeliverLogisticsDetail paramsLogistics = new ParamDeliverLogisticsDetail();
        paramsLogistics.setLogisticsNumber(voProDeliverDetail.getLogisticsNumber());
        paramsLogistics.setPhone(deliverLogistics.getPhone());
        VoDeliver deliver = proDeliverLogisticsService.getDeliverByLogisticsNumAndPhone(paramsLogistics);
        if (LocalUtils.isEmptyAndNull(deliver)) {
            return voProDeliverDetail;
        }
        voProDeliverDetail.setPhone(deliverLogistics.getPhone());

        voProDeliverDetail.setLogisticsState(deliver.getLogisticsState());
        String logisticsStateCname = SFUtil.getOpCodeCname(deliver.getLogisticsState().toString());
        voProDeliverDetail.setLogisticsStateCname(logisticsStateCname);

        return voProDeliverDetail;
    }

    @Override
    public void deleteList(String ids) {
        List<String> idList = Arrays.asList(ids.split(";"));
        idList.forEach(s -> {
            ProDeliver proDeliver = new ProDeliver();
            proDeliver.setId(Integer.parseInt(s));
            proDeliver.setDel("1");
            proDeliverMapper.updateObject(proDeliver);
        });
    }

    /**
     * 筛选条件列表
     *
     * @param shopId
     * @return
     */
    @Override
    public VoFiltrateInfo getFiltrateinfo(Integer shopId) {
        VoFiltrateInfo voFiltrateInfo = new VoFiltrateInfo();
        //发货人员
        List<VoOrderUserInfo> listDeliverUser = proDeliverMapper.getFiltrateinfoByShopId(shopId);
        voFiltrateInfo.setListDeliverUser(listDeliverUser != null ? listDeliverUser : null);
        //锁单人员
        List<VoOrderUserInfo> listLockUser = proLockRecordService.getFiltrateinfoByShopId(shopId);
        voFiltrateInfo.setListLockUser(listLockUser != null ? listLockUser : null);
        //开单人员
        List<VoOrderUserInfo> listOrderrUser = ordOrderService.getFiltrateinfoByShopId(shopId);
        voFiltrateInfo.setListOrderrUser(listOrderrUser != null ? listOrderrUser : null);
        //订单类型
        List<OrdType> ordTypeList = ordTypeService.listOrdTypeByShopId(shopId);
        voFiltrateInfo.setOrdTypeList(ordTypeList != null ? ordTypeList : null);
        return voFiltrateInfo;
    }

    /**
     * 修改锁单数量
     *
     * @param lockRecord
     */
    @Override
    public void updateNum(ProLockRecord lockRecord) {
        ProDeliver proDeliver = proDeliverMapper.getDeliverByLockId(lockRecord.getId());
        if (proDeliver == null) {
            return;
        }
        if (proDeliver.getState() == 0 && !lockRecord.getLockNum().equals(proDeliver.getNum())) {
            proDeliver.setNum(lockRecord.getLockNum());
            proDeliverMapper.updateObject(proDeliver);
        }
    }

    /**
     * 2.6.6发货列表
     *
     * @param param
     * @return
     */
    @Override
    public List<ProDeliver> listDeliver(ParamProPageDeliver param) {
        return proDeliverMapper.listDeliver(param);
    }

    /**
     * 2.6.6新增代发货统计
     *
     * @param startTime
     * @param endTime
     * @param shpUserId
     * @param deliverStartTime
     * @param deliverEndTime
     * @param proName
     * @return
     */
    @Override
    public List<ProDeliver> listDeliverAll(Integer shopId, String startTime, String endTime, String shpUserId, String deliverStartTime, String deliverEndTime, String proName) {
        return proDeliverMapper.listDeliverAll(shopId, startTime, endTime, shpUserId, deliverStartTime, deliverEndTime, proName);
    }

}
