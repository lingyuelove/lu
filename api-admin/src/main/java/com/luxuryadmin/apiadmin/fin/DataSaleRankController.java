package com.luxuryadmin.apiadmin.fin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.data.ParamDataSaleRankQuery;
import com.luxuryadmin.service.data.OpDataSaleRankService;
import com.luxuryadmin.vo.data.VoSaleRank;
import com.luxuryadmin.vo.data.VoSaleRankHomePageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @author by sanjin
 * @Classname DataSaleRankController
 * @Description 销售排行榜Controller
 * @Date 2020/7/29 10:00
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/admin/fin")
@Api(tags = {"F002.【数据统计】模块"}, description = "/shop/user/data/saleRank | 【销售排行榜】 ")
public class DataSaleRankController extends BaseController {

    @Autowired
    private OpDataSaleRankService opDataSaleRankService;


    @ApiOperation(
            value = "查询销售排行榜首页数据;",
            notes = "查询销售排行榜首页数据;</BR> 只展示后台添加的消息",
            httpMethod = "POST")
    @RequestMapping(value = "/loadSaleAnalyse", method = RequestMethod.POST)
    @RequiresPermissions("sale:check:all")
    public BaseResult<VoSaleRankHomePageData> loadSaleAnalyse(ParamDataSaleRankQuery paramDataSaleRankQuery) {
        Integer shopId = getShopId();
        setDefaultQueryDateRange(paramDataSaleRankQuery);
        paramDataSaleRankQuery.setShopId(shopId);
        Page page = PageHelper.startPage(1, 200);
        VoSaleRankHomePageData saleData = opDataSaleRankService.loadSaleRankHomePageData(paramDataSaleRankQuery, page);
        try {
            DecimalFormat df = new DecimalFormat(",##0.##");
            saleData.setTotalSaleCount(df.format(new BigDecimal(saleData.getTotalSaleCount())));
            saleData.setTotalSaleAmount(df.format(new BigDecimal(saleData.getTotalSaleAmount())));
            saleData.setTotalGrossProfit(df.format(new BigDecimal(saleData.getTotalGrossProfit())));
            saleData.setGrossProfitRate(df.format(new BigDecimal(saleData.getGrossProfitRate())));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return BaseResult.okResult(saleData);
    }

    /**
     * 设置默认查询时间范围
     *
     * @param paramDataSaleRankQuery
     */
    private void setDefaultQueryDateRange(ParamDataSaleRankQuery paramDataSaleRankQuery) {
        if (null == paramDataSaleRankQuery.getFinishTimeStart() && null == paramDataSaleRankQuery.getFinishTimeEnd()) {
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
    public BaseResult<PageInfo> listSaleRank(ParamDataSaleRankQuery paramDataSaleRankQuery) {
        Integer shopId = getShopId();
        //Integer shopId = 10001;
        setDefaultQueryDateRange(paramDataSaleRankQuery);
        paramDataSaleRankQuery.setShopId(shopId);
        Page page = PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoSaleRank> saleRankList = opDataSaleRankService.listSaleRank(paramDataSaleRankQuery, page);
        if (LocalUtils.isEmptyAndNull(saleRankList)) {
            return BaseResult.okResultNoData();
        }
        PageInfo pageInfo = new PageInfo(saleRankList);
        pageInfo.setList(saleRankList);
        return BaseResult.okResult(pageInfo);
    }


}
