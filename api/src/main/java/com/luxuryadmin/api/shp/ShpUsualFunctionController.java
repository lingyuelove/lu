package com.luxuryadmin.api.shp;

import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.common.utils.VersionUtils;
import com.luxuryadmin.entity.shp.ShpUserPermissionRef;
import com.luxuryadmin.entity.shp.ShpUsualFunction;
import com.luxuryadmin.enums.shp.EnumShpUserType;
import com.luxuryadmin.mapper.shp.ShpUserPermissionRefMapper;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.ParamProductQuery;
import com.luxuryadmin.param.shp.ParamUsualFunction;
import com.luxuryadmin.service.pro.ProClassifyService;
import com.luxuryadmin.service.pro.ProProductService;
import com.luxuryadmin.service.shp.ShpPermissionService;
import com.luxuryadmin.service.shp.ShpUserPermissionRefService;
import com.luxuryadmin.service.shp.ShpUserShopRefService;
import com.luxuryadmin.service.shp.ShpUsualFunctionService;
import com.luxuryadmin.vo.pro.VoProClassify;
import com.luxuryadmin.vo.shp.VoShopUserPricePerm;
import com.luxuryadmin.vo.shp.VoUsualFunction;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.*;

/**
 * 首页模块;常用服务和全部服务
 *
 * @author monkey king
 * @date 2019-12-30 15:09:24
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user", method = RequestMethod.POST)
@Api(tags = {"B001.【首页】模块"}, description = "/shop/user |【首页】模块的接口")
public class ShpUsualFunctionController extends BaseController {

    @Autowired
    private ShpUsualFunctionService shpUsualFunctionService;

    @Autowired
    private ShpPermissionService shpPermissionService;

    @Autowired
    private ShpUserShopRefService shpUserShopRefService;

    @Autowired
    private ProProductService proProductService;

    @Autowired
    private ProClassifyService proClassifyService;
    //临时加载; 2.6.0版本兼容处理,迭代几个版本之后去掉此代码;
    @Autowired
    private ShpUserPermissionRefService shpUserPermissionRefService;
    @Resource
    private ShpUserPermissionRefMapper shpUserPermissionRefMapper;
    //-------------------------------------------------------------


    /**
     * 加载常用服务(暂时停用)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【常用】服务",
            notes = "加载店铺角色",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写"),
            @ApiImplicitParam(
                    paramType = "query", required = true, dataType = "String",
                    name = "token", value = "登录token"),
    })
    @RequestRequire
    @RequestMapping("/listUsualFunction")
    @ApiIgnore
    public BaseResult listUsualFunction(@RequestParam Map<String, String> params) {
        List<VoUsualFunction> usualList = shpUsualFunctionService.listShpUsualFunction(getShopId(), getUserId());
        String timestamp = "" + System.currentTimeMillis();
        formatIconUrl(usualList, timestamp);
        return LocalUtils.getBaseResult(usualList);
    }

    /**
     * 加载全部服务
     *
     * @param token 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【全部】服务",
            notes = "加载全部服务",
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
    @RequestMapping("/listAllFunction")
    public BaseResult listAllFunction(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        List list = new ArrayList();
        //仅针对2.6.0版本的新版仓库和旧版仓库权限处理;
        BasicParam bp = getBasicParam();
        String appVersion = bp.getAppVersion();
        try {
            //int version = VersionUtils.compareVersion(appVersion, "2.6.0");
            //判断是否有相对应的旧版商品权限;如果有,则为之添加对应新的商品权限;最后需更新权限缓存;迭代几个版本之后去掉此段代码;
            //if (version >= 0) {
            //    boolean own = hasPermWithCurrentUser("pro:check:ownProduct");
            //    boolean entrust = hasPermWithCurrentUser("pro:check:entrustProduct");
            //    boolean other = hasPermWithCurrentUser("pro:check:otherProduct");
            //
            //    boolean own2 = hasPermWithCurrentUser("pro:check:ownProduct2");
            //    boolean entrust2 = hasPermWithCurrentUser("pro:check:entrustProduct2");
            //    boolean other2 = hasPermWithCurrentUser("pro:check:otherProduct2");
            //    List<ShpUserPermissionRef> permList = new ArrayList<>();
            //    List<Integer> permIdList = new ArrayList<>();
            //    if (own) {
            //        if (!own2) {
            //            //添加新版自有商品权限 permId:10311
            //            permIdList.add(10311);
            //        }
            //    }
            //    if (entrust) {
            //        if (!entrust2) {
            //            //添加新版寄卖商品权限 permId:10312
            //            permIdList.add(10312);
            //        }
            //    }
            //    if (other) {
            //        if (!other2) {
            //            //添加新版其它商品权限 permId:10313
            //            permIdList.add(10313);
            //        }
            //    }
            //    for (Integer permId : permIdList) {
            //        ShpUserPermissionRef sp = shpUserPermissionRefService.packShpUserPermissionRef(getShopId(), userId, permId, getUserId());
            //        permList.add(sp);
            //    }
            //    if (!LocalUtils.isEmptyAndNull(permList)) {
            //        try {
            //            shpUserPermissionRefMapper.saveBatch(permList);
            //        } catch (Exception e) {
            //            log.error(e.getMessage(), e);
            //        }
            //        //更新权限缓存;
            //        shpUserPermissionRefService.listUserPermissionByRedis(shopId, userId, true);
            //    }
            //}
            String userTypeId = shpUserShopRefService.getUserTypeByShopIdAndUserId(shopId, userId);
            List<VoUsualFunction> allFunction;
            //如果是店铺创建者（老板），或者当前没有店铺，获取所有功能模块
            if (EnumShpUserType.BOSS.getCode().toString().equals(userTypeId) || shopId == 0) {
                allFunction = shpUsualFunctionService.listBossAllFunction();
            } else {
                allFunction = shpUsualFunctionService.listAllFunction(shopId, userId);
            }
            //针对安卓暂未开发完成的功能;临时解决方案;后期用shp_permission的版本号来控制;修改于2020-12-12 23:51:15

            if ("android".equals(bp.getPlatform().toLowerCase()) && userId != 10000) {
                String noPerm = redisUtil.get("sys_config:androidNoPe");
                if (!LocalUtils.isEmptyAndNull(noPerm)) {
                    String[] perms = noPerm.split(",");
                    //方法一: 使用迭代器,防止java.util.ConcurrentModificationException异常的发生
                    Iterator<VoUsualFunction> iterator = allFunction.iterator();
                    while ((iterator.hasNext())) {
                        VoUsualFunction vo = iterator.next();
                        for (String perm : perms) {
                            if (vo.getCode().contains(perm)) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }

            int version2 = VersionUtils.compareVersion(appVersion, "2.6.6");
            if (version2 == -1) {
                String[] perms = {"dynamic"};
                //265版本(不含)之前都不显示[发货管理]
                Iterator<VoUsualFunction> iterator = allFunction.iterator();
                while ((iterator.hasNext())) {
                    VoUsualFunction vo = iterator.next();
                    for (String perm : perms) {
                        if (vo.getCode().contains(perm)) {
                            iterator.remove();
                        }
                    }
                }
            }

            //if (version >= 0) {
            //    String[] perms = {"ownProduct", "entrustProduct", "otherProduct"};
            //    //260版本以上,去掉旧的自有商品; 添加新模块; 仓库;
            //    Iterator<VoUsualFunction> iterator = allFunction.iterator();
            //    while ((iterator.hasNext())) {
            //        VoUsualFunction vo = iterator.next();
            //        for (String perm : perms) {
            //            if (vo.getCode().contains(perm)) {
            //                iterator.remove();
            //            }
            //        }
            //    }
            //} else {
            //    //260之前的版本,不添加新模块功能; 仓库;已删除;小程序访客;寄卖取回;到期商品
            //    String[] perms = {"allProduct", "deleteHistory", "appletAccessUser", "entrustReturn", "expireProduct"};
            //    //260版本以上,去掉旧的自有商品; 添加新模块; 仓库;
            //    Iterator<VoUsualFunction> iterator = allFunction.iterator();
            //    while ((iterator.hasNext())) {
            //        VoUsualFunction vo = iterator.next();
            //        for (String perm : perms) {
            //            if (vo.getCode().contains(perm)) {
            //                iterator.remove();
            //            }
            //        }
            //    }
            //}
            //boolean unionTemp = false;
            //String shopUnionTemp = ConstantRedisKey.SHOP_UNION_TEMP;
            //String shopUnionTempValue = redisUtil.get(shopUnionTemp);
            //if (!LocalUtils.isEmptyAndNull(shopUnionTempValue)) {
            //    JSONObject unionTempJson = JSONObject.parseObject(shopUnionTempValue);
            //    String expiredTime = unionTempJson.getString(shopId + "");
            //    if (!LocalUtils.isEmptyAndNull(expiredTime)) {
            //        Date expiredDate = DateUtil.parse(expiredTime);
            //        unionTemp = new Date().before(expiredDate);
            //        if (!unionTemp) {
            //            unionTempJson.remove(shopId + "");
            //            redisUtil.set(shopUnionTemp, unionTempJson.toJSONString());
            //        }
            //    }
            //}
            //if(!unionTemp){
            //    //修改于2021-10-21 17:13:45 兼容新版的商家联盟判断；获取不到值的，证明试用资格已经失效；
            //    String shopUnionShopTempKey = ConstantRedisKey.getShopUnionShopTemp(shopId);
            //    String shopTempValue = redisUtil.get(shopUnionShopTempKey);
            //    unionTemp = !LocalUtils.isEmptyAndNull(shopTempValue);
            //}
            boolean accessUnion;
            //会员可直接查看商家联盟;过期会员不可查看;体验会员分情况,超过查看限制也不可查看;
            String memberState = getMemberState();
            switch (memberState) {
                //体验会员
                case "1": {
                    //新规则;新注册店铺可查看30个商品;
                    String shopUnionShopTempKey = ConstantRedisKey.getShopUnionAppUserTemp(getUsername());
                    String shopTempValue = redisUtil.get(shopUnionShopTempKey);
                    accessUnion = LocalUtils.isEmptyAndNull(shopTempValue);
                    if (!accessUnion) {
                        String accessLimitNumKey = "shp:shop_union:accessUser:limitNum";
                        String accessLimitNumValue = redisUtil.get(accessLimitNumKey);
                        //只需要判断是否超过限制的数量即可,超过则提示会员可看;
                        String[] split = shopTempValue.split(":");
                        int userAccessLimitInt = Integer.parseInt(split[0]);
                        //已经超过限制查看的商品数量
                        accessUnion = userAccessLimitInt < Integer.parseInt(accessLimitNumValue);
                    }
                }
                break;
                case "2":
                case "3":
                    //付费会员
                    accessUnion = true;
                    break;
                default:
                    //苹果审核帐号可以直接查看商家联盟
                    accessUnion = "18434363494".equals(getUsername());
                    break;
            }

            boolean hasPerm = servicesUtil.hasSpecialPerm(getShopNumber());
            int parentId = -1;
            String shopUnion = redisUtil.get(ConstantRedisKey.SHOP_UNION);
            for (VoUsualFunction allFunc : allFunction) {
                //店铺不在商家联盟的白名单内 且 不在有效期内(修改于2021-09-16 20:36:39), 则显示h5的code码;
                //修改于2021-11-05 22:30:08;不显示h5页面;到达规则上限;则直接显示会员可使用;(会员不提示)
                //if ("unionShop".equals(allFunc.getCode()) && !shopUnion.contains(getShopId() + "") && !unionTemp) {
                //    allFunc.setCode("h5");
                //}
                if ("unionShop".equals(allFunc.getCode()) && !accessUnion) {
                    if (version2 == -1) {
                        //v265之前(含)显示弹窗开通会员;
                        allFunc.setCostState("1");
                    } else {
                        allFunc.setCode("h5");
                        String s2 = allFunc.getHttpUrl() + URLEncoder.encode(getUserPayUrl(), "utf-8");
                        allFunc.setHttpUrl(s2);
                    }
                }
                int allParentId = allFunc.getParentId();
                if (allParentId == parentId) {
                    continue;
                }
                parentId = allParentId;
                List<VoUsualFunction> groupList = new ArrayList<>();
                for (VoUsualFunction groupFunc : allFunction) {
                    //version=99为特殊权限; 需要把店铺加入白名单才能拥有此功能;
                    if (!hasPerm && groupFunc.getVersion() == 99) {
                        continue;
                    }
                    int groupParentId = groupFunc.getParentId();
                    if (parentId == groupParentId) {
                        groupList.add(groupFunc);
                    }
                }
                HashMap<String, Object> myMap = new HashMap<>(16);
                myMap.put("parentName", allFunc.getParentName());
                myMap.put("parentCode", allFunc.getParentCode());
                myMap.put("list", groupList);
                list.add(myMap);
            }

            String timestamp = redisUtil.get(ConstantRedisKey.ICON_TIMESTAMP) + "";

            formatIconUrl(allFunction, timestamp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return LocalUtils.getBaseResult(list);
    }

    /**
     * 加载当前用户价格相关权限
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载当前用户价格相关权限",
            notes = "加载当前用户价格相关权限",
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
    @RequestMapping("/listShopUserPricePerm")
    public BaseResult<VoShopUserPricePerm> listShopUserPricePerm(@RequestParam Map<String, String> params) {
        VoShopUserPricePerm vo = new VoShopUserPricePerm();
        //同行价（友商价）
        Boolean isHaveFriendPricePerm = hasPermWithCurrentUser("pro:price:tradePrice");
        //销售价
        Boolean isHaveSalePricePerm = hasPermWithCurrentUser("pro:price:salePrice");
        //代理价
        Boolean isHaveAgencyPricePerm = hasPermWithCurrentUser("pro:price:agencyPrice");
        //成本价
        Boolean isHaveInitPricePerm = hasPermWithCurrentUser("pro:price:initPrice");
        //是否有在售商品
        Integer shopId = getShopId();
        Boolean isHaveOnSaleProduct = proProductService.countProductByStateCode(shopId, "onSale") > 0;
        //在售商品列表中第一个商品的图片
        ParamProductQuery queryParam = new ParamProductQuery();
        queryParam.setShopId(shopId);
        queryParam.setStateCode("onSale");
        String imgUrl = proProductService.getFirstProdImg(queryParam);

        vo.setIsHaveFriendPricePerm(isHaveFriendPricePerm);
        vo.setIsHaveSalePricePerm(isHaveSalePricePerm);
        vo.setIsHaveAgencyPricePerm(isHaveAgencyPricePerm);
        vo.setIsHaveInitPricePerm(isHaveInitPricePerm);
        vo.setIsHaveOnSaleProduct(isHaveOnSaleProduct);
        imgUrl = LocalUtils.isEmptyAndNull(imgUrl) ? "" : servicesUtil.formatImgUrl(imgUrl);
        vo.setImageUrl(imgUrl);
        List<VoProClassify> voProClassifyList = proClassifyService.listProClassifyByState(shopId, "1");
        vo.setClassifyList(voProClassifyList);
        return LocalUtils.getBaseResult(vo);
    }


    /**
     * 管理常用服务(暂时停用)
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "管理【常用】服务",
            notes = "排序,添加,减少常用服务",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/modifyUsualFunction")
    @ApiIgnore
    public BaseResult modifyUsualFunction(@RequestParam Map<String, String> params,
                                          @Valid ParamUsualFunction paramFunction, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        String ids = paramFunction.getPermId();
        List<ShpUsualFunction> list = new ArrayList<>();
        if (!LocalUtils.isEmptyAndNull(ids)) {
            List<VoUsualFunction> usualFuncList = shpPermissionService.listShpPermissionByIds(ids);
            if (!LocalUtils.isEmptyAndNull(usualFuncList)) {
                int i = 0;
                Date date = new Date();
                for (VoUsualFunction func : usualFuncList) {
                    ShpUsualFunction newFunc = new ShpUsualFunction();
                    newFunc.setCode(func.getCode());
                    newFunc.setName(func.getName());
                    newFunc.setFkShpShopId(shopId);
                    newFunc.setFkShpUserId(userId);
                    newFunc.setFkShpPermissionId(func.getPermId());
                    newFunc.setIconUrl(func.getIconUrl());
                    newFunc.setParentName(func.getParentName());
                    newFunc.setParentCode(func.getParentCode());
                    newFunc.setParentId(func.getParentId());
                    newFunc.setSort(++i);
                    newFunc.setInsertTime(date);
                    list.add(newFunc);
                }
            }
        }
        //先清除之前的常用功能,再全部新增现有的常用功能;
        shpUsualFunctionService.resetUsualFunction(shopId, userId, list);
        log.info("======permId:" + paramFunction.getPermId());
        return BaseResult.okResult("设置成功");
    }

    private void formatIconUrl(VoUsualFunction func, String timestamp) {
        if (!LocalUtils.isEmptyAndNull(func)) {
            String iconUrl = func.getIconUrl();
            if (!LocalUtils.isEmptyAndNull(iconUrl)) {
                func.setIconUrl(ConstantCommon.ossDomain + iconUrl + timestamp);
            }
        }
    }

    private void formatIconUrl(List<VoUsualFunction> funcList, String timestamp) {
        if (!LocalUtils.isEmptyAndNull(funcList)) {
            for (VoUsualFunction func : funcList) {
                formatIconUrl(func, timestamp);
            }
        }
    }


}

