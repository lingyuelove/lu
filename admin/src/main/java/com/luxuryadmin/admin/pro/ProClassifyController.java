package com.luxuryadmin.admin.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProClassify;
import com.luxuryadmin.enums.sys.EnumTableName;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.sys.SysEnumService;
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
@RequestMapping(value = "/shop/user/pro", method = RequestMethod.POST)
@Api(tags = {"1.【首页】模块"})
public class ProClassifyController extends BaseController {


    @Autowired
    private SysEnumService sysEnumService;

    /**
     * 重置商品分类模板;(只有店长才有操作权限)
     *
     * @param params 前端参数
     * @return Result
     */
    @RequestMapping("/resetProClassifyTpl")
    @RequestRequire
    @ApiOperation(
            value = "重置商品分类模板",
            notes = "重置商品分类模板;(只有店长才有操作权限)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    public BaseResult resetProClassifyTpl(@RequestParam Map<String, String> params) {
        String token = getToken();
        //校验该帐号是否是店长角色;
        boolean isShopkeeper = true;
        //此处校验权限----预留代码

        if (isShopkeeper) {
            sysEnumService.resetSysEnumByType(EnumTableName.PRO_CLASSIFY);
            return BaseResult.okResult();
        }
        return BaseResult.okResultNoData();
    }

}
