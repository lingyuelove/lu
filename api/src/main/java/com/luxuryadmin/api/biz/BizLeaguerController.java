package com.luxuryadmin.api.biz;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.biz.BizLeaguer;
import com.luxuryadmin.enums.shp.EnumShpOperateLogModule;
import com.luxuryadmin.param.biz.*;
import com.luxuryadmin.param.pro.ParamClassifyTypeSearch;
import com.luxuryadmin.param.pro.ParamProductBizId;
import com.luxuryadmin.param.shp.ParamAddShpOperateLog;
import com.luxuryadmin.service.biz.BizLeaguerConfigService;
import com.luxuryadmin.service.biz.BizLeaguerService;
import com.luxuryadmin.service.biz.BizShopRecommendService;
import com.luxuryadmin.service.biz.BizShopSeeService;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProDetailService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.pro.ProStandardService;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpShopService;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.biz.*;
import com.luxuryadmin.vo.pro.VoClassifyTypeSon;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoUserShopBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author monkey king
 * @date 2020-01-11 21:52:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz", method = RequestMethod.POST)
@Api(tags = {"G002.【友商】模块 --2.6.3 --zs "}, description = "/shop/user/biz |【友商】模块相关")
//此权限详细至方法内部
//@RequiresPermissions("leaguer:check:shop")
public class BizLeaguerController extends BizLeaguerBaseController {

    @Autowired
    private BizLeaguerService bizLeaguerService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private BizLeaguerConfigService bizLeaguerConfigService;

    @Autowired
    private ShpWechatService shpWechatService;

    @Autowired
    private ProDetailService proDetailService;

    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ShpShopService shpShopService;

    @Autowired
    private BizShopRecommendService bizShopRecommendService;

    @Autowired
    private BizShopSeeService bizShopSeeService;
    @Autowired
    private ProStandardService proStandardService;

    /**
     * 友商通讯录
     */
    @ApiOperation(
            value = "获取友商通讯录;",
            notes = "获取友商通讯录;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/listContact")
    public BaseResult<VoBizLeaguer> listContact(@RequestParam Map<String, String> params) {
        List<VoBizLeaguer> leaguerList = bizLeaguerService.listBizLeaguerByShopId(getShopId());
        formatVoBizLeaguer(leaguerList);
        return LocalUtils.getBaseResult(leaguerList);
    }


    /**
     * 获取(具体)友商详情;
     */
    @ApiOperation(
            value = "获取(具体)友商详情;",
            notes = "获取(具体)友商详情;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getSpecificLeaguerDetail")
    public BaseResult<VoBizLeaguer> getSpecificLeaguerDetail(
            @RequestParam Map<String, String> params,
            @Valid ParamLeaguerShopId paramLeaguerShopId, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String leaguerShopId = paramLeaguerShopId.getLeaguerShopId();
        VoBizLeaguer voBizLeaguerMine = bizLeaguerService.getSpecificLeaguerDetail(getShopId(), Integer.parseInt(leaguerShopId));
        VoBizLeaguer voBizLeaguer = bizLeaguerService.getSpecificLeaguerDetail(Integer.parseInt(leaguerShopId), getShopId());
        formatVoBizLeaguer(voBizLeaguerMine);

        //如果没有查看权限，则为0
        Integer onSaleProductNum = 0;
        //有权限查看,获取在售商品数量
        if (!LocalUtils.isEmptyAndNull(voBizLeaguer) && ConstantCommon.ONE.equals(voBizLeaguer.getVisible())) {
            //&& ConstantCommon.ONE.equals(""+voBizLeaguerMine.getIsWantSeeLeaguerProd())) {
            onSaleProductNum = proProductService.getOnSaleProductNumByShopId(leaguerShopId);
        }
        if (onSaleProductNum == null) {
            onSaleProductNum = 0;
        }
        voBizLeaguerMine.setOnSaleProductNum(onSaleProductNum);

        //修改【登录者A】对【友商B】看到的【销售价】【同行价】权限
        Integer isCanSeeSalePrice = voBizLeaguerMine.getIsCanSeeSalePrice();
        voBizLeaguerMine.setIsCanSeeSalePrice(1 - isCanSeeSalePrice);
        Integer isCanSeeTradePrice = voBizLeaguerMine.getIsCanSeeTradePrice();
        voBizLeaguerMine.setIsCanSeeTradePrice(1 - isCanSeeTradePrice);
        ParamShopSeeAdd shopSeeAdd = new ParamShopSeeAdd();
        shopSeeAdd.setShopId(getShopId());
        shopSeeAdd.setUserId(getUserId());
        shopSeeAdd.setBeSeenShopId(Integer.parseInt(leaguerShopId));
        bizShopSeeService.saveShopSee(shopSeeAdd);
        return LocalUtils.getBaseResult(voBizLeaguerMine);
    }

