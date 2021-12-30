package com.luxuryadmin.api.pro;

import com.github.pagehelper.PageHelper;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.enums.EnumCode;
import com.luxuryadmin.common.exception.ControllerException;
import com.luxuryadmin.common.exception.MyException;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.pro.ProLockRecord;
import com.luxuryadmin.entity.pro.ProProduct;
import com.luxuryadmin.enums.pro.EnumDynamicProductState;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.*;
import com.luxuryadmin.service.op.OpPushService;
import com.luxuryadmin.service.pro.*;
import com.luxuryadmin.service.util.ProProductBaseController;
import com.luxuryadmin.vo.pro.*;
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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author monkey king
 * @date 2020-05-29 19:30:18
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/pro")
@Api(tags = {"C003.【商品】模块 --2.6.2"}, description = "/shop/user/pro |用户【仓库】模块相关")
public class ProLockRecordController extends ProProductBaseController {

    @Autowired
    private ProLockRecordService proLockRecordService;

    @Autowired
    private OpPushService opPushService;


    @Autowired
    private ProClassifyService proClassifyService;

    @Autowired
    private ProDeliverService proDeliverService;

    @Autowired
    private ProDynamicProductService proDynamicProductService;
    @Autowired
    private ProConveyProductService proConveyProductService;

    /**
     * 初始化锁单列表
     *
     * @return Result
     */
    @ApiOperation(
            value = "初始化锁单列表v--2.6.2",
            notes = "初始化锁单列表v--2.6.2",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/initLockProduct")
    public BaseResult initLockProduct(@Valid ParamToken param, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        List<VoProLockRecord> lockUserList = proLockRecordService.listLockUser(shopId);
        int countMyLockNum = proLockRecordService.countMyLockNum(shopId, userId);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("lockUserList", lockUserList);
        hashMap.put("classifyList", voProClassifyList);
        hashMap.put("myLockNumTxt", "我锁单数量：" + countMyLockNum);
        hashMap.put("currentUserId", getUserId());
        hashMap.put("queryTip", "支持搜索商品名称/描述/独立编码/系统编号");
        return BaseResult.okResult(hashMap);
    }


    /**
     * 加载【锁单中】的商品
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【锁单中】的商品v--2.6.2",
            notes = "加载【锁单中】的商品v--2.6.2",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/loadLockProduct")
    @RequiresPermissions("pro:check:lockProduct")
    public BaseResult loadLockProduct(@Valid ParamLockProductQuery params, BindingResult result) {
        servicesUtil.validControllerParam(result);
        formatLockProductQueryParam(params);
        PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
        List<VoProductLoad> lockProductList = proLockRecordService.listLockProductByParam(params);
        if (LocalUtils.isEmptyAndNull(lockProductList)) {
            return BaseResult.okResultNoData();
        }
        formatLockProduct(lockProductList);
        VoProLockTotal voProLockTotal = proLockRecordService.countProLockTotal(params);
        voProLockTotal.setCountInitPrice(LocalUtils.formatPriceSpilt(voProLockTotal.getCountInitPrice()));
        voProLockTotal.setCountPreMoney(LocalUtils.formatPriceSpilt(voProLockTotal.getCountPreMoney()));
        voProLockTotal.setCountPreFinishMoney(LocalUtils.formatPriceSpilt(voProLockTotal.getCountPreFinishMoney()));
        voProLockTotal.setCountLockNumTxt("锁单数量(个)");
        voProLockTotal.setCountPreMoneyTxt("定金总额(元)");
        voProLockTotal.setCountInitPriceTxt("商品总成本(元)");
        voProLockTotal.setCountPreFinishMoneyTxt("预计成交总额(元)");
        //是否有查看成本价的权限;
        String chkPriceInit = ConstantPermission.CHK_PRICE_INIT;
        boolean hasInitPricePerm = hasPermWithCurrentUser(chkPriceInit);
        if (!hasInitPricePerm) {
            String countLockNum = voProLockTotal.getCountLockNum();
            String countLockNumTxt = voProLockTotal.getCountLockNumTxt();
            voProLockTotal = new VoProLockTotal();
            voProLockTotal.setCountLockNumTxt(countLockNumTxt);
            voProLockTotal.setCountLockNum(countLockNum);
        }
        Map<String, Object> hashMap = LocalUtils.convertBeanToMap(voProLockTotal);
        hashMap.put("objList", lockProductList);
        hashMap.put("uPermInitPrice", hasPermToPageWithCurrentUser(chkPriceInit));
        return BaseResult.okResult(hashMap);
    }


    /**
     * 获取【锁单中】商品的详情v2.6.2
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取【锁单中】商品的详情v--2.6.2",
            notes = "获取【锁单中】商品的详情v--2.6.2",
            httpMethod = "GET")
    @RequestRequire
    @GetMapping("/getLockProductDetail")
    public BaseResult getLockProductDetail(@Valid ParamLockProductDetail params, BindingResult result) throws Exception {
        servicesUtil.validControllerParam(result);
        String lockId = params.getLockId();
        int shopId = getShopId();
        VoProLockRecord lockRecord = proLockRecordService.getProLockRecordByIdAndShopId(shopId, Integer.parseInt(lockId));
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            return BaseResult.okResultNoData();
        }
        String bizId = lockRecord.getBizId();
        VoProductLoad lockProductDetail = proLockRecordService.getProductDetail(getShopId(), bizId);
        formatLockProduct(lockProductDetail);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(lockProductDetail);
        hashMap.put("lockRecord", lockRecord);

        //判断锁单商品详情是否有3种权限,编辑,解锁,开单;
        String lockPerm = ConstantPermission.MOD_LOCK_PRODUCT;
        String unlockAllPerm = ConstantPermission.MOD_UNLOCK_PRODUCT;
        String modConfirmOrd = ConstantPermission.MOD_CONFIRM_ORD;
        boolean isCurrentUser = lockRecord.getLockUserId() == getUserId();
        boolean isUnlockAllPerm = hasPermWithCurrentUser(unlockAllPerm);
        boolean isLockPerm = hasPermWithCurrentUser(lockPerm);
        boolean isConfirmOrd = hasPermWithCurrentUser(modConfirmOrd);
        //权限分是自己锁单还是别人锁单;
        if (isCurrentUser) {
            //自己锁单
            //自己锁单记录只允许自己编辑;
            if (isLockPerm) {
                hashMap.put("uPermLockEdit", "1");
            }
            //解锁功能:需要个人解锁权限或者解锁全部权限
            if (isLockPerm || isUnlockAllPerm) {
                hashMap.put("uPermUnlock", "1");
                //开单功能:需要个人解锁权限 或 解锁全部权限 还有开单权限;
                if (isConfirmOrd) {
                    hashMap.put("uPermOpenOrd", "1");
                }
            }
        } else {
            //查看他人锁单的详情时,不允许出现编辑功能;
            //解锁功能: 需要解锁全部权限
            if (isUnlockAllPerm) {
                hashMap.put("uPermUnlock", "1");
                //开单功能: 需要开单权限;
                if (isConfirmOrd) {
                    hashMap.put("uPermOpenOrd", "1");
                }
            }
        }
        //拥有”解锁全部权限“可以编辑锁单原因；修改与2021-10-22 18:46:53；v2.6.5
        if (isUnlockAllPerm) {
            hashMap.put("uPermLockEdit", "1");
        }
        //寄卖传送关联锁单不可有解锁和开单权限;
        if (!LocalUtils.isEmptyAndNull(lockRecord.getSonRecordId())){
            hashMap.put("uPermLockEdit", "0");
            hashMap.put("uPermUnlock", "0");
            hashMap.put("uPermOpenOrd", "0");
        }

        return BaseResult.okResult(hashMap);
    }


    /**
     * 锁单;每次锁单都是一条新的记录;v2.6.2
     *
     * @return Result
     */
    @ApiOperation(
            value = "锁定商品(锁单);根据业务id;v--2.6.2",
            notes = "锁定商品(锁单);根据业务id;v--2.6.4 -- zs",
            httpMethod = "POST")
    @RequestRequire
    @PostMapping("/lockPro")
    @RequiresPermissions("pro:lockProduct")
    public BaseResult lockPro(@Valid ParamProductLock lockParam, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        String myLockId = "lockProduct";
        try {
            String bizId = lockParam.getBizId();
            int shopId = getShopId();
            int userId = getUserId();
            lockParam.setShopId(shopId);
            lockParam.setUserId(userId);
            redisUtil.synchronizedLock(myLockId, 10, 50);
            ProProduct proProduct = getProProductByShopIdBizId(shopId, bizId);
            int lockId = proLockRecordService.lockProductReturnId(lockParam, request);

            try {
                //2.6.6 商品位置修改
                BasicParam basicParam = getBasicParam();
                if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                   //多个地方使用抽离方法
                    proDynamicProductService.updateListStateByProId(lockParam,proProduct);
                }
            } catch (Exception e) {
                throw new MyException("商品列表动态加载错误" + e);
            }

            redisUtil.synchronizedUnlock(myLockId);
            VoProRedisNum proRedisNum = proProductService.getProRedisNum(getShopId(), bizId);
            ProLockRecord plr = proLockRecordService.getProLockRecordById(lockId);
            String remark = getLockRemark(plr);
            //添加商品修改记录
            addProModifyRecord(proProduct.getId(), "锁单", "", remark, lockParam.getPlatform());
            opPushService.pushLockProductMsg(shopId, proProduct.getName(), bizId, lockId + "");
            //关联添加锁单记录
            proConveyProductService.checkLockPro(plr,proProduct,lockParam,request);
            return LocalUtils.getBaseResult(proRedisNum);
        } catch (Exception e) {
            redisUtil.synchronizedUnlock(myLockId);
            throw new ControllerException("错误为"+e.getMessage(), e);
        }
    }


