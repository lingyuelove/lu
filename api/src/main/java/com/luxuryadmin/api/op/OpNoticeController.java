package com.luxuryadmin.api.op;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.op.OpNotice;
import com.luxuryadmin.service.op.OpNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @date 2020-01-16 20:47:38
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"1.【消息】模块"})
public class OpNoticeController extends BaseController {

    @Autowired
    private OpNoticeService opNoticeService;

    /**
     * 加载店铺消息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载店铺消息",
            notes = "加载店铺消息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页"),
    })
    @RequestRequire
    @PostMapping("/loadShopMessage")
    public BaseResult loadShopMessage(@RequestParam Map<String, String> params) {
        PageHelper.startPage(getPageNum(),PAGE_SIZE_10);
        List<OpNotice> noticeList = opNoticeService.listOpNoticeByShopId(getShopId());
        return LocalUtils.getBaseResult(noticeList);
    }


    /**
     * 加载系统消息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载系统消息",
            notes = "加载系统消息",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页"),
    })
    @RequestRequire
    @PostMapping("/loadSystemMessage")
    public BaseResult loadSystemMessage(@RequestParam Map<String, String> params) {
        PageHelper.startPage(getPageNum(),PAGE_SIZE_10);
        List<OpNotice> noticeList = opNoticeService.listOpNotice();
        return LocalUtils.getBaseResult(noticeList);
    }


    /**
     * 获取消息详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取消息详情",
            notes = "获取消息详情",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "noticeId", value = "id"),
    })
    @RequestRequire
    @GetMapping("/getNoticeDetail")
    public BaseResult getNoticeDetail(@RequestParam Map<String, String> params) {
        String noticeId = params.get("noticeId");
        OpNotice opNotice = opNoticeService.getNoticeById(noticeId);
        return LocalUtils.getBaseResult(opNotice);
    }


    /**
     * 发布消息
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "发布消息(app不需要对接此接口)",
            notes = "发布消息(app不需要对接此接口)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页"),
    })
    @RequestRequire
    @PostMapping("/releaseNotice")
    public BaseResult releaseNotice(@RequestParam Map<String, String> params) {
        PageHelper.startPage(getPageNum(),PAGE_SIZE_10);
        List<OpNotice> noticeList = opNoticeService.listOpNotice();
        return LocalUtils.getBaseResult(noticeList);
    }

}
