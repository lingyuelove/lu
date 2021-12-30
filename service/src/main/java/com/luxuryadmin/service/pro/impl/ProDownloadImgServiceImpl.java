package com.luxuryadmin.service.pro.impl;
import com.luxuryadmin.entity.pro.ProDownloadImg;
import com.luxuryadmin.entity.pro.ProModifyRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.mapper.pro.ProDownloadImgMapper;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.pro.ProDownloadImgService;
import com.luxuryadmin.service.pro.ProModifyRecordService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2020-03-03 17:42:43
 */
@Slf4j
@Service
public class ProDownloadImgServiceImpl implements ProDownloadImgService {

    @Resource
    private ProDownloadImgMapper proDownloadImgMapper;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ProModifyRecordService proModifyRecordService;

    @Override
    public int countDownload(int shopId, String bizId) {
        return proDownloadImgMapper.countDownload(shopId, bizId);
    }

    @Override
    public int existsSelfDownload(int shopId, int userId, String bizId) {
        return proDownloadImgMapper.existsSelfDownload(shopId, userId, bizId);
    }

    @Override
    public void saveProDownloadImg(int shopId, int userId, String bizId, HttpServletRequest request) {
        int selfDownload = proDownloadImgMapper.existsSelfDownload(shopId, userId, bizId);
        if (selfDownload < 1) {
            //新增
            ProDownloadImg proDownloadImg = new ProDownloadImg();
            proDownloadImg.setFkProProductBizId(bizId);
            proDownloadImg.setFkShpShopId(shopId);
            proDownloadImg.setFkShpUserId(userId);
            proDownloadImg.setDownloadCount(1);
            proDownloadImg.setInsertTime(new Date());
            proDownloadImg.setUpdateTime(proDownloadImg.getInsertTime());
            proDownloadImgMapper.saveObject(proDownloadImg);
        }else{//该用户下载过，则更新修改时间
            ProDownloadImg proDownloadImg = proDownloadImgMapper.selectDownload(shopId, userId, bizId);
            proDownloadImg.setUpdateTime(new Date());
            proDownloadImgMapper.updateObject(proDownloadImg);
        }

        //添加【店铺操作日志】-【下载商品图片】
        ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId,bizId);
        if(null != proProduct){
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(shopId);
            paramAddShpOperateLog.setOperateUserId(userId);
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.PROD.getName());
            paramAddShpOperateLog.setOperateName("下载商品图片");
            paramAddShpOperateLog.setOperateContent(proProduct.getName());
            paramAddShpOperateLog.setProdId(proProduct.getId());
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);

            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);

            //添加商品操作记录
            ProModifyRecord proModifyRecord = new ProModifyRecord();
            proModifyRecord.setFkShpShopId(shopId);
            proModifyRecord.setFkShpUserId(userId);
            proModifyRecord.setFkProProductId(proProduct.getId());
            proModifyRecord.setType("下载图片");
            proModifyRecord.setBeforeValue("");
            proModifyRecord.setAfterValue("");
            proModifyRecord.setInsertTime(new Date());
            proModifyRecord.setRemark("");

            proModifyRecordService.saveProModifyRecord(proModifyRecord);

        }
    }

    @Override
    public List<ProDownloadImg> listProDownloadImg() {
        return proDownloadImgMapper.listProDownloadImg();
    }

    @Override
    public void deleteProDownloadImgByShopId(int shopId) {
        proDownloadImgMapper.deleteProDownloadImgByShopId(shopId);
    }
}