    /**
     * 解锁商品;手动解锁商品;v2.6.2
     *
     * @return Result
     */
    @ApiOperation(
            value = "解锁商品(解锁);根据业务id;v--2.6.2",
            notes = "解锁商品(解锁);根据业务id;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/unlockPro")
    public BaseResult unlockPro(@Valid ParamProductUnlock unlockParam, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);

        int lockNum = Integer.parseInt(unlockParam.getLockNum());
        String lockIdStr = unlockParam.getLockId();
        int unlockUserId = getUserId();
        int lockId;
        String bizId = unlockParam.getBizId();
        ProLockRecord lockRecord;
        if (LocalUtils.isEmptyAndNull(lockIdStr)) {
            //商品详情过来的解锁情况;
            List<ProLockRecord> lockList = proLockRecordService.lisProLockRecordByBizId(getShopId(), bizId);
            if (LocalUtils.isEmptyAndNull(lockList)) {
                return BaseResult.defaultErrorWithMsg("暂无锁单记录!");
            }
            //如果锁单记录为1条时,拥有解锁权限,包括解锁全部权限, 则直接解锁该记录;
            if (lockList.size() == 1) {
                lockRecord = lockList.get(0);
            } else {
                //如果锁单记录为>1条时,则需要跳转到锁单列表;调用商品详情跳转到锁单商品列表的接口;参数为"#系统编码";
                return BaseResult.errorResult(EnumCode.ERROR_MOVE_LOCK);
            }
        } else {
            //直接锁单列表进来的解锁
            lockId = Integer.parseInt(lockIdStr);
            lockRecord = proLockRecordService.getProLockRecordById(lockId);
            if (LocalUtils.isEmptyAndNull(lockRecord)) {
                log.info("解锁失败,没找到解锁记录! lockRecordId:{} ", lockId);
                return BaseResult.defaultErrorWithMsg("解锁失败,没找到解锁记录!");
            }
        }
        int lockUserId = lockRecord.getLockUserId();
        if (lockUserId != unlockUserId) {
            //锁单和解锁不是同一个用户, 需要解锁全部的权限;
            boolean hasPerm = servicesUtil.hasPermission(getShopId(), unlockUserId, ConstantPermission.MOD_UNLOCK_PRODUCT);
            if (!hasPerm) {
                return BaseResult.defaultErrorWithMsg("解锁他人锁单记录,需要【解锁全部】权限!");
            }
        }
        boolean isDelDeliver = false;
        if (lockRecord.getLockNum() - lockNum == 0) {
            isDelDeliver = true;
        }

