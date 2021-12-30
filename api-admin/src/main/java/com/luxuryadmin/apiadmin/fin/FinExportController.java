package com.luxuryadmin.apiadmin.fin;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.excel.ExVoProduct;
import com.luxuryadmin.param.fin.ParamFinShopRecordQuery;
import com.luxuryadmin.service.fin.FinShopRecordService;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.fin.VoFinShopRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author monkey king
 * @date 2021-09-08 22:20:22
 */
@Slf4j
@RestController
@Api(tags = {"H004.【店铺账单导出】模块"})
@RequestMapping(value = "/shop/admin/fin")
public class FinExportController extends ProProductBaseController {

    @Autowired
    private OpUploadDownloadService opUploadDownloadService;
    @Autowired
    private FinShopRecordService finShopRecordService;
    /**
     * 店铺账单导出
     *
     * @param queryParam 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "店铺账单导出",
            notes = "店铺账单导出",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/exportFinShopRecord")
    public BaseResult exportFinShopRecord(@Valid ParamFinShopRecordQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String taskName = "店铺账单导出";
        Integer shopId = getShopId();
        queryParam.setShopId(shopId);
        List<VoFinShopRecord> list = finShopRecordService.listFinShopRecordByShopIdForExp(queryParam);
        OpUploadDownload opUploadDownload = packObject(taskName, queryParam);
        int id = opUploadDownloadService.saveOrUpdateObjectAndReturnId(opUploadDownload);
        opUploadDownloadService.uploadFileToOss(opUploadDownload, list);
        return LocalUtils.getBaseResult(id);
    }


    private OpUploadDownload packObject(String taskName, ParamFinShopRecordQuery queryParam) {
        String st = queryParam.getHappenTimeStart();
        String et = queryParam.getHappenTimeEnd();
        validTimeScope(st, et);
        int shopId = getShopId();
        int userId = getUserId();
        String exportType = "out";
        String module = "账单模块";
        Date st1 = null;
        Date et2 = null;
        try {
            st1 = DateUtil.parse(st);
            et2 = DateUtil.parse(et);
        } catch (ParseException ignored) {
        }
        return opUploadDownloadService.packOpUploadDownload(shopId, userId, exportType, module, taskName, st1, et2);
    }
}
