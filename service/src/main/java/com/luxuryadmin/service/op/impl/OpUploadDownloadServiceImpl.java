package com.luxuryadmin.service.op.impl;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import com.alibaba.excel.EasyExcel;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.ThreadUtils;
import com.luxuryadmin.common.utils.aliyun.OSSUtil;
import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.mapper.op.OpUploadDownloadMapper;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.vo.op.VoOpUploadDownload;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-06 14:59:47
 */
@Service
@Slf4j
public class OpUploadDownloadServiceImpl implements OpUploadDownloadService {

    @Resource
    private OpUploadDownloadMapper opUploadDownloadMapper;


    @Override
    public OpUploadDownload getObjectById(Integer id) {
        return (OpUploadDownload) opUploadDownloadMapper.getObjectById(id);
    }

    @Override
    public List<VoOpUploadDownload> listVoOpUploadDownload(Integer shopId) {
        List<VoOpUploadDownload> list = opUploadDownloadMapper.listVoOpUploadDownload(shopId);
        if (!LocalUtils.isEmptyAndNull(list)) {
            //-90:删除 | 10:进行中 | 20:已取消； | 21:导出失败 | 30：已完成；
            for (VoOpUploadDownload vo : list) {
                String state;
                switch (vo.getState()) {
                    case "10":
                        state = "进行中";
                        break;
                    case "20":
                        state = "已取消";
                        break;
                    case "21":
                        state = "导出失败";
                        break;
                    case "30":
                        state = "已完成";
                        break;
                    case "-90":
                        state = "删除";
                        break;
                    default:
                        state = "未知";
                }
                vo.setState(state);
            }
        }
        return list;
    }

    @Override
    public int saveOrUpdateObjectAndReturnId(OpUploadDownload opUploadDownload) {
        Integer id = opUploadDownload.getId();
        if (null == id) {
            opUploadDownloadMapper.saveObject(opUploadDownload);
        } else {
            opUploadDownloadMapper.updateObject(opUploadDownload);
        }
        return opUploadDownload.getId();
    }

    @Override
    public OpUploadDownload packOpUploadDownload(int shopId, int userId, String exportType,
                                                 String module, String taskName, Date st, Date et) {
        OpUploadDownload down = new OpUploadDownload();
        down.setState(10);
        down.setName(taskName);
        down.setTotalMs(0);
        down.setDownloadTimes(0);
        down.setType(exportType);
        down.setModule(module);
        down.setFkShpShopId(shopId);
        down.setFkShpUserId(userId);
        down.setStartTime(st);
        down.setEndTime(et);
        down.setUrl("");
        down.setInsertTime(new Date());
        down.setUpdateTime(down.getInsertTime());
        down.setInsertAdmin(userId);
        down.setUpdateAdmin(0);
        down.setVersions(1);
        down.setRemark("");
        return down;
    }

    @Override
    public void uploadFileToOss(OpUploadDownload opUploadDownload, List list) {
        //创建完任务之后,导出就交给后台线程
        int shopId = opUploadDownload.getFkShpShopId();
        String taskName = opUploadDownload.getName();
        ThreadUtils.getInstance().executorService.execute(() -> {
            //-90:删除 | 10:进行中 | 20:已取消； | 21:导出失败 | 30：已完成；
            int state;
            try {
                String ossUrl = simpleWrite(shopId, list, taskName);
                log.debug("oss地址:{}", ossUrl);
                opUploadDownload.setUrl(ossUrl);
                state = 30;
            } catch (Exception e) {
                state = 21;
                log.error(e.getMessage(), e);
                String reason = e.getMessage().contains("Convert data") ? "导出失败,存在无法访问的图片!" : "导出失败!";
                opUploadDownload.setRemark(reason);
                opUploadDownload.setExceptionRemark(e.getMessage());
            }
            opUploadDownload.setState(state);
            opUploadDownload.setFinishTime(new Date());
            long totalMs = opUploadDownload.getFinishTime().getTime() - opUploadDownload.getInsertTime().getTime();
            opUploadDownload.setTotalMs(Integer.parseInt(totalMs + ""));
            opUploadDownload.setUpdateTime(opUploadDownload.getFinishTime());
            saveOrUpdateObjectAndReturnId(opUploadDownload);
        });
    }

    /**
     * 写出流到阿里云的oss
     */
    private String simpleWrite(int shopId, List list, String fileName) throws Exception {
        @Cleanup ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 写法1
        fileName = "export/" + shopId + "/" + fileName + LocalUtils.getTimestamp() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可

        EasyExcel.write(os, list.get(0).getClass()).sheet("模板").doWrite(list);
        OSSUtil.uploadBytesFile(fileName, os.toByteArray());
        os.close();
        return ConstantCommon.ossDomain + "/" + fileName;
    }
}
