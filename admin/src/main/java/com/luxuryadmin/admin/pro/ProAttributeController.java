package com.luxuryadmin.admin.pro;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.enums.sys.EnumTableName;
import com.luxuryadmin.service.pro.ProAttributeService;
import com.luxuryadmin.service.sys.SysEnumService;
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

import java.util.Map;

/**
 * 商品属性控制层
 *
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro", method = RequestMethod.POST)
@Api(tags = {"1.【首页】模块"}, description = "/shop/user/pro |用户【首页】模块相关")
public class ProAttributeController extends BaseController {

    @Autowired
    private ProAttributeService proAttributeService;

    @Autowired
    private SysEnumService sysEnumService;

    /**
     * 重置商品属性模板;(只有店长才有操作权限)
     *
     * @param params 校验; 防止误操作;
     * @return Result
     */
    @RequestMapping("/resetProAttributeTpl")
    @RequestRequire
    @ApiOperation(
            value = "重置商品属性模板;",
            notes = "重置商品属性模板;(只有店长才有操作权限);",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    public BaseResult resetProAttributeTpl(@RequestParam Map<String, String> params) {
        String token = getToken();
        //校验该帐号是否是店长角色;
        boolean isShopkeeper = true;
        //此处校验权限----预留代码

        if (isShopkeeper) {
            sysEnumService.resetSysEnumByType(EnumTableName.PRO_ATTRIBUTE);
            return BaseResult.okResult();
        }
        return BaseResult.okResultNoData();
    }


}