        try {
            //判断版本控制是否在2.6.6或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                int shopId = getShopId();
                ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId, bizId);
                if (proProduct != null && proProduct.getTotalNum() == lockNum) {
                    proDynamicProductService.updateStateByProId(proProduct.getId(), EnumDynamicProductState.NORMAL.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }

        VoProRedisNum proRedisNumBefore = proProductService.getProRedisNum(getShopId(), bizId);
        lockRecord.setAddress(unlockParam.getAddress());
        proLockRecordService.unlockProductByUserId(lockRecord, unlockUserId, lockNum, request);
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(getShopId(), bizId);
        //添加商品操作记录
        addProModifyRecord(lockRecord.getFkProProductId(), "解锁",
                "解锁前数量: " + proRedisNumBefore.getLockNum(), "解锁后数量: " + proRedisNum.getLockNum(), unlockParam.getPlatform());
        //锁单数量为0时，删除发货代发货信息
        if (isDelDeliver) {
            proDeliverService.deleteDeliverInfoByLockId(lockRecord.getId());
        }
        //解锁商品进行寄卖传送关联解锁
        lockRecord.setLockNum(lockNum);
        proConveyProductService.lockRecordForConvey(lockRecord,request);
        return LocalUtils.getBaseResult(proRedisNum);
    }

