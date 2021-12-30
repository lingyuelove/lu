package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.service.ord.OrdOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * @PackgeName: com.luxuryadmin.api.shp
 * @ClassName: ShpOrderDailyCountController
 * @Author: ZhangSai
 * Date: 2021/6/30 14:00
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/orderDaily", method = RequestMethod.POST)
@Api(tags = {"Z006月报日志"}, description = "/shop/user/orderDaily | 操作日志")
public class ShpOrderDailyCountController extends BaseController {
    @Autowired
    private OrdOrderService ordOrderService;
    @ApiOperation(
            value = "月报任务接口",
            notes = "月报任务接口;",
            httpMethod = "POST")
    @ApiImplicitParams({
    })
    @RequestMapping("/pushDailyCountShopOrderMsgForMonth")
    public BaseResult pushDailyCountShopOrderMsgForMonth(@Valid ParamToken paramToken, BindingResult result){
        servicesUtil.validControllerParam(result);
        Date date =new Date();
        ordOrderService.pushDailyCountShopOrderMsgForMonth(date);
        return BaseResult.okResult();
    }
}
