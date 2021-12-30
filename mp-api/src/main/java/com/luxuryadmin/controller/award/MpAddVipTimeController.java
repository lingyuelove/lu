package com.luxuryadmin.controller.award;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.common.ParamBasic;
import com.luxuryadmin.service.MpAddVipTimeService;
import com.luxuryadmin.vo.award.VOAwardDay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * 追加时长表 controller
 *
 * @author zhangsai
 * @Date 2021-11-24 13:56:00
 */
@Slf4j
@RestController
@RequestMapping(value = "/award")
@Api(tags = "MP.奖励接口", description = "奖励接口 ")
public class MpAddVipTimeController extends BaseController {

    @Autowired
    private MpAddVipTimeService mpAddVipTimeService;

    @GetMapping("/getAwardRecord")
    @ApiOperation(value = "获取奖励记录", httpMethod = "GET")
    public BaseResult getAwardRecord(@Valid ParamBasic param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(getPageNum(), getPageSize());
        VOAwardDay vo = mpAddVipTimeService.getAwardRecord(param);
        return BaseResult.okResult(vo);
    }
}
