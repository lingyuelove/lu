package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProPublic;
import com.luxuryadmin.param.pro.ParamProPublic;
import com.luxuryadmin.service.pro.ProClassifySubService;
import com.luxuryadmin.service.pro.ProPublicService;
import com.luxuryadmin.vo.pro.VoProClassifySub;
import com.luxuryadmin.vo.pro.VoProPublic;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公价查询控制层
 *
 * @author monkey king
 * @Date 2021-06-22 23:32:05
 */
@Slf4j
@RestController
@RequestMapping(value = "/tool")
@Api(tags = {"P001.【基础】模块"})
public class ProPublicController extends BaseController {


    @Autowired
    private ProPublicService proPublicService;


    @Autowired
    private ProClassifySubService proClassifySubService;

    /**
     * 初始化公价查询系统参数
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化公价查询系统参数",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @PostMapping("/initPublicProduct")
    public BaseResult initPublicProduct(@RequestParam Map<String, String> params,
                                        @Valid ParamProPublic queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoProClassifySub> classifySubList = proClassifySubService.listAllProClassifySub(queryParam.getClassifyCode());
        return LocalUtils.getBaseResult(classifySubList);
    }

    /**
     * 获取公价系统商品
     *
     * @return Result
     */
    @ApiOperation(
            value = "获取公价系统商品",
            httpMethod = "POST")
    @PostMapping("/queryPublicProduct")
    public BaseResult queryPublicProduct(@Valid ParamProPublic queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String queryKey = queryParam.getQueryKey();
        String name = queryParam.getName();

        //如果有查询名称参数
        if (!LocalUtils.isEmptyAndNull(queryKey)) {
            queryKey = queryKey.trim();
            queryKey = queryKey.replaceAll("\\s+", ".*");
        }
        //queryKey = LocalUtils.isEmptyAndNull(queryKey) ? null : "%" + queryKey + "%";
        queryParam.setQueryKey(queryKey);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProPublic> proPublicList = proPublicService.listProPublic(queryParam);
        if (!LocalUtils.isEmptyAndNull(proPublicList)) {
            for (VoProPublic proPublic : proPublicList) {
                String smallImg = proPublic.getSmallImg();
                try {
                    String description = proPublic.getDescription();
                    if (!LocalUtils.isEmptyAndNull(description)) {
                        proPublic.setName(description + "/" + proPublic.getName());
                    }
                    //包含public这个关键字证明链接已经更新过为图片地址
                    if (LocalUtils.isEmptyAndNull(smallImg)) {
                        continue;
                    }
                    proPublic.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
                    proPublic.setBigImg(servicesUtil.formatImgUrl(smallImg));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return LocalUtils.getBaseResult(proPublicList);
    }


    /**
     * 根据品牌查询型号或系列
     *
     * @return Result
     */
    @ApiOperation(
            value = "根据品牌查询型号或系列",
            httpMethod = "GET")
    @GetMapping("/querySerialNo")
    public BaseResult querySerialNo(@Valid ParamProPublic queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String name = queryParam.getName();
        if (LocalUtils.isEmptyAndNull(name)) {
            return BaseResult.defaultErrorWithMsg("请选择品牌参数!");
        }
        List<VoProPublic> serialNoList = proPublicService.querySerialNo(name);
        List<VoProPublic> typeNoList = proPublicService.queryTypeNo(name, null);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("serialNoList", serialNoList);
        hashMap.put("typeNoList", typeNoList);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 根据型号查询系列
     *
     * @return Result
     */
    @ApiOperation(
            value = "根据型号查询系列",
            httpMethod = "GET")
    @GetMapping("/queryTypeNo")
    public BaseResult queryTypeNo(@Valid ParamProPublic queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String name = queryParam.getName();
        String serialNo = queryParam.getSerialNo();
        if (LocalUtils.isEmptyAndNull(name) || LocalUtils.isEmptyAndNull(serialNo)) {
            return BaseResult.defaultErrorWithMsg("请填写查询参数!");
        }
        List<VoProPublic> typeNoList = proPublicService.queryTypeNo(name, serialNo);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("typeNoList", typeNoList);
        return BaseResult.okResult(hashMap);
    }


}
