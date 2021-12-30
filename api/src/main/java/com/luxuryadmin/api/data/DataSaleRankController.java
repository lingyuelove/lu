package com.luxuryadmin.api.data;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.service.data.OpDataSaleRankService;
import com.luxuryadmin.vo.data.VoDataProfit;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Classname DataSaleRankController
 * @Description 销售排行榜Controller
 * @Date 2020/7/29 10:00
 * @Created by sanjin
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/data/saleRank")
@Api(tags = {"1.【数据统计】模块"}, description = "/shop/user/data/saleRank | 【销售排行榜】 ")
public class DataSaleRankController extends BaseController {

    @Autowired
    private OpDataSaleRankService opDataSaleRankService;


    @ApiOperation(
            value = "查询销售排行榜首页数据;",
            notes = "查询销售排行榜首页数据;</BR> 只展示后台添加的消息",
            httpMethod = "POST")
    @RequestMapping(value = "/loadSaleRankHomePageData", method = RequestMethod.POST)
    @RequiresPermissions("sale:check:all")
    public BaseResult<VoSaleRankHomePageData> loadSaleRankHomePageData(ParamDataSaleRankQuery paramDataSaleRankQuery) {
       Integer shopId = getShopId();
       setDefaultQueryDateRange(paramDataSaleRankQuery);
       paramDataSaleRankQuery.setShopId(shopId);
       Page page = PageHelper.startPage(1,200);

       VoSaleRankHomePageData voSaleRankHomePageData = opDataSaleRankService.loadSaleRankHomePageData(paramDataSaleRankQuery,page);
       return  BaseResult.okResult(voSaleRankHomePageData);
    }

    /**
     * 设置默认查询时间范围
     * @param paramDataSaleRankQuery
     */
    private void setDefaultQueryDateRange(ParamDataSaleRankQuery paramDataSaleRankQuery) {
        if(null == paramDataSaleRankQuery.getFinishTimeStart() && null == paramDataSaleRankQuery.getFinishTimeEnd()){
            Date startDate = DateUtil.getFirstDayOfCurrentMonth();
            Date endDate = DateUtil.getLastDayOfCurrentMonth();

            paramDataSaleRankQuery.setFinishTimeStart(startDate);
            paramDataSaleRankQuery.setFinishTimeEnd(endDate);
        }
    }

    @ApiOperation(
            value = "分页查询【销售排行榜】记录;",
            notes = "分页查询【销售排行榜】记录;",
            httpMethod = "POST")
    @RequestMapping(value = "/listSaleRank", method = RequestMethod.POST)
    @RequiresPermissions("sale:check:all")
    public BaseResult<List<VoSaleRank>> listSaleRank(ParamDataSaleRankQuery paramDataSaleRankQuery) {
        Integer shopId = getShopId();
        //Integer shopId = 10001;
        setDefaultQueryDateRange(paramDataSaleRankQuery);
        paramDataSaleRankQuery.setShopId(shopId);
        Page page = PageHelper.startPage(getPageNum(),PAGE_SIZE_10);
        List<VoSaleRank> saleRankList = opDataSaleRankService.listSaleRank(paramDataSaleRankQuery,page);
        return  BaseResult.okResult(saleRankList);
    }

    @ApiOperation(
            value = "获取权限",
            notes = "获取权限;",
            httpMethod = "POST")
    @RequestMapping(value = "/getAllDataProfit", method = RequestMethod.POST)
    public BaseResult getAllDataProfit() {
        VoDataProfit dataProfit = new VoDataProfit();
        String saleProfit = hasPermToPageWithCurrentUser(ConstantPermission.CHK_SALE_ALL);
        dataProfit.setSaleProfit(saleProfit);
        dataProfit.setRecycleProfit(saleProfit);
        dataProfit.setMoneyProfit(saleProfit);
        return  BaseResult.okResult(dataProfit);
    }
}
