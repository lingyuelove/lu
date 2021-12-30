package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.StringUtil;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProDynamic;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.pro.ParamModifyRecordSearch;
import com.luxuryadmin.param.pro.ParamProDetail;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.pro.ParamShareProductSave;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ShpOperateLogService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro")
@Api(tags = {"C003.【商品】模块--2.6.4❤"})
public class ProProductController extends ProProductBaseController {

    @Autowired
    private ProDownloadImgService proDownloadImgService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ProShareService proShareService;

    @Autowired
    private ProLockRecordService proLockRecordService;
    @Autowired
    private ProTempProductService proTempProductService;
    @Autowired
    private ProTempService proTempService;
    @Autowired
    private OrdOrderService ordOrderService;
    @Autowired
    private ProDynamicService proDynamicService;
    @Autowired
    private ShpOperateLogService shpOperateLogService;

    @Autowired
    private ProModifyRecordService proModifyRecordService;
    @Autowired
    private ProConveyProductService proConveyProductService;

    /**
     * 初始化【商品】页面查询参数;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化【商品】页面查询参数----2.6.2;",
            notes = "初始化【商品】页面查询参数;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token")
    })
    @RequestRequire
    @RequestMapping("/initQueryParam")
    public BaseResult initQueryParam(@RequestParam Map<String, String> params) throws ControllerException {
        int shopId = getShopId();
        int userId = getUserId();
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoEmployee> listUploadUser = shpUserShopRefService.listUploadUser(shopId);
        List<VoEmployee> listRecycleUser = proProductService.listRecycleUser(shopId);
        //商品分类
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("classifyList", voProClassifyList);
        //商品成色
        //hashMap.put("proQualityList", voProQualityList);
        //入库人员
        hashMap.put("listUploadUser", listUploadUser);
        //商品回收人员
        hashMap.put("listRecycleUser", listRecycleUser);
        //权限控制显示仓库类型;
        boolean ownStore = servicesUtil.hasPermission(shopId, userId, "pro:check:ownProduct");
        boolean entrustStore = servicesUtil.hasPermission(shopId, userId, "pro:check:entrustProduct");
        boolean otherStore = servicesUtil.hasPermission(shopId, userId, "pro:check:otherProduct");
        HashMap<String, String> storeMap;
        List<HashMap> list = new ArrayList<>();
        if (ownStore && entrustStore && otherStore) {
            storeMap = new HashMap<>(16);
            storeMap.put("storeName", "全部商品仓库");
            storeMap.put("storeType", "0");
            list.add(storeMap);
        }
        if (ownStore) {
            storeMap = new HashMap<>(16);
            storeMap.put("storeName", "自有商品仓库");
            storeMap.put("storeType", "1");
            list.add(storeMap);
        }
        if (entrustStore) {
            storeMap = new HashMap<>(16);
            storeMap.put("storeName", "寄卖商品仓库");
            storeMap.put("storeType", "2");
            list.add(storeMap);
        }
        if (otherStore) {
            storeMap = new HashMap<>(16);
            storeMap.put("storeName", "其它商品仓库");
            storeMap.put("storeType", "4");
            list.add(storeMap);
        }
        if (LocalUtils.isEmptyAndNull(list)) {
            storeMap = new HashMap<>(16);
            storeMap.put("storeName", "暂无权限");
            storeMap.put("storeType", "0");
            list.add(storeMap);
        }
        hashMap.put("storeList", list);
        //同行价（友商价）
        Boolean isHaveFriendPricePerm = hasPermWithCurrentUser("pro:price:tradePrice");
        //销售价
        Boolean isHaveSalePricePerm = hasPermWithCurrentUser("pro:price:salePrice");
        //代理价
        Boolean isHaveAgencyPricePerm = hasPermWithCurrentUser("pro:price:agencyPrice");
        //成本价
        Boolean isHaveInitPricePerm = hasPermWithCurrentUser("pro:price:initPrice");
        hashMap.put("isHaveFriendPricePerm", isHaveFriendPricePerm);
        hashMap.put("isHaveSalePricePerm", isHaveSalePricePerm);
        hashMap.put("isHaveAgencyPricePerm", isHaveAgencyPricePerm);
        hashMap.put("isHaveInitPricePerm", isHaveInitPricePerm);
        //获取动态权限
        String uPermDynamic = ConstantPermission.DYNAMIC_LIST;
        hashMap.put("uPermDynamic", hasPermToPageWithCurrentUser(uPermDynamic));
        //临时仓转仓权限
        String tempMove = ConstantPermission.PRO_TEMP_MOVE;
        hashMap.put("uPermMoveTemp", hasPermToPageWithCurrentUser(tempMove));
        return BaseResult.okResult(hashMap);
    }


    /**
     * 加载【在售商品】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【在售商品】页面",
            notes = "加载在售商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listOnSaleProduct")
    @RequiresPermissions(value = {"pro:check:onSaleProduct", "pro:check:store"}, logical = Logical.OR)
    public BaseResult listOnSaleProduct(@RequestParam Map<String, String> params,
                                        @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setStateCode("onSale");
        return listPublicProduct(queryParam, result);
    }

    /**
     * 加载【寄卖取回】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【寄卖取回】页面2.5.5",
            notes = "加载寄卖取回；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")

    @PostMapping("/listRecycleProduct")
    @RequiresPermissions("pro:entrust:entrustReturn")
    public BaseResult listRecycleProduct(@RequestParam Map<String, String> params,
                                         @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setStateCode("-10");
        return listPublicProduct(queryParam, result);
    }

    /**
     * 加载【售罄商品】页面
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【售罄商品】页面",
            notes = "加载售罄商品；支持模糊查询；分页；条件筛选",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listSaleOutProduct")
    @RequiresPermissions("pro:check:saleOutProduct")
    public BaseResult listSaleOutProduct(@RequestParam Map<String, String> params,
                                         @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setStateCode("saleOut");
        return listPublicProduct(queryParam, result);
    }

    private BaseResult listPublicProduct(ParamProductQuery queryParam, BindingResult result) {
        servicesUtil.validControllerParam(result);
        queryParam.setAttributeCode(getAllProAttributeCodeExcludePawnPro(";"));
        //系统编码不支持模糊查询,独立编码支持模糊查询;修改于2021-09-18 16:30:42
        queryParam.setUniqueCode(queryParam.getProName());

        formatQueryParam(queryParam);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);
        String sortKey = queryParam.getSortKey();
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, proProductList, sortKey);

        try {
            //2.6.6版本加动态名称
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0 && proProductList != null && proProductList.size() > 0) {
                int shopId = getShopId();
                List<Integer> proIds = proProductList.stream().map(VoProductLoad::getProId).collect(Collectors.toList());
                List<VoDynamicAndProductInfoList> voDynamicAndProductInfoLists = proDynamicService.listInfoByProId(proIds, shopId);
                if (voDynamicAndProductInfoLists != null && voDynamicAndProductInfoLists.size() > 0) {
                    Map<Integer, VoDynamicAndProductInfoList> dynamicInfoMap = voDynamicAndProductInfoLists.stream().collect(Collectors.toMap(VoDynamicAndProductInfoList::getProId, Function.identity()));
                    proProductList.stream().forEach(pp -> {
                        if (dynamicInfoMap.get(pp.getProId()) != null) {
                            pp.setDynamicName("商品位置：" + dynamicInfoMap.get(pp.getProId()).getName());
                            pp.setDynamicId(dynamicInfoMap.get(pp.getProId()).getDynamicId().toString());
                        } else {
                            pp.setDynamicName("商品位置：无");
                        }
                    });
                } else {
                    proProductList.stream().forEach(pp -> {
                        pp.setDynamicName("商品位置：无");
                    });
                }
            }
        } catch (Exception e) {
            throw new MyException("商品列表动态加载错误" + e);
        }

        //仅针对在售商品有标识符
        if ("onSale".equals(queryParam.getStateCode())) {
            for (VoProductLoad vo : proProductList) {
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(vo.getShopId(), vo.getBizId());
                //判断是否锁单商品
                vo.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");

                //未一键下载的商品都属于new标识
                String key = ConstantRedisKey.getNotDownloadProductKey(getShopId(), vo.getBizId());
                String keyValue = redisUtil.get(key);
                if (LocalUtils.isEmptyAndNull(keyValue) || !keyValue.contains(getUserId() + "")) {
                    vo.setNewState("1");
                } else {
                    vo.setNewState("0");
                }
                //formatRepairCardTime(vo);
            }
        }
        return LocalUtils.getBaseResult(proProductList);
    }


    /**
     * 获取商品详情;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取商品详情;根据业务id--2.6.4--zs.;",
            notes = "获取商品详情;根据业务id--2.6.4--zs;",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getProductDetail")
    public BaseResult getProductDetail(@RequestParam Map<String, String> params,
                                       @Valid ParamProDetail detail, BindingResult result) {
        servicesUtil.validControllerParam(result);
        String bizId = detail.getBizId();
        int shopId = getShopId();
        VoProductLoad vo;
        if (bizId.contains("#")) {
            bizId = bizId.replace("#", "");
            vo = proProductService.getProductDetailByShopIdId(shopId, bizId);
        } else {
            //判断是否为寄卖传送的商品 寄卖传送商品传固定店铺id 防止未接收商品详情为空
            if (LocalUtils.isEmptyAndNull(detail.getConveyId())) {
                vo = proProductService.getProductDetailByShopIdBizId(shopId, bizId);
            } else {
                vo = proProductService.getProductDetailByShopIdBizId(10684, bizId);
            }

        }

        if (LocalUtils.isEmptyAndNull(vo)) {
            return BaseResult.defaultErrorWithMsg("商品不存在!");
        }
        if (LocalUtils.isEmptyAndNull(vo.getAutoNumber())) {
            vo.setAutoNumber("无");
        }
        String tempName = proTempService.getTempName(shopId, vo.getProId());
        vo.setTempName(tempName);

        try {
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                ProDynamic proDynamic = proDynamicService.getDynamicProductInfoByProductId(vo.getProId(), shopId);
                if (proDynamic != null && StringUtil.isNotBlank(proDynamic.getName())) {
                    vo.setDynamicName(proDynamic.getName());
                    vo.setDynamicId(proDynamic.getId().toString());
                }
            }
        } catch (Exception e) {
            throw new MyException("获取商品位置错误" + e);
        }

        //临时仓售卖商品数量
        Integer saleTempNum = ordOrderService.getOrderNumByTemp(getShopId(), vo.getProId(), null);
        vo.setSaleTempNum(saleTempNum);
        if (saleTempNum == null) {
            vo.setSaleTempNum(0);
        }
        String tempPro = detail.getTp();
        //临时仓商品详情,需要优先显示临时仓修改的内容;
        if (!LocalUtils.isEmptyAndNull(tempPro)) {
            //从临时仓分享商品时,必须加上临时仓id
            String tempIdStr = detail.getTid();
            if (LocalUtils.isEmptyAndNull(tempIdStr)) {
                return BaseResult.defaultErrorWithMsg("tempId不允许为空!");
            }
            int tempId = Integer.parseInt(tempIdStr);
            formatTempPro(shopId, tempId, vo);
            String tempProState = proTempProductService.getTempProState(vo.getShopId(), tempId, vo.getProId());
            vo.setTempProState(tempProState);
            if ("1".equals(tempProState)) {
                vo.setTempProStateName("本仓已售罄");
            }
            if ("2".equals(tempProState)) {
                vo.setTempProStateName("仓库无库存");
            }
            //判断版本控制是否在2.5.2或以上版本
            BasicParam basicParam = getBasicParam();
            try {
                if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.5.2") >= 0) {
                    if (vo.getTotalNum() <= 0) {
                        vo.setStateUs("40");
                    }
                }
            } catch (Exception e) {
                throw new MyException("临时仓商品详情错误" + e);
            }

        }

        String appVersion = getBasicParam().getAppVersion();
        if ("-90".equals(vo.getStateUs())) {
            formatVoProductLoad(appVersion, vo, true, null);
        } else {
            formatVoProductLoad(appVersion, vo, null);
        }
        if (!LocalUtils.isEmptyAndNull(detail.getConveyId())) {
            proConveyProductService.getProductForConvey(vo, detail.getConveyId(), shopId);
        }
        //2.6.6添加判断是否是锁单商品
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(vo.getShopId(), vo.getBizId());
        //判断是否锁单商品
        vo.setLockState(proRedisNum.getLeftNum() == 0 ? "1" : "0");
        //if (!LocalUtils.isEmptyAndNull(voProductLoad)) {
        //    int downloadNum = proDownloadImgService.countDownload(shopId, bizId);
        //    voProductLoad.setDownloadNum(downloadNum);
        //    int selfDownload = proDownloadImgService.existsSelfDownload(shopId, getUserId(), bizId);
        //    voProductLoad.setDownloadState(selfDownload + "");
        //}
        String editPerm = ConstantPermission.MOD_UPDATE_INFO;
        String lockPerm = ConstantPermission.MOD_LOCK_PRODUCT;
        String releasePerm = ConstantPermission.MOD_RELEASE_PRODUCT;
        String openPerm = ConstantPermission.MOD_CONFIRM_ORD;
        String recyclePerm = ConstantPermission.PRO_RECYCLE;
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(vo);
        hashMap.put("uPermEdit", hasPermToPageWithCurrentUser(editPerm));
        hashMap.put("uPermLock", hasPermToPageWithCurrentUser(lockPerm));
        hashMap.put("uPermRelease", hasPermToPageWithCurrentUser(releasePerm));
        hashMap.put("uPermBackOff", hasPermToPageWithCurrentUser(releasePerm));
        hashMap.put("uPermOpen", hasPermToPageWithCurrentUser(openPerm));
        //赎回权限和开单合并
        hashMap.put("uPermRedeem", hasPermToPageWithCurrentUser(openPerm));
        if ("-90".equals(vo.getStateUs())) {
            //已删除商品删除权限
            String uPermDeleteHistory = ConstantPermission.PROORORD_DELETE_DELETEHISTORY;
            hashMap.put("uPermHistoryDelete", hasPermToPageWithCurrentUser(uPermDeleteHistory));
        }
        if (!"-90".equals(vo.getStateUs())) {
            //已删除商品删除权限
            String uPermDeleteProduct = ConstantPermission.PRO_DELETEPRODUCT;
            hashMap.put("uPermDeleteProduct", hasPermToPageWithCurrentUser(uPermDeleteProduct));
        }

        //默认没锁单,直接能开单;锁单状态下能否直接开单;如果拥有解锁全部权限或者该商品是该用户锁单,则可以直接开单; 0:不能 | 1:能
        String uPermUnlockOpen = "1";


        //锁单记录;
        List<VoProLockRecord> lockRecordList = proLockRecordService.listVoProLockRecordByBizId(getShopId(), bizId);
        if (!LocalUtils.isEmptyAndNull(lockRecordList)) {
            uPermUnlockOpen = "0";
            //锁单状态下,默认不能直接开单,除非有解锁该商品的权限;
            VoProLockRecord lockRecord = lockRecordList.get(0);
            StringBuilder sb = new StringBuilder();
            sb.append("该商品于 ");
            sb.append(DateUtil.format(lockRecord.getUpdateTime()));
            sb.append(" 由 ");
            sb.append(lockRecord.getLockUser());
            sb.append(" 锁单中");
            hashMap.put("lockTip", sb.toString());

            //解锁全部商品权限
            boolean unlockAllProduct = hasPermWithCurrentUser(ConstantPermission.MOD_UNLOCK_PRODUCT);
            int lockUserId = lockRecord.getLockUserId();

            //锁单人是自己,又或者有解锁全部的权限,则能直接开单;
            if (unlockAllProduct || getUserId() == lockUserId) {
                uPermUnlockOpen = "1";
            }
            //判断只有一单锁单是否为关联锁单
            if (lockRecordList.size() == 1) {
                VoProLockRecord lockRecordNow = lockRecordList.get(0);
                //判断是否为寄卖传送关联锁单 0不是 1是
                if (!LocalUtils.isEmptyAndNull(lockRecordNow.getConveyLockType())) {
                    hashMap.put("uPermConveyLock", "1");
                }
            }

        }
        hashMap.put("uPermUnlockOpen", uPermUnlockOpen);
        if ("20".equals(vo.getAttributeUs())) {
            hashMap.put("recyclePerm", hasPermToPageWithCurrentUser(recyclePerm));
        } else {
            hashMap.put("recyclePerm", 0);
        }
        //获取动态权限
        String uPermDynamic = ConstantPermission.DYNAMIC_LIST;
        hashMap.put("uPermDynamic", hasPermToPageWithCurrentUser(uPermDynamic));
        return BaseResult.okResult(hashMap);
    }

    /**
     * 保存分享【本店商品】的记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "保存分享【本店商品】的记录--2.6.2",
            notes = "保存分享【本店商品】的记录--2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/saveShareProduct")
    //@RequiresPermissions("pro:check:onSaleProduct")
    public BaseResult saveShareProduct(@RequestParam Map<String, String> params,
                                       @Valid ParamShareProductSave save, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProShare share = new ProShare();
        share.setFkShpShopId(getShopId());
        share.setFkShpUserId(getUserId());
        share.setShopNumber(getShopNumber());
        share.setUserNumber(getUserNumber());
        share.setShowPrice(save.getShowPrice());
        share.setProId(save.getProIds());
        if (!LocalUtils.isEmptyAndNull(save.getTempPro())) {
            String tempId = save.getTempId();
            //从临时仓分享商品时,必须加上临时仓id
            if (LocalUtils.isEmptyAndNull(tempId)) {
                return BaseResult.defaultErrorWithMsg("tempId不允许为空!");
            }
            share.setShareName("tempPro_" + tempId);
        }
        //判断分享商品类型 箱包 腕表
        if (!LocalUtils.isEmptyAndNull(save.getClassifyCode())) {
            share.setFkProClassifyCode(save.getClassifyCode());
        }
        String proIds = save.getProIds();

        if (!LocalUtils.isEmptyAndNull(proIds)) {
            String[] proIdArray = proIds.split(",");
            for (String proId : proIdArray) {
                //添加分享记录
                if (LocalUtils.isNumber(proId)) {
                    addProModifyRecord(Integer.parseInt(proId), "分享", "", "", save.getPlatform());
                }
            }
        }
        String saveBatch = proShareService.saveShareProduct(share);
        return LocalUtils.getBaseResult(saveBatch);
    }


    /**
     * 保存分享【友商商品】的记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "保存分享【友商商品】的记录",
            notes = "保存分享【友商商品】的记录",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/saveShareLeaguerProduct")
    @RequiresPermissions("pro:check:onSaleProduct")
    public BaseResult saveShareLeaguerProduct(@RequestParam Map<String, String> params,
                                              @Valid ParamShareProductSave save, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProShare share = new ProShare();
        share.setFkShpShopId(0);
        share.setFkShpUserId(getUserId());
        share.setShopNumber("0");
        share.setUserNumber(getUserNumber());
        //友商商品不显示任何价格
        share.setShowPrice("");
        share.setProId(save.getProIds());

        String saveBatch = proShareService.saveShareProduct(share);
        return LocalUtils.getBaseResult(saveBatch);
    }


    /**
     * 统计图片被下载次数,并返回总下载次数;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "统计图片被下载次数,并返回总下载次数;",
            notes = "统计图片被下载次数,并返回总下载次数;",
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
                    name = "bizId", value = "商品业务id"),
    })
    @RequestRequire
    @PostMapping("/countDownload")
    public BaseResult countDownload(@RequestParam Map<String, String> params, HttpServletRequest request) {
        String bizId = params.get("bizId");
        int shopId = getShopId();
        //全店通不统计下载记录
        if (shopId == 10684) {
            return LocalUtils.getBaseResult(1);
        }
        boolean existsProduct = proProductService.existsProductByBizId(shopId, bizId);
        int downloadNum = 0;
        if (existsProduct) {
            proDownloadImgService.saveProDownloadImg(shopId, getUserId(), bizId, request);
            downloadNum = proDownloadImgService.countDownload(shopId, bizId);
            String key = ConstantRedisKey.getNotDownloadProductKey(getShopId(), bizId);
            String keyValue = LocalUtils.returnEmptyStringOrString(redisUtil.get(key));
            //下载过该商品图片的,标记一下;
            if (LocalUtils.isEmptyAndNull(keyValue) || !keyValue.contains(getUserId() + "")) {
                keyValue += getUserId() + ",";
            }
            redisUtil.set(key, keyValue);
        }

        return LocalUtils.getBaseResult(downloadNum);
    }

    /**
     * 一键上架友商相册
     *
     * @param
     * @return
     */
    @ApiOperation(
            value = "一键上架友商相册",
            notes = "一键上架友商相册;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "share", value = "上架友商相册:22 下架:10"),
    })
    @PostMapping(value = "/updateShareStare")
    public BaseResult updateShareStare(@RequestParam(value = "share", required = true) String share) {
        int shopId = getShopId();
        proProductService.updateShareStare(shopId, share);
        return BaseResult.okResult();
    }

    /**
     * 获取商品操作记录
     *
     * @param modifyRecordSearch
     * @return
     */
    @ApiOperation(
            value = "获取商品操作记录",
            notes = "获取商品操作记录",
            httpMethod = "POST")
    @PostMapping("/listShpOperateLog")
    @RequiresPermissions(value = {"pro:check:proOperateLog", "shp:list:operateLog"}, logical = Logical.OR)
    public BaseResult<VoModifyRecordByPage> listShpOperateLog(@Valid ParamModifyRecordSearch modifyRecordSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        modifyRecordSearch.setShopId(shopId);
        modifyRecordSearch.setCurrentUserId(getUserId());
        VoModifyRecordByPage modifyRecordByPage = proModifyRecordService.getModifyRecordByPage(modifyRecordSearch);

        return BaseResult.okResult(modifyRecordByPage);
    }


}
