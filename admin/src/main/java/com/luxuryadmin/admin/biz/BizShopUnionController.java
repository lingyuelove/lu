package com.luxuryadmin.admin.biz;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequiresPerm;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.biz.*;
import com.luxuryadmin.param.common.ParamIds;
import com.luxuryadmin.param.pro.ParamProProduct;
import com.luxuryadmin.param.pro.ParamUnionAccess;
import com.luxuryadmin.param.shp.ParamShpUser;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.biz.BizUnionVerifyService;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.vo.biz.VoBizUnionVerifyList;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminList;
import com.luxuryadmin.vo.biz.VoShopUnionForAdminPage;
import com.luxuryadmin.vo.pro.VoProductLoadForUnionPage;
import com.luxuryadmin.vo.pro.VoUnionAccess;
import com.luxuryadmin.vo.shp.VoShpUser;
import com.luxuryadmin.vo.shp.VoShpUserExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * biz_shop_union controller
 *
 * @author zhangsai
 * @Date 2021-07-16 17:58:53
 */
@Slf4j
@RestController
@RequestMapping(value = "/biz/recommend/union", method = RequestMethod.POST)
@Api(tags = {"8.【商家联盟】模块 --2.6.1 --zs"}, description = "/biz/recommend |【商家联盟】模块相关")
public class BizShopUnionController extends BaseController {


    @Autowired
    private BizShopUnionService bizShopUnionService;

    @Autowired
    private BizUnionVerifyService bizUnionVerifyService;

    @Autowired
    private ProShareService shareService;


    @ApiOperation(
            value = "商家联盟 --集合显示 --2.6.1 --zs;",
            notes = "商家联盟 --集合显示 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/getShopUnionForAdminPage")
    @RequiresPerm(value =  {"union:seller","union:buyers"})
    public BaseResult<VoShopUnionForAdminPage> getShopUnionForAdminPage(@Valid ParamShopUnionForAdminBySearch shopUnionForAdminBySearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        VoShopUnionForAdminPage recommendAdminPage = bizShopUnionService.getShopUnionForAdminPage(shopUnionForAdminBySearch);
        return BaseResult.okResult(recommendAdminPage);
    }

    @RequestMapping(
            value = "/exportShopUnion",
            method = RequestMethod.GET)
    @ApiOperation(
            value = "导出查询商家联盟列表;",
            notes = "导出查询商家联盟列表",
            httpMethod = "GET")
    public void exportShopUnion(@Valid ParamShopUnionForAdminBySearch shopUnionForAdminBySearch, BindingResult result,
                                HttpServletResponse response) {
        servicesUtil.validControllerParam(result);
        //最多导出5万条记录
        PageHelper.startPage(1, 50000);
        List<VoShopUnionForAdminList> shopUnionForAdminLists = bizShopUnionService.listShopUnionForAdmin(shopUnionForAdminBySearch);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("商家联盟列表-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), VoShopUnionForAdminList.class).sheet("商家联盟列表").doWrite(shopUnionForAdminLists);
        } catch (Exception e) {
            throw new MyException("" + e.getMessage());
        }
    }

