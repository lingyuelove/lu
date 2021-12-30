package com.luxuryadmin.api.share;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpServiceRecordCost;
import com.luxuryadmin.param.ord.ParamShareReceiptQuery;
import com.luxuryadmin.param.ord.ParamShareServiceQuery;
import com.luxuryadmin.service.ord.OrdShareReceiptService;
import com.luxuryadmin.service.shp.ShpServiceService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.ord.VoShareReceipt;
import com.luxuryadmin.vo.shp.VoServiceRecordCost;
import com.luxuryadmin.vo.shp.VoShpServiceRecordDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author sanjin145
 * @date 2020-09-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/share/service", method = RequestMethod.GET)
@Api(tags = {"Z003.【分享店铺服务】模块"}, description = "/shop/share/service | 外部分享,不需要登录")
public class ShareServiceController extends BaseController {


    @Autowired
    private ShpServiceService shpServiceService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private OrdShareReceiptService ordShareReceiptService;

    /**
     * 根据ID获取店铺服务详情
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "根据ID获取店铺服务详情",
            notes = "根据ID获取店铺服务详情;",
            httpMethod = "GET")
    @RequestMapping("/getShpServiceById")
    public BaseResult<VoShpServiceRecordDetail> getShpServiceById(Map<String, String> params,
                                                                  @Valid ParamShareServiceQuery shareService, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ParamShareReceiptQuery shareQuery = new ParamShareReceiptQuery();
        BeanUtils.copyProperties(shareService,shareQuery);
        VoShareReceipt voShareReceipt = ordShareReceiptService.getOrderNoByShareBatch(shareQuery);
        if (LocalUtils.isEmptyAndNull(voShareReceipt)) {
            return BaseResult.defaultErrorWithMsg("服务凭证不存在!");
        }
        Integer shopId = shpShopService.getShopIdByShopNumber(shareService.getShopNumber());
        VoShpServiceRecordDetail recordDetail = shpServiceService.getShpServiceById(shopId,Integer.parseInt(voShareReceipt.getOrderNo()));
        if (LocalUtils.isEmptyAndNull(recordDetail)){
            return BaseResult.okResult(recordDetail);
        }
        if ("context".equals(voShareReceipt.getShowType())){
            List<VoServiceRecordCost> shpServiceRecordCosts =recordDetail.getRecordCosts();
            if (!LocalUtils.isEmptyAndNull(shpServiceRecordCosts)){
                shpServiceRecordCosts.forEach(shpServiceRecordCost -> {
                    shpServiceRecordCost.setServiceCost(null);
                });
                recordDetail.setRecordCosts(shpServiceRecordCosts);
                recordDetail.setTotalCost(null);
            }

        }
        if ("no".equals(voShareReceipt.getShowType())){
            recordDetail.setRecordCosts(null);
            recordDetail.setTotalCost(null);

        }
        recordDetail.setShowType(voShareReceipt.getShowType());
        return BaseResult.okResult(recordDetail);
    }
}