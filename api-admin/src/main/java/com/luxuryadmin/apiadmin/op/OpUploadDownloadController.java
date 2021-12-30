package com.luxuryadmin.apiadmin.op;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpUploadDownload;
import com.luxuryadmin.param.op.ParamOpRecycleQuery;
import com.luxuryadmin.param.op.ParamUploadDownload;
import com.luxuryadmin.service.op.OpRecycleService;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.vo.op.VoOpUploadDownload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回收归集控制层
 * mong
 *
 * @author Administrator
 */
@RestController
@Slf4j
@RequestMapping(value = "shop/admin/op")
@Api(tags = {"H001.上传下载"})
public class OpUploadDownloadController extends BaseController {

    @Autowired
    private OpUploadDownloadService opUploadDownloadService;


    @ApiOperation(
            value = "加载上传下载任务列表",
            notes = "加载上传下载任务列表",
            httpMethod = "POST")
    @RequestMapping("/loadUploadDownload")
    public BaseResult loadUploadDownload(@Valid ParamUploadDownload param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_50);
        List<VoOpUploadDownload> list = opUploadDownloadService.listVoOpUploadDownload(getShopId());
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(list);
        if (LocalUtils.isEmptyAndNull(list)) {
            return BaseResult.okResultNoData();
        }
        hashMap.put("totalSize", new PageInfo(list).getTotal());
        hashMap.put("pageSize", PAGE_SIZE_50);
        return BaseResult.okResult(hashMap);
    }


    @ApiOperation(
            value = "统计下载次数",
            notes = "统计下载次数",
            httpMethod = "GET")
    @GetMapping("/countDownload")
    public BaseResult countDownload(@Valid ParamUploadDownload param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int id = Integer.parseInt(param.getId());
        OpUploadDownload op = opUploadDownloadService.getObjectById(id);
        if (!LocalUtils.isEmptyAndNull(op)) {
            op.setDownloadTimes(op.getDownloadTimes() + 1);
            op.setUpdateTime(new Date());
            op.setUpdateAdmin(getUserId());
            opUploadDownloadService.saveOrUpdateObjectAndReturnId(op);
        }
        return BaseResult.okResult();
    }


}