    /**
     * 编辑锁单原因;需要有解锁权限才能编辑;如果不是自己的锁单商品,则需要解锁全部权限,才能编辑;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "编辑锁单详情;v--2.6.2",
            notes = "编辑锁单详情;v--2.6.4 -- zs",
            httpMethod = "POST")
    @PostMapping("/updateLockProduct")
    public BaseResult updateLockProduct(@Valid ParamProductLock params, BindingResult result) {
        servicesUtil.validControllerParam(result);

        ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(Integer.parseInt(params.getLockId()));
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            log.info("解锁失败,没找到解锁记录! lockRecordId:{} ", params.getLockId());
            return BaseResult.defaultErrorWithMsg("解锁失败,没找到解锁记录!");
        }
        String before = getLockRemark(lockRecord);
        int updateLock = Integer.parseInt(params.getLockNum());
        Integer dbLockNum = lockRecord.getLockNum();
        if (updateLock > dbLockNum) {
            return BaseResult.defaultErrorWithMsg("变更后的锁单数量不允许超过原锁单数量!");
        }
        if (updateLock == 0) {
            return BaseResult.defaultErrorWithMsg("如需更新锁单数量为0,请执行全部解锁操作!");
        }
        lockRecord.setUpdateTime(new Date());
        lockRecord.setLockNum(updateLock);
        lockRecord.setRemark(params.getLockReason());
        lockRecord.setPreMoney(LocalUtils.formatBigDecimal(LocalUtils.isEmptyAndNull(params.getPreMoney()) ? "0" : params.getPreMoney()));
        lockRecord.setPreFinishMoney(LocalUtils.formatBigDecimal(LocalUtils.isEmptyAndNull(params.getPreFinishMoney()) ? "0" : params.getPreFinishMoney()));
        lockRecord.setAddress(params.getAddress());
        proLockRecordService.updateProLockRecord(lockRecord);
        proDeliverService.updateNum(lockRecord);
        int subLockNum = dbLockNum - updateLock;
        proProductService.updateProRedisLockNum(getShopId(), lockRecord.getProBizId(), subLockNum, false);
        String after = getLockRemark(lockRecord);
        //添加商品操作记录
        addProModifyRecord(lockRecord.getFkProProductId(), "修改锁单", before, after, params.getPlatform());
        return BaseResult.okResult();
    }

    /**
     * 格式化锁单日志格式
     *
     * @param plr
     * @return
     */
    private String getLockRemark(ProLockRecord plr) {
        String before = "";
        try {
            String preMoney = LocalUtils.calcNumber(plr.getPreMoney(), "*", 0.01).toString();
            String preFinishMoney = LocalUtils.calcNumber(plr.getPreFinishMoney(), "*", 0.01).toString();
            before = "锁单数量: " + plr.getLockNum() + ";定金:" + LocalUtils.formatPriceSpilt(preMoney) +
                    ";预成交价:" + LocalUtils.formatPriceSpilt(preFinishMoney) + ";原因:" + plr.getRemark();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return before;
    }


    /**
     * 格式化锁单商品的查询参数
     *
     * @param params
     */
    private void formatLockProductQueryParam(ParamLockProductQuery params) {
        int shopId = getShopId();
        //是否有查看全部锁单的权限,锁单不需要查看全部的权限;
        //String chkAllLockProduct = ConstantPermission.CHK_ALL_LOCK_PRODUCT;
        //boolean hasPerm = hasPermWithCurrentUser(chkAllLockProduct);
        //if (!hasPerm) {
        //    //只能查看自己的锁单商品
        //    params.setLockUserId(getUserId() + "");
        //}
        params.setShopId(shopId);
        String keyword = params.getKeyword();
        String uniqueCode = keyword;
        //如果有查询名称参数
        if (!LocalUtils.isEmptyAndNull(keyword)) {
            keyword = keyword.trim();
            keyword = keyword.replaceAll("\\s+", ".*");
            if (keyword.startsWith("#")) {
                String newKeyWord = keyword.substring(1);
                if (LocalUtils.isNumber(newKeyWord)) {
                    //符合直接查询系统编号;不进行商品其它信息的模糊查询
                    uniqueCode = newKeyWord;
                    keyword = null;
                }
            }
        }

        String userId = params.getLockUserId();
        if (!LocalUtils.isEmptyAndNull(userId)) {
            String[] userIdArray = LocalUtils.splitString(userId, ";");
            String newUserId = LocalUtils.packString(userIdArray);
            params.setLockUserId(newUserId);
        }
        final String desc = "DESC";
        String sortValue = params.getSortValue();
        String sortKey = params.getSortKey();
        //默认锁单时间降序
        String sortKeyDb = "lck.`insert_time` DESC, lck.`id` DESC";
        switch (sortKey + "") {
            //按照销售价格排序
            case "price":
                if (desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "lck.`pre_money` DESC, lck.`id` DESC";
                } else {
                    sortKeyDb = "lck.`pre_money` ASC, lck.`id` ASC";
                }
                break;
            //按照时间排序或者按照默认排序;
            case "time":
                if (!desc.equalsIgnoreCase(sortValue)) {
                    sortKeyDb = "lck.`insert_time` ASC, lck.`id` ASC";
                }
                break;
            default:
                break;
        }
        params.setKeyword(keyword);
        params.setUniqueCode(uniqueCode);
        params.setSortKeyDb(sortKeyDb);
    }


    /**
     * 加载【锁单中】的商品
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【锁单中】的商品",
            notes = "加载【锁单中】的商品",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "pageNum", value = "当前页数"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "keyword", value = "搜索关键字"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/listLockProduct")
    @RequiresPermissions("pro:check:lockProduct")
    public BaseResult listLockProduct(@RequestParam Map<String, String> params) {
        try {
            String keyword = params.get("keyword");
            PageHelper.startPage(getPageNum(), PAGE_SIZE_10);
            String uniqueCode = keyword;
            //如果有查询名称参数
            if (!LocalUtils.isEmptyAndNull(keyword)) {
                keyword = keyword.trim();
                keyword = keyword.replaceAll("\\s+", ".*");
            }
            List<VoProductLoad> lockProductList = proLockRecordService.listLockProductByShopId(getShopId(), keyword, uniqueCode);
            String appVersion = getBasicParam().getAppVersion();
            formatVoProductLoad(appVersion, lockProductList, null);
            return LocalUtils.getBaseResult(lockProductList);
        } catch (Exception e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }


    /**
     * 获取【锁单中】商品的详情
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "获取【锁单中】商品的详情",
            notes = "获取【锁单中】商品的详情",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "token登录标识符"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "bizId", value = "商品业务逻辑id"),
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @GetMapping("/getLockProductRecordDetail")
    public BaseResult getLockProductRecordDetail(@RequestParam Map<String, String> params) {
        String bizId = params.get("bizId");
        VoProductLoad lockProductDetail = proLockRecordService.getProductDetail(getShopId(), bizId);
        String appVersion = getBasicParam().getAppVersion();
        formatVoProductLoad(appVersion, lockProductDetail, null);
        List<VoProLockRecord> lockRecordList = proLockRecordService.listVoProLockRecordByBizId(getShopId(), bizId);
        HashMap<String, Object> hashMap = LocalUtils.getHashMap(lockProductDetail);
        hashMap.put("lockRecordList", lockRecordList);
        return BaseResult.okResult(hashMap);
    }

    /**
     * 锁单;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "锁定商品(锁单);根据业务id;",
            notes = "锁定商品(锁单);根据业务id;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/lockProduct")
    @RequiresPermissions("pro:lockProduct")
    public BaseResult lockProduct(@RequestParam Map<String, String> params,
                                  @Valid ParamProductLock lockParam, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);
        String myLockId = "lockProduct";
        try {

            String bizId = lockParam.getBizId();
            int lockNum = Integer.parseInt(lockParam.getLockNum());
            int shopId = getShopId();
            int userId = getUserId();
            redisUtil.synchronizedLock(myLockId, 10, 50);
            ProProduct proProduct = getProProductByShopIdBizId(shopId, bizId);
            //获取锁单前数据
            VoProRedisNum proRedisNum1 = proProductService.getProRedisNum(getShopId(), bizId);
            proLockRecordService.lockProductByUserId(proProduct, userId, lockNum, lockParam, request);
            redisUtil.synchronizedUnlock(myLockId);
            VoProRedisNum proRedisNum = proProductService.getProRedisNum(getShopId(), bizId);
            //锁单前数量
            Integer lockNum1 = 0;
            if (proRedisNum1 != null) {
                lockNum1 = proRedisNum1.getLockNum();
            }
            //添加商品修改记录
            addProModifyRecord(proProduct.getId(), "锁单", "锁单前数量: " + lockNum1, "锁单后数量: " + proRedisNum.getLockNum(), lockParam.getPlatform());

            opPushService.pushLockProductMsg(shopId, proProduct.getName(), bizId, "");

            return LocalUtils.getBaseResult(proRedisNum);
        } catch (Exception e) {
            redisUtil.synchronizedUnlock(myLockId);
            throw new ControllerException(e.getMessage(), e);
        }
    }


    /**
     * 解锁商品;手动解锁商品;
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "解锁商品(解锁);根据业务id;",
            notes = "解锁商品(解锁);根据业务id;",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @PostMapping("/unlockProduct")
    public BaseResult unlockProduct(@RequestParam Map<String, String> params,
                                    @Valid ParamProductUnlock unlockParam, BindingResult result, HttpServletRequest request) {
        servicesUtil.validControllerParam(result);

        int lockNum = Integer.parseInt(unlockParam.getLockNum());
        String lockIdStr = unlockParam.getLockId();
        int unlockUserId = getUserId();
        Integer lockId;
        String bizId = unlockParam.getBizId();
        if (LocalUtils.isEmptyAndNull(lockIdStr)) {
            lockId = proLockRecordService.getLockIdByBizId(getShopId(), bizId);
        } else {
            lockId = Integer.parseInt(lockIdStr);
        }
        if (LocalUtils.isEmptyAndNull(lockId)) {
            return BaseResult.defaultErrorWithMsg("暂无锁单记录!");
        }
        ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(lockId);
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            log.info("解锁失败,没找到解锁记录! lockRecordId:{} ", lockId);
            return BaseResult.defaultErrorWithMsg("解锁失败,没找到解锁记录!");
        }
        Boolean isDelDeliver = false;
        if (lockRecord.getLockNum() - lockNum == 0) {
            isDelDeliver = true;
        }
        int lockUserId = lockRecord.getLockUserId();
        if (lockUserId != unlockUserId) {
            //锁单和解锁不是同一个用户, 需要解锁全部的权限;
            boolean hasPerm = servicesUtil.hasPermission(getShopId(), unlockUserId, ConstantPermission.MOD_UNLOCK_PRODUCT);
            if (!hasPerm) {
                return BaseResult.defaultErrorWithMsg("解锁他人锁单记录,需要【解锁全部】权限!");
            }
        }

        try {
            //判断版本控制是否在2.6.6或以上版本
            BasicParam basicParam = getBasicParam();
            if (VersionUtils.compareVersion(basicParam.getAppVersion(), "2.6.6") >= 0) {
                int shopId = getShopId();
                ProProduct proProduct = proProductService.getProProductByShopIdBizId(shopId, bizId);
                if (proProduct != null && proProduct.getTotalNum() == lockNum) {
                    proDynamicProductService.updateStateByProId(proProduct.getId(), EnumDynamicProductState.NORMAL.getCode(), shopId);
                }
            }
        } catch (Exception e) {
            throw new MyException("商品修改动态错误" + e);
        }


        proLockRecordService.unlockProductByUserId(lockRecord, unlockUserId, lockNum, request);
        int unLockNum2 = lockRecord.getLockNum() - lockNum;
        VoProRedisNum proRedisNum = proProductService.getProRedisNum(getShopId(), bizId);

        //添加商品操作记录
        addProModifyRecord(lockRecord.getFkProProductId(), "解锁", "解锁前数量: " + lockRecord.getLockNum(), "解锁后数量: " + unLockNum2, unlockParam.getPlatform());
        //锁单数量为0时，删除发货代发货信息
        if (isDelDeliver) {
            proDeliverService.deleteDeliverInfoByLockId(lockRecord.getId());
        }
        return LocalUtils.getBaseResult(proRedisNum);
    }


    /**
     * 编辑锁单原因
     *
     * @param productLockRemark 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "编辑锁单原因2.5.5;",
            notes = "编辑锁单原因2.5.5;根据业务id;",
            httpMethod = "POST")
    @PostMapping("/updateLockProductState")
    public BaseResult updateLockProductState(
            @Valid ParamProductLockRemark productLockRemark, BindingResult result) {
        servicesUtil.validControllerParam(result);

        ProLockRecord lockRecord = proLockRecordService.getProLockRecordById(Integer.parseInt(productLockRemark.getLockId()));
        if (LocalUtils.isEmptyAndNull(lockRecord)) {
            log.info("解锁失败,没找到解锁记录! lockRecordId:{} ", productLockRemark.getLockId());
            return BaseResult.defaultErrorWithMsg("解锁失败,没找到解锁记录!");
        }
        proLockRecordService.updateProLockRecordForRemark(productLockRemark);

        //添加商品操作记录
        addProModifyRecord(lockRecord.getFkProProductId(), "编辑锁单原因", "#编辑前原因# " + lockRecord.getRemark(), "#编辑后原因# " + productLockRemark.getLockReason(), productLockRemark.getPlatform()
        );

        return BaseResult.okResult();
    }
}
