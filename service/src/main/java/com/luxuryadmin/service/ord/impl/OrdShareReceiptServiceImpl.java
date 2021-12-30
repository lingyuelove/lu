package com.luxuryadmin.service.ord.impl;

import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.ord.OrdShareReceipt;
import com.luxuryadmin.mapper.ord.OrdShareReceiptMapper;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.service.ord.OrdShareReceiptService;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author monkey king
 * @date 2020-09-04 20:00:29
 */
@Slf4j
@Service
public class OrdShareReceiptServiceImpl implements OrdShareReceiptService {

    @Resource
    private OrdShareReceiptMapper ordShareReceiptMapper;


    @Override
    public VoShareReceipt getOrderNoByShareBatch(ParamShareReceiptQuery shareReceiptQuery) {
        return ordShareReceiptMapper.getOrderNoByShareBatch(shareReceiptQuery);
    }

    @Override
    public String saveShareReceipt(OrdShareReceipt ordShareReceipt) {
        String saveBatch = ordShareReceipt.getShareBatch();
        String shareName = ordShareReceipt.getShareName();
        if (LocalUtils.isEmptyAndNull(saveBatch)) {
            saveBatch = System.currentTimeMillis() + "";
            ordShareReceipt.setShareBatch(saveBatch);
        }
        if (LocalUtils.isEmptyAndNull(shareName)) {
            ordShareReceipt.setShareName(saveBatch);
        }

        ordShareReceiptMapper.saveObject(ordShareReceipt);
        return saveBatch;
    }

}