    @ApiOperation(
            value = "商家联盟 --新增 --2.6.1 --zs;",
            notes = "商家联盟 --新增 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/addShopUnion")
    public BaseResult addShopUnion(@Valid ParamShopUnionAdd shopUnionAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);
        bizShopUnionService.addShopUnion(shopUnionAdd);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "商家联盟 --移除 --2.6.1 --zs;",
            notes = "商家联盟 --移除 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/removeUnionShop")
    public BaseResult removeUnionShop(@Valid ParamShopUnionDelete shopUnionDelete, BindingResult result) {
        servicesUtil.validControllerParam(result);
        bizShopUnionService.removeUnionShop(Integer.parseInt(shopUnionDelete.getId()));
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "商家联盟-- 商品 --集合显示 --2.6.1 --zs;",
            notes = "商家联盟-- 商品 --集合显示 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/getProductLoadForUnionPage")
    @RequiresPerm(value = "union:pro")
    public BaseResult<VoProductLoadForUnionPage> getProductLoadForUnionPage(@Valid ParamProProduct proProduct, BindingResult result) {
        servicesUtil.validControllerParam(result);
        proProduct.setPageSize(PAGE_SIZE_50);
        VoProductLoadForUnionPage productLoadForUnionPage = bizShopUnionService.getProductLoadForUnionPage(proProduct);
        return BaseResult.okResult(productLoadForUnionPage);
    }


    @ApiOperation(
            value = "商家联盟小程序访客",
            notes = "商家联盟小程序访客;",
            httpMethod = "POST")

    @RequestMapping("/listUnionShareUser")
    @RequiresPerm(value = "union:share:user")
    public BaseResult listUnionShareUser(@Valid ParamUnionAccess param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String nickname = param.getNickname();
        if (!LocalUtils.isEmptyAndNull(nickname)) {
            param.setNickname("%" + nickname + "%");
        }
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoUnionAccess> shareUserList = shareService.listUnionShareUser(param);
        for (VoUnionAccess vo : shareUserList) {
            String shareBatch = vo.getShareBatch();
            if (!LocalUtils.isEmptyAndNull(shareBatch)) {
                try {
                    Date date = DateUtil.parse(Long.parseLong(shareBatch));
                    //v262版本之后 不需要额外加多1天;对于之前的数据全部当作已过期处理
                    //date = DateUtil.addDaysFromOldDate(date, 1).getTime();
                    vo.setExpireTime(DateUtil.format(date));
                    vo.setAccessState("正常访问");
                    if (date.before(DateUtil.parse(vo.getInsertTime()))) {
                        vo.setAccessState("过期访问");
                    }
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        PageInfo<VoUnionAccess> page = new PageInfo<>(shareUserList);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(shareUserList);
        hashMap.put("total", page.getTotal());
        String today = DateUtil.formatShort(new Date());
        //默认统计今天
        if (LocalUtils.isEmptyAndNull(param.getStartTime()) && LocalUtils.isEmptyAndNull(param.getEndTime())) {
            param.setStartTime(today);
        }
        int accessUserCount = shareService.countAccessUserCount(param);
        hashMap.put("accessUserCount", accessUserCount);
        int accessCount = shareService.countAccessCount(param);
        hashMap.put("accessCount", accessCount);
        return BaseResult.okResult(hashMap);
    }


    @ApiOperation(
            value = "获取商家联盟分享人",
            notes = "获取商家联盟分享人",
            httpMethod = "GET")

    @GetMapping("/loadShareUser")
    public BaseResult loadShareUser(@Valid ParamUnionAccess param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoShpUser> shpUserList = bizShopUnionService.listUnionMpShareUser();
        return LocalUtils.getBaseResult(shpUserList);
    }

    @ApiOperation(
            value = "从商家联盟下架该商品",
            notes = "从商家联盟下架该商品",
            httpMethod = "GET")

    @GetMapping("/backOfUnion")
    public BaseResult backOfUnion(@Valid ParamIds params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String ids = params.getIds();
        String id = LocalUtils.packString(LocalUtils.splitString(ids, ","));
        bizShopUnionService.backOfUnion(id);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "从商家联盟上架该商品",
            notes = "从商家联盟上架该商品",
            httpMethod = "GET")

    @GetMapping("/upOfUnion")
    public BaseResult upOfUnion(@Valid ParamIds params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String ids = params.getIds();
        String id = LocalUtils.packString(LocalUtils.splitString(ids, ","));
        bizShopUnionService.upOfUnion(id);
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "获取商家联盟卖家审核列表",
            notes = "获取商家联盟卖家审核列表",
            httpMethod = "GET")

    @GetMapping("/getBizUnionVerify")
    public BaseResult getBizUnionVerify(@Valid ParamBizUnionVerifyList param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoBizUnionVerifyList> voBizUnionVerifys = bizUnionVerifyService.getBizUnionVerify(param);
        PageInfo pageInfo = new PageInfo(voBizUnionVerifys);
        return LocalUtils.getBaseResult(pageInfo);
    }

    @ApiOperation(
            value = "审核（通过/未通过）",
            notes = "审核（通过/未通过）",
            httpMethod = "POST")

    @PostMapping("/audit")
    public BaseResult audit(@Valid ParamAudit param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //int userId = getUserId();
        bizUnionVerifyService.audit(param, -9);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "编辑--商家联盟审核",
            notes = "编辑--商家联盟审核",
            httpMethod = "POST")

    @PostMapping("/update")
    public BaseResult updateForUnionVerify(@Valid ParamBizUnionVerifyUpdate param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        //int userId = getUserId();
        bizUnionVerifyService.update(param, -9);
        return BaseResult.okResult();
    }

    @ApiOperation(
            value = "新增--商家联盟审核",
            notes = "新增--商家联盟审核",
            httpMethod = "POST")

    @PostMapping("/save")
    public BaseResult addUnionVerify(@Valid ParamUnionVerifyAdd param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        param.setUserId(-9);
        bizUnionVerifyService.addUnionVerify(param);
        return BaseResult.okResult();
    }
}
