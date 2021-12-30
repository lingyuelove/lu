package com.luxuryadmin.apiadmin.fin;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.fin.FinSaleAnalysis;
import com.luxuryadmin.service.fin.FinSaleAnalysisService;
import com.luxuryadmin.service.fin.FinSaleTopService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.vo.fin.VoSaleTop;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售数据分析---控制层
 *
 * @author monkey king
 * @date 2020-01-15 14:53:27
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/fin", method = RequestMethod.POST)
@Api(tags = "F001.【销售】模块", description = "/shop/admin/fin | 销售数据模块")
public class FinSaleAnalysisController extends BaseController {


    @Autowired
    private FinSaleAnalysisService finSaleAnalysisService;

    @Autowired
    private FinSaleTopService finSaleTopService;

    @Autowired
    private OrdOrderService ordOrderService;


    /**
     * 加载销售数据分析;默认查询当天数据
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载销售数据分析,默认查询当天数据",
            notes = "加载销售数据分析,默认查询当天数据",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "startDate", value = "开始日期,格式yyyy-MM-dd"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "endDate", value = "结束日期,格式yyyy-MM-dd"),
    })
    @RequestRequire
    @RequestMapping("/loadSaleData")
    public BaseResult loadSaleData(@RequestParam Map<String, String> params) {
        Map<String, Date> dateMap = DateUtil.formatQueryDate(params.get("startDate"), params.get("endDate"), true);
        Date startDate = dateMap.get("startDate");
        Date endDate = dateMap.get("endDate");
        FinSaleAnalysis analysis = finSaleAnalysisService.listFinSaleAnalyses(getShopId(), startDate, endDate);
        return LocalUtils.getBaseResult(analysis);
    }


    /**
     * 销售人员排行榜(默认显示30条)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "销售人员排行榜(默认显示30条)",
            notes = "销售人员排行榜(默认显示30条)",
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
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "sortKey", value = "以下3个选其一: saleOrderCount | salePrice | grossProfit"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "sort", value = "asc:升序; desc:降序"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "startDate", value = "开始日期,格式yyyy-MM-dd"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "endDate", value = "结束日期,格式yyyy-MM-dd"),


    })
    @RequestRequire
    @RequestMapping("/saleTop")
    public BaseResult saleTop(@RequestParam Map<String, String> params) {
        String sortKey = params.get("sortKey");
        String sort = params.get("sort");
        Map<String, Date> dateMap = DateUtil.formatQueryDate(params.get("startDate"), params.get("endDate"), true);
        Date startDate = dateMap.get("startDate");
        Date endDate = dateMap.get("endDate");
        PageHelper.startPage(getPageNum(), PAGE_SIZE_30);
        List<VoSaleTop> saleTopList = finSaleTopService.listSaleTopByShopId(
                getShopId(), startDate, endDate, validSortKey(sortKey), validSort(sort));

        return LocalUtils.getBaseResult(saleTopList);
    }


    /**
     * 销售人员的具体销售详情(订单默认显示30条)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "销售人员的具体销售详情(订单默认显示30条)",
            notes = "销售人员的具体销售详情(订单默认显示30条)",
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
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "userId", value = "用户Id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "startDate", value = "开始日期,格式yyyy-MM-dd"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "endDate", value = "结束日期,格式yyyy-MM-dd"),
    })
    @RequestRequire
    @RequestMapping("/saleDetail")
    public BaseResult saleDetail(@RequestParam Map<String, String> params) {
        String userIdStr = params.get("userId");
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            return BaseResult.defaultErrorWithMsg("参数格式错误");
        }
        Map<String, Date> dateMap = DateUtil.formatQueryDate(params.get("startDate"), params.get("endDate"), true);
        Date startDate = dateMap.get("startDate");
        Date endDate = dateMap.get("endDate");

        int shopId = getShopId();
        List<VoSaleTop> saleTopList = finSaleTopService.listSaleTopByShopId(shopId, userId, startDate, endDate);
        if (LocalUtils.isEmptyAndNull(saleTopList)) {
            return BaseResult.defaultErrorWithMsg("查无此人: 0x" + shopId + ":" + userId);
        }
        VoSaleTop saleTop = saleTopList.get(0);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(saleTop);

        Map<String, Object> queryMap = new HashMap<>(16);
        queryMap.put("shopId", shopId);
        queryMap.put("userId", userId);
        queryMap.put("state", "20");

        PageHelper.startPage(getPageNum(), PAGE_SIZE_30);
        //List<VoOrderLoad> orderList = ordOrderService.loadOrderByCondition(queryMap);
        //店长才有查看成本价的权限;待优化
        //formatOrderPrice(orderList, false);
       // hashMap.put("orderList", orderList);
        return BaseResult.okResult(hashMap);
    }


    /**
     * 校验排序列名
     *
     * @param sortKey
     * @return
     */
    private String validSortKey(String sortKey) {
        switch ("" + sortKey) {
            case "saleOrderCount":
            case "salePrice":
            case "grossProfit":
                return sortKey;
            default:
                return "grossProfit";
        }
    }

    /**
     * 校验排序参数
     *
     * @param sort
     * @return
     */
    private String validSort(String sort) {
        String asc = "asc";
        return asc.equals(sort) ? asc : "desc";
    }

}