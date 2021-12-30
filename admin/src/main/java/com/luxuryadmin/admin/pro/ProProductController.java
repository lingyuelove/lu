package com.luxuryadmin.admin.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.param.pro.ParamProProduct;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.vo.pro.VoProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author qwy
 * @Classname ProProductController
 * @Description TODO
 * @Date 2020/6/28 13:48
 * @Created by Administrator
 */
@Slf4j
@RestController
@RequestMapping(value = "/pro")
@Api(tags = {"4.【商品管理】模块"}, description = "/shp/user | 商品列表 ")
public class ProProductController extends BaseController {

    @Autowired
    private ProProductService proProductService;

    @RequestMapping(value = "/listProProduct", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询商品列表;",
            notes = "分页查询商品列表;",
            httpMethod = "GET")
    @RequiresPerm(value =  {"pro:list","pro:pledge:list"})
    public BaseResult listProProduct(@Valid ParamProProduct paramProProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);

        long st = System.currentTimeMillis();
        //独立编码不支持模糊查询
        paramProProduct.setUniqueCode(paramProProduct.getName());
        //如果有查询名称参数
        String name = paramProProduct.getName();
        if (!LocalUtils.isEmptyAndNull(name)) {
            name = name.trim();
            name = name.replaceAll("\\s+", ".*");
            paramProProduct.setName(name);
        }


        PageHelper.startPage(paramProProduct.getPageNum(), paramProProduct.getPageSize());
        //PageHelper.count(null);
        List<VoProduct> voProducts = proProductService.queryShpUserRelList(paramProProduct);
        long et = System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo(voProducts);
        log.info("========listProProduct耗时: {}", et - st);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/getProProductInfo", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询商品详细信息;",
            notes = "查询商品详细信息;",
            httpMethod = "GET")
    public BaseResult getProProductInfo(@RequestParam String id){
        VoProduct info = proProductService.getProProductInfo(id);
        return LocalUtils.getBaseResult(info);
    }

    @RequestMapping(value = "/putProProduct", method = RequestMethod.GET)
    @ApiOperation(
            value = "上架/下架店铺;",
            notes = "上架/下架店铺;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "修改的商品的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "下架-0,上架-1"),
    })
    public BaseResult putProProduct(@RequestParam String id,String state) {
        ProProduct proProduct = proProductService.getProProductById(LocalUtils.strParseInt(id));
        if(null==proProduct){
            return BaseResult.defaultErrorWithMsg("【" + id + "】商品不存在!");
        }
        if(ConstantCommon.PRO_STATE_CODE_UP.equals(state)){
            //首次入库
            if(ConstantCommon.PRO_STATE_FIRST_PUT.equals(proProduct.getFkProStateCode())){
                //首次上架
                proProduct.setFkProStateCode(ConstantCommon.PRO_STATE_FIRST_UP);
                //再次入库
            }else if(ConstantCommon.PRO_STATE_AGAIN_PUT.equals(proProduct.getFkProStateCode())){
                //再次上架
                proProduct.setFkProStateCode(ConstantCommon.PRO_STATE_AGAIN_UP);
            }
        }
        if(ConstantCommon.PRO_STATE_CODE_DOWN.equals(state)){
            //不论是再次上架，还是首次上架，都是再次入库了
            proProduct.setFkProStateCode(ConstantCommon.PRO_STATE_AGAIN_PUT);
        }
        proProductService.updateShpShop(proProduct);
        return BaseResult.okResult();
    }

}
