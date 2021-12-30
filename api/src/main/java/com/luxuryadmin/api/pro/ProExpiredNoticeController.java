package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProCheck;
import com.luxuryadmin.entity.pro.ProExpiredNotice;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.pro.ProCheckService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProExpiredNoticeService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 商品过期提醒表
 *
 * @author ZhangSai
 * @date   2021/05/11 10:45:48
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro/expired")
@Api(tags = {"C007.商品过期提醒 --2.6.2"}, description = "/shop/user/pro/check |商品过期提醒模块相关")
@RequiresPermissions("chk:check:storeWarning")
public class ProExpiredNoticeController extends ProProductBaseController {

    @Autowired
    private ProExpiredNoticeService expiredNoticeService;

    /**
     * 前端新增商品提醒
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "商品到期提醒--添加--2.5.1",
            notes = "商品到期提醒--添加--2.5.1",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token")
    })
    @PostMapping(value = "/addExpired")
    public BaseResult addExpiredNotice(@Valid ParamExpiredNoticeAdd expiredNoticeAdd) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        ParamExpiredNoticeForAdd expiredNoticeForAdd = new ParamExpiredNoticeForAdd();
        BeanUtils.copyProperties(expiredNoticeAdd, expiredNoticeForAdd);
        expiredNoticeForAdd.setUserId(userId);
        expiredNoticeForAdd.setShopId(shopId);
        expiredNoticeService.addExpiredNotice(expiredNoticeForAdd);
        return BaseResult.okResult();
    }


    @ApiOperation(
            value = "商品到期提醒--删除",
            notes = "商品到期提醒--删除",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "id", value = "商品到期提醒主键id"),
    })
    @PostMapping("/deleteExpired")
    public BaseResult deleteExpiredNotice(@RequestParam(name="id",required=true)String id) {
        ProExpiredNotice expiredNotice = expiredNoticeService.getExpiredNoticeById(Integer.parseInt(id));
        if (expiredNotice == null ){
            return BaseResult.errorResult("此盘点不存在");
        }

        expiredNoticeService.deleteExpiredNotice(Integer.parseInt(id));

        return BaseResult.okResult();
    }

    /**
     * 商品到期提醒集合显示
     * @param
     * @return
     */
    @ApiOperation(
            value = "商品到期提醒--集合",
            notes = "商品到期提醒--集合",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @GetMapping("/getExpiredList")
    public BaseResult<VoExpiredNoticeByPage> getExpiredNoticeByPage() {
        Integer shopId = getShopId();
        VoExpiredNoticeByPage expiredNoticeByPage = expiredNoticeService.getExpiredNoticeByPage(shopId);
        return BaseResult.okResult(expiredNoticeByPage);
    }

    /**
     * 商品到期提醒集合显示
     * @param
     * @return
     */
    @ApiOperation(
            value = "记录详情页--商品到期提醒 --2.6.2",
            notes = "记录详情页--商品到期提醒 --2.6.2",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @GetMapping("/getExpiredProductByPage")
    public BaseResult<VoExpiredProductByPage> getExpiredNoticeByPage(@Valid ParamExpiredProductSearch expiredProductSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        expiredProductSearch.setShopId(shopId);
        VoExpiredProductByPage expiredProductByPage = expiredNoticeService.getExpiredProductByPage(expiredProductSearch);
        List<VoExpiredProductByList> expiredProductLists = expiredProductByPage.getExpiredProductLists();
        //是否有查看仓库资产权限 9.10产品改为查看成本价权限
        String chkStoreTotalPrice = ConstantPermission.CHK_PRICE_INIT;
        String uPermStoreTotalPrice = hasPermToPageWithCurrentUser(chkStoreTotalPrice);
        if (expiredProductLists != null && expiredProductLists.size()>0){
            expiredProductLists.forEach(expiredProduct ->{
                if (!"time".equals(expiredProductSearch.getSortKey()) && !"updateTime".equals(expiredProductSearch.getSortKey())){
                    expiredProductSearch.setSortKey("time");
                }
                formatVoProductLoad(expiredProductSearch.getAppVersion(), expiredProduct, expiredProductSearch.getSortKey());
                //查看商品属性(长属性和短属性)
                expiredProduct.setAttributeCn(servicesUtil.getAttributeCn(expiredProduct.getAttributeCode(), false));
                ////缩略图
                //String smallImg = expiredProduct.getSmallImg();
                //if (!LocalUtils.isEmptyAndNull(smallImg) && !smallImg.contains("http")) {
                //    expiredProduct.setSmallImg(servicesUtil.formatImgUrl(smallImg, true));
                //}
                //2.6.6添加判断是否是锁单商品
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(expiredProduct.getShopId(), expiredProduct.getBizId());
                //判断是否锁单商品
                expiredProduct.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");
                if ("0".equals(uPermStoreTotalPrice)){
                    expiredProduct.setInitPrice(null);
                }
            });
        }

        expiredProductByPage.setUPermStoreTotalPrice(uPermStoreTotalPrice);
        if ("0".equals(uPermStoreTotalPrice)){
            expiredProductByPage.setProductTotalPrice("*****");
        }
        return BaseResult.okResult(expiredProductByPage);
    }

    /**
     * 商品到期提醒集合显示
     * @param
     * @return
     */
    @ApiOperation(
            value = "商品到期提醒--添加--提醒设置--商品到期提醒",
            notes = "商品到期提醒--添加--提醒设置--商品到期提醒",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @GetMapping("/getExpiredAddNeed")
    public BaseResult<Map<String,Object>> getExpiredAddNeed() {

        Integer shopId = getShopId();

        Map<String,Object> objectMap = expiredNoticeService.getExpiredAddNeed(shopId);
        return BaseResult.okResult(objectMap);
    }

    /**
     * 商品到期提醒集合显示
     * @param
     * @return
     */
    @ApiOperation(
            value = "记录详情页分类集合--商品到期提醒",
            notes = "记录详情页分类集合--商品到期提醒",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "expiredNoticeId", value = "商品到期提醒主键id"),
    })
    @GetMapping("/getProClassifyList")
    public BaseResult<List<VoProClassify>> getProClassifyList(@RequestParam(name="expiredNoticeId",required=true)String expiredNoticeId) {

        Integer shopId = getShopId();
        List<VoProClassify> proClassifies = expiredNoticeService.getProClassifyList(Integer.parseInt(expiredNoticeId),shopId);
        return BaseResult.okResult(proClassifies);
    }

    /**
     * 商品到期提醒集合显示
     * @param
     * @return
     */
    @ApiOperation(
            value = "定时任务发送接口--商品到期提醒",
            notes = "定时任务发送接口--商品到期提醒",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
    })
    @GetMapping("/sendMessageForExpiredProduct")
    public BaseResult<List<VoProClassify>> sendMessageForExpiredProduct() {


       expiredNoticeService.sendMessageForExpiredProduct();
        return BaseResult.okResult();
    }

}
