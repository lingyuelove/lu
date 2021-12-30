package com.luxuryadmin.apiadmin.shp;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.shp.ParamShpOperateLogQuery;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.vo.shp.VoInitShopOperateLog;
import com.luxuryadmin.vo.shp.VoShpOperateLogRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description 店铺操作日志Contrller
 * @author sanjin
 * @date 2020-09-18 15:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/operatelog", method = RequestMethod.POST)
@Api(tags = {"E008.【店铺】模块"}, description = "/shop/user/operatelog | 操作日志")
public class ShpOperateLogController extends BaseController {

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    /**
     * 获取新增【店铺操作日志】初始化数据
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "获取查询【店铺操作日志】初始化数据",
            notes = "获取查询【店铺操作日志】初始化数据;",
            httpMethod = "POST")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) })
    @RequestMapping("/getInitOperateLogQueryData")
    public BaseResult<VoInitShopOperateLog> getInitOperateLogQueryData() {
        Integer shopId =  getShopId();
        VoInitShopOperateLog vo = shpOperateLogService.getInitOperateLogQueryData(shopId);
        return BaseResult.okResult(vo);
    }

    
    /**
     * 获取店铺操作日志列表
     *
     * @param paramShpOperateLogQuery
     * @return
     */
    @ApiOperation(
            value = "获取店铺操作日志列表",
            notes = "获取店铺操作日志列表;",
            httpMethod = "POST")
    @RequestMapping("/listShpOperateLog")
    @RequiresPermissions("shp:list:operateLog")
    public BaseResult<PageInfo> listShpOperateLog(ParamShpOperateLogQuery paramShpOperateLogQuery) {
        Integer shopId =  getShopId();
        log.error("获取店铺操作日志接口参数：paramShpOperateLogQuery="+ JSON.toJSONString(paramShpOperateLogQuery));
        paramShpOperateLogQuery.setFkShpShopId(shopId);
        PageHelper.startPage(paramShpOperateLogQuery.getPageNum(),PAGE_SIZE_10);
        List<VoShpOperateLogRecord> recordList = shpOperateLogService.listShpOperateLog(paramShpOperateLogQuery);
        PageInfo pageInfo = new PageInfo(recordList);
        return BaseResult.okResult(pageInfo);
    }

}
