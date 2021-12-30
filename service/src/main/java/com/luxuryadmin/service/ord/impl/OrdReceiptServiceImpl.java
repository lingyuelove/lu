package com.luxuryadmin.service.ord.impl;

import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdReceipt;
import com.luxuryadmin.mapper.ord.OrdOrderMapper;
import com.luxuryadmin.mapper.ord.OrdReceiptMapper;
import com.luxuryadmin.service.ord.OrdReceiptService;
import com.luxuryadmin.service.pro.ProDeliverLogisticsService;
import com.luxuryadmin.vo.ord.VoOrdReceipt;
import com.luxuryadmin.vo.ord.VoOrderLoad;
import com.luxuryadmin.vo.pro.VoDeliver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2020-01-20 21:51:39
 */
@Slf4j
@Service
public class OrdReceiptServiceImpl implements OrdReceiptService {

    @Resource
    private OrdReceiptMapper ordReceiptMapper;

    @Resource
    private OrdOrderMapper ordOrderMapper;
    @Autowired
    private ProDeliverLogisticsService proDeliverLogisticsService;
    @Override
    public int saveOrUpdateOrdReceipt(OrdReceipt ordReceipt) {
        Integer id = ordReceipt.getId();
        if (null != id) {
            //更新
            ordReceiptMapper.updateObject(ordReceipt);
        } else {
            //新增
            ordReceiptMapper.saveObject(ordReceipt);
        }
        return ordReceipt.getId();
    }

    @Override
    public VoOrdReceipt getOrdReceiptByOrderNumber(int shopId, String orderBizId) {
        VoOrderLoad orderLoad = ordOrderMapper.getOrderDetailByNumber(shopId, orderBizId);
        if (LocalUtils.isEmptyAndNull(orderLoad)) {
            throw new MyException("订单不存在!");
        }
        VoOrdReceipt voOrdReceipt = ordReceiptMapper.getOrdReceiptByOrderNumber(shopId, orderBizId);
        if (LocalUtils.isEmptyAndNull(voOrdReceipt)) {
            throw new MyException("订单收据不存在!");
        }
        voOrdReceipt.setAfterSaleGuarantee(orderLoad.getAfterSaleGuarantee());
        voOrdReceipt.setReceiveAddress(orderLoad.getReceiveAddress());
        VoDeliver deliver= proDeliverLogisticsService.getDeliverByOrderNumber(shopId, orderBizId);
        if (LocalUtils.isEmptyAndNull(deliver)) {
            voOrdReceipt.setShowDeliverState("3");
            return voOrdReceipt;
        }
        //判断物流状态是否为空
        if (!LocalUtils.isEmptyAndNull(deliver.getState())){

            if (deliver.getState() ==1){
                //判断物流状态是否为顺丰
                if (!LocalUtils.isEmptyAndNull(deliver.getExpressName()) && "顺丰".equals(deliver.getExpressName())){
                    voOrdReceipt.setShowDeliverState("1");
                }else {
                    voOrdReceipt.setShowDeliverState("2");
                }
            }else {
                voOrdReceipt.setShowDeliverState(deliver.getState().toString());
            }

        }else {
            voOrdReceipt.setShowDeliverState("3");

        }
        return voOrdReceipt;
    }
}
