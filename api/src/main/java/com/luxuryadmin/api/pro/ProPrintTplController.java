package com.luxuryadmin.api.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.entity.pro.ProPrintTpl;
import com.luxuryadmin.enums.op.EnumOpMessageSubType;
import com.luxuryadmin.enums.pro.EnumProPrintTpl;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.ParamProDetail;
import com.luxuryadmin.param.pro.ParamProPrintTpl;
import com.luxuryadmin.service.pro.ProPrintTplService;
import com.luxuryadmin.vo.op.VoMessageSubType;
import com.luxuryadmin.vo.pro.ParamProPrintTplAdd;
import com.luxuryadmin.vo.pro.VoPrintTplShowForApp;
import com.luxuryadmin.vo.pro.VoProPrintTpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @desc 商品打印标签模板Controller
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/print", method = RequestMethod.POST)
@Api(tags = {"C005.【打印模板】模块 --2.6.2"}, description = "/shop/user/pro/print |商品打印 --2.6.2")
public class ProPrintTplController extends BaseController {

    @Autowired
    private ProPrintTplService proPrintTplService;

    /**
     * 添加财务流水
     *
     * @param paramProPrintTplAdd 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "添加【打印模板】;",
            notes = "添加【打印模板】",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @RequestRequire
    @PostMapping("/addProPrintTpl")
    //@RequiresPermissions("pro:print:addTpl")
    public BaseResult addProPrintTpl(@Valid ParamProPrintTplAdd paramProPrintTplAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        ProPrintTpl proPrintTpl = proPrintTplService.addProPrintTpl(shopId,userId,paramProPrintTplAdd);
        return BaseResult.okResult(proPrintTpl.getId());
    }

    /**
     * 根据ID查询【打印模板】
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "根据ID查询【打印模板】",
            notes = "根据ID查询【打印模板】;",
            httpMethod = "POST")
    @RequestMapping("/loadProPrintTplById")
    //@RequiresPermissions("pro:print:getTplById")
    @ApiImplicitParams({
                    @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
                    @ApiImplicitParam(paramType = "query", required = true, dataType = "Integer", name = "proPrintTplId", value = "模板ID")
            })
    public BaseResult<VoProPrintTpl> loadProPrintTplById(Integer proPrintTplId,
                                                         HttpServletRequest request) {
        if(null == proPrintTplId){
            throw new MyException("获取打印模板时ID为空");
        }
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        VoProPrintTpl voProPrintTpl = proPrintTplService.loadProPrintTplById(shopId,proPrintTplId);

        return BaseResult.okResult(voProPrintTpl);
    }

    /**
     * 查询【打印模板】列表
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "查询【打印模板】列表(2.6.2之前使用)",
            notes = "查询【打印模板】列表;",
            httpMethod = "POST")
    @RequestMapping("/loadProPrintTplList")
    public BaseResult< List<VoProPrintTpl>> loadProPrintTplList(@Valid ParamProPrintTpl proPrintTpl, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        proPrintTpl.setShopId(shopId);
        List<VoProPrintTpl> voProPrintTplList = proPrintTplService.loadProPrintTplList( proPrintTpl);

        return BaseResult.okResult(voProPrintTplList);
    }

    /**
     * 查询【打印模板】列表
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "查询【打印模板】列表（2.6.2之后使用） --2.6.2",
            notes = "查询【打印模板】列表;--2.6.2",
            httpMethod = "POST")
    @RequestMapping("/loadPrintTplShowForApp")
    public BaseResult<VoPrintTplShowForApp> loadPrintTplShowForApp(@Valid ParamProPrintTpl proPrintTpl, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        proPrintTpl.setShopId(shopId);
        List<VoProPrintTpl> voProPrintTplList = proPrintTplService.loadProPrintTplList(proPrintTpl);
        VoPrintTplShowForApp voPrintTplShowForApp = new VoPrintTplShowForApp();
        voPrintTplShowForApp.setPrintTplList(voProPrintTplList);
        //所有子类型列表
        List<VoMessageSubType> allProPrintTplCnName = EnumProPrintTpl.getAllProPrintTplCnName();
        voPrintTplShowForApp.setPrintTplCnName(allProPrintTplCnName);
        return BaseResult.okResult(voPrintTplShowForApp);
    }

    /**
     * 初始化打印参数
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "初始化打印参数",
            notes = "初始化打印参数;",
            httpMethod = "POST")
    @RequestMapping("/getEnumPrintTplCnName")
    public BaseResult<List<VoMessageSubType>> getEnumPrintTplCnName(@Valid ParamToken token , BindingResult result) {
        servicesUtil.validControllerParam(result);
        //所有子类型列表
        List<VoMessageSubType> allProPrintTplCnName = EnumProPrintTpl.getAllProPrintTplCnName();
        return BaseResult.okResult(allProPrintTplCnName);
    }
    /**
     * 修改【打印模板】
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "修改【打印模板】",
            notes = "修改【打印模板】;",
            httpMethod = "POST")
    @RequestMapping("/updateProPrintTpl")
    //@RequiresPermissions("pro:print:updateTpl")
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) })
    public BaseResult updateProPrintTpl(@Valid ParamProPrintTplAdd paramProPrintTplUpdate,
                                       BindingResult result,
                                       HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        if(null == paramProPrintTplUpdate.getId()){
            throw new MyException("修改打印模板时ID为空");
        }
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        Integer updateResult = proPrintTplService.updateProPrintTpl(shopId,userId,paramProPrintTplUpdate);
        return BaseResult.okResult(updateResult);
    }

    /**
     * 删除【打印模板】
     *
     * @param
     * @return Result
     */
    @ApiOperation(
            value = "删除【打印模板】",
            notes = "删除【打印模板】;",
            httpMethod = "POST")
    @RequestMapping("/delProPrintTpl")
    //@RequiresPermissions("pro:print:deleteTpl")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", value = "token标记", required = true) ,
            @ApiImplicitParam(paramType = "query", required = true, dataType = "Integer", name = "proPrintTplId", value = "模板ID")
    })
    public BaseResult delProPrintTpl(Integer proPrintTplId,
                                                         HttpServletRequest request) {
        if(null == proPrintTplId){
            throw new MyException("删除打印模板时ID为空");
        }
        Integer shopId =  getShopId();
        Integer userId = getUserId();
        Integer result = proPrintTplService.deleteProPrintTplById(shopId,proPrintTplId);

        return BaseResult.okResult(result);
    }



}
