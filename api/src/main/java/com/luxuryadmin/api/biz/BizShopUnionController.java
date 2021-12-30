package com.luxuryadmin.api.biz;


import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.common.constant.ConstantPermission;
import com.luxuryadmin.common.constant.ConstantRedisKey;
import com.luxuryadmin.common.utils.DateUtil;
import com.luxuryadmin.common.utils.LocalUtils;
import com.luxuryadmin.entity.pro.ProShare;
import com.luxuryadmin.param.biz.ParamLeaguerProductQuery;
import com.luxuryadmin.param.biz.ParamShopUnionDelete;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.pro.ParamShareUnion;
import com.luxuryadmin.service.biz.BizShopUnionService;
import com.luxuryadmin.service.op.OpUnionAgentService;
import com.luxuryadmin.service.pro.ProShareService;
import com.luxuryadmin.service.shp.ShpUserService;
import com.luxuryadmin.vo.biz.VoShopUnionByAppShow;
import com.luxuryadmin.vo.biz.VoMpUnionAgencyUser;
import com.luxuryadmin.vo.pro.VoLeaguerProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 * biz_shop_union controller
 *
 * @author zhangsai
 * @Date 2021-07-16 17:58:53
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/biz/recommend", method = RequestMethod.POST)
@Api(tags = {"G002.1.【友商】模块2.6.1 --zs"}, description = "/shop/user/biz/recommend |【友商】模块相关")
public class BizShopUnionController extends BizLeaguerBaseController {


    @Autowired
    private BizShopUnionService bizShopUnionService;

    @Autowired
    private ShpUserService shpUserService;

    @Autowired
    private ProShareService proShareService;

    @Autowired
    private OpUnionAgentService opUnionAgentService;

    /**
     * 联盟商品--初始化
     */
    @ApiOperation(
            value = "联盟商品--初始化 --2.6.1 --zs;",
            notes = "联盟商品--初始化 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/getShopUnionByAppShow")
    @RequiresPermissions("shop:union:show")
    public BaseResult<VoShopUnionByAppShow> getShopUnionByAppShow(@Valid ParamToken paramToken, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer shopId = getShopId();
        int userId = getUserId();
        VoShopUnionByAppShow shopUnionByAppShow = bizShopUnionService.getShopUnionByAppShow(shopId);
        //查看商家联盟信息权限
        String uPermShowUnionShop = ConstantPermission.SHOW_PRODUCT_UNIONSHOP;
        shopUnionByAppShow.setUPermShowUnionShop(hasPermToPageWithCurrentUser(uPermShowUnionShop));
        String sharePermKey = ConstantRedisKey.SHOP_UNION_SHARE_PERM;
        String unionMpImgKey = ConstantRedisKey.SHOP_UNION_MP_IMG;
        String sharePerm = redisUtil.get(sharePermKey);
        String unionMpImg = redisUtil.get(unionMpImgKey);
        //判断是否有分享权限
        if (!LocalUtils.isEmptyAndNull(sharePerm) && sharePerm.contains(getUserId() + "")) {
            shopUnionByAppShow.setUPermShare("1");
            shopUnionByAppShow.setMpImgUrl(servicesUtil.formatImgUrl(unionMpImg));
            shopUnionByAppShow.setMpImgUrlShort(unionMpImg);
            shopUnionByAppShow.setCurrentUserId(userId + "");
            String nickname = shpUserService.getNicknameByUserId(getUserId());
            //如果后台忘记配置,则默认分享出去的关联人只有自己
            List<VoMpUnionAgencyUser> mpUnionList = opUnionAgentService.listVoMpUnionAgencyUser(userId);
            if (LocalUtils.isEmptyAndNull(mpUnionList)) {
                mpUnionList = new ArrayList<>();
            }
            //把自己放在第一位
            VoMpUnionAgencyUser voAgency = new VoMpUnionAgencyUser();
            voAgency.setUserId(getUserId() + "");
            voAgency.setNickname(nickname);
            List<VoMpUnionAgencyUser> newList = new ArrayList<>();
            newList.add(voAgency);
            newList.addAll(mpUnionList);
            shopUnionByAppShow.setMpUnionAgencyList(newList);
        }

        return BaseResult.okResult(shopUnionByAppShow);
    }

