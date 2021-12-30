package com.luxuryadmin.admin.stat;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.ServicesUtil;
import com.luxuryadmin.param.stat.ParamRegDataQuery;
import com.luxuryadmin.service.stat.StatDailyCountService;
import com.luxuryadmin.vo.stat.VoStatRegData;
import com.luxuryadmin.vo.stat.VoStateDataTotal;
import com.luxuryadmin.vo.stat.VoStateProdSellClassify;
import com.luxuryadmin.vo.stat.VoStateShopRank;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sanjin145
 * @Date 2020-12-16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/stat")
@Api(tags = {"0.【数据统计】模块"}, description = "/shop/user/stat |后台运营数据统计")
public class StatController {

    @Autowired
    private StatDailyCountService statDailyCountService;

    @Autowired
    private ServicesUtil servicesUtil;

    /**
     * 查询【运营统计数据】-【顶部汇总】
     *
     * @param params 校验; 防止误操作;
     * @return Result
     */
    @RequestMapping("/loadTotal")
    @RequestRequire
    @ApiOperation(
            value = "查询【运营统计数据】-【顶部汇总】;",
            notes = "查询【运营统计数据】-【顶部汇总】;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    public BaseResult<VoStateDataTotal> loadTotal(@RequestParam Map<String, String> params) {
        //默认查询统计日期为今天的数据
        VoStateDataTotal vo = statDailyCountService.loadStatDataTotal(new Date());
        return BaseResult.okResult(vo);
    }

    /**
     * 查询【运营统计数据】-【店铺排名】
     *
     * @param rankField 排名字段
     * @return Result
     */
    @RequestMapping("/loadShopRank")
    @RequestRequire
    @ApiOperation(
            value = "查询【运营统计数据】-【店铺排行】;",
            notes = "查询【运营统计数据】-【店铺排行】;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "rankField", value = "排序字段,prodNumUpload|上传商品数量</br>" +
                            "prodNumOnsale|在售商品数量</br>" +
                            "totalSaleNum|总销售量</br>" +
                            "totalSaleAmount|总销售额</br>" +
                            "shopStaffNum|店铺员工数量</br>" +
                            "shopLeaguerNum|店铺友商数量",
                    allowableValues = "prodNumUpload,prodNumOnsale,totalSaleNum," +
                            "totalSaleAmount,shopStaffNum,shopLeaguerNum")
    })
    public BaseResult<List<VoStateShopRank>> loadShopRank(@RequestParam String rankField) {
        List<VoStateShopRank> voList = statDailyCountService.loadStatDataShopRank(rankField);
        return BaseResult.okResult(voList);
    }

    /**
     * 查询【运营统计数据】-【商品销售分类】
     *
     * @return Result
     */
    @RequestMapping("/loadProdSellClassify")
    @RequestRequire
    @ApiOperation(
            value = "查询【运营统计数据】-【商品销售分类】;",
            notes = "查询【运营统计数据】-【商品销售分类】;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "showField", value = "展示字段,totalSaleNum|总销售量</br>" +
                    "totalSaleAmount|总销售额</br>",
                    allowableValues = "totalSaleNum,totalSaleAmount")
    })
    public BaseResult<List<VoStateProdSellClassify>> loadProdSellClassify(@RequestParam String showField) {
        List<VoStateProdSellClassify> voList = statDailyCountService.loadProdSellClassify(showField);
        return BaseResult.okResult(voList);
    }

    /**
     * 查询【运营统计数据】-【注册量数据】
     *
     * @return Result
     */
    @RequestMapping("/loadRegData")
    @RequestRequire
    @ApiOperation(
            value = "查询【运营统计数据】-【注册量数据】;",
            notes = "查询【运营统计数据】-【注册量数据】;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    public BaseResult<VoStatRegData> loadRegData(ParamRegDataQuery paramRegDataQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoStatRegData vo = statDailyCountService.loadRegData(paramRegDataQuery);
        return BaseResult.okResult(vo);
    }


}
