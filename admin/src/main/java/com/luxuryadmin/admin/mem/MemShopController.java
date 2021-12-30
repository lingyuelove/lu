package com.luxuryadmin.admin.mem;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.shp.ShpShop;
import com.luxuryadmin.param.mem.ParamMemShop;
import com.luxuryadmin.param.ord.ParamOrdOrder;
import com.luxuryadmin.param.shp.ParamUpdateShopNumber;
import com.luxuryadmin.service.shp.ShpShopNumberService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.vo.mem.VoExportMemShop;
import com.luxuryadmin.vo.mem.VoMemShop;
import com.luxuryadmin.vo.mem.VoMemShopById;
import com.luxuryadmin.vo.ord.VoOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mem/shop")
@Api(tags = {"7.【会员管理】模块"}, description = "/mem/shop | 商铺会员列表 ")
public class MemShopController extends BaseController {

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private ShpShopNumberService shpShopNumberService;

    @RequestMapping(value = "/listMemShop", method = RequestMethod.GET)
    @ApiOperation(
            value = "分页查询商铺会员列表;",
            notes = "分页查询商铺会员列表;",
            httpMethod = "GET")
    @RequiresPerm(value = "member:list:shop")
    public BaseResult listOrdOrder(@Valid ParamMemShop paramMemShop, BindingResult result) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(paramMemShop.getPageNum(), paramMemShop.getPageSize());
        List<VoMemShop> voMemShopList = shpShopService.queryMemShopList(paramMemShop);
        PageInfo pageInfo = new PageInfo(voMemShopList);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @RequestMapping(value = "/exportListOrdOrder", method = RequestMethod.GET)
    @ApiOperation(
            value = "导出商铺会员列表;",
            notes = "导出商铺会员列表;",
            httpMethod = "GET")
    public void exportListOrdOrder(@Valid ParamMemShop paramMemShop, BindingResult result, HttpServletResponse response) {
        servicesUtil.validControllerParam(result);
        PageHelper.startPage(1, 50000);
        List<VoMemShop> voMemShopList = shpShopService.queryMemShopList(paramMemShop);
        List<VoExportMemShop> voExportMemShops = new ArrayList<>();
        voMemShopList.stream().forEach(vo -> {
            VoExportMemShop voExport = new VoExportMemShop();
            BeanUtils.copyProperties(vo, voExport);
            if (vo.getMemberShopState() != null) {
                if (vo.getMemberShopState() == 1) {
                    voExport.setMemberShopState("正常");
                } else if (vo.getMemberShopState() == 2) {
                    voExport.setMemberShopState("封禁");
                } else if (vo.getMemberShopState() == 3) {
                    voExport.setMemberShopState("过期");
                }
            }
            if (vo.getFkShpStateCode()!=null){
                if (vo.getFkShpStateCode() == 10) {
                    voExport.setFkShpStateCode("启用");
                } else  {
                    voExport.setFkShpStateCode("禁用");
                }
            }
            voExportMemShops.add(voExport);
        });
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("商铺会员列表-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), VoExportMemShop.class).sheet("商铺会员列表").doWrite(voExportMemShops);
        } catch (Exception e) {
            throw new MyException("" + e.getMessage());
        }
    }

    @ApiOperation(
            value = "设为靓号;",
            notes = "设为靓号;",
            httpMethod = "GET")
    @RequestMapping(value = "/updateShopNumber", method = RequestMethod.GET)
    public BaseResult updateShopNumber(@Valid ParamUpdateShopNumber param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String newShopNumber = param.getNewShopNumber();
        String shopIdStr = param.getShopId();
        int shopId = Integer.parseInt(shopIdStr);
        Integer bossUserId = shpShopService.getBossUserIdByShopId(shopId);
        if (LocalUtils.isEmptyAndNull(bossUserId)) {
            return BaseResult.defaultErrorWithMsg("店铺不存在!");
        }
        Integer dbShopId = shpShopService.getShopIdByShopNumber(newShopNumber);
        if (!LocalUtils.isEmptyAndNull(dbShopId)) {
            return BaseResult.defaultErrorWithMsg(newShopNumber + " 店铺编号已被使用!");
        }
        shpShopNumberService.useShopNumber(shopId, newShopNumber);
        return BaseResult.okResult();
    }


    @RequestMapping(value = "/offShpShop", method = RequestMethod.GET)
    @ApiOperation(
            value = "禁用/启用店铺;",
            notes = "禁用/启用店铺;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "禁用商户的id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "state", value = "禁用:-99,启用:10"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "remark", value = "禁用理由"),
    })
    public BaseResult offShpShop(@RequestParam String id, String state, String remark) {
        ShpShop shpShop = shpShopService.getShpShopById(id);
        if (null == shpShop) {
            return BaseResult.defaultErrorWithMsg("【" + id + "】店铺不存在!");
        }
        shpShop.setFkShpStateCode(LocalUtils.strParseInt(state));
        if ("10".equals(state)) {
            shpShop.setRemark("");
        }
        shpShopService.updateShpShop(shpShop);
        return BaseResult.okResult();
    }

    @RequestMapping(value = "/getMemShop", method = RequestMethod.GET)
    @ApiOperation(
            value = "查询商铺会员详细信息;",
            notes = "查询商铺会员详细信息;",
            httpMethod = "GET")
    public BaseResult getForAdminVoMemShopById(@RequestParam(value = "shopId", required = true) String shopId, @RequestParam(value = "token", required = true) String token) {
        VoMemShopById voMemShopById = shpShopService.getForAdminVoMemShopById(LocalUtils.strParseInt(shopId));
        return LocalUtils.getBaseResult(voMemShopById);
    }

}