    /**
     * 获取(具体)友商价格配置详情;
     */
    @ApiOperation(
            value = "获取(具体)友商价格配置详情;",
            notes = "获取(具体)友商价格配置详情;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @GetMapping("/getCanSeeLeaguerPriceInfo")
    public BaseResult<VoCanSeeLeaguerPriceInfo> getCanSeeLeaguerPriceInfo(@RequestParam Map<String, String> params) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        VoCanSeeLeaguerPriceInfo priceInfo;
        try {
            priceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(shopId, userId);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult(priceInfo);
    }

    /**
     * 友商相册--店铺信息;
     */
    @ApiOperation(
            value = "友商相册--店铺信息 --2.5.2 --zs;",
            notes = "友商相册--店铺信息 --2.5.2 --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @GetMapping("/getLeaguerShop")
    @RequiresPermissions("leaguer:check:shop")
    public BaseResult<VoBizLeaguerShop> getLeaguerShop() {
        Integer shopId = getShopId();
        VoBizLeaguerShop leaguerShop = bizLeaguerConfigService.getLeaguerShop(shopId);

        return BaseResult.okResult(leaguerShop);
    }

    /**
     * 友商相册--店铺信息;
     */
    @ApiOperation(
            value = "推荐友商列表--友商店铺 --2.5.2 --zs;",
            notes = "推荐友商列表--友商店铺 --2.5.2 --zs;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "pageNum")
    })
    @PostMapping("/getRecommendLeaguerList")
    public BaseResult<VoLeaguerRecommendPage> getRecommendLeaguerList(@Valid ParamLeaguerRecommendBySearch leaguerRecommendBySearch) {
        try {
            VoCanSeeLeaguerPriceInfo seeLeaguerPriceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(getShopId(), getUserId());
            if (!LocalUtils.isEmptyAndNull(seeLeaguerPriceInfo) && "0".equals(seeLeaguerPriceInfo.getRecommend())) {
                return BaseResult.okResult();
            }
        } catch (Exception e) {
            throw new MyException("推荐友商列表--友商店铺错误为" + e);
        }
        Integer shopId = getShopId();
        leaguerRecommendBySearch.setShopId(shopId);
        VoLeaguerRecommendPage recommendPage = bizShopRecommendService.getRecommendLeaguerList(leaguerRecommendBySearch);
        return BaseResult.okResult(recommendPage);
    }

    /**
     * 友商相册--店铺信息;
     */
    @ApiOperation(
            value = "推荐友商n条数据--友商店铺 --2.5.2 --zs;",
            notes = "推荐友商n条数据--友商店铺 --2.5.2 --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @GetMapping("/getByShowRecommend")
    public BaseResult<List<VoLeaguerRecommend>> getByShowRecommend() {
        Integer shopId = getShopId();
        try {
            VoCanSeeLeaguerPriceInfo seeLeaguerPriceInfo = bizLeaguerConfigService.getCanSeeLeaguerPriceInfo(shopId, getUserId());
            if (!LocalUtils.isEmptyAndNull(seeLeaguerPriceInfo) && "0".equals(seeLeaguerPriceInfo.getRecommend())) {
                return BaseResult.okResult();
            }
        } catch (Exception e) {
            throw new MyException("推荐友商列表--友商店铺错误为" + e);
        }
        List<VoLeaguerRecommend> leaguerRecommends = bizShopRecommendService.getByShowRecommend(shopId);
        if (leaguerRecommends != null && leaguerRecommends.size() > 0) {
            return BaseResult.okResult(leaguerRecommends);
        }
        return BaseResult.okResult();
    }

    /**
     * 更新(具体)友商价格配置详情;
     */
    @ApiOperation(
            value = "更新(具体)友商价格配置详情;",
            notes = "更新(具体)友商价格配置详情;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @GetMapping("/updateCanSeeLeaguerPriceInfo")
    public BaseResult updateCanSeeLeaguerPriceInfo(@Valid ParamCanSeeLeaguerPriceInfo paramCanSeeLeaguerPriceInfo, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        Integer userId = getUserId();

        try {
            bizLeaguerConfigService.updateCanSeeLeaguerPriceInfo(shopId, userId, paramCanSeeLeaguerPriceInfo, request);
        } catch (Exception e) {
            return BaseResult.defaultErrorWithMsg(e.getMessage());
        }
        return LocalUtils.getBaseResult("更新成功");
    }


    /**
     * 修改对友商的备注
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "修改对友商的备注",
            notes = "修改对友商的备注",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/updateLeaguerNote")
    public BaseResult updateLeaguerNote(@RequestParam Map<String, String> params,
                                        @Valid ParamUpdateLeaguerNote updateNote, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String note = updateNote.getNote();
        String leaguerIdStr = updateNote.getLeaguerId();
        String leaguerShopIdStr = updateNote.getLeaguerShopId();
        int leaguerId = Integer.parseInt(leaguerIdStr);
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        bizLeaguerService.updateLeaguerNote(leaguerId, getShopId(), leaguerShopId, note);
        return BaseResult.okResult();
    }


    /**
     * 是否允许查看
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "是否允许查看商品",
            notes = "是否允许查看商品",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "leaguerId", value = "leaguerId"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "leaguerShopId", value = "友商商店id"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "int",
                    name = "visible", value = "是否允许友商查看店铺商品; 0:不允许; 1:允许"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/settingLeaguer")
    public BaseResult settingLeaguer(@RequestParam Map<String, String> params,
                                     @Valid ParamUpdateLeaguerVisible updateVisible, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);


        String leaguerIdStr = updateVisible.getLeaguerId();
        String leaguerShopIdStr = updateVisible.getLeaguerShopId();
        String visible = updateVisible.getVisible();
        int leaguerId = Integer.parseInt(leaguerIdStr);
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        BizLeaguer bizLeaguerOld = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(getShopId(), leaguerShopId);

        BizLeaguer bizLeaguer = new BizLeaguer();
        bizLeaguer.setId(leaguerId);
        bizLeaguer.setFkInviterShopId(getShopId());
        bizLeaguer.setFkBeInviterShopId(leaguerShopId);
        bizLeaguer.setVisible(visible);//不让他看我的商品
        bizLeaguer.setTop(updateVisible.getTop());
        bizLeaguer.setUpdateTime(new Date());
        bizLeaguer.setUpdateAdmin(getUserId());
        bizLeaguer.setIsCanSeeSalePrice(1 - updateVisible.getIsCanSeeSalePrice());//销售价
        bizLeaguer.setIsCanSeeTradePrice(1 - updateVisible.getIsCanSeeTradePrice());//友商价
        bizLeaguer.setIsWantSeeLeaguerProd(updateVisible.getNotSeeGoods());//不看他的商品
        bizLeaguerService.modifyVisible(bizLeaguer);

        //添加【店铺操作日志】-【设置单个友商权限】
        VoUserShopBase voUserShopBaseLeaguer = shpShopService.getVoUserShopBaseByShopId(leaguerShopId);
        if (null != voUserShopBaseLeaguer) {
            String leaguerShopName = voUserShopBaseLeaguer.getShopName();
            ParamAddShpOperateLog paramAddShpOperateLog = new ParamAddShpOperateLog();
            paramAddShpOperateLog.setShopId(getShopId());
            paramAddShpOperateLog.setOperateUserId(getUserId());
            paramAddShpOperateLog.setModuleName(EnumShpOperateLogModule.LEAGUER_ALBUM.getName());
            paramAddShpOperateLog.setOperateName("设置单个友商权限");
            paramAddShpOperateLog.setProdId(null);
            paramAddShpOperateLog.setOrderId(null);
            paramAddShpOperateLog.setRequest(request);
            String operateLog = "【" + leaguerShopName + "-" + leaguerShopId + "】";
            constructSetSingleLeaguerPermContent(operateLog, bizLeaguerOld, bizLeaguer, paramAddShpOperateLog);

        }
        return BaseResult.okResult();
    }

    /**
     * 构建【设置单个友商权限】操作日志
     *
     * @param bizLeaguerOld
     * @param bizLeaguerNew
     * @return
     */
    private void constructSetSingleLeaguerPermContent(String operateLog, BizLeaguer bizLeaguerOld, BizLeaguer bizLeaguerNew, ParamAddShpOperateLog paramAddShpOperateLog) {
        String operateContent = operateLog;
        Integer updateFieldCount = 0;
        //让他看我的店铺商品
        String isCanSeeMyGoodsOld = bizLeaguerOld.getVisible();
        String isCanSeeMyGoodsNew = bizLeaguerNew.getVisible();
        Boolean isUpdateCanSeeMyGoods = !isCanSeeMyGoodsOld.equals(isCanSeeMyGoodsNew);
        if (isUpdateCanSeeMyGoods) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if ("1".equals(isCanSeeMyGoodsNew)) {
                operateContent += "让他看我的店铺商品";
            } else {
                operateContent += "不让他看我的店铺商品";
            }
        }

        //看他的店铺商品
        Integer isWantSeeLeaguerGoodsOld = bizLeaguerOld.getIsWantSeeLeaguerProd();
        Integer isWantSeeLeaguerGoodsNew = bizLeaguerNew.getIsWantSeeLeaguerProd();
        Boolean isUpdateWantSeeLeaguerMyGoods = !isWantSeeLeaguerGoodsOld.equals(isWantSeeLeaguerGoodsNew);
        if (isUpdateWantSeeLeaguerMyGoods) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isWantSeeLeaguerGoodsNew.equals(1)) {
                operateContent += "看他的店铺商品";
            } else {
                operateContent += "不看他的店铺商品";
            }
        }

        //让他看销售价
        Integer isShowSalePriceOld = bizLeaguerOld.getIsCanSeeSalePrice();
        Integer isShowSalePriceNew = bizLeaguerNew.getIsCanSeeSalePrice();
        Boolean isUpdateShowSalePrice = !isShowSalePriceOld.equals(isShowSalePriceNew);
        if (isUpdateShowSalePrice) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isShowSalePriceNew.equals(1)) {
                operateContent += "让他看销售价";
            } else {
                operateContent += "不让他看销售价";
            }
        }

        //让他看友商价/同行价
        Integer isShowTradeOld = bizLeaguerOld.getIsCanSeeTradePrice();
        Integer isShowTradePriceNew = bizLeaguerNew.getIsCanSeeTradePrice();
        Boolean isUpdateShowTradePrice = !isShowTradeOld.equals(isShowTradePriceNew);
        if (isUpdateShowTradePrice) {
            updateFieldCount++;
            if (updateFieldCount != 1) {
                operateContent += ";";
            }
            if (isShowTradePriceNew.equals(1)) {
                operateContent += "让他看同行价";
            } else {
                operateContent += "不让他看同行价";
            }
        }

        paramAddShpOperateLog.setOperateContent(operateContent);
        if (updateFieldCount > 0) {
            shpOperateLogService.saveShpOperateLog(paramAddShpOperateLog);
        }
    }


    /**
     * 删除友商(互相删除)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "删除友商;(互相删除)",
            notes = "删除友商;(互相删除)",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "leaguerShopId", value = "友商商店id"),
    })
    @RequestRequire
    @RequestMapping("/deleteLeaguer")
    public BaseResult deleteLeaguer(@RequestParam Map<String, String> params,
                                    @Valid ParamDeleteLeaguer deleteLeaguer, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        boolean newVersion = newAndroidAndIOSVersion("2.1.0", "2.2.1");
        //新版本权限,临时做此判断
        if (newVersion) {
            boolean leaguerAddPerm = hasPermWithCurrentUser(ConstantPermission.MOD_LEAGUER_DELETE);
            if (!leaguerAddPerm) {
                return BaseResult.errorResult(EnumCode.ERROR_NO_SHOP_PERMISSION);
            }
        }
        String leaguerShopIdStr = deleteLeaguer.getLeaguerShopId();
        int leaguerShopId = Integer.parseInt(leaguerShopIdStr);
        bizLeaguerService.deleteBizLeaguer(getUserId(), getShopId(), leaguerShopId, request);
        return BaseResult.okResult();
    }


    /**
     * 加载所有友商商品
     *
     * @param productQuery 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载所有友商商品;",
            notes = "加载所有友商商品;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/listLeaguerProduct")
    @RequiresPermissions("leaguer:check:shop")
    public BaseResult<List<VoLeaguerProduct>> listLeaguerProduct(
            @Valid ParamLeaguerProductQuery productQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        formatLeaguerQueryParam(productQuery);
        List<VoLeaguerProduct> listLeaPro = bizLeaguerService.listBizLeaguerProductByShopIds(productQuery);
        return LocalUtils.getBaseResult(listLeaPro);
    }


    /**
     * 获取友商商品，和联盟商品详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取友商商品，和联盟商品详情 --2.6.4❤ --zs;",
            notes = "获取友商商品，和联盟商品详情--2.6.4❤ --zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getLeaguerProductDetail")
    public BaseResult<VoLeaguerProduct> getLeaguerProductDetail(
            @RequestParam Map<String, String> params, @Valid ParamProductBizId proDetail, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String bizId = proDetail.getBizId();
        Integer leaguerShopId = proProductService.getShopIdByBizId(bizId);
        Integer shopId = getShopId();
        VoLeaguerProduct leaProDetail = proProductService.getBizLeaguerProductDetailByBizId(bizId, leaguerShopId, shopId, proDetail.getType());
        if (LocalUtils.isEmptyAndNull(leaProDetail)) {
            return BaseResult.defaultErrorWithMsg("商品不存在!");
        }
        //该方法体验会员才需要判断
        if ("union".equals(proDetail.getType()) && "1".equals(getMemberState())) {
            // =============增加查看商品个数限制规则;修改于2021-11-05 22:18:10
            String accessLimitNumKey = "shp:shop_union:accessUser:limitNum";
            String accessLimitNumValue = redisUtil.get(accessLimitNumKey);
            String shopUnionShopTempKey = ConstantRedisKey.getShopUnionAppUserTemp(getUsername());
            String userAccessLimit = redisUtil.get(shopUnionShopTempKey);
            userAccessLimit = LocalUtils.isEmptyAndNull(userAccessLimit) ? "0: " : userAccessLimit;
            String[] split = userAccessLimit.split(":");
            //看过商品数量
            int userAccessLimitInt = Integer.parseInt(split[0]);
            if (userAccessLimitInt >= Integer.parseInt(accessLimitNumValue)) {
                return BaseResult.defaultErrorWithMsg("此功能仅对奢当家店铺会员开放！");
            }
            //已看过的商品不进行次数统计
            String accessProduct = split[1];
            if (!accessProduct.contains(bizId)) {
                accessProduct += "," + bizId;
                redisUtil.set(shopUnionShopTempKey, (++userAccessLimitInt) + ":" + accessProduct);
            }
            //========================End==================
        }


        String productImg = leaProDetail.getProductImg();
        if (!LocalUtils.isEmptyAndNull(productImg)) {
            String[] productImgArray = productImg.split(";");
            for (int i = 0; i < productImgArray.length; i++) {
                if (!productImgArray[i].contains("http")) {
                    productImgArray[i] = servicesUtil.formatImgUrl(productImgArray[i], false);
                }
            }
            leaProDetail.setProductImgList(productImgArray);
            leaProDetail.setProductImg(null);
        }

        String videoUrl = leaProDetail.getVideoUrl();
        leaProDetail.setVideoUrl(servicesUtil.formatImgUrl(videoUrl));


        //设置友商微信列表
        List<VoShpWechat> voShpWechatList = shpWechatService.listShpWechat(leaProDetail.getShopId());
        try {
            //设置友商微信列表判断版本控制是否在2.6.2或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.2") >= 0) {
                VoShpWechat voShpWechat = shpWechatService.addByShopId(leaProDetail.getShopId(), getUserId());
                if (voShpWechat != null) {
                    voShpWechatList.add(0, voShpWechat);
                }
            }
        } catch (Exception e) {
            throw new MyException("订单添加回收人员错误" + e);
        }
        leaProDetail.setVoShpWechatList(voShpWechatList);
        ParamShopSeeAdd shopSeeAdd = new ParamShopSeeAdd();
        shopSeeAdd.setShopId(getShopId());
        shopSeeAdd.setUserId(getUserId());
        shopSeeAdd.setBeSeenShopId(leaguerShopId);
        bizShopSeeService.saveShopSee(shopSeeAdd);
        //查看此友商是否是好友友商是否
        String leaguerFriendState = bizLeaguerService.getLeaguerFriendState(getShopId(), leaguerShopId);

        leaProDetail.setLeaguerFriendState(leaguerFriendState);
        ParamClassifyTypeSearch classifyTypeSearch = new ParamClassifyTypeSearch();
//            Integer productId = proProductService.getShopIdByBizId(vo.getBizId());
        classifyTypeSearch.setBizId(leaProDetail.getBizId());
        classifyTypeSearch.setShopId(leaProDetail.getShopId());
        classifyTypeSearch.setProductId(leaProDetail.getProId());
        VoClassifyTypeSon classifyTypeSon = proStandardService.getClassifyTypeList(classifyTypeSearch);
        if (!LocalUtils.isEmptyAndNull(classifyTypeSon)) {
            leaProDetail.setClassifyTypeList(classifyTypeSon.getClassifyTypeSonLists());
            if (!LocalUtils.isEmptyAndNull(classifyTypeSon.getPublicPrice()) && !"0".equals(classifyTypeSon.getPublicPrice())) {
                leaProDetail.setPublicPrice("￥" + classifyTypeSon.getPublicPrice());
            }
        }
        //判断是否为商家联盟的商品详情
        if (!LocalUtils.isEmptyAndNull(proDetail.getType()) && "union".equals(proDetail.getType())) {
            //设置【查看友商信息】权限
            String uPermShowUnionShop = ConstantPermission.SHOW_UNION_UNIONSHOP;
            leaProDetail.setUnionPerm(hasPermToPageWithCurrentUser(uPermShowUnionShop));
        }
        return LocalUtils.getBaseResult(leaProDetail);
    }


    /**
     * 初始化友商店铺参数(包括分类)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化友商店铺参数(包括分类)----2.6.3❤",
            notes = "初始化友商店铺参数(包括分类)----2.6.3❤",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/initLeaguerShop")
    public BaseResult initLeaguerShop(@RequestParam Map<String, String> params,
                                      @Valid ParamLeaguerShopId paramLeaguer, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int leaguerShopId = Integer.parseInt(paramLeaguer.getLeaguerShopId());
        VoBizLeaguer voBizLeaguer = bizLeaguerService.getLeaguerShop(getShopId(), leaguerShopId);
        formatVoBizLeaguer(voBizLeaguer);
        try {
            //设置友商微信列表判断版本控制是否在2.6.2或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.2") >= 0) {
                VoShpWechat voShpWechat = shpWechatService.addByShopId(leaguerShopId, getUserId());
                if (voShpWechat != null) {
                    List<VoShpWechat> voShpWechatList = voBizLeaguer.getVoShpWechatList();
                    voShpWechatList.add(0, voShpWechat);
                    voBizLeaguer.setVoShpWechatList(voShpWechatList);

                }
            }
        } catch (Exception e) {
            throw new MyException("订单添加回收人员错误" + e);
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(voBizLeaguer);
        List<VoProClassify> classifyList = proClassifyService.listLeaguerProClassifyByState(leaguerShopId);
        hashMap.put("classifyList", classifyList);

        return BaseResult.okResult(hashMap);
    }

    /**
     * 获取(具体)某个友商的所有商品
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取具体某个友商的所有商品",
            notes = "获取具体某个友商的所有商品",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/listSpecificLeaguerProduct")
    public BaseResult<List<VoLeaguerProduct>> listSpecificLeaguerProduct(
            @RequestParam Map<String, String> params,
            @Valid ParamSpecificLeaguerProductQuery productQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoLeaguerProduct> listLeaPro = new ArrayList<>();
        int leaguerShopId = Integer.parseInt(productQuery.getLeaguerShopId());
        BizLeaguer leaguer = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(leaguerShopId, getShopId());
        BizLeaguer leaguerMine = bizLeaguerService.getBizLeaguerByShopIdAndLeaguerShopId(getShopId(), leaguerShopId);


        if (!LocalUtils.isEmptyAndNull(productQuery.getType()) && "union".equals(productQuery.getType())) {
            formatLeaguerQueryParam(productQuery);
            listLeaPro = bizLeaguerService.listSpecificBizUnionProduct(productQuery);
        } else {
            //V2.1.0 友商设置允许查看商品,并且自己想看友商的商品
            //V2.2.0 友商设置允许查看商品，则可以看，不用考虑登陆者的权限设置
            if (!LocalUtils.isEmptyAndNull(leaguer) && ConstantCommon.ONE.equals(leaguer.getVisible())) {
                //&& ConstantCommon.ONE.equals(""+leaguerMine.getIsWantSeeLeaguerProd())) {
                formatLeaguerQueryParam(productQuery);
                listLeaPro = bizLeaguerService.listSpecificBizLeaguerProduct(productQuery, getShopId());
            }

        }
        return LocalUtils.getBaseResult(listLeaPro);
    }


}
