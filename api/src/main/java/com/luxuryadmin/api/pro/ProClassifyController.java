package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.vo.pro.VoProClassify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"E002.【分类】模块"}, description = "/shop/user |店铺分类相关")
public class ProClassifyController extends BaseController {

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    /**
     * 加载店铺分类--编辑页面;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载店铺分类编辑--编辑;",
            notes = "可根据状态来查询state;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/listProClassifyEdit")
    public BaseResult listProClassifyEdit(@RequestParam Map<String, String> params) {
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(getShopId(), null);
        if (LocalUtils.isEmptyAndNull(voProClassifyList)) {
            return BaseResult.okResultNoData();
        }

        List<VoProClassify> usedList = new ArrayList<>();
        //使用中
        for (VoProClassify used : voProClassifyList) {
            if (1 == used.getState()) {
                usedList.add(used);
            }
        }
        //未使用
        List<VoProClassify> unUsedList = new ArrayList<>();
        for (VoProClassify unUsed : voProClassifyList) {
            if (0 == unUsed.getState()) {
                unUsedList.add(unUsed);
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("usedList", usedList);
        hashMap.put("unUsedList", unUsedList);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 修改店铺分类
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改店铺分类",
            notes = "修改店铺分类",
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
                    name = "used", value = "使用中的分类;把code用分号拼接传上来"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "unUsed", value = "未使用的分类;把code用分号拼接传上来"),
    })
    @RequestRequire
    @RequestMapping("/updateClassify")
    public BaseResult updateClassify(@RequestParam Map<String, String> params, HttpServletRequest request) {
        String used = params.get("used");
        String unUsed = params.get("unUsed");
        if (LocalUtils.isEmptyAndNull(used)) {
            return BaseResult.defaultErrorWithMsg("参数错误");
        }

        String operateLogContent = "";
//        //获取变动的分类
//        List<VoProClassify> usedListDb = proClassifyService.listProClassifyByState(getShopId(),"used");
//        List<VoProClassify> unUsedListDb = proClassifyService.listProClassifyByState(getShopId(),"unUsed");
//        //记录App传递过来的分类
//        List<VoProClassify> usedListApp = new ArrayList<>();
//        List<VoProClassify> unUsedListApp = new ArrayList<>();

        String[] usedArray = used.split(";");
        List<ProClassify> list = new ArrayList<>();
        int sort = 1;
        //使用中
        for (String usedClassify : usedArray) {
            ProClassify proClassify = new ProClassify();
            proClassify.setCode(usedClassify);
            proClassify.setState(1);
            proClassify.setSort(sort);
            sort++;
            list.add(proClassify);

            //设置已使用类型
//            VoProClassify voProClassify = new VoProClassify();
//            voProClassify.setCode(usedClassify);
//            usedListApp.add(voProClassify);
        }
        if (!LocalUtils.isEmptyAndNull(unUsed)) {
            String[] unUsedArray = unUsed.split(";");
            //未使用
            for (String unUsedClassify : unUsedArray) {
                ProClassify proClassify = new ProClassify();
                proClassify.setCode(unUsedClassify);
                proClassify.setState(0);
                proClassify.setSort(sort);
                sort++;
                list.add(proClassify);

                //设置未使用类型
//                VoProClassify voProClassify = new VoProClassify();
//                voProClassify.setCode(unUsedClassify);
//                unUsedListApp.add(voProClassify);
            }
        }

        int row = proClassifyService.updateProClassify(list, getShopId());
        return BaseResult.okResult(row);
    }

    /**
     * 获取设置分类操作日志内容
     * @param state
     * @return
     */
    private String getClassifyOpeLog(String state,List<VoProClassify> subSetA,List<VoProClassify> subSetB) {
        String operateLog = "";

        String prefix = "";
        if("used".equals(state)){
            prefix = "增加分类";
        }else if("unused".equals(state)){
            prefix = "删除分类";
        }

        return operateLog;
    }

}
