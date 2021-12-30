package com.luxuryadmin.apiadmin.pro;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.op.OpUploadDownloadService;
import com.luxuryadmin.service.ord.OrdOrderService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
import com.luxuryadmin.vo.shp.VoEmployee;
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
import java.text.ParseException;
import java.util.*;

/**
 * @author monkey king
 * @Date 2019/12/01 4:07
 */
@Slf4j
@RestController
@Api(tags = {"C003.【商品】模块--2.6.5❤"})
@RequestMapping(value = "/shop/admin/pro")
public class ProProductController extends ProProductBaseController {

    @Autowired
    private ProDownloadImgService proDownloadImgService;

    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;
    @Autowired
    private OrdOrderService ordOrderService;
    @Autowired
    private ProShareService proShareService;

    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private ProTempProductService proTempProductService;
    @Autowired
    private ProModifyRecordService proModifyRecordService;
    /**
     * 获取仓库统计
     *
     * @param paramToken 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取仓库统计",
            notes = "获取仓库统计",
            httpMethod = "GET")
    @GetMapping("/countProduct")
    public BaseResult countProduct(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ParamProductQuery queryParam = new ParamProTempProductQuery();
        queryParam.setShopId(getShopId());
        queryParam.setUserId(getUserId());
        queryParam.setStateCode("all");
        queryParam.setAttributeCode(getAllProAttributeCodeExcludePawnPro(";"));
        formatQueryParam(queryParam);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        //统计商品总数,商品总成本
        Map<String, String> totalNumMap = proProductService.countLeftNumAndInitPrice(queryParam);
        //统计今日上传,今日上架
        String today = DateUtil.formatShort(new Date());
        queryParam.setTodayDate(today);
        //只统计今天的上传上架数据, 需要把查询时间设置为空,把状态设置为all;才不会因为状态和查询时间而改变查询结果;
        queryParam.setUploadStDateTime(null);
        queryParam.setUploadEtDateTime(null);
        Integer upload = proProductService.countTodayUpload(queryParam);
        Integer release = proProductService.countTodayOnRelease(queryParam);
        hashMap.put("todayUpload", upload);
        hashMap.put("todayOnRelease", release);
        if (!hasPermWithCurrentUser(ConstantPermission.CHK_PRICE_INIT)) {
            totalNumMap.put("totalPrice", "******");
        }
        hashMap.putAll(totalNumMap);
        //是否有查看仓库资产权限
        String chkStoreTotalPrice = ConstantPermission.CHK_OWN_PRODUCT;
        hashMap.put("uPermStoreTotalPrice", hasPermToPageWithCurrentUser(chkStoreTotalPrice));
        return BaseResult.okResult(hashMap);
    }


    /**
     * 初始化【商品】页面查询参数;
     *
     * @param paramToken token和基础参数
     * @return Result
     */
    @ApiOperation(
            value = "初始化【商品】页面查询参数;",
            notes = "初始化【商品】页面查询参数;",
            httpMethod = "POST")
    @RequestMapping("/initQueryParam")
    public BaseResult initQueryParam(@Valid ParamToken paramToken, BindingResult result) throws ControllerException {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoEmployee> listUploadUser = shpUserShopRefService.listUploadUser(shopId);
        //商品分类
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("classifyList", voProClassifyList);
        //商品成色
        //hashMap.put("proQualityList", voProQualityList);
        //入库人员
        hashMap.put("listUploadUser", listUploadUser);

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
        hashMap.put("storeList", list);
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
    @RequiresPermissions("pro:check:onSaleProduct")
    public BaseResult listOnSaleProduct(@RequestParam Map<String, String> params,
                                        @Valid ParamProductQuery queryParam, BindingResult result) {
        queryParam.setStateCode("onSale");
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
        if (LocalUtils.isEmptyAndNull(queryParam.getAttributeCode())) {
            queryParam.setAttributeCode(getAllProAttributeCodeExcludePawnPro(";"));
        }
        //独立编码不支持模糊查询
        queryParam.setUniqueCode(queryParam.getProName());
        formatQueryParam(queryParam);
        PageHelper.startPage(getPageNum(), getPageSize());
        List<VoProductLoad> proProductList = proProductService.listProProductByVoProductQueryParam(queryParam);
        String sortKey = queryParam.getSortKey();
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, proProductList, sortKey);
        //仅针对在售商品有标识符
        if ("onSale".equals(queryParam.getStateCode())) {
            for (VoProductLoad vo : proProductList) {
                VoProRedisNum proRedisNum = proProductService.getProRedisNum(vo.getShopId(), vo.getBizId());
                //判断是否锁单商品
                vo.setLockState(proRedisNum.getLockNum() > 0 ? "1" : "0");

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
        PageInfo pageInfo = new PageInfo(proProductList);
        pageInfo.setList(proProductList);

        Map<String, Object> hashMap = LocalUtils.convertBeanToMap(pageInfo);

        //编辑商品信息
        String updateProPerm = ConstantPermission.MOD_UPDATE_INFO;
        String hasUpdateProPerm = hasPermToPageWithCurrentUser(updateProPerm);
        hashMap.put("uPermUpdatePro", hasUpdateProPerm);
        //删除商品
        String deleteProPerm = ConstantPermission.MOD_DELETE_PRO;
        String hasDeleteProPerm = hasPermToPageWithCurrentUser(deleteProPerm);
        hashMap.put("uPermDeletePro", hasDeleteProPerm);


        return BaseResult.okResult(hashMap);
    }


    /**
     * 获取商品详情;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取商品详情;根据业务id;",
            notes = "获取商品详情;根据业务id;",
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
        VoProductLoad vo = proProductService.getProductDetailByShopIdBizId(shopId, bizId);
        if (LocalUtils.isEmptyAndNull(vo)) {
            return BaseResult.defaultErrorWithMsg("商品不存在!");
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

        }
        //临时仓售卖商品数量
        Integer saleTempNum = ordOrderService.getOrderNumByTemp(getShopId(), vo.getProId(), null);
        vo.setSaleTempNum(saleTempNum);
        if (saleTempNum == null) {
            vo.setSaleTempNum(0);
        }
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, vo, null);
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
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(vo);
        hashMap.put("uPermEdit", hasPermToPageWithCurrentUser(editPerm));
        hashMap.put("uPermLock", hasPermToPageWithCurrentUser(lockPerm));
        hashMap.put("uPermRelease", hasPermToPageWithCurrentUser(releasePerm));
        hashMap.put("uPermBackOff", hasPermToPageWithCurrentUser(releasePerm));
        hashMap.put("uPermOpen", hasPermToPageWithCurrentUser(openPerm));
        //赎回权限和开单合并
        hashMap.put("uPermRedeem", hasPermToPageWithCurrentUser(openPerm));

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
        }
        hashMap.put("uPermUnlockOpen", uPermUnlockOpen);

        return BaseResult.okResult(hashMap);
    }


    /**
     * 锁单记录
     *
     * @param param 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "锁单记录",
            notes = "锁单记录",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @GetMapping("/loadLockProductList")
    public BaseResult loadLockProductList(@Valid ParamProductBizId param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        List<VoProLockRecord> lockRecordList = proLockRecordService.listVoProLockRecordByBizId(getShopId(), param.getBizId());
        return LocalUtils.getBaseResult(lockRecordList);
    }


    /**
     * 保存分享【本店商品】的记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "保存分享【本店商品】的记录",
            notes = "保存分享【本店商品】的记录",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/saveShareProduct")
    @RequiresPermissions("pro:check:onSaleProduct")
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
        String proIds = save.getProIds();
        if (!LocalUtils.isEmptyAndNull(proIds)) {
            String[] proIdArray = proIds.split(",");
            for (String proId : proIdArray) {
                //添加分享记录
                if (LocalUtils.isNumber(proId)) {
                    addProModifyRecord(Integer.parseInt(proId), "分享", "", "","pc");
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
     * 获取商品操作记录
     *
     * @param modifyRecordSearch
     * @return
     */
    @ApiOperation(
            value = "获取商品操作记录--2.6.5❤",
            notes = "获取商品操作记录--2.6.5❤",
            httpMethod = "POST")
    @PostMapping("/listShpOperateLog")
    public BaseResult<VoModifyRecordByPage> listShpOperateLog(@Valid ParamModifyRecordSearch modifyRecordSearch, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        modifyRecordSearch.setShopId(shopId);
        modifyRecordSearch.setCurrentUserId(getUserId());
        VoModifyRecordByPage modifyRecordByPage = proModifyRecordService. getModifyRecordByPage(modifyRecordSearch);

        return BaseResult.okResult(modifyRecordByPage);
    }

}
