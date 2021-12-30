package com.luxuryadmin.admin.sys;

import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.service.shp.ShpShopNumberService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpUserNumberService;
import com.luxuryadmin.service.sys.SysConfigService;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/sys")
@Api(tags = {"5.【系统管理】模块", "5.【系统管理】模块"}, description = "/sys | 平台初始化和参数配置相关 ")
public class SysConfigController extends BaseController {
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private ShpUserNumberService shpUserNumberService;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @Autowired
    private ShpShopService shpShopService;

    /**
     * 获取App系统配置;
     * 配置加密开关对此接口不影响;该接口返回的数据;是经过AES对称加密;<br/>
     * 接口参数map可分区平台是android还是iOS
     *
     * @param params
     * @return Result
     */
    @RequestMapping(value = "/getAppSysConfig", method = RequestMethod.GET)
    @ApiOperation(
            value = "1.获取App系统配置",
            notes = "配置加密开关对此接口不影响;该接口返回的数据;是经过AES对称加密;<br/>",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
    })
    public BaseResult getAppSysConfig(@RequestParam Map<String, String> params) {
        return BaseResult.okResult(sysConfigService.initSysConfig());
    }

    /**
     * 生成用户编码
     *
     * @param startNumber
     * @param endNumber
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/generateUserNumber", method = RequestMethod.GET)
    public BaseResult generateUserNumber(@RequestParam String startNumber, @RequestParam String endNumber) {
        int saveCount;
        int startNumberInt = Integer.parseInt(startNumber);
        int endNumberInt = Integer.parseInt(endNumber);
        if (endNumberInt < startNumberInt) {
            return BaseResult.errorResult("必须 endNum ≥ startNum");
        }
        try {
            saveCount = shpUserNumberService.generateUserRandomNumber(startNumberInt, endNumberInt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(saveCount);
    }

    /**
     * 生成店铺编码
     *
     * @param startNumber
     * @param endNumber
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/generateShopNumber", method = RequestMethod.GET)
    public BaseResult generateShopNumber(@RequestParam String startNumber, @RequestParam String endNumber) {
        int saveCount = 0;
        int startNumberInt = Integer.parseInt(startNumber);
        int endNumberInt = Integer.parseInt(endNumber);
        if (endNumberInt < startNumberInt) {
            return BaseResult.errorResult("必须 endNum ≥ startNum");
        }
        try {
            saveCount = shpShopNumberService.generateShopRandomNumber(startNumberInt, endNumberInt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.errorResult(e.getMessage());
        }
        return BaseResult.okResult(saveCount);
    }

    /**
     * 临时开通会员渠道
     *
     * @param token
     * @return Result
     */
    @ApiOperation(
            value = "临时开通会员渠道",
            notes = "临时开通会员渠道",
            httpMethod = "GET")
    @RequestMapping(value = "/updateShopMember", method = RequestMethod.GET)
    public BaseResult updateShopMember(@RequestParam String token, String shopNumber, String member) {
        validPerm(token);
        Integer shopId = shpShopService.getShopIdByShopNumber(shopNumber);
        if (LocalUtils.isEmptyAndNull(shopId)) {
            return BaseResult.errorResult("shop not exists");
        }
        if (LocalUtils.isEmptyAndNull(member) || !"yes,no".contains(member)) {
            return BaseResult.errorResult("[member]param error");
        }
        return BaseResult.okResult(shpShopService.updateShopMember(shopId, member));
    }

    private void validPerm(String value) {
        int userId = getUserId();
        if (userId != 10000 && !value.equals("15112304365")) {
            throw new MyException("无操作权限");
        }
    }


}