    /**
     * 加载商家联盟商品
     *
     * @param productQuery 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "加载商家联盟商品 --2.6.1;",
            notes = "加载商家联盟商品 --2.6.1;",
            httpMethod = "POST")
    @PostMapping("/getUnionProductByAppShow")
    public BaseResult<List<VoLeaguerProduct>> getUnionProductByAppShow(
            @Valid ParamLeaguerProductQuery productQuery, BindingResult result) {
        servicesUtil.validControllerParam(result);
        int shopId = getShopId();
        //体验会员才需要判断
        if ("1".equals(getMemberState())) {
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
            //========================End==================
        }

        productQuery.setShopId(shopId);
        formatLeaguerQueryParam(productQuery);
        List<VoLeaguerProduct> listLeaPro = bizShopUnionService.listUnionProductNoPage(productQuery);
        if(!LocalUtils.isEmptyAndNull(listLeaPro)){
            listLeaPro.forEach(voLeaguerProduct -> {
                String uPermShowUnionShop = ConstantPermission.SHOW_UNION_UNIONSHOP;
                String unionState =hasPermToPageWithCurrentUser(uPermShowUnionShop);
                if ("0".equals(unionState)) {
                    voLeaguerProduct.setShopName("");
                }
            });
        }
        return LocalUtils.getBaseResult(listLeaPro);
    }

    @ApiOperation(
            value = "商家联盟 --移除 --2.6.1 --zs;",
            notes = "商家联盟 --移除 --2.6.1 --zs;",
            httpMethod = "POST")
    @PostMapping("/removeUnionShop")
    public BaseResult removeUnionShopByApp() {
        ParamShopUnionDelete shopUnionDelete = new ParamShopUnionDelete();
        shopUnionDelete.setUserId(getUserId());
        shopUnionDelete.setShopId(getShopId());
        bizShopUnionService.removeUnionShopByApp(shopUnionDelete);
        return BaseResult.okResult();
    }

    /**
     * 保存分享【商家联盟】的记录
     *
     * @param params 前端参数
     * @return Result
     */
    @ApiOperation(
            value = "保存分享【商家联盟】的记录--2.6.2",
            notes = "保存分享【商家联盟】的记录--2.6.2",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    paramType = "query", required = false, dataType = "String",
                    name = "params", value = "基础参数;可不填写")
    })
    @RequestRequire
    @RequestMapping("/saveShareUnion")
    public BaseResult saveShareUnion(@RequestParam Map<String, String> params,
                                     @Valid ParamShareUnion save, BindingResult result) {
        servicesUtil.validControllerParam(result);
        ProShare share = new ProShare();
        String title = "找货源，知行情";
        try {
            int agencyUserId = Integer.parseInt(save.getUserId());
            Date endDate = DateUtil.parse(save.getEndTime());
            Date now = new Date();
            Integer validDay = opUnionAgentService.getUnionAgencyValidDay(agencyUserId);
            //需要校验代理的分享有效期
            if (!LocalUtils.isEmptyAndNull(validDay)) {
                Date validDate = DateUtil.addDaysFromOldDate(now, validDay).getTime();
                if (endDate.after(validDate)) {
                    return BaseResult.defaultErrorWithMsg("分享时长最长限制" + validDay + "天之内!");
                }
            }
            share.setFkShpShopId(getShopId());
            share.setFkShpUserId(getUserId());
            share.setShopNumber(getShopNumber());
            share.setUserNumber(getUserNumber());
            share.setShowPrice("");
            share.setProId("all");
            share.setShareName(title);
            share.setInsertTime(now);
            share.setEndTime(endDate);
            share.setShareBatch(System.currentTimeMillis() + "");
            share.setType("1");
            share.setUnionUserId(agencyUserId);
            share.setShareImg(save.getMpImg());
            proShareService.saveOrUpdateObject(share);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return BaseResult.defaultErrorWithMsg("分享失败!");
        }
        HashMap<String, Object> hashMap = LocalUtils.getHashMap("title", title);
        String key = ConstantRedisKey.MP_EXPIRE_FLAG;
        String mpExpireFlag = redisUtil.get(key);
        String timestamp = share.getEndTime().getTime() + "";
        //拼接小程序链接
        String path = "pages/goodsList/goodsList" +
                "?userId=" + share.getUnionUserId() +
                "&timestamp=" + timestamp +
                "&flag=" + mpExpireFlag +
                "&title=" + title +
                "&mpImg=" + servicesUtil.formatImgUrl(save.getMpImg()) +
                "&shareId=" + share.getId();

        hashMap.put("path", path);
        return BaseResult.okResult(hashMap);
    }
}
