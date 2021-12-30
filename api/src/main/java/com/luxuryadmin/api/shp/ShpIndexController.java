package com.luxuryadmin.api.shp;

import com.alibaba.fastjson.JSON;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.base.BasicParam;
import com.luxuryadmin.common.constant.ConstantCommon;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.service.shp.ShpIndexService;
import com.luxuryadmin.service.shp.ShpPermUserRefService;
import com.luxuryadmin.vo.shp.VoShpIndex;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class ShpIndexController extends BaseController {

    @Autowired
    private ShpIndexService shpIndexService;

    @Autowired
    private ShpPermUserRefService shpPermUserRefService;

    /**
     * 加载【首页】列表
     *
     * @param token 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载【首页】列表",
            notes = "加载【首页】列表",
            httpMethod = "POST")
    @RequestMapping("/listIndex")
    public BaseResult listIndex(@Valid ParamToken token, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        int userId = getUserId();
        turnOldPermToNewPerm(shopId, userId);
        List list = new ArrayList();
        BasicParam bp = getBasicParam();
        String appVersion = bp.getAppVersion();
        int version = LocalUtils.formatVersion(appVersion);
        try {
            List<VoShpIndex> allFunction;
            //如果是店铺创建者（老板），或者当前没有店铺，获取所有功能模块
            if (isBossForCurrentUser() || shopId == 0) {
                allFunction = shpIndexService.listAppIndexFunction(bp.getPlatform(), version);
            } else {
                allFunction = shpIndexService.listAllPermByShopIdUserId(bp.getPlatform(), version, shopId, userId);
            }
            //会员可直接查看商家联盟;过期会员不可查看;体验会员分情况,超过查看限制也不可查看;
            boolean accessUnion;
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

            int parentId = -1;
            for (VoShpIndex allFunc : allFunction) {
                //店铺不在商家联盟的白名单内 且 不在有效期内(修改于2021-09-16 20:36:39), 则显示h5的code码;
                if ("unionShop".equals(allFunc.getCode()) && !accessUnion) {
                    allFunc.setCode("h5");
                    String s2 = allFunc.getHttpUrl() + URLEncoder.encode(getUserPayUrl(), "utf-8");
                    allFunc.setHttpUrl(s2);
                }
                int allParentId = allFunc.getParentId();
                if (allParentId == parentId) {
                    continue;
                }
                parentId = allParentId;
                List<VoShpIndex> groupList = new ArrayList<>();
                for (VoShpIndex groupFunc : allFunction) {
                    //指定显示的模块,判断显示的店铺
                    if (!LocalUtils.isEmptyAndNull(groupFunc.getOnlyShopId()) && groupFunc.getOnlyShopId().length()
                            >= 5 && !groupFunc.getOnlyShopId().contains(getShopId() + "")) {
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


    private void formatIconUrl(VoShpIndex func, String timestamp) {
        if (!LocalUtils.isEmptyAndNull(func)) {
            String iconUrl = func.getIconUrl();
            if (!LocalUtils.isEmptyAndNull(iconUrl)) {
                func.setIconUrl(ConstantCommon.ossDomain + iconUrl + timestamp);
            }
        }
    }

    private void formatIconUrl(List<VoShpIndex> funcList, String timestamp) {
        if (!LocalUtils.isEmptyAndNull(funcList)) {
            for (VoShpIndex func : funcList) {
                formatIconUrl(func, timestamp);
            }
        }
    }


    /**
     * 新旧权限合并,新权限为第一个,多个权限用逗号隔开;
     *
     * @return
     */
    private void turnOldPermToNewPerm(int shopId, int userId) {
        String tempKey = "shp:movePerm:" + shopId + ":" + userId;
        String tempValue = redisUtil.get(tempKey);
        if (!LocalUtils.isEmptyAndNull(tempValue)) {
            //已经迁移过了权限, 不需要再次执行
            return;
        }
        String userPerms = servicesUtil.getUserPerms(shopId, userId);
        redisUtil.set(tempKey, userPerms);
        if (LocalUtils.isEmptyAndNull(userPerms)) {
            return;
        }
        Map<String, Object> hashMap = (Map<String, Object>) JSON.parse(userPerms);
        Object permObj = hashMap.get("permList");
        if (!ConstantCommon.NO_PERM.equals(permObj)) {
            List<String> oldPermList = (List<String>) permObj;
            shpPermUserRefService.turnOldPermToNewPerm(shopId, userId, oldPermList);
            //更新权限缓存;
            shpPermUserRefService.listPermUserByRedis(shopId, userId, true);
        }
    }
}

