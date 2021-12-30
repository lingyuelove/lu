package com.luxuryadmin.api.shp;

import com.luxuryadmin.api.util.PublicProductUtil;
import com.luxuryadmin.common.aop.check.RequestRequire;
import com.luxuryadmin.common.base.BaseController;
import com.luxuryadmin.common.base.BaseResult;
import com.luxuryadmin.entity.shp.ShpWechat;
import com.luxuryadmin.param.common.ParamToken;
import com.luxuryadmin.param.shp.ParamShpWechatAdd;
import com.luxuryadmin.param.shp.ParamShpWechatUpdate;
import com.luxuryadmin.service.shp.ShpWechatService;
import com.luxuryadmin.vo.shp.VoShpWechat;
import com.luxuryadmin.vo.shp.VoShpWechatByShow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 店铺微信控制器Controller
 *
 * @author sanjin
 * @Date 2020/08/31 16:01
 */
@Slf4j
@RestController
@RequestMapping(value = "/shop/user/wechat", method = RequestMethod.POST)
@Api(tags = {"E006.【店铺】模块--2.6.2"}, description = "/shop/user/wechat | 店铺微信模块--2.6.2")
public class ShpWechatController extends BaseController {

    @Autowired
    private ShpWechatService shpWechatService;
    @Autowired
    PublicProductUtil publicProductUtil;

    /**
     * 添加店铺微信;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "【添加】店铺微信",
            notes = "【添加】店铺微信;<br/>",
            httpMethod = "POST")
    @RequestMapping("/addShpWechat")
    public BaseResult addShpWechat(@Valid ParamShpWechatAdd paramShpWechatAdd, BindingResult result) {
        servicesUtil.validControllerParam(result);

        Integer shopId = getShopId();
        Integer userId = getUserId();
        ShpWechat shpWechat = new ShpWechat();
        shpWechat.setFkShopId(shopId);
        shpWechat.setContactPersonName(paramShpWechatAdd.getContactPersonName());
        shpWechat.setContactPersonWechat(paramShpWechatAdd.getContactPersonWechat());
        shpWechat.setContactResponsible(paramShpWechatAdd.getContactResponsible());
        shpWechat.setInsertAdmin(userId);
        shpWechat.setUpdateAdmin(getUserId());
        shpWechatService.addShpWechat(shpWechat);
        return BaseResult.okResult(shpWechat.getId());
    }

    /**
     * 【查询】店铺微信列表;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "【查询】店铺微信列表",
            notes = "【查询】店铺微信列表;<br/>",
            httpMethod = "POST")
    @RequestMapping("/listShpWechat")
    public BaseResult<List<VoShpWechat>> listShpWechat(@RequestParam Map<String, String> params) {
        Integer shopId = getShopId();
        List<VoShpWechat> voShpWechatList = shpWechatService.listShpWechat(shopId);
        return BaseResult.okResult(voShpWechatList);
    }

    @RequestRequire
    @ApiOperation(
            value = "【查询】店铺微信列表 --2.6.2",
            notes = "【查询】店铺微信列表--2.6.2;<br/>",
            httpMethod = "POST")
    @RequestMapping("/listShpWechatByShow")
    public BaseResult<VoShpWechatByShow> getWechatByShow(ParamToken token) {
        Integer shopId = getShopId();
        Integer userId = getUserId();
        String unionUrl = publicProductUtil.getUnionUrl();
        VoShpWechatByShow wechatByShow = shpWechatService.getWechatByShow(shopId, userId, unionUrl);
        return BaseResult.okResult(wechatByShow);
    }

    /**
     * 【逻辑删除】店铺微信;
     *
     * @return Result
     */
    @RequestRequire
    @ApiOperation(
            value = "【逻辑删除】店铺微信",
            notes = "【逻辑删除】店铺微信;<br/>",
            httpMethod = "POST")
    @RequestMapping("/deleteShpWechat")
    public BaseResult deleteShpWechat(@RequestParam String id) {
        Integer shopId = getShopId();
        shpWechatService.deleteShpWechat(id, shopId);
        return BaseResult.okResult("【删除】店铺微信成功");
    }

    @RequestRequire
    @ApiOperation(
            value = "【修改】店铺微信--2.6.2",
            notes = "【修改】店铺微信--2.6.2;<br/>",
            httpMethod = "POST")
    @RequestMapping("/updateShpWechat")
    public BaseResult updateShpWechat(@Valid ParamShpWechatUpdate shpWechatUpdate, BindingResult result) {
        servicesUtil.validControllerParam(result);
        Integer userId = getUserId();
        shpWechatUpdate.setUserId(userId);
        shpWechatService.updateShpWechat(shpWechatUpdate);
        return BaseResult.okResult(shpWechatUpdate.getId());
    }
}
